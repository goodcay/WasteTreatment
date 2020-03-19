package com.waste.treatment.ui.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityMapBinding;
import com.waste.treatment.databinding.ActivityMapTestBinding;

import java.util.ArrayList;
import java.util.List;

public class MapTestActivity extends AppCompatActivity {
    private LocationClient mLocationClient;
    private MapView mapView;
    private ActivityMapTestBinding bindingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_map_test);
        initBaiDuMap();
        //构建折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < 1000; i++) {
            double a = i / 350;
            LatLng p = new LatLng(39.97923 + a, 116.437428 - a);
            points.add(p);
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
        Overlay mPolyline = mapView.getMap().addOverlay(mOverlayOptions);



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

    private void initBaiDuMap() {
        BaiduMapOptions bOptions = new BaiduMapOptions();
        bOptions.zoomControlsEnabled(false); //放大缩小控件
        // bOptions.mapType(BaiduMap.M); //地图模式
        bOptions.scrollGesturesEnabled(false); //是都拖拽
        bOptions.compassEnabled(true);
        mapView = new MapView(this, bOptions);
        bindingView.mapLl.addView(mapView);

        mapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null, 0xAAFFFF88, 0xAA00FF00);
        mapView.getMap().setMyLocationConfiguration(myLocationConfiguration);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


    }
}
