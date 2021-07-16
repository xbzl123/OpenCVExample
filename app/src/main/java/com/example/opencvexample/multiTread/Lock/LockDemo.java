package com.example.opencvexample.multiTread.Lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    ArrayList<Integer> objects = new ArrayList<>();
    Lock lock = new ReentrantLock();

    public static void main(String[] args){
        LockDemo lockDemo = new LockDemo();

        new Thread(()->{
            lockDemo.insert(Thread.currentThread());
        }).start();
        new Thread(()->{
            lockDemo.insert(Thread.currentThread());
        }).start();
    }

    private void insert(Thread currentThread) {
//        lock.lock();
        if(lock.tryLock()){
        try {
        System.out.println(currentThread.getName()+"，得到锁,objects size = "+objects.size());
        for (int i = 0; i < 1000; i++) {
            objects.add(i);
            }
        }finally {
            lock.unlock();
        System.out.println(currentThread.getName()+"，释放锁");
        }
        }
    }
}
