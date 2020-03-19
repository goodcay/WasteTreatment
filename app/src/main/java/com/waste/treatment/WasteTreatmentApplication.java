package com.waste.treatment;

import android.app.Application;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.Utils;

public class WasteTreatmentApplication extends Application {
    private   String userName="Admin";
    private   String userId="0";
    private   String routeId =null;

    public void setLoginMsg(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }
    public void setRouteId(String routeId){
        this.routeId = routeId;
    }
    public String getRouteId (){
        return routeId;
    }
    public String getUserName (){
        return userName;
    }
    public String getUserId(){
        return userId;
    }
    public final static String TAG ="CAY";
    public static WasteTreatmentApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        HttpClient.getInstance().setContext(getApplicationContext());
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //SharedPreferencesUtil.getInstance(this).putSP("grayLevel",6);
    }
    public static WasteTreatmentApplication getInstance(){
        if(instance == null){
            instance = new WasteTreatmentApplication();
        }
        return instance;
    }
}
