package com.example.library.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;


public class NetworkUtils {
    private NetworkUtils() {
        throw new UnsupportedOperationException("不能初始化。");
    }

    public static boolean isAvailableNetwork(Context context) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static void gotoNetworkSettingsPage(Context context) {
        Intent intent = null;
        if (Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.setting.WirelessSetting");
        }
        context.startActivity(intent);
    }
}
