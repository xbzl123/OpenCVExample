package com.example.opencvexample.enum_test;

public enum Plant {
    EARTH(1),MARS(2),SUN(3);

    private int value = 0;

    Plant(int size) {
        this.value = size;
    }

    public int getValue() {
        return value;
    }


//这个是不安全的写法
//    public int getValue() {
//        return ordinal()+1;
//    }
}
