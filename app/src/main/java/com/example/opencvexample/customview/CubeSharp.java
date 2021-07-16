package com.example.opencvexample.customview;

import java.util.List;

public enum CubeSharp implements DropSharp{
    THREE(3),FOUR(4);

    CubeSharp(int i) {
    }
    public int orient;
    @Override
    public List<CustRect> dealDrop(int type) {
        return null;
    }

    public void setOrient(int orient) {
        this.orient = orient;
    }
}
