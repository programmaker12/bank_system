//package com.project.bank_system.util;
//
//import com.project.bank_system.entity.Account;
//import com.project.bank_system.repository.AccountRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class DataGenerator implements CommandLineRunner {
//
//    private final AccountRepository accountRepository;
//    private final Random random = new Random();
//
//    public DataGenerator(AccountRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (accountRepository.count() > 0) return; // Avoid duplicate generation
//
//        System.out.println("Generating large dummy data...");
//
//        List<Account> accounts = new ArrayList<>();
//        for (int i = 1; i <= 100_000; i++) { // ✅ Generate 1 lakh records
//            String username = "user" + i;
//            BigDecimal balance = BigDecimal.valueOf(random.nextInt(1_000_000));
//            String status = (i % 5 == 0) ? "INACTIVE" : "ACTIVE";
//
//            accounts.add(new Account(null, username, balance, status));
//
//            // Batch insert every 1000 to reduce memory usage
//            if (i % 1000 == 0) {
//                accountRepository.saveAll(accounts);
//                accounts.clear();
//                System.out.println("Inserted " + i + " records...");
//            }
//        }
//
//        System.out.println("✅ Data generation complete!");
//    }
//}
