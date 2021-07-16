package com.example.opencvexample.Static_enum;

public class Demo {
//    @Test
    public static void main(String[] a){
        DeviceBox instance = DeviceBox.INSTANCE;
        System.out.println("Demo the size is "+instance.getList().size());
        instance.getList().clear();
        System.out.println("Demo the last size is "+instance.getList().size());
    }
}
//    Initing success !231685785
//    Demo1 the size is 1,hashCode id is 114935352