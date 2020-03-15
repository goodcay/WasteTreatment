package com.waste.treatment.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.BeginRouteBean;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetDriverBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.FragmentHomeBinding;
import com.waste.treatment.http.HttpUtils;
import com.waste.treatment.ui.CollectActivity;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.ui.ScanActivity;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.math.BigDecimal;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.waste.treatment.WasteTreatmentApplication.TAG;

public class HomeFragment extends BaseFragment<FragmentHomeBinding>{
    private LocationClient mLocationClient;
    private MapView mapView;
    private boolean isGps =false;
    private int carId =0;
    private int driveId =0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public int setContent() {
        Log.d(TAG, "setContent: ");
        return R.layout.fragment_home;
    }

    @Override
    protected void loadData() {
        super.loadData();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        bindingView.ilTitle.tvTitle.setText(getResources().getString(R.string.home));
        bindingView.ilTitle.tvRightText.setText(Utils.getDate(Utils.DATE_YMD));
     //   bindingView.ilTitle.tvLeftText.setText(WasteTreatmentApplication.instance.userName);

        bindingView.llYfsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });

        bindingView.llYfrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RuiKuActivity.class));
            }
        });
        bindingView.llYfsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanActivity.class));
            }
        });
       // showContentView();


            initLocation();
            initBaiDuMap();
       // showNormalDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");






    }

    private void initLocation() {
        //定位初始化
        mLocationClient = new LocationClient(getActivity());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//请求间隔时间
        option.setLocationNotify(true);//GPS有效时  按照1s 输出GPS 结果
        //option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程 。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        //设置locationClientOptionABC

        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        HomeFragment.MyLocationListener myLocationListener = new HomeFragment.MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }

    private void initBaiDuMap() {
        BaiduMapOptions bOptions = new BaiduMapOptions();
        bOptions.zoomControlsEnabled(false); //放大缩小控件
        // bOptions.mapType(BaiduMap.M); //地图模式
        bOptions.scrollGesturesEnabled(false); //是都拖拽
        bOptions.compassEnabled(true);
        mapView = new MapView(Objects.requireNonNull(getActivity()), bOptions);
        bindingView.llBaiduMap.addView(mapView);

        mapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null, 0xAAFFFF88, 0xAA00FF00);
        mapView.getMap().setMyLocationConfiguration(myLocationConfiguration);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));



    }



    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView.getMap() == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mapView.getMap().setMyLocationData(locData);
            showContentView();



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
           // Log.d(WasteTreatmentApplication.TAG, "latitude: "+latitude+"    longitude:"+longitude+"    radius:"+radius+"     coorType:"+coorType+"     errorCode:"+errorCode);
            /*HttpUtils.getInstance().geData().addPos(2,Double.toString(latitude),Double.toString(longitude)).subscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Success>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Success success) {
                          //  Log.d(WasteTreatmentApplication.TAG, "onNextppos" + success.toString()+success.getIsSuccess());

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "pos:"+ e.getMessage()+new BigDecimal(3.5));

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
*/
        }
    }

    @Override
    public void onPause() {
        super.onPause();
       //mapView.onPause();
        Log.d(TAG, "onPause: ");    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // mapView.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onResume() {
        super.onResume();
      //  mapView.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        //  normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("是否生成路线");
        //normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showCarIdDialog();
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showSingleChoiceDialog();
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
    private void showCarIdDialog() {
        HttpUtils.getInstance().geData().getCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCarsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final GetCarsBean getCarsBean) {
                        final String[] items = new String[getCarsBean.getContent().size()];
                        carId=getCarsBean.getContent().get(0).getOid();

                        for (int i=0 ;i<getCarsBean.getContent().size();i++){
                            items[i]=getCarsBean.getContent().get(i).getName();
                        }
                        AlertDialog.Builder singleChoiceDialog =
                                new AlertDialog.Builder(getActivity());
                        singleChoiceDialog.setTitle("请选择运输车辆");
                        // 第二个参数是默认选项，此处设置为0
                        singleChoiceDialog.setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        carId=getCarsBean.getContent().get(which).getOid();
                                        Log.d(TAG, "onClick: "+carId);

                                    }
                                });
                        singleChoiceDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showDriveDialog();
                                    }
                                });
                        singleChoiceDialog.show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    private void showDriveDialog() {
        HttpUtils.getInstance().geData().getDriver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetDriverBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final GetDriverBean getCarsBean) {
                        final String[] items = new String[getCarsBean.getContent().size()];

                        for (int i=0 ;i<getCarsBean.getContent().size();i++){
                            items[i]=getCarsBean.getContent().get(i).getChineseName();
                        }
                        driveId=getCarsBean.getContent().get(0).getID();
                        AlertDialog.Builder singleChoiceDialog =
                                new AlertDialog.Builder(getActivity());
                        singleChoiceDialog.setTitle("请选择司机");
                        // 第二个参数是默认选项，此处设置为0
                        singleChoiceDialog.setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        driveId=getCarsBean.getContent().get(which).getID();

                                        Log.d(TAG, "onClick: "+carId);

                                    }
                                });
                        singleChoiceDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        beginRoute();
                                    }
                                });
                        singleChoiceDialog.show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }
    private void beginRoute(){
        Log.d(TAG, "carId: "+carId+"   driveId: "+driveId+"    userId: "+WasteTreatmentApplication.instance.userId);
        HttpUtils.getInstance().geData().beginRoute(Integer.toString(carId),Integer.toString(driveId),WasteTreatmentApplication.instance.userId)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeginRouteBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BeginRouteBean beginRouteBean) {
                        Log.d(TAG, "beginRouteBean: "+beginRouteBean.toString());
                        if (beginRouteBean.getIsSuccess()){
                            Tips.show("已生成路线");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Throwable: "+e.toString());


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
