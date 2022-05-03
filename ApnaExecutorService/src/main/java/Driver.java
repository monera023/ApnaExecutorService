import service.CustomExecutorService;

import java.util.concurrent.TimeUnit;

public class Driver {
    public static void main(String[] args) {
        CustomExecutorService customExecutorService = new CustomExecutorService();
        customExecutorService.schedule(getRunnableTask("Task1"), 500, TimeUnit.MILLISECONDS, "Task1");

        customExecutorService.schedule(getRunnableTask("Task2"), 3000, TimeUnit.MILLISECONDS, "Task2");

//        customExecutorService.scheduleAtFixedRate(getRunnableTask("Task3"), 2000, 2000, TimeUnit.MILLISECONDS, "Task3");

        customExecutorService.init();
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
