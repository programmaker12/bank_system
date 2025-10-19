package com.project.bank_system.controller;

import com.project.bank_system.entity.Account;
import com.project.bank_system.dto.AccountDTO;
import com.project.bank_system.service.interfaces.AccountService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.logging.Logger;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 1️⃣ Fetch all accounts
    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        long start = System.currentTimeMillis();
        List<Account> allAccounts = accountService.getAllAccounts();
        long end = System.currentTimeMillis();
        logger.info("/api/accounts/all | Execution time: " + (end - start) + " ms");
        return allAccounts;
    }

    @GetMapping("/paged")
    public Page<Account> getAllAccountsPaged(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "100") int size) {
        long start = System.currentTimeMillis();
        Page<Account> accounts = accountService.getAllAccountsPaged(page, size);
        long end = System.currentTimeMillis();
        System.out.println("/api/accounts/paged |Execution time: " + (end - start) + " ms");
        return accounts;
    }
    @GetMapping("/summary")
    public List<AccountDTO> getAllAccountsSummary() {
        long start = System.currentTimeMillis();
        List<AccountDTO> accounts = accountService.getActiveAccountSummaries(); // DTO query
        long end = System.currentTimeMillis();
        System.out.println("/api/accounts/summary |Execution time: " + (end - start) + " ms");
        return accounts;
    }


    // 2️⃣ Fetch account by username
    @GetMapping("/{username}")
    public Account getAccountByUsername(@PathVariable String username) {
        return accountService.getAccountByUsername(username);
    }

    // 3️⃣ Fetch all active accounts
    @GetMapping("/active")
    public List<Account> getAllActiveAccounts() {
        return accountService.getAllActiveAccounts();
    }

    // 4️⃣ Fetch active account summaries (DTO)
    @GetMapping("/active-summary")
    public List<AccountDTO> getActiveAccountSummaries() {
        return accountService.getActiveAccountSummaries();
    }
}
