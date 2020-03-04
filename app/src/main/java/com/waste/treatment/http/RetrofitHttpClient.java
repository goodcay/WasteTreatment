package com.waste.treatment.http;


import com.waste.treatment.bean.DouBan;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.bean.newsBean;

import java.math.BigDecimal;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 请求数据接口
 * Created by Cay on 2017/2/23.
 */

public interface RetrofitHttpClient {





 /**
 * &#x4e0a;&#x4f20;&#x767b;&#x5f55;&#x7edf;&#x8ba1;
 *
 * @return &#x7b49;&#x7b49;
 */

 @POST("/toutiao/index")
        Observable<newsBean> getTouTiao(@Query("key") String key,@Query("type") String type);


 @GET("/WhhService.asmx/GetCars")
    Observable<GetCarsBean> getCars();



@POST("/WhhService.asmx/GetCars")
    Observable<Success> beginRoute(@Query("carId") String carId, @Query("driverId") String driverId, @Query("operatorId") String operatorId);


@GET("/WhhService.asmx/AddPos")
    Observable<Success> addPos(@Query("routeId") int routeId, @Query("x") String x, @Query("y") String y);
/*
    @POST("/YouShiServer/LoginCount")
    Observable<UpDdtaBackBean> loginCount();
    *//**
     * 上传收索统计
     *
     * @return 等等
     *//*
    @POST("/YouShiServer/SearchCount")
    Observable<UpDdtaBackBean> searchCount(@Query("name") String name, @Query("movie_id") String movie_id, @Query("img_url") String img_url);


    *//**
     * 获取首页数据
     * @return xx
     *//*
    @GET("/YouShiServer/FirstData")
    Observable<YouShiFirstDataBean> getYouShiFirstData();

    *//**
     * 获取电影详情
     * @param id s
     * @return s
     *//*
    @GET("/YouShiServer/MovieDetail")
    Observable<YouShiMovieDealisBean> getYouShiMovieDetail(@Query("id") String id);

    *//**
     * 优视 单个条件模糊查询
     * @param type 类型
     * @param value 类型值
     * @param position 起始位置
     * @param num  查询 数量
     * @return 返回类型
     *//*
    @GET("/YouShiServer/SingleVagueLookup")
    Observable<YouShiSingleLookupResultBean> singleLookupResult(@Query("type") String type, @Query("value") String value, @Query("position") String position, @Query("num") String num);

    *//**
     * 优视 单个条件准确查询
     * @param type 类型
     * @param position 起始位置
     * @param num  查询 数量
     * @return 返回类型
     *//*
    @GET("/YouShiServer/SingleLookupData")
    Observable<YouShiSingleLookupResultBean> oneLookupResult(@Query("type") String type, @Query("position") String position, @Query("num") String num);

    *//**
     * 优视 单个条件准确查询
     * @param type 类型
     * @return 返回类型
     *//*
    @GET("/YouShiServer/movieTopBarData")
    Observable<YouShiTopbarResultBean> getTopbar(@Query("type") String type);

    *//**
     * 优视 获取今天更新数据  只能回去7天的
     * @return 返回类型
     *//*
    @GET("/YouShiServer/ToadyUpdate")
    Observable<YouShiTodayBackResultBean> getToday();
    *//**
     * 优视 获取今天更新数据  只能回去7天的
     * @return 返回类型
     *  type = 0表示链接失效  1表示更新电视
     *
     *//*
    @GET("/YouShiServer/UpUpdateAndInvail")
    Observable<UpDdtaBackBean> updateAndInvail(@Query("type") String type, @Query("issue") String name);
    *//**
     * 版本更新检测
     * @return s
     *//*
    @GET("/YouShiServer/checkVersion")
    Observable<VersionUpdataBean> checkVersion();*/
}

