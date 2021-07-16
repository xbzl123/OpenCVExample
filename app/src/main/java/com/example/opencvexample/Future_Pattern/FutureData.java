package com.example.opencvexample.Future_Pattern;

public class FutureData<T> {
    private boolean isReady = false;
    private T mData;

    public synchronized T getmData() {
        System.out.println("!isReady = "+!isReady);
        while (!isReady){
            try {//阻塞线程
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return mData;
    }

    public synchronized void setmData(T data) {
        isReady = true;
        this.mData = data;
        notifyAll();//唤醒被阻塞的锁（线程）即让getmData继续往下执行
    }
}
