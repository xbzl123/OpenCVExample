package com.example.opencvexample.multiTread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private final ThreadPoolExecutor threadPoolExecutor;
    //    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();
    //懒汉单例模式
//    public static synchronized ThreadPoolManager getInstance(){
//        if(threadPoolManager != null){
//            return threadPoolManager;
//        }
//        return null;
//    }

    //使用静态内部类来创建单例
    public static ThreadPoolManager getInstance(){
     return ThreadPoolClassLoader.threadPoolManager;
    }
    private static class ThreadPoolClassLoader{
         private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
    }

    public void addTask(Runnable runnable){
        if(runnable != null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeTask(Runnable runnable){
        if(runnable != null){
            try {
                mQueue.remove(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ThreadPoolManager(){
        //自定义线程池
        threadPoolExecutor = new ThreadPoolExecutor(3, 10,
                15, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });
        threadPoolExecutor.execute(ddThread);
    }
    //创建调度线程
    public Runnable ddThread = new Runnable() {
        Runnable runn = null;
        @Override
        public void run() {
            while (true){
                try {
                    runn = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadPoolExecutor.execute(runn);
            }
        }
    };
}
