//
// Created by Administrator on 2019/8/19.
//
#include <android/bitmap.h>
#include "utils.h"
#include "include/opencv2/core/mat.hpp"
#include "include/opencv2/core/base.hpp"
#include "include/opencv2/core/cvdef.h"
#include "include/opencv2/video.hpp"
#include <android/log.h>
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,"android_native_log",__VA_ARGS__)

void bitmap2Mat(JNIEnv *env,jobject bitmap,Mat *mat, bool needPremultiplyAlpha){
    AndroidBitmapInfo info;
    void *pixels = 0;
    Mat &dst = *mat;
    //获得bitmap信息
    CV_Assert(AndroidBitmap_getInfo(env,bitmap,&info)>=0);
    //必须是rgba8888 rgb565
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888
    ||info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    //lock 获得数据
    CV_Assert(AndroidBitmap_lockPixels(env,bitmap,&pixels)>=0);
    CV_Assert(pixels);
    dst.create(info.height,info.width,CV_8UC4);
    if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
        LOGE("bitmap2Mat： RGBA_8888 bitmap %s",bitmap);
        Mat tmp(info.height,info.width,CV_8UC4,pixels);
    if(needPremultiplyAlpha)
        cvtColor(tmp,dst,COLOR_mRGBA2RGBA);
        else
            tmp.copyTo(dst);
    }else{
        LOGE("bitmap2Mat： BGR565 bitmap ");
        Mat tmp(info.height,info.width,CV_8UC2,pixels);
        cvtColor(tmp,dst,COLOR_BGR5652RGBA);
    }
    AndroidBitmap_unlockPixels(env,bitmap);

}

void mat2Bitmap(JNIEnv *env,Mat mat,jobject bitmap, bool needPremultiplyAlpha){
    AndroidBitmapInfo info;
    void *pixels = 0;
    //获取bitmap信息
    CV_Assert(AndroidBitmap_getInfo(env,bitmap,&info)>=0);
    //bitmap的格式必须是rgba8888或者RGB565
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
    info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(mat.dims == 2 && info.height == (uint32_t)mat.rows&&
    info.width == (uint32_t)mat.cols);
    CV_Assert(mat.type() == CV_8UC1 || mat.type() == CV_8UC3 || mat.type() == CV_8UC4);
    CV_Assert(AndroidBitmap_lockPixels(env,bitmap,&pixels)>=0);
    CV_Assert(pixels);
    if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
        Mat tmp(info.height,info.width,CV_8UC4,pixels);
        if(mat.type() == CV_8UC1){
            cvtColor(mat,tmp,COLOR_GRAY2BGRA);
        }else if(mat.type() == CV_8UC3){
            cvtColor(mat,tmp,COLOR_RGB2BGR);

        }else if(mat.type() == CV_8UC4){
            if(needPremultiplyAlpha)
            cvtColor(mat,tmp,COLOR_RGBA2mRGBA);
            else
                mat.copyTo(tmp);
        }
    } else{
        Mat tmp(info.height,info.width,CV_8UC2,pixels);
        if(mat.type() == CV_8UC1){
            cvtColor(mat,tmp,COLOR_GRAY2BGR565);
        }else if (mat.type() == CV_8UC3){
            cvtColor(mat,tmp,COLOR_RGB2BGR565);
        }else if (mat.type() == CV_8UC4){
            cvtColor(mat,tmp,COLOR_RGBA2BGR565);
        }
    }
    AndroidBitmap_unlockPixels(env,bitmap);
}

jobject createBitmap(JNIEnv *env,Mat srcData,jobject config){
    jclass java_bitmap_class = (jclass)env->FindClass("android/graphics/Bitmap");
    jmethodID mid = env->GetStaticMethodID(java_bitmap_class,"createBitmap",
    "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jobject bitmap = env->CallStaticObjectMethod(java_bitmap_class,mid,srcData.cols,srcData.rows,config);
    mat2Bitmap(env,srcData,bitmap,0);
    return bitmap;
}

