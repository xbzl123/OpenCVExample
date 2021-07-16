package com.example.opencvexample.build_pattern;

class Demo {
    public static void main(String[] args) {
        Notification.Builder builder = new Notification.Builder();
        builder.setColor(1);
        builder.setContent("123");
        builder.setTitle("111");
        builder.setId(123456);
        Notification build = builder.build();
        System.out.println("----"+ build.toString());
    }
}
