#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include "utils.h"

#include <opencv2/imgproc/imgproc_c.h>
#include "stdio.h"
#include<iostream>
#include <opencv2/imgproc/types_c.h>

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <unistd.h>

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,"android_native_log",__VA_ARGS__)
#include"opencv2/face/facerec.hpp"
#include"opencv2/highgui.hpp"
#include "native-lib.h"
#include "pthread.h"
#define DEFAULT_CARD_WIDTH 640
#define DEFAULT_CARD_HEIGHT 400

#define srcWidth 848
#define srcHeight 480

#define outWidth 640
#define outHeight 480

#define FIX_IDCARD_SIZE Size(DEFAULT_CARD_WIDTH,DEFAULT_CARD_HEIGHT)
JavaVM *g_p_jvm;
using namespace std;
using namespace cv;
using namespace cv::face;

extern "C"{
#include <libavformat/avformat.h>
#include <libavutil/pixfmt.h>
#include <libavutil/pixdesc.h>
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
#include <libavutil/opt.h>
#include <libavutil/mem.h>
#include <libavutil/time.h>
#include <libswresample/swresample.h>
extern int p_main(int argc, char *argv[]);
}
std::vector<AVPacket*> queues;//队列
SwrContext *swrContext;
uint8_t *out_buffer;
int out_channer_nb;
//同步锁
pthread_mutex_t pthread_mutex;
pthread_t mx;

//条件变量
pthread_cond_t cond;
double clocks;//从第一zhen开始所需要时间
AVRational time_bases;
SLObjectItf engineObject;
SLEngineItf engineEngine;
SLEnvironmentalReverbItf outputMixEnvironmentalReverb;
SLObjectItf outputMixObject;
SLObjectItf bqPlayerObject;
SLEffectSendItf bqPlayerEffectSend;
SLVolumeItf bqPlayerVolume;
SLPlayItf bqPlayerPlay;
SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue;
//注册函数
static int registerNatives(JNIEnv *env, jclass clazz, JNINativeMethod *gMethods,
                           int numMethods) {
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0)
        return -1;

    return 0;
}
//#define className "com/example/opencvexample/JavaApi"

