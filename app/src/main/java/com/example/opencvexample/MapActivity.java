package com.example.opencvexample;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableMap;

import com.example.opencvexample.Static_enum.DeviceBox;
import com.example.opencvexample.databinding.ActivityMapBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    SvgView img1;
    Spinner province;
    TextView locate_status;
    ActivityMapBinding mapBinding;

    //放入经纬度就可以了
    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String countryName = address.getCountryName();
                String adminArea = address.getAdminArea();
                String locality = address.getLocality();
                String featureName = address.getFeatureName();
                return countryName+adminArea+locality+featureName ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MapActivity.this, "onLocationChanged函数被触发！", Toast.LENGTH_SHORT).show();
            Log.e("test","onLocationChanged=getLatitude="+location.getLatitude());
            Log.e("test","onLocationChanged=getAltitude="+location.getLongitude());

            new Handler().post(()->{
                locate_status.setText("所在位置："+getAddress(location.getLatitude(),location.getLongitude())+",纬度："+location.getLatitude()+"，经度："+location.getLongitude()+"，海拔："+location.getAltitude()
                +"，时间：" + new SimpleDateFormat("yyy-MM-dd,HH:mm:ss").format(new Date(location.getTime())));

            });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Toast.makeText(MapActivity.this, "onStatusChanged：当前GPS状态为可见状态", Toast.LENGTH_SHORT).show();
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(MapActivity.this, "onStatusChanged:当前GPS状态为服务区外状态", Toast.LENGTH_SHORT).show();
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(MapActivity.this, "onStatusChanged:当前GPS状态为暂停服务状态", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapActivity.this, "onProviderEnabled！", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private LocationManager locationManager;
    private String locateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
        mapBinding = DataBindingUtil.setContentView(this,R.layout.activity_map);
        ObservableMap<String, Object> map = new ObservableArrayMap<>();
        map.put("name",":hello");
        map.put("age",22);
        map.put("address","china");
        mapBinding.setMap(map);
        map.put("name",":hi");
        DeviceBox instance = DeviceBox.INSTANCE;
        Log.e("test","Demo1 the size is "+ instance.getList().size());
        List<String> provinces = insertChinaProvincesData();
        img1 = findViewById(R.id.img1);
        province = findViewById(R.id.location);
        img1.setNameList(provinces);
        img1.init(this, R.raw.chinahigh, (v,pos)->{
                province.setSelection(pos);
        });
        locate_status = findViewById(R.id.location_status);
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(provinces.size()>0)
                img1.setSelect(position);
                getGPS();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        openGPS();
    }

    private List<String> insertChinaProvincesData() {
        String[] strings =  getResources().getStringArray(R.array.provinces_array);
        List<String> list = new ArrayList<>();
        for (String s:strings){
            list.add(s);
        }
//        list.add("安徽");
//        list.add("北京");
//        list.add("重庆");
//        list.add("福建");
//        list.add("广东");
//        list.add("甘肃");
//        list.add("广西");
//        list.add("贵州");
//        list.add("海南");
//        list.add("河北");
//        list.add("河南");
//        list.add("香港");
//        list.add("黑龙江");
//        list.add("湖南");
//        list.add("湖北");
//        list.add("吉林");
//        list.add("江苏");
//        list.add("江西");
//        list.add("辽宁");
//        list.add("澳门");
//        list.add("内蒙古");
//        list.add("宁夏");
//        list.add("青海");
//        list.add("陕西");
//        list.add("四川");
//        list.add("山东");
//        list.add("上海");
//        list.add("山西");
//        list.add("天津");
//        list.add("台湾");
//        list.add("新疆");
//        list.add("西藏");
//        list.add("云南");
//        list.add("浙江");
        return list;
    }

    /**
     * @return
     */
//    public native String getJNIString();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGPS(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        Log.e("test","locationEnabled="+locationEnabled);

        locateType = LocationManager.GPS_PROVIDER;
//        locateType = locationManager.getBestProvider(createFineCriteria(),true);
//        Log.e("test","provider="+providerName);
//        LocationProvider provider = locationManager.getProvider(providerName);
    }
    
    private void getGPS(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            return;
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        Location location = locationManager.getLastKnownLocation(locateType); // 通过GPS获取位置
        Log.e("test1","onLocationChanged=locateType="+locateType);
        Log.e("test1","onLocationChanged=location="+location);


        if (location == null) {
//            Log.e("test1","onLocationChanged=getLatitude="+location.getLatitude());
//            Log.e("test1","onLocationChanged=getAltitude="+location.getAltitude());
//            while (location == null){

//                Log.e("test1","onLocationChanged=getLatitude="+location.getLatitude());

//            }
            locateType = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(locateType, 3000,0,
                    locationListener);
//            updateUI(location);
        }
//        locationListener.onLocationChanged(location);
        // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米

    }
    private Criteria createFineCriteria(){
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        c.setAltitudeRequired(true);//包含高度信息
        c.setBearingRequired(true);//包含方位信息
        c.setSpeedRequired(true);//包含速度信息
        c.setCostAllowed(true);//允许付费
        c.setPowerRequirement(Criteria.POWER_HIGH);//高耗电
        return c;
    }
}
