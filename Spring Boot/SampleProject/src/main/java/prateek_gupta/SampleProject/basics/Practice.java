package prateek_gupta.SampleProject.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Practice {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Thread going to sleep.");
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                System.out.println("Thread was interrupted.");
            }
        });
        thread.start();
        thread.interrupt();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}



