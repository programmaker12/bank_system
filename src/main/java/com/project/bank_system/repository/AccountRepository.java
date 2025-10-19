package com.project.bank_system.repository;


import com.project.bank_system.dto.AccountDTO;
import com.project.bank_system.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Example: Fetch account by username
    @Query("SELECT a FROM Account a WHERE a.username = :username")
    Account findByUsername(@Param("username") String username);

    // Example: Fetch all active accounts
    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE'")
    List<Account> findAllActiveAccounts();

    @Query("SELECT new com.project.bank_system.dto.AccountDTO(a.username, a.balance) " +
            "FROM Account a WHERE a.status = 'ACTIVE'")
    List<AccountDTO> findActiveAccountSummaries();

}

