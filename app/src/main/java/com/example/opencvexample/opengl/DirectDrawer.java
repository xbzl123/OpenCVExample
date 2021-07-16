package com.example.opencvexample.opengl;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

class DirectDrawer {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +  //传给片元着色器的变量
                    "void main()" +
                    "{"+
                    "gl_Position = vPosition;"+
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n"+  //固定指令
                    "precision mediump float;" +  //固定指令
                    "varying vec2 textureCoordinate;\n" +  //顶点着色器中传递过来的变量
                    "uniform samplerExternalOES s_texture;\n" + //固定指定
                    "void main() {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    "}";

    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; //绘制顶点的顺序

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 2;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    //顶点坐标-坐标值是固定的搭配，不同的顺序会出现不同的预览效果
    static float squareCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f,  1.0f,
    };
    //纹理坐标-相机预览时对片元着色器使用的是纹理texture而不是颜色color
    static float textureVertices[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private int texture;
    private int uMatrix;
    private float[] matrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];


    public DirectDrawer(int texture)
    {
        this.texture = texture;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(transTextureCoordinates(squareCoords));  //第一个装载的数据是顶点坐标
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);  //第二个装载的数据是绘制顶点的顺序
        drawListBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);  //第三个装载的数据是纹理坐标
        textureVerticesBuffer.position(0);

        //解析之前变量中声明的两个着色器:顶点着色器和片元着色器
        int vertexShader    = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader  = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // 创建空程式
        GLES20.glAttachShader(mProgram, vertexShader);   // 添加顶点着色器到程式中
        GLES20.glAttachShader(mProgram, fragmentShader); // 添加片元着色器到程式中
        GLES20.glLinkProgram(mProgram);                  // 链接程式
        uMatrix = GLES20.glGetUniformLocation(mProgram, "u_Matrix");


    }

    public void setUseBackCamera(boolean isUseBackCamera){
        vertexBuffer.clear();
        vertexBuffer.put(isUseBackCamera?squareCoords:transTextureCoordinates(squareCoords));
        //第一个装载的数据是顶点坐标
        vertexBuffer.position(0);
    }
    public void draw(float[] mtx)
    {
        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //给变换矩阵赋值
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, 0);
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the <insert shape here> coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        //使用一次glEnableVertexAttribArray方法就要使用一次glVertexAttribPointer方法
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);

        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);


        // 使用了glEnableVertexAttribArray方法就必须使用glDisableVertexAttribArray方法
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }

    public void previewAngle() {
//        int angle = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
//        Log.e("888","=========================angle="+angle);
        Matrix.setIdentityM(matrix, 0);
//        switch (angle) {
//            case Surface.ROTATION_0:
//                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    render.setAngle(90, 0, 0, 1);
//                    render.setAngle(180, 1, 0, 0);
//                } else {
                Matrix.rotateM(matrix, 0,90f, 0f, 0f, 1f);
//                }

//                break;
//            case Surface.ROTATION_90:
////                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
////                    render.setAngle(180, 0, 0, 1);
////                    render.setAngle(180, 0, 1, 0);
////                } else {
//                Matrix.rotateM(matrix, 0, 90f, 0f, 0f, 1f);
//
////                }
//                break;
//            case Surface.ROTATION_180:
////                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
////                    render.setAngle(90f, 0.0f, 0f, 1f);
////                    render.setAngle(180f, 0.0f, 1f, 0f);
////                } else {
//                Matrix.rotateM(matrix, 0, -90f, 0f, 0f, 1f);
//
////                }
//                break;
//            case Surface.ROTATION_270:
////                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
////                    render.setAngle(180f, 0.0f, 1f, 0f);
////                } else {
//                Matrix.rotateM(matrix, 0, 0f, 0f, 0f, 1f);
//
////                }
//                break;
//        }
    }

    private  int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
    private float[] transformTextureCoordinates(
            float[] coords, float[] matrix)    {
        float[] result = new float[ coords.length ];
        float[] vt = new float[4];
        for ( int i = 0 ; i < coords.length ; i += 2 ) {
            float[] v = { coords[i], coords[i+1], 0 , 1  };
            Matrix.multiplyMV(vt, 0, matrix, 0, v, 0);
            result[i] = vt[0];
            result[i+1] = vt[1];
        }
        return result;
    }

    public float[] transTextureCoordinates(
            float[] coords)    {
        float[] tmp = new float[8];
        for ( int i = 0 ; i < coords.length ; i ++ ) {
            if(i%2==1){
            tmp[i] = -coords[i];
            }else{
                tmp[i] = coords[i];
            }
        }
        return tmp;
    }


}
