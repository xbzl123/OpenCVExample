package com.example.opencvexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.example.opencvexample.service.MyService;

public class MotionActivity extends AppCompatActivity {

    private boolean isChange;
    MyService.MyBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            MyService service1 = binder.getService();
            service1.setPostman(new MyService.PostMan() {
                @Override
                public void post(int num) {
                    Log.e("post","num ="+num);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

//    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion1);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        findViewById(R.id.stopcount).setOnClickListener(v->{
            binder.stopCount();
        });
//        MotionLayout viewById = findViewById(R.id.motion_1);
//        ImageView imageView = findViewById(R.id.sun_runing);
//        viewById.addTransitionListener(new MotionLayout.TransitionListener() {
//            float v1 = 0;
//            @Override
//            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
//            }
//
//            @Override
//            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
//                if(v > 0.5 && v > v1){
//                        if(!isChange){
//                            imageView.setBackgroundResource(R.drawable.ic_baseline_bedtime_24);
//                            isChange = true;
//                    }
//                }else if(v < 0.5 && v < v1){
//                        if(!isChange){
//                            imageView.setBackgroundResource(R.drawable.ic_baseline_wb_sunny_24);
//                            isChange = true;
//                        }
//                }
//                v1 = v;
//            }
//
//            @Override
//            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
//                isChange = false;
//            }
//
//            @Override
//            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
//            }
//        });

    }
}