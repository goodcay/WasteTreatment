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
import com.waste.treatment.bean.GetRouteBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.FragmentHomeBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.ui.CollectActivity;
import com.waste.treatment.ui.QueryActivity;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.ui.test.TraceActivity;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.math.BigDecimal;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.waste.treatment.WasteTreatmentApplication.TAG;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener{
    private LocationClient mLocationClient;
    private MapView mapView;
    private int carId = 0;
    private int driveId = 0;
  //  private String driveName = null;
   // private String carName = null;
    private boolean firstLocation = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int setContent() {
        return R.layout.fragment_home;
    }

    @Override
    protected void loadData() {
        super.loadData();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindingView.ilTitle.tvTitle.setText(getResources().getString(R.string.home));
        bindingView.ilTitle.tvRightText.setText(Utils.getDate(Utils.DATE_YMD));

        bindingView.ilTitle.tvLeftText.setText(WasteTreatmentApplication.instance.getUserName());


        bindingView.llYfsj.setOnClickListener(this);
        bindingView.llYfrk.setOnClickListener(this);
        bindingView.llYfcx.setOnClickListener(this);
        bindingView.llYfys.setOnClickListener(this);
        bindingView.llYfxf.setOnClickListener(this);
        bindingView.llYfck.setOnClickListener(this);
        initLocation();
        initBaiDuMap();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_yfsj:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.ll_yfrk:
                startIntent(RuiKuActivity.class, Objects.requireNonNull(getActivity()).getResources().getString(R.string.yfrk),"InStock",1,getActivity().getResources().getString(R.string.kc_hint));
                break;
            case R.id.ll_yfck:
                startIntent(RuiKuActivity.class,Objects.requireNonNull(getActivity()).getResources().getString(R.string.yfck),"OutStock",2,getActivity().getResources().getString(R.string.dxh_hint));
                break;
            case R.id.ll_yfxf:
                startIntent(RuiKuActivity.class,Objects.requireNonNull(getActivity()).getResources().getString(R.string.yfxf),"Invalid",3,null);
                break;
            case R.id.ll_yfys:
                startIntent(RuiKuActivity.class,Objects.requireNonNull(getActivity()).getResources().getString(R.string.yfys),"new",0,getActivity().getResources().getString(R.string.ys_hint));
                break;
            case R.id.ll_yfcx:
                Intent intent = new Intent(getActivity(), QueryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("code", "");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }

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
            if (!firstLocation) {
                firstLocation = true;
                showContentView();
                getRoute();
            }


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


            if (WasteTreatmentApplication.instance.getRouteId() != null) {
               // Log.d(WasteTreatmentApplication.TAG, "latitude: " + latitude + "    longitude:" + longitude + "    radius:" + radius + "     coorType:" + coorType + "     errorCode:" + errorCode);
                HttpClient.getInstance().geData().addPos(WasteTreatmentApplication.instance.getRouteId(), Double.toString(latitude), Double.toString(longitude)).subscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Success>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Success success) {
                              //  Log.d(WasteTreatmentApplication.TAG, "经纬度上传：" + success.getIsSuccess());

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "pos:" + e.getMessage() + new BigDecimal(3.5));

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
        Log.d(TAG, "HomeFragment:onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mapView.onDestroy();
        Log.d(TAG, "HomeFragment:onDestroy: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        //  mapView.onResume();
        Log.d(TAG, "HomeFragment:onResume: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "HomeFragment:onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "HomeFragment:onDetach: ");
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
        HttpClient.getInstance().geData().getCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCarsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final GetCarsBean getCarsBean) {
                        final String[] items = new String[getCarsBean.getContent().size()];
                        carId = getCarsBean.getContent().get(0).getOid();
                      //  carName = getCarsBean.getContent().get(0).getName();
                        for (int i = 0; i < getCarsBean.getContent().size(); i++) {
                            items[i] = getCarsBean.getContent().get(i).getName();
                        }
                        AlertDialog.Builder singleChoiceDialog =
                                new AlertDialog.Builder(getActivity());
                        singleChoiceDialog.setTitle("请选择运输车辆");
                        // 第二个参数是默认选项，此处设置为0
                        singleChoiceDialog.setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        carId = getCarsBean.getContent().get(which).getOid();
                                      //  carName = getCarsBean.getContent().get(which).getName();
                                        Log.d(TAG, "onClick: " + carId);

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
        HttpClient.getInstance().geData().getDriver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetDriverBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final GetDriverBean getCarsBean) {
                        final String[] items = new String[getCarsBean.getContent().size()];

                        for (int i = 0; i < getCarsBean.getContent().size(); i++) {
                            items[i] = getCarsBean.getContent().get(i).getChineseName();
                        }
                        driveId = getCarsBean.getContent().get(0).getID();
                       // driveName = getCarsBean.getContent().get(0).getChineseName();
                        AlertDialog.Builder singleChoiceDialog =
                                new AlertDialog.Builder(getActivity());
                        singleChoiceDialog.setTitle("请选择司机");
                        // 第二个参数是默认选项，此处设置为0
                        singleChoiceDialog.setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        driveId = getCarsBean.getContent().get(which).getID();
                                        //driveName = getCarsBean.getContent().get(which).getChineseName();

                                        Log.d(TAG, "onClick: " + carId);

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

    private void beginRoute() {
        HttpClient.getInstance().geData().beginRoute(Integer.toString(carId), Integer.toString(driveId), WasteTreatmentApplication.instance.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeginRouteBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BeginRouteBean beginRouteBean) {
                        if (beginRouteBean.getIsSuccess()) {
                            WasteTreatmentApplication.getInstance().setRouteId(Integer.toString(beginRouteBean.getContent().getOid())/*, driveName, carName*/);
                            Tips.show("已生成路线");
                        } else {
                            Tips.show(beginRouteBean.getErrorMsg());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Throwable: " + e.toString());


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startIntent(Class<?> cl,String titleName,String type,int state,String hint){
        //利用Intent实现跳转
        Intent intent = new Intent(getActivity(), cl);
//利用Bundle携带数据,类似于Map集合,携带数据有很多种这里主要介绍这种
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        bundle.putString("type", type);
        bundle.putInt("state",state);
        bundle.putString("hint",hint);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getRoute(){
        HttpClient.getInstance().geData().getRoute(WasteTreatmentApplication.instance.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetRouteBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetRouteBean getRouteBean) {
                        if (getRouteBean.getIsSuccess()){
                            WasteTreatmentApplication.instance.setRouteId(Integer.toString(getRouteBean.getContent().getOid())/*,getRouteBean.getContent().getDriver().getChineseName(),getRouteBean.getContent().getCarId().getName()*/);
                        }else {
                            showNormalDialog();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showNormalDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
