package service;

public class BankEmailService {

    // Volatile ensures visibility across threads
    private boolean running = true;

    public void startEmailJob() {
        Thread jobThread = new Thread(() -> {
            System.out.println("Email job started...");

            while (running) {

                    // Simulate sending daily summary emails
                    System.out.println(Thread.currentThread().getName() + " sending emails...");
//                    Thread.sleep(1000); // simulate delay

            }

            System.out.println("Email job stopped gracefully.");
        });

        jobThread.start();
    }

    public void stopEmailJob() {
        System.out.println("Stopping email job...");
        running = false; // this update is visible to jobThread immediately
    }


}
