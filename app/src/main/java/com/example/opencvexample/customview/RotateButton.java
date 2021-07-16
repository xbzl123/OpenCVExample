package com.example.opencvexample.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class RotateButton extends androidx.appcompat.widget.AppCompatImageView {
    public RotateButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("test","onTouchEvent ="+event.getAction());

        this.animate().setDuration(3000);
        this.animate().rotation(360);
        this.animate().start();
        return super.onTouchEvent(event);
    }
}
