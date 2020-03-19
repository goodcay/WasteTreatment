package com.waste.treatment.http;

import android.content.Context;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/** 请求数据工具
 * Created by Cay on 2017/1/20.
 */

public class HttpClient {
    private Context context;
    private static HttpClient sHttpUtils;
    private Api mWTClient;
    private Api mMyObservableClient;
    private OkHttpClient okHttpClient;
    private Api mJuHelient;


    private static final String baseUrl="http://192.168.121.59/";
    private static final String juheUrl = "http://v.juhe.cn/";
    public static HttpClient getInstance() {
        if (sHttpUtils == null) {
            sHttpUtils = new HttpClient();
        }
        return sHttpUtils;
    }


    public void setContext(Context context) {
        this.context = context;
    }
    /**
     * 创建Okhttp客服端
     */
    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            File cacheFile = new File(context.getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
            int cacheSize = 10 * 1024 * 1024;
            Cache cache = new Cache(cacheFile, cacheSize);
            okHttpClient = new OkHttpClient.Builder()
                    .cache(cache).readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20,TimeUnit.SECONDS).writeTimeout(20,TimeUnit.SECONDS).build();
        }
        return okHttpClient;
    }

    public Api geData() {

            mWTClient = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient()).build().create(Api.class);


        return mWTClient;
    }
    public Api geData1() {

        mWTClient = new Retrofit.Builder().baseUrl("http://192.168.1.59:8000/")
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient()).build().create(Api.class);
        return mWTClient;
    }

    public Api getDouBan() {
        if (mMyObservableClient == null) {
            mMyObservableClient = new Retrofit.Builder().baseUrl("https://api.douban.com/")
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient()).build().create(Api.class);
        }
        return mMyObservableClient;
    }

    public Api getmJuHeClient(){
        mJuHelient = new Retrofit.Builder().baseUrl(juheUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build()
                .create(Api.class);
        return  mJuHelient;

    }

}
