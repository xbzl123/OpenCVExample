package com.example.opencvexample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;
import java.util.ArrayList;

public class AppUtils {

    public static String getFileProvider(Context context){
        return context.getApplicationInfo().packageName + ".fileprovider";
    }

    public static Uri getUriForFile(Context context,File file){
        return FileProvider.getUriForFile(context,getFileProvider(context),file);
    }

    public static void installApk(Activity activity, File file){
        if(!file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Uri uri = getUriForFile(activity,file);
            intent.setDataAndType(uri,"application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        activity.startActivity(intent);

    }
}
