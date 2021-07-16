package com.example.opencvexample.Future_Pattern;

public class Server {
    public FutureData<String> getString(){
        FutureData<String> data = new FutureData<>();
        new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.setmData("task 2 deal with");
        }).start();
        return data;
    }
}
