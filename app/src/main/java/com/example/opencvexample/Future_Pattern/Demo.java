package com.example.opencvexample.Future_Pattern;

import org.junit.Test;

public class Demo {
    @Test
    public void main(){
        Server server = new Server();
        FutureData<String> string = server.getString();

        String hello = "task 1 deal with";
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
            System.out.println(hello+" the process total spend time is "+(System.currentTimeMillis()-startTime)+" ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(string.getmData()+" the process total spend time is "+(System.currentTimeMillis()-startTime)+" ms");
    }
}
