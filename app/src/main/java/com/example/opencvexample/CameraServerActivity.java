package com.example.opencvexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class CameraServerActivity extends AppCompatActivity {
    String receviceVideoPath = Environment.getExternalStorageDirectory() + "/yuv/camera_";

    private H264DeCodePlay h264DeCodePlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_server);

        new Thread(){
            @Override
            public void run() {
                createServer();
            }
        }.start();
        SurfaceView surfaceView = findViewById(R.id.camera_surfaceView);

        //把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                h264DeCodePlay = new H264DeCodePlay(holder.getSurface(), 640,480);
                h264DeCodePlay.setPlayStatusCallback(new H264DeCodePlay.PlayStatusCallback() {
                    @Override
                    public void isPlayPrepare(boolean isPlayPrepare) {
                        if (isPlayPrepare) {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessageDelayed(message,50);
                        }
                    }

                    @Override
                    public void isPalying(boolean isPalying) {

                    }

                    @Override
                    public void isPlayed(boolean isPlayed) {
                        if (isPlayed) {
                            h264DeCodePlay.resetPlay(640,480);
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    int tag = 0;
    private void createServer() {
        //建立tcp的服务端，并建立一个监听端口
        ServerSocket serversocket = null;
        try {
            serversocket = new ServerSocket(9090);
            //接受客户端的连接，accept()接受客户端的连接方法也是一个阻塞型的方法，没有客户端与其连接时，会一直等待下去。
            Socket socket = serversocket.accept();
            //获取输入流对象，读取客户端发送的内容
            boolean isStartSave = false;

            while (true) {
                BufferedOutputStream outputStream = null;
                InputStream inputstream = socket.getInputStream();
                byte[] buf = new byte[serversocket.getReceiveBufferSize()];
                int length = 0;
                String result = "";
                while ((length = inputstream.read(buf)) != -1) {
                    result = new String(buf,0,length);
                    try {
                            if (isStartSave) {
                                outputStream.write(buf,0,length);
                                Log.e("zqf-dev", "write");
                            }
                            if ("success".equals(result)) {
                                Log.e("zqf-dev", "decodePlay :success");
                                isStartSave = false;
                            } else if ("start".equals(result)) {
                                String saveFilePath = receviceVideoPath + UUID.randomUUID().toString() + ".h264";
                                Log.e("zqf-dev", "decodePlay :saveFilePath="+saveFilePath);

                                h264DeCodePlay.filePathQueue.add(saveFilePath);
                                outputStream = createfile(saveFilePath);
                                if (tag == 1) {
                                    mHandler.sendEmptyMessage(1);
                                }
                                tag++;
                                isStartSave = true;
                            }
                    } catch (Exception exception) {
                        Log.e("exception", "result is " +exception.getMessage());
                        throw new RuntimeException(exception);
                    }
                }
//            System.out.println(new String(buf,0,length));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private BufferedOutputStream createfile(String path){
        File file = new File(path);
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (Exception e){
            e.printStackTrace();
        }
        return outputStream;
    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.e("zqf-dev", "decodePlay :" +msg.what);
            h264DeCodePlay.decodePlay();
        }
    };
}