//这是JNI_OnLoad的声明，必须按照这样的方式声明
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void *reserved)
{
    const char* className = "com/module/picturerecognition/JavaApi";
    JNIEnv *venv;
    if (vm->GetEnv((void**)&venv, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    if (g_p_jvm == NULL) {
        //保存全局JVM以便在子线程中使用
        venv->GetJavaVM(&g_p_jvm);
    }

    //注册函数表
    jclass cls = venv->FindClass(className);
    if(cls==NULL)
    {
        LOGE("cls is error");
    }
    registerNatives(venv, cls, methods, sizeof(methods)/sizeof(methods[0]));

    LOGE("JNI_OnLoad!");

    return JNI_VERSION_1_4;

    /* 
     * 这里可以找到要注册的类，前提是这个类已经加载到java虚拟机中。 
     * 这里说明，动态库和有native方法的类之间，没有任何对应关系。
     */
}
//extern "C" JNIEXPORT jstring JNICALL
void updateApp(JNIEnv* env,jobject instance,jstring input,jstring patch,jstring output){
    const char *input_ = env->GetStringUTFChars(input, 0);
    const char *patch_ = env->GetStringUTFChars(patch,0);
    const char *output_ = env->GetStringUTFChars(output,0);
    char *argv[] = {"", const_cast<char *>(input_), const_cast<char *>(output_),
                    const_cast<char *>(patch_)};
    LOGE("====================Patch start!=");
    p_main(4,argv);
    LOGE("====================Patch success!=");
    env->ReleaseStringUTFChars(input,input_);
    env->ReleaseStringUTFChars(patch,patch_);
    env->ReleaseStringUTFChars(output,output_);
}


//冒泡排序
int popu(){
    int test[] = {9,4,3,2,8,6,7,1};
    int len = (int)sizeof(test)/ sizeof(*test);
    int tmp;
    for (int i = 0; i < len ; ++i) {
        for (int j = 0; j < len-1-i; ++j) {
            if(test[j] > test[j+1]){
                tmp = test[j+1];
                test[j+1] = test[j];
                test[j] = tmp;
            }
        }
    }
    for (int i = 0; i < len; ++i) {
        cout<<test[i]<<endl;
//        LOGE("======================test的值是=%d",test[i]);

    }
    return 1;
}
jstring stringFromJNI(
        JNIEnv* env) {
    int test = f(10);
    for (int i = 0; i < 10; ++i) {
//        LOGE("======================第%d项的值是=%d",i,f(i));
    }

    float x,*prt = &x;
    *prt = 1.7;
    ++*prt;
//    LOGE("=====================x是=%f",x);
    popu();
//    bubbling();
    main();
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

//int mergeSort(){
//    int a[] = {16,9,5,3,1,7,4,2};
//    int len = sizeof(a)/ sizeof(int);
//    vector<map<int ,int>> vector1;
//    int tmp;
//    for (int i = 0; i < len; ++i) {
//        int group = len / 2;
//        int postion = len % 2;
//        for (int j = 0; j < group; ++j) {//4
//            map<int ,int> map1;
//            for (int k = 0; k < postion; ++k) {//2
//                if(a[i]>a[i+1]){
//                    tmp = a[i+1];
//                    a[i+1] = a[i];
//                    a[i] = tmp;
//                }
//                map1.insert(j,a[i]);
//            }
//            vector1.push_back(map1);
//        }
//    }
//    for (int i = 0; i < 4; ++i) {
//    }
//    return 1;
//}
//int bubbling(){
//    int a[] = {16,9,5,3,1,7,4,2};
//    int len = sizeof(a)/ sizeof(int);
//    int tmp;
//    for (int i = 0; i < len; ++i) {
//        LOGE("==============轮数=%d",i);
//        for (int j = 0; j < len - i - 1 ; ++j) {
//            if(a[j]>a[j+1]){
//                tmp = a[j+1];
//                a[j+1] = a[j];
//                a[j] = tmp;
//            }
//        }
//    }
//    for (int i = 0; i < len; ++i) {
//        LOGE("==============aa========第%d项的值是=%d",i,a[i]);
//    }
//    return 1;
//}
void MERGE(int *A, int b, int m, int e)

{
    int l = m-b+1, r = e-m, i;
    //创建左右2个数组，往里面补充数据
    int L[l+1], R[r+1];
    for(i=0; i< l; i++)
    {
        L[i] = A[b+i];
    }

    for (i=0; i< r; i++)
    {
        R[i] = A[m+i+1];
    }
    L[l] = 32767;
    R[r] = 32767;
    l = 0;
    r = 0;

    for(i=0; i< e-b+1; i++)
    {
        //数值小的先赋值到A数值里面
        if(L[l] < R[r])
        {
            A[b+i] = L[l];
            l ++;
        }
        else            {
            A[b+i] = R[r];
            r ++;
        }
    }
}



void MERGE_SORT(int *A, int b, int e)

{
    if(b < e)

    {
        int m = (b + e) / 2;
        MERGE_SORT(A, b, m);
        MERGE_SORT(A, m+1, e);
        MERGE(A, b, m, e);
    }
}

int main()
//合并排序
{
    int A[] = { 5, 2, 4, 7, 1, 3, 8 ,6,10};
    int lens, i;
    lens = sizeof(A)/ sizeof(int);
    LOGE("======================the lenghth of array is %d", lens);

    printf("Please Enter the lenghth of array:");
    scanf("%d", &lens);

    printf("Please Enter the elements of the array:");

    for(i=0; i< lens; i++)
        scanf("%d", &A[i]);

    MERGE_SORT(A, 0, lens-1);

//    printf("the result of the sort is:\n");

    for(i=0; i< lens; i++)
    {
//        LOGE("======================the result of the sort is %d", A[i]);
//        printf("%d ", A[i]);
    }
    return 0;

}
//斐波那契数列
int f(int n){
    if(n<=1)
        return 1;
    return f(n-1)+f(n-2);
}

//extern "C" JNIEXPORT jobject JNICALL
jobject findNumber(JNIEnv *env,jobject instance,jobject bitmap, jobject argb8888) {
    //1.将bitmap转化成Mat
    Mat src_img;
    Mat dst_img;
    bitmap2Mat(env,bitmap,&src_img);
    // LOGE("11111111111111111111==src_img=%d",src_img.size);
    //2.归一化
    Mat dst;
    resize(src_img,dst,FIX_IDCARD_SIZE);
    //3.灰度化处理
    cvtColor(src_img,dst,COLOR_BGR2GRAY);
    //4.二值化处理
    threshold(dst,dst,200,255,THRESH_BINARY);
    //颜色反转
    bitwise_not(dst,dst);
    //5.图片腐蚀处理
    Mat erodeElement = getStructuringElement(MORPH_RECT,Size(80,2));
    erode(dst,dst,erodeElement);
    //6.轮廓检测
    vector<vector<Point>> contours;
    vector<Rect> rects;
    findContours(dst,contours,RETR_TREE,CHAIN_APPROX_SIMPLE,Point(0,0));
    //7.获取身份证区域
    for (int i = 0; i < contours.size(); ++i) {
        Rect rect = boundingRect(contours.at(i));
        if(rect.width > rect.height*3 && rect.width < rect.height*6){
            rects.push_back(rect);
            //LOGE("66666666666666666==y=%d",i);
        }
    }
    int lowArea = 0;
    Rect finalRect;
    for (int i = 0; i < rects.size(); ++i) {
        Rect rect = rects.at(i);
//        Point point = rect.tl();
        int area = rect.area();
        if(area> lowArea){
            lowArea = area;
//            rect = rect + Size(-100,-90)/* + Point(100,50)*/;
            finalRect = rect;
//            LOGE("77777777777777==y=%d",lowPoint);

        }
    }
    //图形分割
    dst_img = src_img(finalRect);

    //返回身份证号码图片
    return createBitmap(env,dst_img,argb8888);
}

jobject findIdNumber(JNIEnv *env,jobject instance,jobject bitmap, jobject argb8888) {
    //1.将bitmap转化成Mat
    Mat src_img;
    Mat dst_img;
    bitmap2Mat(env,bitmap,&src_img);
    // LOGE("11111111111111111111==src_img=%d",src_img.size);
    //2.归一化
    Mat dst;
    resize(src_img,dst,FIX_IDCARD_SIZE);
    //3.灰度化处理
    cvtColor(src_img,dst,COLOR_BGR2GRAY);
    //4.二值化处理
    threshold(dst,dst,100,255,THRESH_BINARY);
    //5.图片腐蚀处理
    Mat erodeElement = getStructuringElement(MORPH_RECT,Size(40,10));
    erode(dst,dst,erodeElement);
    //6.轮廓检测
    vector<vector<Point>> contours;
    vector<Rect> rects;
    findContours(dst,contours,RETR_TREE,CHAIN_APPROX_SIMPLE,Point(0,0));
    //7.获取身份证区域
    for (int i = 0; i < contours.size(); ++i) {
        Rect rect = boundingRect(contours.at(i));
        if(rect.width > rect.height*8 && rect.width < rect.height*16){
            rects.push_back(rect);
            //LOGE("66666666666666666==y=%d",i);
        }
    }
    int lowPoint = 0;
    Rect finalRect;
    for (int i = 0; i < rects.size(); ++i) {
        Rect rect = rects.at(i);
        Point point = rect.tl();
        if(point.y > lowPoint){
            lowPoint = point.y;
            finalRect = rect;
            LOGE("77777777777777==y=%d",lowPoint);

        }
    }
    //图形分割
    dst_img = src_img(finalRect);
    //返回身份证号码图片
    return createBitmap(env,dst_img,argb8888);
}
// 加载人脸识别的级联分类器
CascadeClassifier cascadeClassifier;

jobject FaceDetection_loadCascade(JNIEnv *env ,jobject instance,jstring filePath_,jobject bitmap,jobject config) {
    const char *filePath = env->GetStringUTFChars(filePath_, 0);
    cascadeClassifier.load(filePath);
//    LOGE("======================filePath_==%s",filePath);
    jobject tmp = FaceDetection_faceDetectionSave(env, instance, bitmap, config, filePath);
    env->ReleaseStringUTFChars(filePath_, filePath);
    LOGE("人脸识别级联分类器加载成功");

    return tmp;
}

jobject FaceDetection_faceDetectionSave(JNIEnv *env, jobject instance, jobject bitmap, jobject config,
                                        const char *path) {
// opencv 操作图片操作的都是 矩阵 Mat
// 1. bitmap2Mat
Mat mat;

bitmap2Mat(env,bitmap,&mat);

Mat grayMat;
// 2. 转成灰度图，提升运算速度，灰度图所对应的 CV_8UC1 单颜色通道，信息量少 0-255 1u
cvtColor(mat, grayMat, CV_RGBA2GRAY);

// 3. 转换直方图均衡化补偿
Mat equalizeMat;
equalizeHist(grayMat, equalizeMat);

// 4. 检测人脸，这是个大问题
std::vector<Rect> faces;
vector<int> labels;
cascadeClassifier.detectMultiScale(equalizeMat, faces, 1.1, 5, 0 | CV_COVAR_SCALE,
Size(160, 160));

LOGE("检测到人脸的个数：%d", faces.size());
if (faces.size() == 1) {
Rect faceRect = faces[0];

// 画一个框框，标记出人脸
rectangle(mat, faceRect, Scalar(255, 155, 155), 3);
mat2Bitmap(env, mat, bitmap);
// 只裁剪人脸部分的直方均衡补偿
Mat saveMat = Mat(equalizeMat, faceRect);
// mat 保存成文件  png ,上传到服务器吧，接着下一张（眨眼，张嘴巴）
LOGE("====================FIND=%s",path);
const char *filepath = "/sdcard/xxx.png";
imwrite(filepath, saveMat);
//    Ptr<cv::face::EigenFaceRecognizer> model = cv::face::EigenFaceRecognizer::create();
//    Ptr<FaceRecognizer> model = EigenFaceRecognizer::create();
//    model->train(faces, labels);
//    model->save(filepath);
    return createBitmap(env,saveMat,config);

}
    return createBitmap(env,mat,config);
}

void cannyCheck(JNIEnv *env,jclass type,jobject src, jobject dst){
    //1.bitmap -> Mat
    Mat src_mat;
    bitmap2Mat(env,src,&src_mat);
    Mat gray_mat,dst_mat;
    // 2. 将图片处理成 Gray 可以提升处理速度
    cvtColor(src_mat, gray_mat, COLOR_BGRA2GRAY);
    // 2.2 3X3降噪处理
    blur(gray_mat,gray_mat,Size(3,3));
    // 3. 处理边缘检测
    Canny(gray_mat,dst_mat,50,30);
    // 4. mat->bitmap
    mat2Bitmap(env,dst_mat,dst);
}


jint main1(JNIEnv* env,jobject bitmap,jobject config)
{
//    VideoCapture cap(0);    //打开默认摄像头
//    if (!cap.isOpened())
//    {
//        return -1;
//    }
//    char *picpath = "/sdcard/xxx.png";
//    FILE *file = fopen(picpath,"wb");
//    Mat frame;
//    bitmap2Mat(env,bitmap,&frame);
//    Mat edges;
//    Mat gray;
//
//    CascadeClassifier cascade;
//    bool stop = false;
//    //训练好的文件名称，放置在可执行文件同目录下
//    cascade.load("/sdcard/lbpcascade_frontalface.xml");
//
//    Ptr<FaceRecognizer> modelPCA = EigenFaceRecognizer::create();
//    modelPCA->load("/sdcard/xxx.xml");
////
//    while (!stop)Renderer
//    {
////        cap >> frame;
//        //建立用于存放人脸的向量容器
//        vector<Rect> faces(0);
//
//        cvtColor(frame, gray, CV_BGR2GRAY);
//        //改变图像大小，使用双线性差值
//        //resize(gray, smallImg, smallImg.size(), 0, 0, INTER_LINEAR);
//        //变换后的图像进行直方图均值化处理
//        equalizeHist(gray, gray);
//
//        cascade.detectMultiScale(gray, faces,
//            1.1, 2, 0
//            //|CV_HAAR_FIND_BIGGEST_OBJECT
//            //|CV_HAAR_DO_ROUGH_SEARCH
//            /*| CV_HAAR_SCALE_IMAGE*/,
//            Size(30, 30));
//
//        Mat face;
//        Point text_lb;
//
//        for (size_t i = 0; i < faces.size(); i++)
//        {
//            if (faces[i].height > 0 && faces[i].width > 0)
//            {
//                face = gray(faces[i]);
//                text_lb = Point(faces[i].x, faces[i].y);
//                rectangle(frame, faces[i], Scalar(255, 0, 0), 1, 8, 0);
//            }
//        }
//
//        Mat face_test;
//
//        int predictPCA = 0;
//        if (face.rows >= 120)
//        {
//            resize(face, face_test, Size(92, 112));
//
//        }
//        //Mat face_test_gray;
//        //cvtColor(face_test, face_test_gray, CV_BGR2GRAY);
//
//        if (!face_test.empty())
//        {
//            //测试图像应该是灰度图
//            predictPCA = modelPCA->predict(face_test);
//        }
//
////        cout << predictPCA << endl;
//        LOGE("==================predictPCA=%d",predictPCA);
////        if (predictPCA == 40)
////        {
////            string name = "LiuXiaoLong";
////            putText(frame, name, text_lb, FONT_HERSHEY_COMPLEX, 1, Scalar(0, 0, 255));
////        }
//
////        imshow("face", frame);
//        if (waitKey(50) >= 0)
//            stop = true;
//    }

    return 0;
}

void play(JNIEnv *env,jobject instance, jobject surface){
    // TODO
    FILE *src_file =fopen("/sdcard/class.yuv", "rb");
    if(src_file == NULL){
        LOGE("yuv420.yuv open failed !");
        return;
    }
    //设置输入视频文件的大小和视频格式
    const int src_w=srcWidth,src_h=srcHeight;
    AVPixelFormat src_pixfmt=AV_PIX_FMT_YUV420P;
    //获取输入视频文件每个像素占有的BIT数
    int src_bpp=av_get_bits_per_pixel(av_pix_fmt_desc_get(src_pixfmt));
    //设置输出图像的大小和格式
    const int dst_w=outWidth,dst_h=outHeight;
    AVPixelFormat dst_pixfmt=AV_PIX_FMT_RGBA;
    //获取输出视频文件每个像素占有的BIT数
    int dst_bpp=av_get_bits_per_pixel(av_pix_fmt_desc_get(dst_pixfmt));
    //Structures
    uint8_t * src_data[4];
    int src_linesize[4];
    uint8_t *dst_data[4];
    int dst_linesize[4];
    struct SwsContext *img_convert_ctx;
    //用于临时存放yuv420p文件的buf
    uint8_t * temp_buffer=(uint8_t *)new char[src_w * src_h * src_bpp / 8];
    int frame_idx=0;
    int ret=0;
    /*通过输入宽和高以及像素格式分配输入缓冲，对于yuv420p的格式：
     * src_data[0]：代表Y通道；src_linesize[0]：对齐后的Y宽度，可能会大于输入的图像宽度
     * src_data[1]：代表U通道；src_linesize[1]：对齐后的U宽度
     * src_data[2]：代表V通道；src_linesize[2]：对齐后的V宽度
     * align：输入宽度需要对齐到多少
     */
    ret= av_image_alloc(src_data, src_linesize,src_w, src_h, src_pixfmt, 1);
    if (ret< 0) {
        LOGE( "Could not allocate source image\n");
        return ;
    }
    /*通过输出宽和高以及像素格式分配输入缓冲，对于RGBA的格式：
     * dst_data[0]：代表RGBA通道；dst_linesize[0]：对齐后的RGBA宽度(W*H*4)，可能会大于输入的图像宽度
     * dst_data[1]：RGBA是非平面数据
     * dst_data[2]：RGBA是非平面数据
     * align：输出宽度需要对齐到多少
     */
    ret = av_image_alloc(dst_data, dst_linesize,dst_w, dst_h, dst_pixfmt, 1);
    if (ret< 0) {
        LOGE( "Could not allocate destination image\n");
        return ;
    }
    //Init Method 1
    //分配图像转换的上下文
    img_convert_ctx =sws_alloc_context();
    //Set Value
    //SWS_BICUBIC代表的是图像转换的其中一种算法
    av_opt_set_int(img_convert_ctx,"sws_flags",SWS_BICUBIC|SWS_PRINT_INFO,0);
    //设置图像转换输入源的宽
    av_opt_set_int(img_convert_ctx,"srcw",src_w,0);
    //设置图像转换输入源的高
    av_opt_set_int(img_convert_ctx,"srch",src_h,0);
    //设置图像转换输入源的格式
    av_opt_set_int(img_convert_ctx,"src_format",src_pixfmt,0);
    //'0' for MPEG (Y:0-235);'1' for JPEG (Y:0-255)
    /* FFmpeg中可以通过使用av_opt_set()设置“src_range”和“dst_range”来设置输入和输出的YUV的取值范围。
     * 如果“dst_range”字段设置为“1”的话，则代表输出的YUV的取值范围遵循“jpeg”标准；
     * 如果“dst_range”字段设置为“0”的话，则代表输出的YUV的取值范围遵循“mpeg”标准。
     * 下面记录一下YUV的取值范围的概念。与RGB每个像素点的每个分量取值范围为0-255不同（每个分量占8bit），YUV取值范围有两种：
     * （1）以Rec.601为代表（还包括BT.709 / BT.2020）的广播电视标准中，Y的取值范围是16-235，U、V的取值范围是16-240。FFmpeg中称之为“mpeg”范围。
     * （2）以JPEG为代表的标准中，Y、U、V的取值范围都是0-255。FFmpeg中称之为“jpeg” 范围。*/
    av_opt_set_int(img_convert_ctx,"src_range",1,0);
    //设置图像转换输出图像的宽
    av_opt_set_int(img_convert_ctx,"dstw",dst_w,0);
    //设置图像转换输出图像的高
    av_opt_set_int(img_convert_ctx,"dsth",dst_h,0);
    //设置图像转换输出图像的目标格式
    av_opt_set_int(img_convert_ctx,"dst_format",dst_pixfmt,0);
    //同上设置输出图像遵循Mjpeg还是Jpeg
    av_opt_set_int(img_convert_ctx,"dst_range",1,0);
    //初始化图像转换上下文
    sws_init_context(img_convert_ctx,NULL,NULL);
    //显示窗口初始化
    ANativeWindow *nwin = ANativeWindow_fromSurface(env,surface);
    //设置显示窗口的格式
    ANativeWindow_setBuffersGeometry(nwin,outWidth,outHeight,WINDOW_FORMAT_RGBA_8888);
    //定义窗口buf
    ANativeWindow_Buffer wbuf;
    while(1)
    {
        //从源图像中读取一帧的数据到temp_buffer中
        if (fread(temp_buffer, 1, src_w*src_h*src_bpp/8, src_file) != src_w*src_h*src_bpp/8){
            break;
        }
        switch(src_pixfmt){
            case AV_PIX_FMT_GRAY8:{
                memcpy(src_data[0],temp_buffer,src_w*src_h);
                break;
            }
            case AV_PIX_FMT_YUV420P:{
                //通道转换，YUV420P有三个通道需要转换
                memcpy(src_data[0],temp_buffer,src_w*src_h);                    //Y
                memcpy(src_data[1],temp_buffer+src_w*src_h,src_w*src_h/4);      //U
                memcpy(src_data[2],temp_buffer+src_w*src_h*5/4,src_w*src_h/4);  //V
                break;
            }
            case AV_PIX_FMT_YUV422P:{
                //通道转换，YUV422P有三个通道需要转换
                memcpy(src_data[0],temp_buffer,src_w*src_h);                    //Y
                memcpy(src_data[1],temp_buffer+src_w*src_h,src_w*src_h/2);      //U
                memcpy(src_data[2],temp_buffer+src_w*src_h*3/2,src_w*src_h/2);  //V
                break;
            }
            case AV_PIX_FMT_YUV444P:{
                memcpy(src_data[0],temp_buffer,src_w*src_h);                    //Y
                memcpy(src_data[1],temp_buffer+src_w*src_h,src_w*src_h);        //U
                memcpy(src_data[2],temp_buffer+src_w*src_h*2,src_w*src_h);      //V
                break;
            }
            case AV_PIX_FMT_YUYV422:{
                memcpy(src_data[0],temp_buffer,src_w*src_h*2);                  //Packed
                break;
            }
            case AV_PIX_FMT_RGB24:{
                memcpy(src_data[0],temp_buffer,src_w*src_h*3);                  //Packed
                break;
            }
            default:{
                printf("Not Support Input Pixel Format.\n");
                break;
            }
        }
        //将数据转换,YUV420P转换为RGBA格式
        sws_scale(img_convert_ctx, src_data, src_linesize, 0, src_h, dst_data, dst_linesize);
        LOGE("Finish process frame %5d\n",frame_idx);
        frame_idx++;
        ANativeWindow_lock(nwin,&wbuf,0);
        //wbuf.bits：对应的就是surface的buf
        uint8_t *dst = (uint8_t*)wbuf.bits;
        //因为RGBA是非平面的数据，所以转换后数据是存放在dst_data[0]中的
        memcpy(dst,dst_data[0],outWidth*outHeight*4);
        ANativeWindow_unlockAndPost(nwin);
    }
    sws_freeContext(img_convert_ctx);
    free(temp_buffer);
    av_freep(&src_data[0]);
    av_freep(&dst_data[0]);
    return;
}
int video_stream_idx = -1;
int audio_stream_idx = -1;
ANativeWindow *nativeWindow;
AVFormatContext * avFormatContext;
AVCodecContext *pContext;
AVPacket *pPacket;
int isPlaying;
void putToQueues(AVPacket *pPacket);

void get(AVPacket *pPacket){
    pthread_mutex_lock(&pthread_mutex);
        while (isPlaying){
            LOGE("取出对垒 xxxxxx");
            if(!queues.empty()){
                LOGE("start_music_thread 888");
                //如果队列中有数据可以拿出来
                if(av_packet_ref(pPacket,queues.front())){
                    break;
                }
                //取成功了，弹出队列，销毁packet
                AVPacket *packet2 = queues.front();
                queues.erase(queues.begin());
                av_free(packet2);
                break;
            } else{
                LOGE("音频执行wait");
                LOGE("ispause %d");
                pthread_cond_wait(&cond,&pthread_mutex);

            }
        }
    pthread_mutex_unlock(&pthread_mutex);
};

int getPcm() {
    AVPacket *avPacket = (AVPacket *) av_mallocz(sizeof(AVPacket));
    AVFrame *avFrame = av_frame_alloc();

    int size;
    int gotframe;
    LOGE("start_music_thread 准备解码");
    while (isPlaying) {
        LOGE("start_music_thread 取出对垒 xxxxxx isPlaying = %d",isPlaying);
        size = 0;
        get(avPacket);
        //时间矫正
        if (avPacket->pts != AV_NOPTS_VALUE) {
            clocks = av_q2d(time_bases) * avPacket->pts;
        }
        //            解码  mp3   编码格式frame----pcm   frame
        avcodec_decode_audio4(pContext, avFrame, &gotframe, avPacket);
        LOGE("start_music_thread 解码 gotframe = %d",gotframe);

        if (gotframe) {
            LOGE("start_music_thread swr_convert ");
            swr_convert(swrContext, reinterpret_cast<uint8_t **>(out_buffer), 44100 * 2,
                        (const uint8_t **) avFrame->data, avFrame->nb_samples);
//                缓冲区的大小
            size = av_samples_get_buffer_size(NULL, out_channer_nb, avFrame->nb_samples,
                                              AV_SAMPLE_FMT_S16, 1);
            break;
        }
        av_free(avPacket);
        av_frame_free(&avFrame);
    }
    return size;
}

void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context){
    //音頻播放

    int datasize = getPcm();
    if (datasize > 0) {
        //第一针所需要时间采样字节/采样率
        double time = datasize / (44100 * 2 * 2);
        //
        clocks = time + clocks;
        LOGE("当前一帧声音时间%f   播放时间%f", time, clocks);

        (*bq)->Enqueue(bq, out_buffer, datasize);
        LOGE("播放 %d ",queues.size());
    }
}


void *start_music_thread(void *args){
    LOGE("========start_music_thread=111");
    AVCodecParameters *audiocodecParameters = (AVCodecParameters*)args;
    //获取音频解码器
    AVCodec *pCodec = avcodec_find_decoder(audiocodecParameters->codec_id);
    //解码器的上下文
    pContext = avcodec_alloc_context3(pCodec);
    //打开解码器
    avcodec_open2(pContext,pCodec,NULL);

    //把解码器参数copy到解码器上下文
    avcodec_parameters_to_context(pContext,audiocodecParameters);
    //音頻組件準備
    swrContext = swr_alloc();
    LOGE("========start_music_thread=222");
    FILE *fp_pcm = fopen("/sdcard/class.pcm","wb");
    int length=0;
    int got_frame;
    //    44100 * 2
    out_buffer = (uint8_t *) (av_mallocz(44100 * 2));
    uint64_t  out_ch_layout=AV_CH_LAYOUT_STEREO;
    //    输出采样位数  16位
    enum AVSampleFormat out_formart = AV_SAMPLE_FMT_S16;
    LOGE("========start_music_thread=333");

    //输出的采样率必须与输入相同
    uint64_t input_ch_layout = pContext->channel_layout;
    AVSampleFormat in_format = pContext->sample_fmt;
    LOGE("========start_music_thread=444 = %d",input_ch_layout);

    int in_sample_rate = pContext->sample_rate;
    LOGE("=======start_music_thread=input_ch_layout=%d,in_sample_rate=%d");
    int out_sample_rate = pContext->sample_rate;
    swr_alloc_set_opts( swrContext, out_ch_layout, out_formart, out_sample_rate,
                        input_ch_layout, in_format, in_sample_rate, 0,NULL);
    swr_init(swrContext);
    AVFrame *pFrame;

    LOGE("开始解码");
    int count = 0;
    while (av_read_frame(avFormatContext,pPacket)>=0) {
        avcodec_send_packet(pContext, pPacket);
        pFrame = av_frame_alloc();
        int ret = avcodec_receive_frame(pContext, pFrame);
        while (ret) {
            LOGE("正在解码 start_music_thread=%d,count=%d", ret, count++);
            if (pPacket->stream_index == audio_stream_idx) {
                putToQueues(pPacket);
            }
            if (ret == AVERROR(EAGAIN)) {
                continue;
            } else if (ret < 0) {
                LOGE("start_music_thread 解码完成");
//            break;
            }
            LOGE("========start_music_thread=555");
        }
        if(pPacket->stream_index != audio_stream_idx){
            continue;
        }
        LOGE("========start_music_thread=666");


        swr_convert(swrContext, &out_buffer,2*44100,
                (const uint8_t **)(pFrame->data), pFrame->nb_samples);
        //    获取通道数  2
        out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
        int size = av_samples_get_buffer_size(NULL, out_channer_nb, pFrame->nb_samples, out_formart, 1);
        LOGE("size start_music_thread=%d",size);

        fwrite(out_buffer, 1, size, fp_pcm);
        LOGE("start_music_thread 保存pcm文件完成");

    }

    fclose(fp_pcm);
    swr_free(&swrContext);
    avcodec_close(pContext);
    avformat_close_input(&avFormatContext);
    av_free(out_buffer);
    av_frame_free(&pFrame);
            LOGE("创建opnsl es播放器 start_music_thread");
            //创建播放器
            SLresult result;
            // 创建引擎engineObject
            result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
            if (SL_RESULT_SUCCESS != result) {
                return NULL;
            }
            // 实现引擎engineObject
            result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
            if (SL_RESULT_SUCCESS != result) {
                return NULL;
            }
            // 获取引擎接口engineEngine
            result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE,
                                                   &engineEngine);
            if (SL_RESULT_SUCCESS != result) {
                return NULL;
            }
            // 创建混音器outputMixObject
            result = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 0,
                                                      0, 0);
            if (SL_RESULT_SUCCESS != result) {
                return NULL;
            }
            // 实现混音器outputMixObject
            result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
            if (SL_RESULT_SUCCESS != result) {
                return NULL;
            }
            result = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,
                                                      &outputMixEnvironmentalReverb);
            const SLEnvironmentalReverbSettings settings = SL_I3DL2_ENVIRONMENT_PRESET_DEFAULT;
            if (SL_RESULT_SUCCESS == result) {
                (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(
                        outputMixEnvironmentalReverb, &settings);
            }

            SLDataLocator_AndroidSimpleBufferQueue android_queue = {
                    SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
                    2};
            SLDataFormat_PCM pcm = {SL_DATAFORMAT_PCM, 2, SL_SAMPLINGRATE_44_1,
                                    SL_PCMSAMPLEFORMAT_FIXED_16,
                                    SL_PCMSAMPLEFORMAT_FIXED_16,
                                    SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
                                    SL_BYTEORDER_LITTLEENDIAN};
            //   新建一个数据源 将上述配置信息放到这个数据源中
            SLDataSource slDataSource = {&android_queue, &pcm};
            //    设置混音器
            SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};

            SLDataSink audioSnk = {&outputMix, NULL};
            const SLInterfaceID ids[3] = {SL_IID_BUFFERQUEUE, SL_IID_EFFECTSEND,
                    /*SL_IID_MUTESOLO,*/ SL_IID_VOLUME};
            const SLboolean req[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE,
                    /*SL_BOOLEAN_TRUE,*/ SL_BOOLEAN_TRUE};
            //先讲这个
            (*engineEngine)->CreateAudioPlayer(engineEngine, &bqPlayerObject, &slDataSource,
                                               &audioSnk, 2,
                                               ids, req);
            //初始化播放器
            (*bqPlayerObject)->Realize(bqPlayerObject, SL_BOOLEAN_FALSE);

            //    得到接口后调用  获取Player接口
            (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_PLAY, &bqPlayerPlay);

            //    注册回调缓冲区 //获取缓冲队列接口
            (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_BUFFERQUEUE,
                                            &bqPlayerBufferQueue);
            //缓冲接口回调
            (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue, bqPlayerCallback,NULL);
            //    获取音量接口
            (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_VOLUME, &bqPlayerVolume);

            //    获取播放状态接口
            (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);

           bqPlayerCallback(bqPlayerBufferQueue, NULL);

           pthread_exit(0);
}

