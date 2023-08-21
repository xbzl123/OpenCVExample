//
// Created by Administrator on 2019/9/11.
//
#ifndef OPENCVEXAMPLE_NATIVE_LIB_H
#define OPENCVEXAMPLE_NATIVE_LIB_H

#endif //OPENCVEXAMPLE_NATIVE_LIB_H
jstring stringFromJNI(JNIEnv* env);
jobject findNumber(JNIEnv* env,jobject instance,jobject bitmap,jobject config);
jobject findIdNumber(JNIEnv* env,jobject instance,jobject bitmap,jobject config);
void cannyCheck(JNIEnv *env,jclass type,jobject src, jobject dst);

jobject FaceDetection_loadCascade(JNIEnv *env,jobject instance,jstring filePath_,jobject bitmap,jobject config);
jobject FaceDetection_faceDetectionSave(JNIEnv *env, jobject instance, jobject bitmap, jobject config,
                                        const char *path);
jint main1(JNIEnv* env,jobject bitmap, jobject comfig);
void play(JNIEnv* env,jobject instance,jobject surface);

void playMP4(JNIEnv* env,jobject instance,jstring _path,jobject surface);

int f(int n);
int popu();
//int bubbling();
//int mergeSort();
void MERGE(int *A, int b, int m, int e);
void MERGE_SORT(int *A, int b, int e);
void updateApp(JNIEnv* env,jobject instance,jstring input,jstring patch,jstring output);

int main();
static JNINativeMethod methods[] = {
        {"findIdNumber",    		"(Landroid/graphics/Bitmap;Landroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;",	(void*)findIdNumber},
        {"findPhotoNumber",    		"(Landroid/graphics/Bitmap;Landroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;",	(void*)findNumber},
        {"getStringFromJNI",    	"()Ljava/lang/String;",	  	(void*)stringFromJNI},
        {"FaceDetection_loadCascade","(Ljava/lang/String;Landroid/graphics/Bitmap;Landroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;",(void*)FaceDetection_loadCascade},
        {"main1",    	"(Landroid/graphics/Bitmap;Landroid/graphics/Bitmap$Config;)I",	  	(void*)main1},
        {"play",    	"(Ljava/lang/Object;)V",	  	(void*)play},
        {"playMP4",    	"(Ljava/lang/String;Ljava/lang/Object;)V",	  	(void*)playMP4},

        {"updateApp",    	"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",	  	(void*)updateApp},

};

