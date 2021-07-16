package com.example.opencvexample.utils;

import android.content.Context;
import android.util.Log;

import com.example.opencvexample.TinkerActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixUtils {
    private static HashSet<File> loadedDex = new HashSet<>();
    static {
        loadedDex.clear();
    }

    public static void loadFixedDex(Context context) {
        File file = context.getDir(Constants.DEX_DIR,Context.MODE_PRIVATE);
        File[] files = file.listFiles();
        for (File file1 : files) {
            //遍历安装的目录，如果有包含.dex结尾的或者文件名不是classes.dex的就加到loadedDex集合里面
            if(file1.getName().endsWith(Constants.DEX_SUFFIX) && !"classes.dex".equals(file1.getName())){
                loadedDex.add(file1);
            }
        }
        createDexClassLoader(context,file);
    }

    private static void createDexClassLoader(Context context, File file) {
        String optimizedDir = file.getAbsolutePath() + File.separator + "opt_dex";
        //
        File outFile = new File(optimizedDir);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        for (File dex : loadedDex) {
            DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(),
                    optimizedDir,null,context.getClassLoader());
            hotfix(dexClassLoader,context);
        }

    }

    private static void hotfix(DexClassLoader dexClassLoader, Context context)  {
        try {
            //获取系统的PathClassLoader
            PathClassLoader pathClassLoader = (PathClassLoader)context.getClassLoader();
            //获取系统的pathList属性（通过反射）
            Object systemPathList = ReflectUtils.getPathList(pathClassLoader);
            //获取自有的dexElements数组
            Object myElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(dexClassLoader));
            //获取系统的dexElements数组
            Object systemElemrnts = ReflectUtils.getDexElements(systemPathList);
            //合并数组，生成一个新的dexElements
            Object tempElements = ArrayUtils.combineArray(myElements,systemElemrnts);

            Log.e("www","====systemPathList="+ systemPathList.toString());
            //通过反射把新的pathList赋值给系统的pathList
            ReflectUtils.setField(systemPathList,systemPathList.getClass(),tempElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
