package com.example.opencvexample.observable;

import java.util.Observable;

public class Test {

    public static void main(String[] args){
        //1-4 (0,1,0,1) value = 0*2^0 + 1*2^1 + 0*2^2 + 1*2^3
        long value = (long)(0*Math.pow(2,0) + 1*Math.pow(2,1) + 0*Math.pow(2,2) + 1*Math.pow(2,3));
        System.out.println(">> result = "+((value >> 0) & 1));
        System.out.println("<< result = "+((value >> 1) & 1));
        System.out.println("<< result = "+((value >> 2) & 1));
        System.out.println("<< result = "+((value >> 3) & 1));

//        PostObservable postObservable = new PostObservable();
//        GetObserver getObserver = new GetObserver();
//        postObservable.addObserver(getObserver);
//        postObservable.setSalary(1000);
        MyObervable instance = MyObervable.INSTACE;
        instance.addObserver("a", new MyObervable.CustomObserver<User>(){
            @Override
            protected void updateMessage(Observable o, User arg) {
                System.out.println("a = "+arg.getName());
            }
        });
        instance.addObserver("a", new MyObervable.CustomObserver<Student>(){
            @Override
            protected void updateMessage(Observable o, Student arg) {
                System.out.println("a = "+arg.getName());
            }
        });

        instance.postMessage("a",new User(7,"Mr zhou"));
        instance.postMessage("a",new Student(8,"Mr wang"));

    }
}
