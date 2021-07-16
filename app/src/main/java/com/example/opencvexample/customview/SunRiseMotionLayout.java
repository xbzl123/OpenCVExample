package com.example.opencvexample.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.opencvexample.R;

public class SunRiseMotionLayout extends MotionLayout {

    public SunRiseMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.sun_run, this);
//        // 获取控件
        ImageView imageView = (ImageView) view.findViewById(R.id.sun_runing);
        MotionLayout motionLayout = view.findViewById(R.id.motion_1);
//        View view = View.inflate(context, R.layout.sun_run, this);
//        Log.e("SunRiseMotionLayout","imageView = "+imageView);
        motionLayout.setTransitionListener(new TransitionListener() {
            private float v1 = 0;
            private boolean isChange;

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                if(v > 0.5 && v > v1){
                    if(!isChange){
                        imageView.setBackgroundResource(R.drawable.ic_baseline_bedtime_24);
                        isChange = true;
                    }
                }else if(v < 0.5 && v < v1){
                    if(!isChange){
                        imageView.setBackgroundResource(R.drawable.ic_baseline_wb_sunny_24);
                        isChange = true;
                    }
                }
                v1 = v;
            }


            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                isChange = false;
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        MotionScene motionScene = new MotionScene(this);
//        MotionScene.Transition transition = getTransition(R.id.transition);
//        int startConstraintSetId = transition.getStartConstraintSetId();
//        ConstraintSet startConstraintSet = getConstraintSet(startConstraintSetId);
//        startConstraintSet.clone(this);
//
//        int endConstraintSetId = transition.getEndConstraintSetId();
//        ConstraintSet endConstraintSet = getConstraintSet(endConstraintSetId);
//        endConstraintSet.clone(this);
//
//        setTransition(transition);
//        rebuildScene();
    }
}
