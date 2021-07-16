package com.example.opencvexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if(TextUtils.equals(name,"TextView")){
                    Button button = new Button(SkinActivity.this);
                    button.setText("This a test!");
                    button.setOnClickListener(v -> {
                        startActivity(new Intent(SkinActivity.this,ImageShowActivity.class));
                    });
                    return button;
                }
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }
}
