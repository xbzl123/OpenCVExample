package com.example.opencvexample.skin;

import android.content.Context;
import android.content.res.Configuration;

import com.example.opencvexample.BaseApplication;
import com.example.opencvexample.R;
import com.qmuiteam.qmui.skin.QMUISkinManager;

public class QDSkinManager {

    public static final int SKIN_BLUE = 1;
    public static final int SKIN_DARK = 2;
    public static final int SKIN_WHITE = 3;


    public static void install(Context context) {
        QMUISkinManager skinManager = QMUISkinManager.defaultInstance(context);
        skinManager.addSkin(SKIN_BLUE, R.style.app_skin_blue);
        skinManager.addSkin(SKIN_DARK, R.style.app_skin_dark);
        skinManager.addSkin(SKIN_WHITE, R.style.app_skin_white);
//        Log.e("QDSkinManager","the size of QDSkinManager is "+);
        boolean isDarkMode = (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        int storeSkinIndex = QDPreferenceManager.getInstance(context).getSkinIndex();
        if (isDarkMode && storeSkinIndex != SKIN_DARK) {
            skinManager.changeSkin(SKIN_DARK);
        } else if (!isDarkMode && storeSkinIndex == SKIN_DARK) {
            skinManager.changeSkin(SKIN_BLUE);
        }else{
            skinManager.changeSkin(storeSkinIndex);
        }
    }

    public static void changeSkin(int index) {
        QMUISkinManager.defaultInstance(BaseApplication.getContext()).changeSkin(index);
        QDPreferenceManager.getInstance(BaseApplication.getContext()).setSkinIndex(index);
    }

    public static int getCurrentSkin() {
        return QMUISkinManager.defaultInstance(BaseApplication.getContext()).getCurrentSkin();
    }
}
