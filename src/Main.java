import exceptions.InsufficientFundsException;
import service.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Bank bank = new Bank();
//        bank.createUser("Yash");
//        bank.depositAmount("Yash", new BigDecimal("1000"));
//
//        // Run the test multiple times to see race conditions
//        for (int i = 0; i < 5; i++) {
//            System.out.println("\n--- Test Run " + (i+1) + " ---");
//            runConcurrentWithdrawals(bank);
//        }


//        BankWithReentrantLock bank = new BankWithReentrantLock();
//
//        bank.createUser("Alice");
//        bank.createUser("Bob");
//
//        bank.depositAmount("Alice", new BigDecimal("1000"));
//        bank.depositAmount("Bob", new BigDecimal("1000"));
//
//        // Simulate concurrent transfers that could deadlock with synchronized
//        Thread t1 = new Thread(() -> {
//            try {
//                bank.transfer("Alice", "Bob", new BigDecimal("300"));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "Thread-1");
//
//        Thread t2 = new Thread(() -> {
//            try {
//                bank.transfer("Bob", "Alice", new BigDecimal("500"));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "Thread-2");
//
//        t1.start();
//        t2.start();

//        BankWithSynchronized bank1 = new BankWithSynchronized();
//        bank1.createUser("Alice");
//        bank1.createUser("Bob");
//
//        Thread ts1 = new Thread(() -> bank1.transfer("Alice", "Bob", BigDecimal.valueOf(300)), "Thread-1");
//        Thread ts2 = new Thread(() -> bank1.transfer("Bob", "Alice", BigDecimal.valueOf(500)), "Thread-2");
//
//        ts1.start();
//        ts2.start();
//        BankEmailService service = new BankEmailService();
//        service.startEmailJob();
//
//        Thread.sleep(5000); // let it run for 5 seconds
//
//        service.stopEmailJob(); // stop the job
        ExchangeRateService service = new ExchangeRateService();

        // Thread-1 updating rate frequently
        Thread updater1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                service.updateExchangeRate(83.00 + Math.random());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }, "Updater-1");

        // Thread-2 updating simultaneously
        Thread updater2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                service.updateExchangeRate(83.50 + Math.random());
                try { Thread.sleep(700); } catch (InterruptedException e) {}
            }
        }, "Updater-2");

        // Thread-3 reading rate constantly
        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() +
                        " reads current rate: " + service.getExchangeRate());
                try { Thread.sleep(300); } catch (InterruptedException e) {}
            }
        }, "Reader");

        updater1.start();
        updater2.start();
        reader.start();

//        updater1.join();
//        updater2.join();
//        reader.join();
    }

    private static void runConcurrentWithdrawals(Bank bank) throws InterruptedException {
        Thread t1 = new Thread(() -> bank.withdrawAmount("Yash", new BigDecimal("500")));
        Thread t2 = new Thread(() -> {
            try {
                bank.withdrawAmount("Yash", new BigDecimal("700"));
            } catch (InsufficientFundsException e) {
                System.out.println(e.getMessage());
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Balance after transactions: " + bank.getUserBalance("Yash"));
    }
}
