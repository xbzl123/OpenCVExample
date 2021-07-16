package com.example.opencvexample.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class CustRect extends RectF {
     private int id;
     private boolean isSelect = false;
     private boolean isTouch = false;
    private boolean isDroping = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    @Override
    public String toString() {
        return "CustRect{" +
                "id=" + id +
                ", isSelect=" + isSelect +
                ", isTouch=" + isTouch +
                '}';
    }

    public boolean isDroping() {
        return isDroping;
    }

    public void setDroping(boolean droping) {
        isDroping = droping;
    }
}
