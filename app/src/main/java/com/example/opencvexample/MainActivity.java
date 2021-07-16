package com.example.opencvexample;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.AlarmClock;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.FileUtils;
import com.example.library.Utils.NetworkUtils;
import com.example.opencvexample.Static_enum.DeviceBox;
import com.example.opencvexample.annotation.NetworkCheck;
import com.example.opencvexample.annotation.NoNetwork;
import com.example.opencvexample.multiTread.ThreadPoolManager;
import com.example.opencvexample.soundDealwith.SinWave;
import com.example.opencvexample.utils.AppUtils;
import com.example.opencvexample.utils.BroadCastUtils;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.qmuiteam.qmui.skin.QMUISkinManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("OpenCV");
    }

    private Bitmap bitmap1;
    private MediaProjectionManager mediaProjectionManager;
    private VirtualDisplay virtualDisplay;
    private MediaCodec mediaCodec = null;
    private MediaProjection mediaProjection;
    private boolean isStartCast;
    private MediaRecorder mMediaRecorder;
    private MediaProjection.Callback mediaProjectionCallback;
    private boolean isEncode;
    private QMUISkinManager mSkinManager;
    private AudioTrack audioTrack;
    private byte[] wave = new byte[20480000];
    private int length, waveLen, hz;
    private byte[] lock = new byte[0];
    private LinkedList<byte[]> audioDataList = new LinkedList<byte[]>();
    private ThreadPoolManager threadPoolManager;
    private Runnable runnable;
    private boolean isPlaying = false;
    private DisplayMetrics displayMetrics;
    private ComponentName adminReceiver;
    private Button alarm;
    private int state = 0;


    private int f(int i) {
        if (i <= 1) {
            return 1;
        }
        return f(i - 1) + f(i - 2);
    }

    int[] images = new int[]{R.drawable.timg, R.drawable.timg2};
    private AsyncTask<Void, Void, Boolean> asyncTask;
    private TessBaseAPI tessBaseAPI;
    private TextView tv;
    private ImageView iv;
    String language = "chi_sim";
    //    String language = "eng";
    int image = images[0];
    Button button;
    BluetoothAdapter bluetoothAdapter;
    AlertDialog dialog;
    String[] data;
    private View view1;
    private Button sound_change;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.e("onActivityResult", "=====requestCode=" + requestCode + ",resultCode = " + resultCode + ",data = " + data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 100){
                recordScreen(resultCode,data);

        }else if(requestCode == 101){
                Toast.makeText(this,"open bluetooth success!",Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void recordScreen(int resultCode, Intent data) {
        displayMetrics = new DisplayMetrics();
//            WindowManager systemService = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager systemService = getWindowManager();
//            int  heightPixels = this.getResources().getDisplayMetrics().heightPixels;
//            Log.e("getMetrics0","the height is"+heightPixels);
        systemService.getDefaultDisplay().getMetrics(displayMetrics);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent service = new Intent(this,ScreenRecorder.class);
            service.putExtra("code",100);
            service.putExtra("data", this.data);
            startForegroundService(service);
        }else {
            mMediaRecorder = new MediaRecorder();
            initRecorder();
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            mediaProjectionCallback = new MediaProjectionCallback();
            mediaProjection.registerCallback(mediaProjectionCallback, null);
            try {
                mediaCodec = MediaCodec.createEncoderByType("video/avc");
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaFormat format = MediaFormat.createVideoFormat("video/avc", 640, 480);
            format.setInteger(MediaFormat.KEY_BIT_RATE, 5 * 1024 * 1000);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); //关键帧间隔时间 单位s
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Surface surface = mediaCodec.createInputSurface();
            ImageReader reader = ImageReader.newInstance(640, 480, 1, 2);
            Surface surface1 = reader.getSurface();
            mediaCodec.start();
            isStartCast = true;
            //            new EncodeThread().start();
            virtualDisplay = mediaProjection.createVirtualDisplay("screen-codec", displayMetrics.widthPixels, displayMetrics.heightPixels, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
                    , mMediaRecorder.getSurface(), null, null);
            mMediaRecorder.start();
            mMediaRecorder.getMaxAmplitude();
        }
    }


    private void initRecorder() {
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video_11.mp4");
            mMediaRecorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1000);
            mMediaRecorder.setVideoFrameRate(30);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //pMediaRecorder.setAudioSamplingRate(16000);
            mMediaRecorder.setAudioSamplingRate(44100);
            mMediaRecorder.setAudioEncodingBitRate(96000);
            mMediaRecorder.setAudioChannels(1);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(rotation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
        }, 5);
        threadPoolManager = ThreadPoolManager.getInstance();
        processObservable();
        initData();
        initBroadCast();
