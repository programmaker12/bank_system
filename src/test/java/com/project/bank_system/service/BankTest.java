package com.project.bank_system.service;

import com.project.bank_system.entity.Account;
import com.project.bank_system.exceptions.InsufficientFundsException;
import com.project.bank_system.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankTest {

    private Bank bank;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EntityManager entityManager;
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
    void testCreateUserSuccessfully() {
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

        assertTrue(finalBalance.compareTo(BigDecimal.ZERO) >= 0);
    }

    // ✅ Fix LazyInitializationException and N+1 problem
    @Test
    @Transactional
    public void testNPlusOne() {
        List<Account> accounts = accountRepository.findAllWithTransactions(PageRequest.of(0, 10));
        // Access transactions safely inside a transaction
        for (Account acc : accounts) {
            System.out.println("Account: " + acc.getUsername() + ", Transactions: " + acc.getTransactions().size());
        }
    }


    @Test
    @Transactional
    public void testNPlusOneWithStats() {
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Account> accounts = accountRepository.findAllWithTransactions(PageRequest.of(0, 10)); // lazy
        for (Account acc : accounts) {
            acc.getTransactions().size();
        }

        System.out.println("Number of queries executed: " + stats.getPrepareStatementCount());
    }
}
