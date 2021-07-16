package com.example.opencvexample;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.net.NetworkCapabilities.*;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImp extends ConnectivityManager.NetworkCallback {

    @Override
    public void onAvailable(Network network) {
        Log.e("network","有网络");
        super.onAvailable(network);
    }

    @Override
    public void onLost(Network network) {
        Log.e("network","没有网络");

        super.onLost(network);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {

        super.onCapabilitiesChanged(network, networkCapabilities);
        if(networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED)){
            if(networkCapabilities.hasTransport(TRANSPORT_WIFI)){
                Log.e("network","网络:WIFI");

            }else {
                Log.e("network","网络:other");

            }
        }
    }
}
