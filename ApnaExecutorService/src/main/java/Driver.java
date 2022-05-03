import service.CustomExecutorService;

import java.util.concurrent.TimeUnit;

public class Driver {
    public static void main(String[] args) throws InterruptedException {
        CustomExecutorService customExecutorService = new CustomExecutorService();
        System.out.println("Producer thread::.." + Thread.currentThread().getName());

        Thread consumer = new Thread(customExecutorService::init);
        consumer.start();

        customExecutorService.schedule(getRunnableTask("Task1"), 3000, TimeUnit.MILLISECONDS, "Task1");
        customExecutorService.schedule(getRunnableTask("Task2"), 8000, TimeUnit.MILLISECONDS, "Task2");

        customExecutorService.shutDown();

        customExecutorService.schedule(getRunnableTask("Task3"), 900, TimeUnit.MILLISECONDS, "Task3");

//        customExecutorService.scheduleAtFixedRate(getRunnableTask("Task3"), 2000, 2000, TimeUnit.MILLISECONDS, "Task3");

        Thread.sleep(10000);


    }

    private static Runnable getRunnableTask(String task) {
        return () -> {
            System.out.println("Task.." + task + "..started at.." + System.currentTimeMillis() );
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task.." + task + "..finished at.." + System.currentTimeMillis());
        };
    }
}
