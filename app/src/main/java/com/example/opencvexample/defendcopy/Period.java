package com.example.opencvexample.defendcopy;

import java.util.Date;

public class Period {
    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        if(start.compareTo(end) > 0){
            throw new IllegalArgumentException("时间段为零");
        }
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
    }

    public Date getStart() {
//        return start;
        return new Date(start.getTime());
    }

    public Date getEnd() {
//        return end;
        return new Date(end.getTime());
    }
}
