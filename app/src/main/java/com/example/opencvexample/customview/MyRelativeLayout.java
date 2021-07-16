package com.example.opencvexample.customview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.opencvexample.R;

public class MyRelativeLayout extends RelativeLayout {
    private boolean isChange;

    public MyRelativeLayout(Context context) {
        super(context);

    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_relativelayout, this);
//        MotionLayout motionLayout = findViewById(R.id.motion_1);
        Log.e("SunRiseMotionLayout","view = "+view);
        //        // 获取控件
//        ImageView imageView = (ImageView) findViewById(R.id.sun_runing);
//        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
//            float v1 = 0;
//            @Override
//            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
//
//            }
//
//            @Override
//            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
//                if(v > 0.5 && v > v1){
//                    if(!isChange){
//                        imageView.setBackgroundResource(R.drawable.ic_baseline_bedtime_24);
//                        isChange = true;
//                    }
//                }else if(v < 0.5 && v < v1){
//                    if(!isChange){
//                        imageView.setBackgroundResource(R.drawable.ic_baseline_wb_sunny_24);
//                        isChange = true;
//                    }
//                }
//                v1 = v;
//            }
//
//
//            @Override
//            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
//                isChange = false;
//            }
//
//            @Override
//            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
//
//            }
//        });
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