void *start_video_thread(void *args){
    AVCodecParameters *videocodecParameters = (AVCodecParameters*)args;

    //h264解码器getDeclaredMethod
    AVCodec *pCodec = avcodec_find_decoder(videocodecParameters->codec_id);
    //解码器的上下文
    AVCodecContext *pContext = avcodec_alloc_context3(pCodec);
    //把解码器参数copy到解码器上下文
    avcodec_parameters_to_context(pContext,videocodecParameters);

    avcodec_open2(pContext,pCodec,NULL);
    //解码 yuv数据 AVPacket
    SwsContext *pSwsContext = sws_getContext(pContext->width, pContext->height, pContext->pix_fmt, pContext->width, pContext->height
            , AV_PIX_FMT_RGBA, SWS_BILINEAR, 0, 0, 0);
    ANativeWindow_setBuffersGeometry(nativeWindow,pContext->width,pContext->height,WINDOW_FORMAT_RGBA_8888);
    ANativeWindow_Buffer outBuffer;



    //从视频流中读取数据包
    while (av_read_frame(avFormatContext,pPacket)>=0) {
        if (pPacket->stream_index == audio_stream_idx) {
            putToQueues(pPacket);
        }
        avcodec_send_packet(pContext, pPacket);
        AVFrame *pFrame = av_frame_alloc();
        int ret = avcodec_receive_frame(pContext, pFrame);
        LOGE("正在解码=%d",ret);

        if (ret == AVERROR(EAGAIN)) {
            continue;
        } else if (ret < 0) {
            break;
        }
        //接收的容器
        uint8_t *dst_data[4];
        //每一行的首地址
        int dst_linesize[4];
        av_image_alloc(dst_data, dst_linesize, pFrame->width, pFrame->height, AV_PIX_FMT_RGBA, 1);
        sws_scale(pSwsContext, pFrame->data, pFrame->linesize, 0, pFrame->height, dst_data,
                  dst_linesize);
        int32_t window_lock = ANativeWindow_lock(nativeWindow, &outBuffer, 0);
        LOGE("mp4 open 9999: window_lock = %d", window_lock);

        //渲染
        uint8_t *fristWindow = static_cast<uint8_t *>(outBuffer.bits);
        //输入源（rgb）的
        uint8_t *src_data = dst_data[0];
        //拿到一行有多少个字节RBGA
        int destStride = outBuffer.stride * 4;
        int src_linesize = dst_linesize[0];
        for (int i = 0; i < outBuffer.height; ++i) {
            //内存拷贝来进行渲染
            memcpy(fristWindow + i * destStride, src_data + i * src_linesize, destStride);
        }
        ANativeWindow_unlockAndPost(nativeWindow);
        usleep(1000 * 16);
        av_frame_free(&pFrame);
    }
}
void putToQueues(AVPacket *pPacket) {
        LOGE("插入队列");
        AVPacket *avPacket1 = (AVPacket *) av_mallocz(sizeof(AVPacket));
        //克隆
        if (av_packet_ref(avPacket1, pPacket)) {
            //克隆失败
            return ;
        }
        //push的时候需要锁住，有数据的时候再解锁
        pthread_mutex_lock(&pthread_mutex);
        queues.push_back(avPacket1);//将packet压入队列
        //压入过后发出消息并且解锁
        pthread_cond_signal(&cond);
        pthread_mutex_unlock(&pthread_mutex);
    }

