package src;

import java.util.concurrent.*;

public class TimeLimit {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            System.out.println(Thread.currentThread().getId()+" : Starting Thread");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Thread");
            }
            System.out.println(Thread.currentThread().getId()+" : Stopping Thread");
        });

        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Execution timed out!");
            System.out.println("Cancelled : "+future.cancel(true));
        } catch (Exception e) {
            System.out.println("An exception occurred");
        } finally {
            executor.shutdown();
        }

    }
}
