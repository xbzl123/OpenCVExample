package com.example.opencvexample.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private PostMan postman;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public void setPostman(PostMan postman) {
        this.postman = postman;
    }

    public class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }

        public void stopCount(){
            count = 1000;
        }
    }

    int count = 0;
    @Override
    public void onCreate() {
        new Thread(()->{
            while (count < 1000){
                if (postman!= null)
                postman.post(count++);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public interface PostMan{
         void post(int num);
    }
}
