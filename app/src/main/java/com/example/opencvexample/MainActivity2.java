package com.example.opencvexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opencvexample.customview.CubeView;
import com.example.opencvexample.customview.HourView;
import com.example.opencvexample.customview.HourViewGroup;
import com.example.opencvexample.customview.MyView;
import com.example.opencvexample.customview.RotateButton;
import com.example.opencvexample.customview.WeekContainer;
import com.example.opencvexample.ui.mainactivity2.MainActivity2Fragment;

import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    private float lxpos,lypos,sxpos,sypos;
    private WeekContainer weekContainer;
    private HourViewGroup hourView;
    private CubeView cubeView;
    private Button start,stop,next,alarm;

    private ImageButton left,right ,top,bottom ;
    RotateButton rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity2_activity);
        FrameLayout frameLayout = findViewById(R.id.view_container);
//        Button button = new Button(this);
//        button.setText("XXX");
        frameLayout.removeAllViews();
        weekContainer = new WeekContainer(this);
        weekContainer.requestLayout();
        weekContainer.addViewsToContainer();
        hourView = new HourViewGroup(this);
//        frameLayout.addView(hourView);
        cubeView = new CubeView(this);
//        MyView myView = new MyView(this);
//        myView.setExampleString("test");
        frameLayout.addView(cubeView);

        start = findViewById(R.id.start);
        start.setOnClickListener((v)->{
            cubeView.drop();
        });
        stop = findViewById(R.id.stop);
        stop.setOnClickListener((v)->{
            cubeView.stop();
        });
        next = findViewById(R.id.next_page);
        next.setOnClickListener((v)->{
            startActivity(new Intent(this,MotionActivity.class));
        });

        rotation = findViewById(R.id.rotation);
        rotation.setOnClickListener((v)->{
            cubeView.gotoRotation();
        });

        left = findViewById(R.id.left);
        left.setOnClickListener((v)->{
            cubeView.gotoLeft();
        });

        right = findViewById(R.id.right);
        right.setOnClickListener((v)->{
            cubeView.gotoRight();
        });
        top = findViewById(R.id.top);
        top.setOnClickListener((v)->{
            cubeView.gotoRight();
        });

        bottom = findViewById(R.id.bottom);
        bottom.setOnClickListener((v)->{
            cubeView.gotoBottom();
        });

        bottom = findViewById(R.id.bottom);
        bottom.setOnClickListener((v)->{
            cubeView.gotoBottom();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
