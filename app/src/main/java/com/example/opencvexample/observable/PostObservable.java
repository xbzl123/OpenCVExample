package com.example.opencvexample.observable;

import java.util.Observable;

public class PostObservable extends Observable {
    private int salary;

    public int getSalary() {
        return salary;
    }


    public void setSalary(int salary) {
        this.salary = salary;
        postObservable(salary);
    }

    public void postObservable(Object obj){
        setChanged();
        notifyObservers(obj);
    }

    public void clearObserver(){
        deleteObservers();
    }
}
