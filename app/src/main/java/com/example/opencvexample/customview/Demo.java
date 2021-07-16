package com.example.opencvexample.customview;

import java.util.Arrays;

public class Demo {
    public static void main(String[] args) {
        double[][] data = new double[2][3];
        for(int i = 0;i<2;i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = Math.random();
            }
        }
        MyMatrix myMatrix = new MyMatrix(2,3,data);
//        myMatrix.set(1,1,2);
        myMatrix.transpose();

    }
}
