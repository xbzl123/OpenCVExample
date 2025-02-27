package com.example.opencvexample.opengl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{
    private static final String TAG = CameraGLSurfaceView.class.getSimpleName();
    private final Context mContext;
    private int mTextureID;
    private SurfaceTexture mSurface;
    private DirectDrawer mDirectDrawer;
    private float[] mProjectionMatrix = new float[16];
    private CameraHelper cameraHelper;

    public CameraGLSurfaceView(Context context) {
        super(context);
        mContext = context;
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        //实例化摄像头对象
        cameraHelper =  CameraHelper.getInstance(mContext);
    }

    public CameraHelper getCameraHelper() {
        return cameraHelper;
    }

    //生成纹理ID
    private int createTextureID()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public SurfaceTexture getSurfaceTexture(){
        return mSurface;
    }

    //该方法是实现SurfaceTexture.OnFrameAvailableListener接口时实现的方法
    //用于提示新的数据流的到来
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub
        Log.e("888","onFrameAvailable====");
        this.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub  
        Log.i(TAG, "onSurfaceCreated...");
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        //回调一般用来读取解码后的数据
        mSurface.setOnFrameAvailableListener(this);

        mDirectDrawer = new DirectDrawer(mTextureID);
//        mDirectDrawer.previewAngle();

        cameraHelper.startCamera(mSurface); //①在创建surface时打开相机
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub  
        Log.i(TAG, "onSurfaceChanged...");
        GLES20.glViewport(0, 0, width, height);
        if(!cameraHelper.isPreviewing()){
            cameraHelper.startCamera(mSurface); //②预览
        }
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub  
        Log.i(TAG, "onDrawFrame...");
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage(); //SurfaceTexture的关键方法
        float[] mtx = new float[16];

//        mSurface.getTransformMatrix(mtx); //SurfaceTexture的关键方法
        mDirectDrawer.draw(mtx);  //③调用图形类的draw()方法
    }


    public void changeCameraType(boolean isBack) {
        cameraHelper.changeCameraId(isBack?0:1);
        mDirectDrawer.setUseBackCamera(isBack);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("888","onTouchEvent=========================");
        Matrix matrix = new Matrix();
        return super.onTouchEvent(event);
    }
}
