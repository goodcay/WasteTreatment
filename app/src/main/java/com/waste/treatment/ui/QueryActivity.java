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
import android.printer.sdk.PosFactory;
import android.printer.sdk.bean.BarCodeBean;
import android.printer.sdk.bean.TextData;
import android.printer.sdk.bean.enums.ALIGN_MODE;
import android.printer.sdk.constant.BarCode;
import android.printer.sdk.interfaces.IPosApi;
import android.printer.sdk.interfaces.OnPrintEventListener;
import android.printer.sdk.util.PowerUtils;
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

import cn.pda.serialport.SerialDriver;
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
    private IPosApi mPosApi;


    private String danwei;
    private String types;
    private String chepai;
    private String zhongliang;
    private String siji;
    private String shoujiren;
    private String time;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_query);
        Bundle bundle = getIntent().getExtras();
        String code1 = bundle.getString("code");
        assert code1 != null;
        if (!code1.equals("")){
            showData(code1);
            getPos(code1);

        }
        mBinding.llMap.setVisibility(View.INVISIBLE);
        sm = new ScanDevice();
        sm.setOutScanMode(0);//启动就是广播模式
        sm.openScan();

        initPos();
        initBaiDuMap();

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
        mBinding.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print(danwei,types,chepai,zhongliang,siji,shoujiren,time,code);
            }
        });
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
       /* if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }*/
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
                            danwei = data2Bean.getContent().getCompany().getName();
                            types = data2Bean.getContent().getName();
                            chepai = data2Bean.getContent().getRouteId().getCarId().getName();
                            zhongliang = data2Bean.getContent().getWeight().toString();
                            time = Utils.timeToTime1(data2Bean.getContent().getRecyleTime());
                            siji = data2Bean.getContent().getRouteId().getDriver().getChineseName();
                            shoujiren = data2Bean.getContent().getRouteId().getDriver().getChineseName();
                            code = data2Bean.getContent().getCode();


                            Log.d(WasteTreatmentApplication.TAG, "onNext: " + data2Bean.toString());

                            Log.d(WasteTreatmentApplication.TAG, "onNext: " + data2Bean.getContent().toString());
                            mBinding.tvGsmc.setText(data2Bean.getContent().getCompany().getName());
                            mBinding.tvFwbm.setText(data2Bean.getContent().getCode());
                            mBinding.tvFwlx.setText(data2Bean.getContent().getName());
                            mBinding.tvFwzl.setText(data2Bean.getContent().getWeight().toString());
                            mBinding.tvSjsj.setText(Utils.timeToTime1(data2Bean.getContent().getRecyleTime()));
                            mBinding.tvYscp.setText(data2Bean.getContent().getRouteId().getCarId().getName());
                            mBinding.tvYssj.setText(data2Bean.getContent().getRouteId().getDriver().getChineseName());
                            mBinding.tvSjr.setText(data2Bean.getContent().getRouteId().getBeginOperator().getChineseName());
                            mBinding.printBtn.setVisibility(View.VISIBLE);

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
                        Log.d(WasteTreatmentApplication.TAG, "getPosBean: " + getPosBean.toString());
                        //构建折线点坐标
                        List<LatLng> points = new ArrayList<LatLng>();
                        for (int i = 0; i < getPosBean.getContent().getPos().size(); i++) {
                            LatLng p = new LatLng(getPosBean.getContent().getPos().get(i).getX(), getPosBean.getContent().getPos().get(i).getY());
                            points.add(p);
                        }
                        if (points.size() > 2) {
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


    public void initPos() {
        PowerUtils.powerOnOrOff(1, "1");
        PosFactory.registerCommunicateDriver(this, new SerialDriver()); // 注册串口类 Register serial driver
        mPosApi = PosFactory.getPosDevice(); // 获取打印机实例 get printer driver
        mPosApi.setPrintEventListener(onPrintEventListener);
        mPosApi.openDev("/dev/ttyS2", 115200, 0);
        mPosApi.setPos()  //设置打印机
                .setAutoEnableMark(true)//开启黑标
                .setEncode(-1)  //设置编码  1 为UNICODE编码  2为UFT-8编码 3 为 CODEPAGE 编码 默认-1
                .setLanguage(15) // 0 为英语 15简体中文 39 阿拉伯语 21 俄语  默认-1
                .setPrintSpeed(-1) //  设置打印速度
                .setMarkDistance(-1) //检测到黑标后走的距离
                .init();// 初始化打印机 init printer

        //mPosApi.addFeedPaper(true,60); //设置走空纸   最大956mm
        Log.d(WasteTreatmentApplication.TAG, "initPos: ");
    }

    private void print(String danwei, String types, String chepai, String zhongliang, String siji, String shoujiren, String time, String code) {
        TextData textData1 = new TextData();
        textData1.addConcentration(25);
        textData1.addFont(BarCode.FONT_ASCII_12x24);
        textData1.addTextAlign(BarCode.ALIGN_LEFT);
        textData1.addFontSize(BarCode.NORMAL);
        textData1.addText("单位：" + danwei);
        textData1.addText("\n");
               /* textData1.addText("标号：2020031900000039");
                textData1.addText("\n");*/
        textData1.addText("名称：" + types);
        textData1.addText("\n");
        textData1.addText("车牌：" + chepai + "  重量：" + zhongliang + "Kg");
        textData1.addText("\n");
        textData1.addText("司机：" + siji + "  收集人：" + shoujiren);
        textData1.addText("\n");
        textData1.addText("时间：" + time);
        mPosApi.addText(textData1);
        BarCodeBean barCodeBean = new BarCodeBean();
        barCodeBean.setConcentration(25);
        barCodeBean.setHeight(60);
        barCodeBean.setWidth(2);// 条码宽度1-4; Width value 1 2 3 4
        barCodeBean.setText(code);
        barCodeBean.setBarType(BarCode.CODE128);
        mPosApi.addBarCode(barCodeBean, ALIGN_MODE.ALIGN_CENTER);
        mPosApi.addMark();
        //  mPosApi.addFeedPaper(true, 2);
        mPosApi.printStart();


    }

    public OnPrintEventListener onPrintEventListener = new OnPrintEventListener() {
        @Override
        public void onPrintState(int state) {
            switch (state) {
                case BarCode.ERR_POS_PRINT_SUCC:
                    Tips.show("打印成功");
                    //showToastShort (getString (R.string.toast_print_success));
                    break;
                case BarCode.ERR_POS_PRINT_FAILED:
                    Tips.show("打印错误");
                    //  showToastShort (getString (R.string.toast_print_error));
                    break;
                case BarCode.ERR_POS_PRINT_HIGH_TEMPERATURE:
                    Tips.show("温度过高");
                    // showToastShort (getString (R.string.toast_high_temperature));
                    break;
                case BarCode.ERR_POS_PRINT_NO_PAPER:
                    Tips.show("没有纸张");
                    //showToastShort (getString (R.string.toast_no_paper));
                    break;
                case 4:
                    break;
            }
        }
    };
}