//        for (int i = 0; i < 10; i++) {
//            Log.e("test111", "===================" + f(i));
//        }
        //Example of a call to a native method
        tv = findViewById(R.id.sample_text);
        tv.setText(JavaApi.getStringFromJNI());
        iv = findViewById(R.id.sample_img);
        iv.setImageResource(image);
        alarm = findViewById(R.id.alarm);
        alarm.setOnClickListener((v)->{
            Date date = new Date();
            int hours = date.getHours();
            int minutes = date.getMinutes();
            Log.e("setSystemAlarmClock","hour = "+hours+",minutes = "+minutes);
            setSystemAlarmClock(this,"nice",hours,minutes+1);
        });
        sound_change = findViewById(R.id.sound_change);
        sound_change.setOnClickListener(v -> {
            showChangeSoundDialog();
        });
        findViewById(R.id.suface_play).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SurfaceViewActivity.class));
        });

        tessBaseAPI = new TessBaseAPI();
//        AlertDialog alertDialog;
        initTess();

        view1 = getLayoutInflater().inflate(R.layout.sound_set_layout, null);

        button = findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResult();
            }
        });
        findViewById(R.id.next_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = images[1];
                iv.setImageResource(image);
                button.setText("识别车牌号码");
            }
        });
        findViewById(R.id.scan_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ScanResult> results = getWifiList();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_activated_1, convertList(results));
                data = convert(results);
                Log.e("select", "===================length=" + data.length);
                dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("选择你的wifi")
