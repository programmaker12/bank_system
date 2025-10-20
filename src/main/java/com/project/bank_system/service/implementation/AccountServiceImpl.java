package com.project.bank_system.service.implementation;

import com.project.bank_system.entity.Account;
import com.project.bank_system.dto.AccountDTO;
import com.project.bank_system.repository.AccountRepository;
import com.project.bank_system.service.interfaces.AccountService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Cacheable(value = "accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Cacheable(value = "accounts", key = "#username")
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<Account> getAllActiveAccounts() {
        return accountRepository.findAllActiveAccounts();
    }

    @Override
    public Page<AccountDTO> getAllAccountSummaries(Pageable pageable) {
        // Simply call repository method with Pageable
        return accountRepository.findActiveAccountSummaries(pageable);
    }

    @Override
    public Page<Account> getAllAccountsPaged(int page, int size) {
        return accountRepository.findAll(PageRequest.of(page, size));
    }

}