void stopMusic() {
    pthread_mutex_lock(&pthread_mutex);
    pthread_cond_signal(&cond);
    pthread_mutex_unlock(&pthread_mutex);
//    pthread_join(playId, 0);
    if (bqPlayerPlay) {
        (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_STOPPED);
        bqPlayerPlay = 0;
    }
    if (bqPlayerObject) {
        (*bqPlayerObject)->Destroy(bqPlayerObject);
        bqPlayerObject = 0;

        bqPlayerBufferQueue = 0;
        bqPlayerVolume = 0;
    }

    if (outputMixObject) {
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject = 0;
    }

    if (engineObject) {
        (*engineObject)->Destroy(engineObject);
        engineObject = 0;
        engineEngine = 0;
    }
    if (swrContext)
        swr_free(&swrContext);
    if (pContext) {
        if (avcodec_is_open(pContext))
            avcodec_close(pContext);
        avcodec_free_context(&pContext);
        pContext = 0;
    }
}

void playMP4(JNIEnv* env, jobject instance, jstring _path, jobject surface){
    //先进行av组件注册
    av_register_all();
    const char *path = env->GetStringUTFChars(_path,0);

    nativeWindow = ANativeWindow_fromSurface(env,surface);

    //初始化网络模块
    avformat_network_init();
    //总上下文
    avFormatContext = avformat_alloc_context();
    //新建字典对象
    AVDictionary *opts = NULL;
    //字典赋值
    av_dict_set(&opts,"timeout","3000000",0);
    //打开文件流
    LOGE("mp4 path result :%d",avFormatContext->duration);

    int ret = avformat_open_input(&avFormatContext,path,NULL,&opts);
    LOGE("mp4 open result :%d",ret);
    //寻找视频流
    if(ret || avformat_find_stream_info(avFormatContext,NULL) < 0){
        return;
    }
    //得到播放总时间
    if (avFormatContext->duration != AV_NOPTS_VALUE) {
        LOGE("====================duration=%d",avFormatContext->duration);//微秒
    }
    pPacket = av_packet_alloc();
    //遍历流
/*    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
        if(avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO){
            //拿到视频流的索引
            video_stream_idx = i;
            LOGE("视频索引 = %d",audio_stream_idx);
            AVCodecParameters *videocodecParameters = avFormatContext->streams[video_stream_idx]->codecpar;
            start_video_thread(videocodecParameters);

//            break;
//        } else if(avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO){
//            //拿到音频流的索引
//            audio_stream_idx = i;
//            break;
            LOGE("音频索引 = %d",audio_stream_idx);
//            time_bases = avFormatContext->streams[i]->time_base;
//            pthread_create(&mx,NULL,start_music_thread,audiocodecParameters);
        }
    }*/

    pthread_mutex_init(&pthread_mutex,NULL);
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
        if(avFormatContext->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO){
//            //拿到音频流的索引
            audio_stream_idx = i;
//            break;
            LOGE("音频索引 = %d",audio_stream_idx);
            time_bases = avFormatContext->streams[i]->time_base;
            AVCodecParameters *audiocodecParameters = avFormatContext->streams[audio_stream_idx]->codecpar;
//            start_music_thread(audiocodecParameters);
            pthread_create(&mx,NULL,start_music_thread,audiocodecParameters);
        }
    }
