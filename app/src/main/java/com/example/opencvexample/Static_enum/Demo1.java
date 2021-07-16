package com.example.opencvexample.Static_enum;

public class Demo1 {
//    @Test
    public static void main(String[] a){
        DeviceBox instance = DeviceBox.INSTANCE;
        System.out.println("Demo1 the size is "+ instance.getList().size());
    }
}
