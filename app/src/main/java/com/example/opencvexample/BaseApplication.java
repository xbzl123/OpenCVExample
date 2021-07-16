package com.example.opencvexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.opencvexample.skin.QDSkinManager;
import com.example.opencvexample.utils.FixUtils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import java.lang.reflect.Field;

public class BaseApplication extends MultiDexApplication {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        QMUISwipeBackActivityManager.init(this);
        context = getApplicationContext();

        int[] systemeThemeAttr = new int[]{R.attr.colorPrimary};
        TypedArray array = this.obtainStyledAttributes(R.style.AppTheme, systemeThemeAttr);
        Field[] declaredFields = array.getClass().getDeclaredFields();
//        for (Field field:declaredFields) {

        Log.e("liangD","getColorStateList="+array.getColorStateList(0));

        Log.e("liangD","field="+declaredFields[0].getName());
//        }
        Log.e("liangD","colorPrimary="+array.getColor(0,100));

        array.recycle();


        QDSkinManager.install(this);

        NetworkCheck1();
        super.onCreate();
    }

    private static Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void NetworkCheck1() {
        ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImp();
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest networkRequest = builder.build();
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        FixUtils.loadFixedDex(this);

    }


    public static Context getContext() {
        return context;
    }
}
