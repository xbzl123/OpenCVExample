package com.example.opencvexample.defendcopy;

import java.util.Date;

class Demo {
    public static void main(String[] args) {
        Date start = new Date();
        Date end = new Date();
        Period period = new Period(start,end);
        //第一次修改（攻击）
        start.setTime(12);
        //第二次修改（攻击）
        period.getStart().setYear(20);
        System.out.println(period.getStart().getYear());
        System.out.println(Math.log(7+1));

    }
}
