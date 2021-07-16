package com.example.opencvexample.hook;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestA {
    private String name = "rabbit";

    public void Method(){
        System.out.println("name is "+name);
    }
    static void count(int x){
        x = 200;

    }

    public static void main(String[] args){
        int x = 10;
        count(10);
        System.out.println("x = "+x);
        BlockingQueue<TestA> testAQueue = new LinkedBlockingQueue<>();
        TestA a = new TestA();
        //采用Hook
        try {
            Field field = a.getClass().getDeclaredField("name");
            field.setAccessible(true);
            System.out.println("name = "+field.get(a));
            field.set(a,"tiger");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Integer[] data = new Integer[]{2,1,6,4,8,5,91,23,13,11};
        popuSort(data);
        a.Method();
    }

    public static void popuSort(Integer[] data){
        if(data == null || data.length < 2)return;
//        int temp;
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < data.length - 1 - i; j++) {
//                if (data[j] > data[j + 1]) {
//                    temp = data[j + 1];
//                    data[j + 1] = data[j];
//                    data[j] = temp;
//                }
//            }
//        }
//        Arrays.sort(data);

        Integer a=127;
        Integer b=127;
        Integer a1=128;
        Integer b1=128;
        System.out.println(a==b); //false   1
        System.out.println(a1==b1); //true  2
        System.out.println(a1.equals(b1)); //true  2

        List<Integer> list = Arrays.asList(data);
        Collections.sort(list);
        Collections.reverse(list);
        System.out.println("=========arrays="+list);

        Map<Integer,String> map = new HashMap<>();
        for (Object t: list) {
        }
    }
}
