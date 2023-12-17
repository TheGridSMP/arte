package the.grid.smp.arte.util;

import the.grid.smp.arte.util.lambda.RunnableIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private final List<Callable<Void>> threads = new ArrayList<>();

    public void add(Callable<Void> runnable) {
        this.threads.add(runnable);
    }

    public void add(Runnable runnable) {
        this.add(() -> {
            runnable.run();
            return null;
        });
    }

    public void addCatchable(RunnableIO runnable) {
        this.add(() -> {
            try {
                runnable.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            executor.invokeAll(this.threads);
            executor.shutdown();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}