//                        .setItems(data, new DialogInterface.OnClickListener() {
                        .setAdapter(adapter, (d, w) -> {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Log.e("select","===================="+data[which]);
                            final String wifiName = data[w];
                            showPassWordDialog(wifiName);
//                            }
                        }).create();

                dialog.show();

            }
        });
        findViewById(R.id.svg_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.surfaceview_activity).setOnClickListener(v -> {
            startActivity(new Intent(this, SecondActivity.class));
        });

        findViewById(R.id.check_network).setOnClickListener(v -> {
            checkNetwork();
            startActivity(new Intent(MainActivity.this, ThirdActivity.class));
        });
        findViewById(R.id.tinker_fix).setOnClickListener(v -> {
            startActivity(new Intent(this, SkinActivity.class));
        });

        findViewById(R.id.scan_bluetooth).setOnClickListener(v -> {
            getBluetoothAdapter();
        });
        findViewById(R.id.start_cast).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startScreenRequest();
            }
        });
        findViewById(R.id.stop_cast).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                stopScreenRequest();
            }
        });
        findViewById(R.id.snap_shoot).setOnClickListener(v -> {
            startSnapShoot(tv);
        });

        findViewById(R.id.play_video).setOnClickListener(v -> {
            startActivity(new Intent(this, CustomPlayVideoActivity.class));
        });

        TextView view = findViewById(R.id.app_version);
        String version = "V " + BuildConfig.VERSION_NAME;
        view.setText(version);
        view.setOnClickListener(v -> {
            updateApp();
        });
        findViewById(R.id.custom_view).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MainActivity2.class));
        });
    }

    CustomEditText sound_hz_value, sound_time;

    private void showChangeSoundDialog() {
        ViewGroup viewGroup = (ViewGroup) view1.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        sound_hz_value = view1.findViewById(R.id.sound_hz);
        sound_time = view1.findViewById(R.id.sound_time_value);

        TextView textTip = view1.findViewById(R.id.sound_name);
        textTip.setText("频率为：");
        sound_hz_value.setText("50");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view1).setTitle("输入你要的声音频率")
                .setPositiveButton("确认", (d, i) -> {
//                    Log.e("qqq","viewById = "+viewById.getText().toString());
                    playSound(Integer.parseInt(sound_hz_value.getText().toString()), Integer.parseInt(sound_time.getText().toString()));
                }).create();
        builder.show();
    }

    private void stopSound() {
//        if(isPlaying){
//
//        }
    }

    private void playSound(int num, int time) {
        start(num);
        isPlaying = !isPlaying;
        if (isPlaying) {
            runnable = () -> {
                while (isPlaying) {
                    synchronized (lock) {
                        audioDataList.add(wave);
                        if (!audioDataList.isEmpty()) {
                            audioTrack.write(audioDataList.poll(), 0, length);
                        }
                    }
                }
            };
            threadPoolManager.addTask(runnable);
            if (time > 0) {
                new Handler().postDelayed(() -> {
                    isPlaying = false;
                    threadPoolManager.removeTask(runnable);
                }, 60 * 1000 * time);
                CountDownTimer countDownTimer = new CountDownTimer(60 * 1000 * time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.e("test", "time is " + millisUntilFinished);
                        String s = "声音换频" + millisUntilFinished / 1000;
                        sound_change.setText(s);
                    }

                    @Override
                    public void onFinish() {
                        sound_change.setText("声音换频");
                        shutdownPhone();
                        closeScreen();
                    }
                };
                countDownTimer.start();
            }
        } else {
            isPlaying = false;
            threadPoolManager.removeTask(runnable);
        }

    }

    private void closeScreen() {
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        adminReceiver = new ComponentName(MainActivity.this, LockReceiver.class);
        if (policyManager.isAdminActive(adminReceiver)) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (powerManager.isScreenOn()) {
                policyManager.lockNow();
            }
        } else {
            activeManager();
            Toast.makeText(this, "没有设备管理权限", Toast.LENGTH_LONG).show();
            closeScreen();
        }
