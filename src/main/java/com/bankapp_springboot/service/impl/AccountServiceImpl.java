package com.bankapp_springboot.service.impl;

import com.bankapp_springboot.dto.AccountDto;
import com.bankapp_springboot.entity.Account;
import com.bankapp_springboot.mapper.AccountMapper;
import com.bankapp_springboot.repository.AccountRepository;
import com.bankapp_springboot.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {


    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount= accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account= accountRepository.findById(id).orElseThrow((() -> new RuntimeException("Account Not Exist")));

        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account= accountRepository.findById(id).orElseThrow((() -> new RuntimeException("Account Not Exist")));

        double totalBalance = account.getBalance()+amount;
        account.setBalance(totalBalance);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

        Account account= accountRepository.findById(id).orElseThrow((() -> new RuntimeException("Account Not Exist")));
        if(account.getBalance()<amount) {
            throw new RuntimeException("Insufficient Balance");
        }

        double totalBalance= account.getBalance()-amount;
        account.setBalance(totalBalance);
        Account savedAccount= accountRepository.save(account);


        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
      return   accountRepository.findAll().stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Long id) {
        Account account= accountRepository.findById(id).orElseThrow((() -> new RuntimeException("Account Not Exist")));

        accountRepository.delete(account);
    }


}
