package com.project.bank_system.service.implementation;

import com.project.bank_system.entity.Account;
import com.project.bank_system.dto.AccountDTO;
import com.project.bank_system.repository.AccountRepository;
import com.project.bank_system.service.interfaces.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<Account> getAllActiveAccounts() {
        return accountRepository.findAllActiveAccounts();
    }

    @Override
    public List<AccountDTO> getActiveAccountSummaries() {
        return accountRepository.findActiveAccountSummaries();
    }

    @Override
    public Page<Account> getAllAccountsPaged(int page, int size) {
        return accountRepository.findAll(PageRequest.of(page, size));
    }

}