//    //视频流的解码参数
//    AVCodecParameters *videocodecParameters = avFormatContext->streams[video_stream_idx]->codecpar;
////    AVCodecParameters *audiocodecParameters = avFormatContext->streams[audio_stream_idx]->codecpar;
//    //h264解码器
//    AVCodec *pCodec = avcodec_find_decoder(videocodecParameters->codec_id);
//    //解码器的上下文
//    AVCodecContext *pContext = avcodec_alloc_context3(pCodec);
//    //把解码器参数copy到解码器上下文
//    avcodec_parameters_to_context(pContext,videocodecParameters);
//
//    avcodec_open2(pContext,pCodec,NULL);
//    //解码 yuv数据 AVPacket
//    SwsContext *pSwsContext = sws_getContext(pContext->width, pContext->height, pContext->pix_fmt, pContext->width, pContext->height
//            , AV_PIX_FMT_RGBA, SWS_BILINEAR, 0, 0, 0);
//    ANativeWindow_setBuffersGeometry(nativeWindow,pContext->width,pContext->height,WINDOW_FORMAT_RGBA_8888);
//    ANativeWindow_Buffer outBuffer;
//
//
//
//    //从视频流中读取数据包
//    while (av_read_frame(avFormatContext,pPacket)>=0) {
//        if (pPacket->stream_index == audio_stream_idx) {
//            putToQueues(pPacket);
//        }
//        avcodec_send_packet(pContext, pPacket);
//        AVFrame *pFrame = av_frame_alloc();
//        int ret = avcodec_receive_frame(pContext, pFrame);
//        LOGE("=正在解码=%d",ret);
//
//        if (ret == AVERROR(EAGAIN)) {
//            continue;
//        } else if (ret < 0) {
//            break;
//        }
//        //接收的容器
//        uint8_t *dst_data[4];
//        //每一行的首地址
//        int dst_linesize[4];
//        av_image_alloc(dst_data, dst_linesize, pFrame->width, pFrame->height, AV_PIX_FMT_RGBA, 1);
//        sws_scale(pSwsContext, pFrame->data, pFrame->linesize, 0, pFrame->height, dst_data,
//                  dst_linesize);
//        int32_t window_lock = ANativeWindow_lock(nativeWindow, &outBuffer, 0);
//        LOGE("mp4 open 9999: window_lock = %d", window_lock);
//
//        //渲染
//        uint8_t *fristWindow = static_cast<uint8_t *>(outBuffer.bits);
//        //输入源（rgb）的
//        uint8_t *src_data = dst_data[0];
//        //拿到一行有多少个字节RBGA
//        int destStride = outBuffer.stride * 4;
//        int src_linesize = dst_linesize[0];
//        for (int i = 0; i < outBuffer.height; ++i) {
//            //内存拷贝来进行渲染
//            memcpy(fristWindow + i * destStride, src_data + i * src_linesize, destStride);
//        }
//        ANativeWindow_unlockAndPost(nativeWindow);
//        usleep(1000 * 16);
//        av_frame_free(&pFrame);
//    }
    isPlaying = 1;

//    AVPacket *packet = (AVPacket *) av_mallocz(sizeof(AVPacket));
//    //跳转到某一个特定的帧上面播放
//    int ret1;
//
//    while (isPlaying) {
//        ret1 = av_read_frame(avFormatContext, pPacket);
//        if (ret1 == 0) {
//            if (pPacket->stream_index == video_stream_idx) {
//                //将视频packet压入队列
////                putToQueues(packet);
//            } else if (pPacket->stream_index == audio_stream_idx) {
//                putToQueues(pPacket);
//            }
//            //保存数据
//            av_packet_unref(pPacket);
//        } else if (ret1 == AVERROR_EOF) {
//            // 读完了 读取完毕 但是不一定播放完毕
//            LOGE("读取完毕");
//            while (isPlaying) {
//                if (queues.empty()) {
//                    break;
//                }
//                 LOGE("等待播放完成");
//                av_usleep(10000);
//            }
//        }
//    }

//    isPlaying = 0;

//    stopMusic();
    //释放
//    av_free_packet(packet);
//    avformat_free_context(avFormatContext);
//    pthread_exit(0);
}

