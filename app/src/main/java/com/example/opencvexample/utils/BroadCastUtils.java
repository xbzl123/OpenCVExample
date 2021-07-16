package com.example.opencvexample.utils;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.List;

public class BroadCastUtils implements LifecycleEventObserver {
    private Context mContext;
    private IntentFilter intentFilter;
    private LocalBroadCastRecevicer localBroadCastRecevicer;
    private BroadCastCallBack broadCastCallBack;
    private volatile static BroadCastUtils instance;
    private List<String> mBroadcastList;


    public static synchronized BroadCastUtils getInstance(Context context) {
        if(instance == null){
            instance = new BroadCastUtils(context);
        }
        return instance;
    }

    private BroadCastUtils(Context context){
        mContext = context;
//        if(mContext instanceof Activity){
            ((ComponentActivity)mContext).getLifecycle().addObserver(this);
//        }else if (mContext instanceof FragmentActivity){
//            ((FragmentActivity)mContext).getLifecycle().addObserver(this);
//        }

        if(intentFilter == null) {
            intentFilter = new IntentFilter();
        }
        if(localBroadCastRecevicer == null){
            localBroadCastRecevicer = new LocalBroadCastRecevicer();
        }

    }

    public BroadCastUtils setBroadcastcmd(String broadcastcmd) {
        mBroadcastList.add(broadcastcmd);
        intentFilter.addAction(broadcastcmd);
        return instance;
    }
    public BroadCastUtils setBroadcastcmd(List<String> broadcastList) {
        mBroadcastList = broadcastList;
        for (String broadcastcmd:broadcastList) {
            intentFilter.addAction(broadcastcmd);
        }
        return instance;
    }

    public void register(){
        mContext.registerReceiver(localBroadCastRecevicer,intentFilter);
    }
    public void unregister(){
        mContext.unregisterReceiver(localBroadCastRecevicer);
    }

    public BroadCastUtils setBroadCastCallBack(BroadCastCallBack broadCastCallBack) {
        this.broadCastCallBack = broadCastCallBack;
        return instance;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

        //判断Context的类型
        if(mContext instanceof Application){
            if(event.name().equals("ON_CREATE")){
                register();
            }
        }else{
            if(event.name().equals("ON_RESUME")){
                Log.e("yyyy","==========event==>"+"ON_RESUME");
                register();
            }else if(event.name().equals("ON_STOP")){
                unregister();
            }
        }

    }

    class LocalBroadCastRecevicer extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                Log.e("yyyy","==========event==>"+intent);
                if(mBroadcastList.contains(intent.getAction())){
                broadCastCallBack.returnResult(intent);
                }
            }
    }

    public interface BroadCastCallBack {
        void returnResult(Intent intent);
    }

}
