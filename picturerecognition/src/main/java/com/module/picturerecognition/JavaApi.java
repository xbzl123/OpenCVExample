package com.module.picturerecognition;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class JavaApi {

    private static final String TAG = JavaApi.class.getSimpleName();

    static {
        try {
            System.loadLibrary("picturerecognition");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "can't load OpenCV library, message: " + e.getMessage());
        }
    }
    public static native Bitmap findPhotoNumber(Bitmap bitmap, Config argb8888);
    public static native Bitmap findIdNumber(Bitmap bitmap, Config argb8888);

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String getStringFromJNI();
    public static native Bitmap FaceDetection_loadCascade(String path,Bitmap bitmap,Config argb8888);
    public static native int main1(Bitmap bitmap,Config argb8888);
    public static native void play(Object surface);
    public static native void playMP4(String path,Object surface);

    public static native void updateApp(String input,String patch,String output);

}
