package com.example.opencvexample.enum_test;

public enum Face {
    ONE(1),TWO(2),THREE(3),FOUR(4),FIVE(5),SIX(6);

    private int num = 0;
    Face(int i) {
        this.num = i;
    }
    public int getNum() {
        return num;
    }
}