//        policyManager.lockNow();
    }

    /**
     * 获取设备管理权限
     */
    private void activeManager() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "screenLock");
        startActivity(intent);
    }

    private void shutdownPhone() {
        AlarmManager systemService = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        Intent intent = new Intent("com.android.settings.action.REQUEST_POWER_OFF");
        PendingIntent pendInternet = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        systemService.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), pendInternet);
    }

    /**
     * 设置频率
     * @param rate
     */
    public void start(int rate) {
        if (rate > 0) {
            hz = rate;
            waveLen = 44100 / hz;
            length = waveLen * hz;
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_8BIT, length, AudioTrack.MODE_STREAM);
            //生成正弦波
            wave = SinWave.sin(wave, waveLen, length);
            if (audioTrack != null) {
                audioTrack.play();
            }
        } else {
            return;
        }

    }

    private void updateApp() {
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                //获取现在运行的apk的路径
                String oldApkPath = getApplicationInfo().sourceDir;
                Log.e("updateApp", "第一步成功");
                String patchPath = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                Log.e("updateApp", "第二步成功");
                String newApkPath = createNewApk().getAbsolutePath();
                Log.e("updateApp", "第三步成功");
                JavaApi.updateApp(oldApkPath, patchPath, newApkPath);
                Log.e("updateApp", "第四步成功");
                return new File(newApkPath);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                Log.e("updateApp", "安装合成的apk" + file);
                AppUtils.installApk(MainActivity.this, file);
            }
        }.execute();
    }

    private File createNewApk() {
        File file = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    protected void onStart() {
        if (mSkinManager != null) {
            mSkinManager.register(this);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mSkinManager != null) {
            mSkinManager.unRegister(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopScreenRequest();
        }

        super.onStop();
    }

    private void startSnapShoot(View view) {
        View view1 = getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view1.getWidth(), view1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        String savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "a.png";
        Log.e("SnapShoot", "savePath = " + savePath);

        File saveFile = new File(savePath);
        if (bitmap != null) {
            try (FileOutputStream outputStream = new FileOutputStream(saveFile)) {
                Bitmap bitmaptmp = Bitmap.createBitmap(bitmap, 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                bitmaptmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                Toast.makeText(this, "SnapShoot Successfull!", Toast.LENGTH_LONG).show();
                outputStream.flush();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "FileNotFoundException!", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "IOException!", Toast.LENGTH_LONG).show();

//                e.printStackTrace();
            }

//            try {
//                FileOutputStream outputStream = new FileOutputStream(saveFile);
//                Bitmap bitmaptmp = Bitmap.createBitmap(bitmap, 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//                bitmaptmp.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//                Toast.makeText(this,"SnapShoot Successfull!",Toast.LENGTH_LONG).show();
//                outputStream.flush();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScreenRequest() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent mediaIntent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(mediaIntent, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScreenRequest() {
        if (!isStartCast) {
            return;
        }

        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        mediaCodec.flush();
        mediaCodec.release();
        mMediaRecorder.release();
        mediaProjection.stop();
        mediaProjection = null;
        isStartCast = false;
    }
    ProgressDialog progressDialog;
    public void showProgressDialog(){
         progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在搜索蓝牙设备");
        progressDialog.setMessage("请稍等。。。");
         if(!progressDialog.isShowing())
         progressDialog.show();
    }
    BlueAdapter blueAdapter;
    private void initBroadCast() {
        List<String> list = new ArrayList<String>();
        list.add(BluetoothDevice.ACTION_FOUND);
        list.add(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        list.add(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        BroadCastUtils broadCastUtils = BroadCastUtils.getInstance(MainActivity.this);
        broadCastUtils.setBroadcastcmd(list).setBroadCastCallBack(new BroadCastUtils.BroadCastCallBack() {
            @Override
            public void returnResult(Intent intent) {
                String action = intent.getAction();
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.blue_layout,null);
                ListView listView = view.findViewById(R.id.listview);
                if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                    bluetoothDeviceList.clear();
                    setProgressBarIndeterminateVisibility(true);
                } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String mac = device.getAddress();
                    if (device != null) {
                        bluetoothDeviceList.add(device);
//                Log.e("test","device is ="+ device.getName());
                    }

                    if(blueDialog != null && blueDialog.isShowing()){
                        blueAdapter.refresh(bluetoothDeviceList);
                        return;
                    }
                    blueDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("BlueTooth").create();
                    blueDialog.setView(view);
                    blueAdapter = new BlueAdapter(MainActivity.this,bluetoothDeviceList);
                    listView.setAdapter(blueAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
//                            BluetoothDevice tmp = bluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
                            ConnectThread connectThread = new ConnectThread(bluetoothDeviceList.get(position), null);
                            connectThread.start();
                        }
                    });
                        blueDialog.show();
                } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                    setProgressBarIndeterminateVisibility(false);
                    Log.e("intent", "action is " + action);
                }
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            List<BluetoothDevice> obj = (List<BluetoothDevice>) msg.obj;
            blueAdapter.refresh(obj);
        }
    };
    class BlueAdapter extends BaseAdapter{

        private List<BluetoothDevice> list;
        private Context context;

        public BlueAdapter(MainActivity mainActivity, List<BluetoothDevice> bluetoothDeviceList) {
            list = bluetoothDeviceList;
            context = mainActivity;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null)
                view = LayoutInflater.from(context.getApplicationContext()).inflate(android.R.layout.simple_list_item_2, parent,false);
            TextView textView1 = view.findViewById(android.R.id.text1);
            TextView textView2 = view.findViewById(android.R.id.text2);

            BluetoothDevice bluetoothDevice = (BluetoothDevice)getItem(position);
//            Log.e("blueDialog", "bluetoothDeviceList is " + bluetoothDevice.getName());
            textView1.setTextColor(R.color.black);
            textView2.setTextColor(R.color.black);
            textView1.setText(bluetoothDevice.getAddress());
            textView2.setText(bluetoothDevice.getName());
            return view;
        }
        public void refresh(List<BluetoothDevice> list){
            this.list = list;
            notifyDataSetChanged();
        }
    }
    @NetworkCheck("test")
    public void checkNetwork() {
        Log.e("test", "mm===" + NetworkUtils.isAvailableNetwork(this));
//        startActivity(new Intent(this,MapActivity.class));
    }

    @NoNetwork
    public void noNetwork() {
        new AlertDialog.Builder(this).setMessage("No Network !!! ").show();
    }

    AlertDialog alertDialog, blueDialog;

    private void showPassWordDialog(final String wifiName) {
        CustomEditText customEditText;
        view1 = getLayoutInflater().inflate(R.layout.login_layout, null);

        ViewGroup parent = (ViewGroup) view1.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        customEditText = view1.findViewById(R.id.password_edit);
        customEditText.setBackgroundStrokeColor(Color.RED);
        customEditText.setBackgroundTextSize(10);
        alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(view1)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this,"why you are so handsome",Toast.LENGTH_LONG).show();
                        if (wifiManager == null) {
                            return;
                        }
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }else {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                        }
                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        boolean isConnect = false;
                        for (WifiConfiguration wifiConfiguration:list){
                            if(wifiConfiguration.SSID.equals(wifiName) || wifiConfiguration.SSID.equals("\""+wifiName+"\"")){
                                if(!wifiManager.isWifiEnabled()){
                                    wifiManager.setWifiEnabled(true);
                                }
                                boolean ret = wifiManager.enableNetwork(wifiConfiguration.networkId,true);
                                Log.e("result"," =====ret="+ret);
                                isConnect = true;
                            }
                        }
                        if(!isConnect){
                            int result = wifiManager.addNetwork(createWifiConfig(wifiName,wifiName,customEditText.getText().toString()));
                            if(result < 0){
                                new Handler().postDelayed(()->{
                                        customEditText.setText("");
                                        alertDialog.setMessage("密码错误，重新输入");
                                        alertDialog.show();
                                },500);
                            }
                            Log.e("result"," =====result="+result);
                        }
                    }
                }).setNegativeButton("NO", (d,w)->{
                        customEditText.setText("");
                }).create();
        alertDialog.setMessage(wifiName);
