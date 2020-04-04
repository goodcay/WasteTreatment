package com.waste.treatment.ui.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.TransportMode;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.ActivityTraceBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TraceActivity extends AppCompatActivity {
    private LBSTraceClient mTraceClient;
    private ActivityTraceBinding mBinding;

    private MapView mapView;
    private Overlay mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trace);
        initBaiDuMap();
        // 轨迹服务ID
        long serviceId = 220106;
        // 设备标识
        String entityName = "myTrace";
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        boolean isNeedObjectStorage = true;
        // 初始化轨迹服务
        final Trace mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(getApplicationContext());


        //设置定位和回传周期

        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);


        //初始化监听器
        final OnTraceListener mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {
                Log.d(WasteTreatmentApplication.TAG, "onBindServiceCallback: " + s);

            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStartTraceCallback: " + message);
                if (message.equals("成功")) {
                    mBinding.tvFw.setText("服务开启");
                }

            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStopTraceCallback: " + message);
                if (message.equals("成功")) {
                    mBinding.tvFw.setText("服务停止");
                }
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStartGatherCallback: " + message);
                if (message.equals("成功")) {
                    mBinding.tvCj.setText("采集开启");
                }

            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStopGatherCallback: " + message);
                if (message.equals("成功")) {
                    mBinding.tvCj.setText("采集停止");
                }
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                Log.d(WasteTreatmentApplication.TAG, "onPushCallback: " + message);

            }

            @Override
            public void onInitBOSCallback(int i, String s) {
                Log.d(WasteTreatmentApplication.TAG, "onInitBOSCallback: " + s);


            }
        };

        mBinding.btnKqfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WasteTreatmentApplication.TAG, "开启: ");
                // 开启服务
                mTraceClient.startTrace(mTrace, mTraceListener);
            }
        });
        mBinding.btnKqcj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启采集
                mTraceClient.startGather(mTraceListener);
            }
        });
        mBinding.btnTzfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止服务
                mTraceClient.stopTrace(mTrace, mTraceListener);

            }
        });
        mBinding.btnTzcj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止采集
                mTraceClient.stopGather(mTraceListener);
            }
        });
        mBinding.btnCxgj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkTrace();
            }
        });





    }

    /**
     * 查询轨迹
     */
    private void checkTrace(){




        // 请求标识
        int tag = 10;
// 轨迹服务ID
        long serviceId = 220106;
// 设备标识
        String entityName = "myTrace";
// 创建历史轨迹请求实例
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);

         long startTime =dateTimeStamp("2020-04-02 17:56:30","yyyy-MM-dd HH:mm:ss");
        long endTime =dateTimeStamp("2020-04-02 18:05:30","yyyy-MM-dd HH:mm:ss");
        Log.d(WasteTreatmentApplication.TAG, "startTime: "+startTime);
        Log.d(WasteTreatmentApplication.TAG, "endTime: "+endTime);
// 设置开始时间
        historyTrackRequest.setStartTime(startTime);
// 设置结束时间
        historyTrackRequest.setEndTime(endTime);


// 设置需要纠偏
        historyTrackRequest.setProcessed(true);


// 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
// 设置需要去噪
        processOption.setNeedDenoise(true);
// 设置需要抽稀
        processOption.setNeedVacuate(true);
// 设置需要绑路
        processOption.setNeedMapMatch(true);
// 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
// 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.auto);
// 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);


// 设置里程填充方式为驾车
        historyTrackRequest.setSupplementMode(SupplementMode.driving);


// 初始化轨迹监听器
        OnTrackListener mTrackListener = new OnTrackListener() {
            // 历史轨迹回调
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                List<LatLng> points = new ArrayList<LatLng>();
                for (int i=0;i<response.getTrackPoints().size();i++){
                    LatLng p = new LatLng(response.getTrackPoints().get(i).getLocation().latitude, response.getTrackPoints().get(i).getLocation().longitude);
                    points.add(p);
                }
                showMap(points);
                Log.d(WasteTreatmentApplication.TAG, "onHistoryTrackCallback: "+response);
            }
        };


// 查询轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);








    }
    public static long dateTimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return Long.parseLong(String.valueOf(sdf.parse(date).getTime() / 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    private void initBaiDuMap() {
        BaiduMapOptions bOptions = new BaiduMapOptions();
        bOptions.zoomControlsEnabled(false); //放大缩小控件
        // bOptions.mapType(BaiduMap.M); //地图模式
        bOptions.scrollGesturesEnabled(true); //是都拖拽
        bOptions.compassEnabled(true);
        mapView = new MapView(this, bOptions);
        mBinding.llMap.addView(mapView);

        mapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null, 0xAAFFFF88, 0xAA00FF00);
        mapView.getMap().setMyLocationConfiguration(myLocationConfiguration);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


    }
    private void showMap(List<LatLng> points) {
        if (mPolyline != null) {
            mPolyline.remove();
        }
            /*  LatLng p1 = new LatLng(39.97923, 116.357428);
        LatLng p2 = new LatLng(39.94923, 116.397428);

        points.add(p2);
        points.add(p3);*/

//设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
//在地图上绘制折线
//mPloyline 折线对象
        mPolyline = mapView.getMap().addOverlay(mOverlayOptions);


        // LatLng cenpt = new LatLng(29.806651, 160.606983);

        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(points.get((int) Math.ceil(points.size() / 2)))
                .zoom(13)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mapView.getMap().setMapStatus(mMapStatusUpdate);//改变地图状态


     /*   MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(points.get((int) Math.ceil(points.size() / 2)).latitude)
                .longitude(points.get((int) Math.ceil(points.size() / 2)).longitude).build();
        mapView.getMap().setMapStatus();*/

    }
}
