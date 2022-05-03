package service;

import java.util.concurrent.TimeUnit;

public interface ExecutorService {
    void init();

    void schedule(Runnable command, long delay, TimeUnit unit, String taskId);

    void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String taskId);

    void shutDown();

}
