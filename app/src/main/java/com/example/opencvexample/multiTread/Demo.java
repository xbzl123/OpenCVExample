package com.example.opencvexample.multiTread;

public class Demo {
    public static void main(String[] args){
        ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
        for (int i = 0; i < 10000; i++) {
            final int j = i * i;
            threadPoolManager.addTask(()->{
                System.out.println("the result is "+ (Math.sqrt(j)));
            });
        }
    }
}
