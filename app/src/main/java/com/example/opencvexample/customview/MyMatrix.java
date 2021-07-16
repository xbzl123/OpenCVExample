package com.example.opencvexample.customview;
import java.util.Arrays;

public class MyMatrix {
    private int row;
    private int col;
    double[][] postion;

    public MyMatrix(int row, int col, double[][] postion) {
        this.row = row;
        this.col = col;
        this.postion = postion;
    }

    public void set(int row, int col, double value){
        postion[row-1][col-1] = value;
    }

    public double getPostion(int i, int i1) {
        return postion[row-1][col-1];
    }


    public MyMatrix transpose() {

        double tran[][] = new double[this.col][this.row];
        System.out.println("before transpose:");
        for (int i = 0; i < postion.length; i++) {
            System.out.println(Arrays.toString(postion[i]));
        }
        for(int i = 0;i<this.row;i++) {
            for(int j = 0;j<this.col;j++) {
                tran[j][i] = this.postion[i][j];
            }
        }

        MyMatrix another = new MyMatrix(this.col,this.row,tran);

        System.out.println("after transpose:");
        for (int i = 0; i < tran.length; i++) {
            System.out.println(Arrays.toString(tran[i]));
        }
        return another;

    }

    @Override
    public String toString() {
        return "MyMatrix{" +
                "row=" + row +
                ", col=" + col +
                ", postion=" + postion +
                '}';
    }
}
