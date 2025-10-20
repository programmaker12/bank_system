package com.project.bank_system.service.interfaces;

import com.project.bank_system.dto.AccountDTO;
import com.project.bank_system.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    // Fetch all accounts
    List<Account> getAllAccounts();

    // Fetch account by username
    Account getAccountByUsername(String username);

    // Fetch all active accounts
    List<Account> getAllActiveAccounts();

    // Fetch only username and balance for optimization
    Page<AccountDTO> getAllAccountSummaries(Pageable pageable);

    Page<Account> getAllAccountsPaged(int page, int size);
}
