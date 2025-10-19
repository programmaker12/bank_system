package com.project.bank_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountDTO {

    private String username;
    private BigDecimal balance;

    public AccountDTO(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }

    // Explicit getters for Jackson
    public String getUsername() {
        return username;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

