package com.example.opencvexample.hook;

import java.lang.reflect.Field;

public class HookExample {
    private String label = "pear";

    public static void main(String[] args){
        HookExample hookExample = new HookExample();
            System.out.println("label=================>"+hookExample.label);
        try {
            Field field = hookExample.getClass().getDeclaredField("label");
            field.setAccessible(true);
            field.set(hookExample,"apple");
            System.out.println("label=================>"+hookExample.label);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
