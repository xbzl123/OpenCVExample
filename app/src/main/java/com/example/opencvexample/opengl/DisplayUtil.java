package com.example.opencvexample.opengl;

import android.content.Context;

public class DisplayUtil {

    public static int getScreenW(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenH(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
