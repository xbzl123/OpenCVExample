package com.example.opencvexample.observable;

import java.util.Observable;
import java.util.Observer;

public class GetObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("我的工资是："+Integer.valueOf((Integer) arg));
    }
}
