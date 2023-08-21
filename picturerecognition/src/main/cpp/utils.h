//
// Created by Administrator on 2019/8/19.
//

#ifndef OPENCVEXAMPLE_UTILS_H
#define OPENCVEXAMPLE_UTILS_H

#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include "include/opencv2/core/mat.hpp"

using namespace cv;

extern "C" {
    void bitmap2Mat(JNIEnv *env,jobject bitmap,Mat* mat, bool needPremultiplyAlpha = 0);
    void mat2Bitmap(JNIEnv *env,Mat mat,jobject bitmap, bool needPremultiplyAlpha = 0);
    jobject createBitmap(JNIEnv *env,Mat srcData,jobject config);
}

#endif //OPENCVEXAMPLE_UTILS_H
