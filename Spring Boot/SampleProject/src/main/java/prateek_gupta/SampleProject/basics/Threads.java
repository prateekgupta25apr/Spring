package src;

public class Threads extends Thread {
    public static long startTime=0L;

    public Threads(String name) {
        super(name);
        System.out.println(getTimeDifference()+" : "+getName() +
                " - 1. New state: Thread is created.");
    }

    @Override
    public synchronized void start() {
        System.out.println(getTimeDifference()+" : "+getName() +
                " - 2. Runnable state: Thread is started.");
        super.start();
    }

    @Override
    public void run() {
        try {
            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 3. Running state: Thread is running.");

            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 3. Running state: Processing started.");
            Thread.sleep(1000);
            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 3. Running state: Processing ended.");

            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 4. Blocked state: Thread is waiting to acquire a lock.");
            synchronized (this) {
                System.out.println(getTimeDifference()+" : "+getName() +
                        " - 3. Running state: Thread acquired the lock and " +
                        "is executing synchronized code.");
                wait(1000);
            }
            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 3. Running state: Thread released the lock and is running again.");

            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 5. Timed Waiting state: Thread is sleeping for 2 seconds.");
            Thread.sleep(2000);

            System.out.println(getTimeDifference()+" : "+getName() +
                    " - 3. Running state: Thread is resumed and running.");
        } catch (InterruptedException e) {
            System.out.println(getTimeDifference()+" : "+getName() + " - Interrupted.");
        }

        System.out.println(getTimeDifference()+" : "+getName() +
                " - 6. Terminated state: Thread has finished execution.");
    }
    public static void main(String[] args) {
        startTime=System.currentTimeMillis();
        Threads thread = new Threads("Thread-1");

        thread.start();

        try {
            synchronized (thread) {
                Thread.sleep(2000);
                System.out.println(getTimeDifference()+" : "+
                        Thread.currentThread().getName() +  " notifying other threads");
                thread.notify();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    
    static long getTimeDifference(){
        return System.currentTimeMillis()-startTime;
    }
}
