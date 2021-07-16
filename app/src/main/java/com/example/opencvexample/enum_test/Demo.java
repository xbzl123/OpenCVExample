package com.example.opencvexample.enum_test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

public class Demo {
    //可变操作符的应用
    static int min(int...args){
        if(args.length == 0){
            throw new IllegalArgumentException("参数错误");
        }
        int min = args[0];
        for (int i: args) {
            if(i<min){
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
//        System.out.println(Plant.EARTH.getValue());
//        double x = Double.parseDouble(args[0]);
//        double y = Double.parseDouble(args[1]);
        double x = 4.0;
        double y = 5.0;
        double z = 14.0;
        double l = 40.0;
        System.out.println(min(9,6,8,1));
        System.out.println(BasicOperation.dealOperation(BasicOperation.PULS,x,y));
//        System.out.printf("%f %s %f = %f %n",x,BasicOperation.PULS.getOper(),y,
//                BasicOperation.PULS.apply(x, y));
//        System.out.printf("%f %s %f = %f %n",z,BasicOperation.MINUS.getOper(),y,
//                BasicOperation.MINUS.apply(z, y));
//        System.out.printf("%f %s %f = %f %n",x,BasicOperation.TIMES.getOper(),y,
//                BasicOperation.TIMES.apply(x, y));
//        System.out.printf("%f %s %f = %f %n",l,BasicOperation.DIVIDE.getOper(),y,
//                BasicOperation.DIVIDE.apply(l, y));
//        testOperation(Arrays.asList(BasicOperation.values()),x,y);
        operation(BasicOperation.class,l,y);
        System.out.println("Arrays: "+ Arrays.toString(toArray(123,"aaa","bbb","ccc","ddd","eee")));
        System.out.printf("小梅 周一 去加班3小时 所得 ：%f %s %n" ,OverTimeCalculate.MONDAY.calculate(3,20),"元");
        System.out.println("小梅 周日 去加班3小时 所得 ：" +OverTimeCalculate.SUNDAY.calculate(3,20)+"元");

        //掷骰子
        Collection<Face> faces = EnumSet.allOf(Face.class);
        Collections.synchronizedCollection(faces);
        for (Face face1:faces) {
            for (Face face2:faces) {
                System.out.println("face is "+face1.getNum()+","+face2.getNum());
            }
        }

        //有100块，买东西10元的，20的。。。能买几件
        int cash = 100;
        int count = 0;
        for (int price = 10; price <= cash; price += 10) {
            cash-=price;
            count++;
        }
        System.out.println("count is "+count);

    }
    //这种写法也可以
//    private static void test(Class<? extends Operation> basicOperationClass, double x, double y) {
    private static <T extends Operation> void test(Class<T> basicOperationClass, double x, double y) {
        for (Operation operation:basicOperationClass.getEnumConstants()) {
            System.out.printf("%f %s %f = %f %n",x,operation,y,operation.apply(x,y)
            );
        }
    }

    private static void operation(Class<BasicOperation> basicOperationClass, double x, double y){
        for (BasicOperation operation:basicOperationClass.getEnumConstants()) {
            System.out.printf("%f %s %f = %f %n",x,operation.getOper(),y,operation.apply(x,y)
            );
        }
    }
    private static void testOperation(Collection<? extends Operation> basicOperationClass, double x, double y) {
        for (Operation operation:basicOperationClass) {
            System.out.printf("%f %s %f = %f %n",x,operation,y,operation.apply(x,y));
        }
    }

    //泛型 + 可变参数
    static <T> T[] toArray(T...arg){
        return arg;
    }
}