//                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                                FrameLayout.LayoutParams.MATCH_PARENT,
//                                FrameLayout.LayoutParams.MATCH_PARENT);
//                        alertDialog.addContentView(editText,params);
        alertDialog.show();
    }
    private void reInputPassword() {
        setTitle("密码错误，重新输入");
    }

    private WifiConfiguration createWifiConfig(String ssid,String cap,String pwd) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + pwd + "\"";
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }

    private String[] convert(List<ScanResult> list){
        String[] tmp = new String[list.size()];
        for (int i=0;i<list.size();i++){
            tmp[i] = list.get(i).SSID;
        }
        return tmp;
    }
    private List<String> convertList(List<ScanResult> list){
        List<String> stringList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            stringList.add(list.get(i).SSID);
        }
        return stringList;
    }
    @Override
    protected void onResume() {
//        getWifiList();
        super.onResume();
    }

    private void initData() {
        DeviceBox instance = DeviceBox.INSTANCE;
        Log.e("test","Demo1 the size is "+ instance.getList().size());
        Observable.fromIterable(Arrays.asList("/sdcard/Download/", "/sdcard/Documents/"))
                .map((Function<String, List<File>>) dirPath -> {
                    File dir = new File(dirPath);
                    if (dir.exists()) {
                        return FileUtils.listFilesInDirWithFilter(dir, pathname -> !pathname.getName().endsWith(".xlog"));
                    } else {
                        return Collections.emptyList();
                    }
                })
                .toList()
//                .map(lists -> {
//                    List<String> fileNameList = new ArrayList<>();
//                    for (List<File> list : lists) {
//                        for (File file : list) {
//                            fileNameList.add(file.getAbsolutePath());
//                        }
//                    }
//                    return fileNameList;
//                })

                .flatMapObservable((Function<List<List<File>>, ObservableSource<List<SvgItem>>>) filePaths -> {
                    List<SvgItem> itemList = new ArrayList<>();
                    for (List<File> list : filePaths) {
                        for (File file : list) {
                            itemList.add(new SvgItem(null,file.getAbsolutePath()));
                            Log.e("itemList","filePath = "+file.getAbsolutePath());
                        }
                    }
//                    for (String filePath : filePaths) {
//                        itemList.add(new SvgItem(null,filePath));
//                        Log.e("itemList","filePath = "+filePath);
//                    }
                    Log.e("itemList","itemList = "+itemList.size());

                    return Observable.just(itemList);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SvgItem>>() {
            @Override
            public void accept(List<SvgItem> svgItems) throws Throwable {

            }
        });
    }

    private void processObservable() {

        loop:for (int i=0;i<100;i++){
//            Log.e("tttt","=================i = "+i);

            for (int j=0;j<100;j++){
//                Log.e("tttt","=================i = "+i+",j = "+j);

                if(j == 16){
                    continue loop;
                }

            }
        }
        new Thread(()->{
//            Log.e("tttt","=================this is a test 1");

        }).start();
        Disposable schedule = Schedulers.newThread().createWorker().schedule(() ->{
//                    Log.e("tttt","=================this is a test 2");
                }
        );

        ArrayList<SvgItem> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            SvgItem svgItem = new SvgItem(null, "" + Math.sqrt(i));
            list.add(svgItem);
        }
        Observable.fromIterable(list).map(result -> result.getName())
                .subscribeOn(Schedulers.computation()).observeOn(Schedulers.newThread()).subscribe(o->{

                }
//                Log.e("tttt","===================o="+o)
        );

    }

    WifiManager wifiManager;
    private List<ScanResult> getWifiList() {
        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        Log.e("select","===================="+scanResultList.size());
        List<ScanResult> wifilist = new ArrayList<ScanResult>();
        if(scanResultList != null && scanResultList.size()>0){
            List<String> containList = new LinkedList<>();

        for(ScanResult result:scanResultList){
            if(!result.SSID.isEmpty()){
                Log.e("select","===================name="+result.SSID+"-capabilities: "+result.capabilities);

                String wifiId = result.SSID+"-"+result.capabilities;

//                new AsyncTask<Long, Integer, File>() {
//                    @Override
//                    protected File doInBackground(Long... longs) {
//                        return null;
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(Integer... values) {
//                        super.onProgressUpdate(values);
//                    }
//
//                    @Override
//                    protected void onPostExecute(File file) {
//                        super.onPostExecute(file);
//                    }
//                };


                if(!containList.contains(wifiId)){
                    containList.add(wifiId);
                    wifilist.add(result);
                }
            }
        }
        }
        return wifilist;
    }

    private void showResult() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),image);
        Log.e("111","===================bitmap="+bitmap.getRowBytes());
        Bitmap idNumber;
        if(image == images[0]){
            idNumber = JavaApi.findIdNumber(bitmap,Bitmap.Config.ARGB_8888);
        }else{
            idNumber = JavaApi.findPhotoNumber(bitmap,Bitmap.Config.ARGB_8888);
        }
        bitmap.recycle();
        if(idNumber != null){
            iv.setImageBitmap(idNumber);
        }else{
            return;
        }
        tessBaseAPI.setImage(idNumber);
        Log.e("111","===================getUTF8Text="+tessBaseAPI.getUTF8Text());
        tv.setText(tessBaseAPI.getUTF8Text());
    }

    private void initTess() {
        asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = getAssets().open("lbpcascade_frontalface.xml");
                    File file = new File("/sdcard/lbpcascade_frontalface.xml");

//                    is = getAssets().open(language+".traineddata");
//                    File file = new File("/sdcard/tess/tessdata/"+language+".traineddata");
                    if(!file.exists()){
                        file.getParentFile().mkdirs();
                        fos = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = is.read(buffer))!= -1){
                            fos.write(buffer,0,len);
                        }
                        fos.close();
                    }
                    is.close();

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),image);
                    bitmap1 =  (Bitmap)JavaApi.FaceDetection_loadCascade(file.getAbsolutePath(),bitmap,Bitmap.Config.ARGB_8888);
                    boolean isInited = tessBaseAPI.init("/sdcard/tess",language);
                    Log.e("result","=============11===isInited="+isInited);

                    return isInited;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                        try {
                            if(null != is) {
                                is.close();
                            }
                            if(null != fos) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Log.e("result","================aBoolean="+aBoolean);
                iv.setImageBitmap(bitmap1);
//                JavaApi.main1(bitmap1, Bitmap.Config.ARGB_8888);
                if(aBoolean){
                    Toast.makeText(MainActivity.this,"初始化成功!",Toast.LENGTH_LONG).show();
                    tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "/*京津苏粤闽甘桂鄂赣新皖浙沪渝新鲁冀豫云辽黑湘*/ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"); // 识别白名单
                    tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?"); // 识别黑名单

                }
                else{
                    finish();
                }
            }
        };
        asyncTask.execute();
    }

    Map<String,BluetoothDevice> blueToothDeviceMap = new ArrayMap<>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.enable()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            registerForActivityResult(new ActivityResultContract() {
//                @NonNull
//                @Override
//                public Intent createIntent(@NonNull Context context, Object input) {
//                    Log.e("Result111","result = ");
//
//                    return null;
//                }
//
//                @Override
//                public Object parseResult(int resultCode, @Nullable Intent intent) {
//                    Log.e("Result222","intent = "+intent);
//                    return intent;
//                }
//            }, new ActivityResultCallback() {
//                @Override
//                public void onActivityResult(Object result) {
//                    Log.e("Result333","result = "+result);
//
//                }
//            }).launch(intent);
            startActivityForResult(intent, 101);
            return;
        }
            bluetoothAdapter.startDiscovery();
            BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            if(bluetoothLeScanner != null)
            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
                    String device = result.toString();
                    String deviceName = device.substring(device.indexOf("=") + 1, device.indexOf(","));
                    if (blueToothDeviceMap.get(deviceName) == null) {
                        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceName);
                        blueToothDeviceMap.put(deviceName, bluetoothDevice);
                        Log.e("test", "result is " + deviceName + ",bluetoothDevice = " + bluetoothDevice.toString());
                    }
                    super.onScanResult(callbackType, result);
                }
            });

            Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
            List<String> stringList = new ArrayList<>();
            if (bluetoothDeviceSet.size() > 0) {
                for (BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
                    stringList.add(bluetoothDevice.getName());
                    Log.e("test", "bluetoothname is " + bluetoothDevice.getName());
                }
            }
        if(bluetoothAdapter != null){
            AcceptThread acceptThread = new AcceptThread();
            acceptThread.start();
        }

    }
    public class AcceptThread extends Thread {
        BluetoothServerSocket serverSocket = null;
        BluetoothSocket socket = null;
        public AcceptThread() {
            try {
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("BLUE_TEST", MY_UUID_INSECURE);
                Log.e("test","====serverSocket="+serverSocket.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true){
                if(serverSocket!= null)
                    try {
                        socket = serverSocket.accept();
                        InputStream inputStream = socket.getInputStream();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if(state == 1){
                    connectBlue(socket);
                }

            }
        }
    }

    private void connectBlue(BluetoothSocket serverSocket) {

        ReadTread readTread = new ReadTread(serverSocket);
        readTread.start();

    }
    public class ReadTread extends Thread{
        BluetoothSocket socket;
        public ReadTread(BluetoothSocket serverSocket) {
            socket = serverSocket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                Log.e("ReadTread","inputStream = "+inputStream.available());
                OutputStream outputStream = socket.getOutputStream();
                Log.e("ReadTread","outputStream = "+outputStream.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    public class ConnectThread extends Thread{

        private final BluetoothSocket socket;
        public ConnectThread(BluetoothDevice device, Handler handler) {
            Log.e("ConnectThread","device="+device.getName());
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            BluetoothSocket tmp = null;
//            Method m = null;
//            try {
//                m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//                //由BluetoothDevice衍生出BluetoothSocket, createRfcommSocket来选择连接的服务和协议
//                tmp = (BluetoothSocket) m.invoke(device, 1);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
//                tmp = device.createRfcommSocketToServiceRecord(uuid);
                Log.e("ConnectThread","socket="+(tmp == null));
            } catch (IOException e) {
                Log.e("ConnectThread","e="+e);
                e.printStackTrace();
            }
            socket = tmp;
            state = 1;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
                Log.e("ConnectThread","socket="+socket.isConnected());

            } catch (IOException e) {
                Log.e("ConnectThread","socket connect fail!");
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
                return;
            }
        }
    }

    List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("MediaProjectionCallback","========onStop========");
        stopScreenRequest();
            super.onStop();
        }
    }

    private class EncodeThread extends Thread{
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            encodeProcess();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void encodeProcess() {
        //编码部分
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        //获取byteBuffer在队列中的位置
        int index = mediaCodec.dequeueOutputBuffer(bufferInfo,0);

        while (isStartCast){

            if(index >= 0){
                //取出编码成功的bytebuffer
                ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(index);
                byte[] outData = new byte[bufferInfo.size];
                //将bytebuffer的数据放在outData中
                outputBuffer.get(outData);
//                    try {
//                        fileOutputStream.write(outData);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                if(outData != null){
                    for (byte b:outData){
                        //关键帧
                        if(b == 0x65){
                            Log.e("outData","==============index=>"+index);
                        }
                    }
                }

                mediaCodec.releaseOutputBuffer(index,false);
                index = mediaCodec.dequeueOutputBuffer(bufferInfo,0);
            }else {
                index = mediaCodec.dequeueOutputBuffer(bufferInfo,0);
                continue;
            }
        }
    }
    private void networkRequestLoadDown(){
        try {
            String tmp = "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=2&pn=0&spn=0&di=173470&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2534506313%2C1688529724&os=1097436471%2C408122739&simid=3354786982%2C133358663&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=%E5%9B%BE%E7%89%87&objurl=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fp7rtwg_z%26e3Bkwthj_z%26e3Bv54AzdH3Ftrw1AzdH3Fwn_89_0c_a8naaaaa8m98bm8d8nmm0cmbanmbm_3r2_z%26e3Bip4s&gsm=1&islist=&querylist=";
            URL url = new URL("https://");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            File file = new File("");
            OutputStream fileOutputStream = new FileOutputStream(file);
            int len = 0;
            byte[] bytes = new byte[256];
            while ((len = inputStream.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,len);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeSkin(View v){
//        String[] items = new String[]{"蓝色（默认）", "黑色", "白色"};
//        new QMUIDialog.MenuDialogBuilder(this)
//                .addItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        QDSkinManager.changeSkin(which + 1);
//                        dialog.dismiss();
//                    }
//                })
//                .setSkinManager(QMUISkinManager.defaultInstance(this))
//                .create()
//                .show();
    }

    public static boolean setSystemAlarmClock(Context context, String message, int hour, int minute) {
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        }
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        if (Build.VERSION.SDK_INT >= 11) {
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
