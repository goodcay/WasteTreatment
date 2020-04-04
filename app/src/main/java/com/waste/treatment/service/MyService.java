package com.waste.treatment.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.util.Utils;


public class MyService extends Service {
    public static final String CHANNEL_ID_STRING = "service_01";
    private LBSTraceClient mTraceClient;
    private OnTraceListener mTraceListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //适配8.0service
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID_STRING).build();
            startForeground(1, notification);
        }

        baiduInit();

     /*   final Handler mHandler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(r,100);*/
    }
    public void baiduInit(){
        // 轨迹服务ID
        long serviceId = 220106;
        // 设备标识
        String entityName = Utils.getIMEI(getApplication());
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
        mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {
                Log.d(WasteTreatmentApplication.TAG, "onBindServiceCallback: " + s);
                Log.d(WasteTreatmentApplication.TAG, "绑定服务: " + s);

            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                if (status==0){
                    mTraceClient.startGather(mTraceListener);
                    Log.d(WasteTreatmentApplication.TAG, "服务开启: " + message);

                }
                Log.d(WasteTreatmentApplication.TAG, "onStartTraceCallback: " + message);


            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "停止服务: " + message);

                Log.d(WasteTreatmentApplication.TAG, "onStopTraceCallback: " + message);
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStartGatherCallback: " + message);
                Log.d(WasteTreatmentApplication.TAG, "开启采集: " + message);

            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                Log.d(WasteTreatmentApplication.TAG, "onStopGatherCallback: " + message);
                Log.d(WasteTreatmentApplication.TAG, "停止采集: " + message);

            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                Log.d(WasteTreatmentApplication.TAG, "onPushCallback: " + message);
                Log.d(WasteTreatmentApplication.TAG, "推送回调: " + message);

            }

            @Override
            public void onInitBOSCallback(int i, String s) {
                Log.d(WasteTreatmentApplication.TAG, "onInitBOSCallback: " + s);
                Log.d(WasteTreatmentApplication.TAG, "初始化BOS回到: " + s);


            }
        };

        // 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);

    }
}
