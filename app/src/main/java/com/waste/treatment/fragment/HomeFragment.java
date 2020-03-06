package com.waste.treatment.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import com.waste.treatment.databinding.FragmentHomeBinding;
import com.waste.treatment.ui.CollectActivity;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.ui.ScanActivity;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;
    private  LocationClient mLocationClient;
   private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        return mBinding.getRoot();


    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.home));
        mBinding.ilTitle.tvRightText.setText(Utils.getDate(Utils.DATE_YMD));
        mBinding.ilTitle.tvLeftText.setText("Admin");
        getPermission();
        initLocation();
        initBaiDuMap();




      //  mBinding.tvName.setText(Utils.getShangOrXia()+"好，Admin");
      //  mBinding.tvTime.setText(Utils.getMonthDay());
        mBinding.llYfsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });
       /* mBinding.llYfck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListViewActivity.class));
            }
        });*/
        mBinding.llYfrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RuiKuActivity.class));
            }
        });
        mBinding.llYfsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanActivity.class));
            }
        });
       /* mBinding.enteringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestPrintActivity.class));

            }
        });
        mBinding.imgGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapActivity.class));

            }
        });*/

    }

    private void  initLocation(){
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

    private void initBaiDuMap(){
        BaiduMapOptions bOptions = new BaiduMapOptions();
         bOptions.zoomControlsEnabled(false); //放大缩小控件
        // bOptions.mapType(BaiduMap.M); //地图模式
         bOptions.scrollGesturesEnabled(false); //是都拖拽
        bOptions.compassEnabled(true);
       mapView = new MapView(Objects.requireNonNull(getActivity()),bOptions);

        mapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,false, null,R.color.color_white,R.color.color_white);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().setMyLocationConfiguration(myLocationConfiguration);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBinding.llBaiduMap.addView(mapView);

      /*  mBinding.baiduMap.getMap().setMyLocationEnabled(true);
       MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,false, null,R.color.color_white,R.color.color_white);
        mBinding.baiduMap.getMap().setMyLocationEnabled(true);
        mBinding.baiduMap.getMap().setMyLocationConfiguration(myLocationConfiguration);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mBinding.baiduMap.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/


    }

    private void getPermission(){

        //添加这下面的一部分
        //动态申请权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }

    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView.getMap()== null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mapView.getMap().setMyLocationData(locData);

        }
    }
}
