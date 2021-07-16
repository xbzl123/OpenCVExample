package com.example.opencvexample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.opencvexample.opengl.CameraGLSurfaceView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SecondActivity extends AppCompatActivity {

    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ConstraintLayout content_container = findViewById(R.id.content_container);
        CameraGLSurfaceView cameraGLSurfaceView = new CameraGLSurfaceView(this);
        content_container.addView(cameraGLSurfaceView);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            isBack = !isBack;
            cameraGLSurfaceView.changeCameraType(isBack);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
