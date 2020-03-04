package com.waste.treatment.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityGpsBinding;

import java.util.List;



public class GpsActivity extends AppCompatActivity {
    private String TAG = "CAY";
    private ActivityGpsBinding mBinding;
    private Geocoder geocoder;
    private List<Address> addressList;
    private StringBuilder sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_gps);
      //  LocationClient ml = new LocationClient(this);
      /*  ml.registerLocationListener(new MyLocationListener());
        mBinding.getGpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mBinding.gpsAddressBtn.setText("获取位置");
               // mBinding.gpsAddressBtn.setText("获取位置");
               // mBinding.gpsAddressBtn.setText("获取位置");
                //initData();
           //     initLocationOption();
            }
        });*/

    }
    /**
     * 初始化定位参数配置
     */

/*
    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        locationClient.start();
    }
*/
    /**
     * 实现定位回调
     */
    public class MyLocationListener1 extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
            Log.d(TAG,"address"+location.getAddrStr());
            Log.d(TAG,"city"+location.getCity());
            Log.d(TAG,"countyr"+location.getCountry());
            Log.d(TAG,"province"+location.getProvince());
            Log.d(TAG,"district"+location.getDistrict());
            Log.d(TAG,"street"+location.getStreet());
            Log.d(TAG,"latitude"+location.getLatitude());
            Log.d(TAG,"longitude"+location.getLongitude());
            mBinding.gpsJwTv.setText(""+latitude+","+longitude);
            mBinding.gpsAddressTv.setText(location.getAddrStr());

        }
    }

/*
    private void initData() {
        mBinding.gpsAddressBtn.setText("开始");

        // 获取经纬度坐标
        // 1 获取位置管理者对象
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 2 通过lm获得经纬调度坐标
        // (参数： provider（定位方式 提供者 通过 LocationManager静态调用），
        // minTime（获取经纬度间隔的最小时间 时时刻刻获得传参数0），
        // minDistance（移动的最小间距 时时刻刻传0），LocationListener（监听）)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(GpsActivity.this,"没权限",Toast.LENGTH_LONG).show();
             mBinding.gpsAddressBtn.setText("没权限");

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 获取经纬度主要方法
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Toast.makeText(GpsActivity.this,"精度"+latitude+","+longitude,Toast.LENGTH_LONG).show();
                mBinding.gpsJwBtn.setText(latitude+","+longitude);
                sb = new StringBuilder();
                geocoder = new Geocoder(GpsActivity.this);
                addressList = new ArrayList<Address>();

                try {
                    // 返回集合对象泛型address
                    addressList= geocoder.getFromLocation(latitude,longitude,1);


                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getFeatureName());//周边地址
                    }
                    Toast.makeText(GpsActivity.this,"位置："+sb.toString(),Toast.LENGTH_LONG).show();

                    mBinding.gpsAddressBtn.setText("当前位置"+sb.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                mBinding.gpsJwBtn.setText("onStatusChanged："+s);

                //状态发生改变监听
            }

            @Override
            public void onProviderEnabled(String s) {
                mBinding.gpsJwBtn.setText("onProviderEnabled："+s);
                // GPS 开启的事件监听
            }

            @Override
            public void onProviderDisabled(String s) {
                mBinding.gpsJwBtn.setText("onProviderDisabled："+s);
                // GPS 关闭的事件监听
            }
        });
    }
*/
}
