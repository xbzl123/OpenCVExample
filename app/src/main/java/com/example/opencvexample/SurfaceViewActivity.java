package com.example.opencvexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class SurfaceViewActivity extends AppCompatActivity {
    private EditText nameText;
    private String path;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private boolean pause;
    private int position;
    private H264DeCodePlay h264DeCodePlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //不息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //获取屏幕高宽
        DisplayMetrics outMetrics = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        setContentView(R.layout.activity_surface_view);
        mediaPlayer = new MediaPlayer();
        nameText = (EditText) this.findViewById(R.id.filename);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        findViewById(R.id.playbutton).setOnClickListener(onClickListener);
        findViewById(R.id.pausebutton).setOnClickListener(onClickListener);
        findViewById(R.id.resetbutton).setOnClickListener(onClickListener);
        findViewById(R.id.stopbutton).setOnClickListener(onClickListener);

        //把输送给surfaceView的视频画面，直接显示到屏幕上,不要维持它自身的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, outMetrics.widthPixels/4*3);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallback());

//        GameSurfaceView.SCREEN_WIDTH = outMetrics.widthPixels;
//
//        GameSurfaceView.SCREEN_HEIGHT = outMetrics.heightPixels;
//
//        gameView = new GameSurfaceView(this);
//
//        setContentView(gameView);
    }
    GameSurfaceView gameView;

    private final class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (position > 0 && path != null) {
                play(position);
                position = 0;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mediaPlayer.isPlaying()) {
                position = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!= null)
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mediaplay(v);
        }
    };
    public void mediaplay(View v) {
        switch (v.getId()) {
            case R.id.playbutton:
//                startActivity(new Intent(this,LayoutActivity.class));

                String filename = nameText.getText().toString();
                if (filename.startsWith("http")) {
                    path = filename;
                    play(0);
                } else {
                    File file = new File(Environment.getExternalStorageDirectory(), filename);
                    if (file.exists()) {
                        path = file.getAbsolutePath();
                        play(0);
                    } else {
                        path = null;
                        Toast.makeText(this, "File is not exit!", 1).show();
                    }
                }
                break;
            case R.id.pausebutton:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pause = true;
                } else {
                    if (pause) {
                        mediaPlayer.start();
                        pause = false;
                    }
                }
                break;
            case R.id.resetbutton:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);
                } else {
                    if (path != null) {
                        play(0);
                    }
                }
                break;
            case R.id.stopbutton:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
        }
    }

    private void play(int position) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();//缓冲
            mediaPlayer.setOnPreparedListener(new PrepareListener(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class PrepareListener implements MediaPlayer.OnPreparedListener {
        private int position;

        public PrepareListener(int position) {
            this.position = position;
        }

        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();//播放视频
            if(position>0) mediaPlayer.seekTo(position);
        }
    }

    public void clearDraw(SurfaceHolder holder,int color) {
        Log.w("tan","clearDraw");
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            canvas.drawColor(color);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            if(canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}

