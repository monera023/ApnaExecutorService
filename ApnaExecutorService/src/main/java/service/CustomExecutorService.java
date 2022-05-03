package service;

import entities.ScheduleTask;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomExecutorService implements  ExecutorService {
    private PriorityQueue<ScheduleTask> taskQueue = new PriorityQueue<ScheduleTask>(Comparator.comparingLong(ScheduleTask::getScheduledTime));
    private Lock lock = new ReentrantLock();
    private Condition newTaskAdded = lock.newCondition();
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    @Override
    public void init() {
        long timeToSleep = 0;
        while(true) {
            lock.lock();
            try {
                while(taskQueue.isEmpty()) {
                    System.out.println("Queue empty..");
                    newTaskAdded.await();
                }

                while(!taskQueue.isEmpty()) {
                    timeToSleep =  taskQueue.peek().getScheduledTime() - System.currentTimeMillis();
                    System.out.println("Checking time for task.." + taskQueue.peek().getTaskId() + "..." + timeToSleep);
                    if(timeToSleep <= 0) {
                        System.out.println("Maaro time aayo.. " + taskQueue.peek().getTaskId());
                        break;
                    }
                    newTaskAdded.await(timeToSleep, TimeUnit.MILLISECONDS);
                }

                // Now we can schedule the task

                ScheduleTask task = taskQueue.poll();
                long newScheduledTime = 0;
                switch (task.getTaskType()) {
                    case 1:
                        executor.submit(task.getRunnable());
                        break;
                    case 2:
                        newScheduledTime = System.currentTimeMillis() + task.getUnit().toMillis(task.getPeriod());
                        executor.submit(task.getRunnable());
                        task.setScheduledTime(newScheduledTime);
                        taskQueue.add(task);
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error..." + e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void schedule(Runnable command, long delay, TimeUnit unit, String taskId) {
        lock.lock();
        try {
            long scheduledTime = System.currentTimeMillis() + unit.toMillis(delay);
            ScheduleTask task = new ScheduleTask(command, scheduledTime, 1, 0, 0, unit, taskId);
            taskQueue.add(task);
            newTaskAdded.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, String taskId) {
        lock.lock();
        try {
            long scheduledTime = System.currentTimeMillis() + unit.toMillis(initialDelay);
            ScheduleTask task = new ScheduleTask(command, scheduledTime, 2, period, initialDelay, unit, taskId);
            taskQueue.add(task);
            newTaskAdded.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
