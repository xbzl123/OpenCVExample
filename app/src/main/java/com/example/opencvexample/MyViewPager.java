package com.example.opencvexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //1,继承父类
        return super.onInterceptTouchEvent(ev);
        //2.自己处理
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//                super.onInterceptTouchEvent(ev);
//                return false;
//        }
//        return true;
    }
}
