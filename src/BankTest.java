import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.Bank;
import exceptions.InsufficientFundsException;

import java.math.BigDecimal;

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
}
