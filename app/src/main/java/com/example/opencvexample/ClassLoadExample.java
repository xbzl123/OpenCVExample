package com.example.opencvexample;


class people{
    static String people_str = "people_JVM";
    static {
        System.out.println("load people");
    }
}
class father extends people{
    final static String father_str = "father_JVM";
    static {
        System.out.println("load father");
    }
}

class son extends father{
    static String son_str = "son_JVM";
    static {
        System.out.println("load son");
    }
}
public class ClassLoadExample {

    public static void main(String[] args){
//        System.out.println("1......"+son.father_str);
        System.out.println("2......"+son.son_str);

    }
}
