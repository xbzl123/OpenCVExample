package com.example.opencvexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.opencvexample.utils.Constants;
import com.example.opencvexample.utils.FileUtils;
import com.example.opencvexample.utils.FixUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TinkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinker);
        findViewById(R.id.show).setOnClickListener(v -> {
            ParamTest paramTest = new ParamTest();
            paramTest.math(this);
//            Toast.makeText(this, "result = "+result, Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.fix).setOnClickListener(v -> {
            fix();
        });
    }

    private void fix() {

        //用来替换旧的dex
        File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);
        //旧的dex
        File targetFile = new File(getDir(Constants.DEX_DIR,MODE_PRIVATE).getAbsolutePath()+
                File.separator + Constants.DEX_NAME);
        if(targetFile.exists()){
            targetFile.delete();
            Toast.makeText(this, "删除原有的文档", Toast.LENGTH_SHORT).show();
        }
        try {
            FileUtils.copyFile(sourceFile,targetFile);
            Toast.makeText(this, "复制文档成功", Toast.LENGTH_SHORT).show();

            FixUtils.loadFixedDex(this);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AssetManager assetManager;
    Resources newResource;
    private void fixResource(String apkpath) {
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssertPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath",String.class);
            addAssertPathMethod.setAccessible(true);
            addAssertPathMethod.invoke(assetManager,apkpath);
            //手动实例化
            Method ensureStringBlocksMethod = assetManager.getClass().getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocksMethod.setAccessible(true);
            ensureStringBlocksMethod.invoke(assetManager);

            Resources supResource = getResources();
            newResource = new Resources(assetManager,supResource.getDisplayMetrics(),supResource.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public Resources getResources() {
        return newResource==null?super.getResources():newResource;
    }

    @Override
    public AssetManager getAssets() {
        return (assetManager == null)?super.getAssets():assetManager;
    }
//    @Override
//    public Resources getResources() {
//        if(getApplication() != null && getApplication().getResources()!= null){
//            return getApplication().getResources();
//        }
//        return super.getResources();
//    }
//
//    @Override
//    public AssetManager getAssets() {
//        if(getApplication()!= null && getApplication().getAssets() != null){
//            return getApplication().getAssets();
//        }
//        return super.getAssets();
//    }
}
