package com.example.opencvexample;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import com.module.picturerecognition.JavaApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomPlayVideoActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_play_video);
        ConstraintLayout constraintLayout = findViewById(R.id.container);
        constraintLayout.addView(new CustomSurfaceView(this));

    }


    private class CustomSurfaceView extends GLSurfaceView implements  Runnable,SurfaceHolder.Callback {
        public CustomSurfaceView(Context context) {
            super(context);
        }

        public CustomSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            new Thread(this).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
        String receviceVideoPath = Environment.getExternalStorageDirectory() + "/yuv/A.h264";

        @Override
        public void run() {
//            File file = new File("/sdcard/class.mp4");
//            Log.e("===","file :"+file.exists());
            JavaApi.playMP4(Environment.getExternalStorageDirectory()+"/yuv/test.mp4",getHolder().getSurface());
//            JavaApi.play(getHolder().getSurface());
        }
    }
}
