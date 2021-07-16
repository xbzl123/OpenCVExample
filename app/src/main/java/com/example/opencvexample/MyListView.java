package com.example.opencvexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {
    private int mLastX;
    private int mLastY;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }



//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int)ev.getX();
//        int y = (int)ev.getY();
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:{
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            }
//            case MotionEvent.ACTION_UP:{
//                break;
//            }
//            case MotionEvent.ACTION_MOVE:{
//                int deX = x - mLastX;
//                int deY = y - mLastY;
//                if(Math.abs(deY)>Math.abs(deX)){
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//            break;
//
//        }
//        }
//        mLastX = x;
//        mLastY = y;
//        return super.dispatchTouchEvent(ev);
//
//    }
}
