package service;

import java.util.concurrent.atomic.AtomicReference;

public class ExchangeRateService {

    // Shared, thread-safe variable holding the latest rate
    private final AtomicReference<Double> usdToInrRate = new AtomicReference<>(83.12);

    // Thread simulating rate updates from API
    public void updateExchangeRate(double newRate) {
        double oldRate = usdToInrRate.getAndSet(newRate);
        System.out.println(Thread.currentThread().getName() +
                " updated rate from " + oldRate + " to " + newRate);
    }

    // Thread reading latest rate
    public double getExchangeRate() {
        return usdToInrRate.get();
    }

    public static void main(String[] args) throws InterruptedException {

    }
}
