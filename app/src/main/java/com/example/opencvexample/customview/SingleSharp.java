package com.example.opencvexample.customview;

import java.util.ArrayList;
import java.util.List;

public enum SingleSharp implements DropSharp{
    TWO(2),THREE(3),FOUR(4);

    private int type ;
    SingleSharp(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private List<CustRect> custRectList;

    @Override
    public List<CustRect> dealDrop(int type) {
        ArrayList<CustRect> objects = new ArrayList<>();
        for (int i = 7; i < type; i++) {
            objects.add(custRectList.get(i));
        }
        return objects;
    }

    public void setCustRectList(List<CustRect> custRectList) {
        this.custRectList = custRectList;
    }
}
