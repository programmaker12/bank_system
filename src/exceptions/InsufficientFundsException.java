package exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    private final BigDecimal exceededAmount;
    public InsufficientFundsException(String message, BigDecimal exceededAmount) {
        super(message);

        this.exceededAmount = exceededAmount;
    }
}
