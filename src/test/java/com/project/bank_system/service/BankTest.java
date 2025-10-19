package com.project.bank_system.service;

import com.project.bank_system.exceptions.InsufficientFundsException;

import com.project.bank_system.service.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bank.createUser("Yash");
        bank.depositAmount("Yash", new BigDecimal("1000"));
    }

    @Test
    void testDepositSuccess() {
        bank.depositAmount("Yash", new BigDecimal("500"));
        assertEquals(new BigDecimal("1500"), bank.getUserBalance("Yash"));
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.depositAmount("Yash", new BigDecimal("-100")));
    }

    @Test
    void testDepositToNonExistingUser() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.depositAmount("Unknown", new BigDecimal("100")));
    }

    @Test
    void testWithdrawSuccess() {
        bank.withdrawAmount("Yash", new BigDecimal("700"));
        assertEquals(new BigDecimal("300"), bank.getUserBalance("Yash"));
    }

    @Test
    void testWithdrawMoreThanBalance() {
        assertThrows(InsufficientFundsException.class,
                () -> bank.withdrawAmount("Yash", new BigDecimal("2000")));
    }

    @Test
    void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.withdrawAmount("Yash", new BigDecimal("-500")));
    }

    @Test
    void testWithdrawNullUser() {
        assertThrows(IllegalArgumentException.class,
                () -> bank.withdrawAmount(null, new BigDecimal("100")));
    }

    @Test
    void  testCreateUserSuccessfully() {
        bank.createUser("Amit");
        assertEquals(BigDecimal.ZERO, bank.getUserBalance("Amit"));
    }

    @Test
    void testDuplicateUserCreationThrowsError() {
        assertThrows(IllegalArgumentException.class, () -> bank.createUser("Yash"));
    }

    @Test
    void testDepositAmountUpdatesBalance() {
        bank.depositAmount("Yash", new BigDecimal("500"));
        assertEquals(new BigDecimal("1500"), bank.getUserBalance("Yash"));
    }

    @Test
    void testWithdrawAmountUpdatesBalance() {
        bank.withdrawAmount("Yash", new BigDecimal("400"));
        assertEquals(new BigDecimal("600"), bank.getUserBalance("Yash"));
    }

    @Test
    void testWithdrawAmountExceedingBalanceThrowsException() {
        assertThrows(InsufficientFundsException.class, () ->
                bank.withdrawAmount("Yash", new BigDecimal("1500"))
        );
    }

    @Test
    void testConcurrentWithdrawalsAreThreadSafe() throws InterruptedException {
        int threads = 2;
        CountDownLatch latch = new CountDownLatch(threads);

        Thread t1 = new Thread(() -> {
            try {
                bank.withdrawAmount("Yash", new BigDecimal("600"));
            } catch (Exception ignored) {
            }
            latch.countDown();
        });

        Thread t2 = new Thread(() -> {
            try {
                bank.withdrawAmount("Yash", new BigDecimal("600"));
            } catch (Exception ignored) {
            }
            latch.countDown();
        });

        t1.start();
        t2.start();
        latch.await();

        BigDecimal finalBalance = bank.getUserBalance("Yash");
        System.out.println("Final Balance after concurrent withdrawals: " + finalBalance);

        // Either one withdrawal succeeds or both partially fail safely
        assertTrue(finalBalance.compareTo(BigDecimal.ZERO) >= 0);
    }
}
