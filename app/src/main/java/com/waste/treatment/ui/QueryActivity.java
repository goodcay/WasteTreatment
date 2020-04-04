package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.DatabaseUtils;
import android.device.ScanDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

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
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.Data1Bean;
import com.waste.treatment.bean.Data2Bean;
import com.waste.treatment.bean.GetPosBean;
import com.waste.treatment.databinding.ActivityQueryBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QueryActivity extends AppCompatActivity {
    private ActivityQueryBinding mBinding;
    ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";
    private String barcodeStr;
    private MapView mapView;
    private Overlay mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_query);
        mBinding.llMap.setVisibility(View.INVISIBLE);
        sm = new ScanDevice();
        sm.setOutScanMode(0);//启动就是广播模式
        sm.openScan();
        mBinding.tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtCode.getText().toString().trim().isEmpty()) {
                    Tips.show("请输入或者扫描条码");
                } else {
                    if (mBinding.edtCode.getText().toString().trim().length() < 14) {
                        Tips.show("条码为14位");
                    } else {
                        showData(mBinding.edtCode.getText().toString().trim());
                        getPos(mBinding.edtCode.getText().toString().trim());

                    }

                }
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initBaiDuMap();
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            byte[] aimid = intent.getByteArrayExtra("aimid");
            barcodeStr = new String(barocode, 0, barocodelen);
            mBinding.edtCode.setText(barcodeStr);
            showData(mBinding.edtCode.getText().toString().trim());
            getPos(mBinding.edtCode.getText().toString().trim());
            sm.stopScan();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }
    }

    private void showData(String recyleCode) {
        HttpClient.getInstance().geData().getRecyleByCode(recyleCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data2Bean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Data2Bean data2Bean) {
                        if (data2Bean.getIsSuccess()) {
                            Log.d(WasteTreatmentApplication.TAG, "onNext: " + data2Bean.getContent().toString());
                            mBinding.tvGsmc.setText(data2Bean.getContent().getCompany().getName());
                            mBinding.tvFwbm.setText(data2Bean.getContent().getCode());
                            mBinding.tvFwlx.setText(data2Bean.getContent().getName());
                            mBinding.tvFwzl.setText(data2Bean.getContent().getWeight().toString());
                            mBinding.tvSjsj.setText(Utils.timeToTime1(data2Bean.getContent().getRecyleTime()));
                            mBinding.tvYscp.setText(data2Bean.getContent().getRouteId().getCarId().getName());
                            mBinding.tvYssj.setText(data2Bean.getContent().getRouteId().getDriver().getChineseName());
                            mBinding.tvSjr.setText(data2Bean.getContent().getRouteId().getBeginOperator().getChineseName());

                        } else {
                            Log.d(WasteTreatmentApplication.TAG, "onNext:error ");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "onError: " + e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void getPos(String code) {

        HttpClient.getInstance().geData().getPos(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetPosBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetPosBean getPosBean) {
                        //构建折线点坐标
                        List<LatLng> points = new ArrayList<LatLng>();
                        for (int i = 0; i < getPosBean.getContent().getPos().size(); i++) {
                            LatLng p = new LatLng(getPosBean.getContent().getPos().get(i).getX(), getPosBean.getContent().getPos().get(i).getY());
                            points.add(p);
                        }
                        if (points.size()>2){
                            showMap(points);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


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
        mBinding.llMap.setVisibility(View.VISIBLE);

     /*   MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(points.get((int) Math.ceil(points.size() / 2)).latitude)
                .longitude(points.get((int) Math.ceil(points.size() / 2)).longitude).build();
        mapView.getMap().setMapStatus();*/

    }
}
