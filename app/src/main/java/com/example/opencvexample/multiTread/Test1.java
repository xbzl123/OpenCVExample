package com.example.opencvexample.multiTread;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Test1 {

    @Test
    public void main(){

//        ExecutorService executorService = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(4);
        Task task = new Task();
//        Future<Integer> integerFutureTask = executorService.submit(task);
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
        scheduledExecutorService.submit(integerFutureTask);
        try {
            System.out.println("是否运行在主线程："+ Thread.currentThread().getName());
            System.out.println("result is "+integerFutureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
//            System.out.println("运行在子线程...");
            System.out.println("是否运行在主线程："+ Thread.currentThread().getName());
            Integer result = new Integer(0);
            Thread.sleep(1000);
            for (int i = 0; i < 1000; i++) {
                result += i;
            }
            return result;
        }
    }
}
