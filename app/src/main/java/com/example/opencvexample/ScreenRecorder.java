package com.example.opencvexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ScreenRecorder extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        mResultCode = intent.getIntExtra("code", -1);
//        mResultData = intent.getParcelableExtra("data");
//        //mResultData = intent.getSelector();
//
//        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, Objects.requireNonNull(mResultData));
        return super.onStartCommand(intent, flags, startId);
    }
}
