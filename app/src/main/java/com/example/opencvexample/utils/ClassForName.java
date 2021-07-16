package com.example.opencvexample.utils;

class Demo{
    static {
        System.out.println("加載静态块。。。。");
    }
}
public class ClassForName {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = ClassForName.class.getClassLoader();
        //没有加载静态块
        System.out.println("使用类加载器加载类。。。。");
        classLoader.loadClass("com.example.opencvexample.utils.Demo");

        //有加载静态块
        System.out.println("使用Class.forName。。。。");
        Class.forName("com.example.opencvexample.utils.Demo");


        System.out.println("使用Class.forName。。。。关闭静态块加载");
        //没有加载静态块
        Class.forName("com.example.opencvexample.utils.Demo",false,classLoader);
    }
}
