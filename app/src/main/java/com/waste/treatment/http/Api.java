package com.waste.treatment.http;


import com.waste.treatment.bean.BeginRouteBean;
import com.waste.treatment.bean.Data2Bean;
import com.waste.treatment.bean.DataBean;
import com.waste.treatment.bean.GenRecyleBean;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetDriverBean;
import com.waste.treatment.bean.GetPosBean;
import com.waste.treatment.bean.GetRouteBean;
import com.waste.treatment.bean.GetTypesBean;
import com.waste.treatment.bean.GetUsersBean;
import com.waste.treatment.bean.Success;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Multipart;


/**
 * 请求数据接口
 * Created by Cay on 2017/2/23.
 */

public interface Api {


    /**#########################  API 接口 #################**/

    /**
     * 获取车辆信息
     *
     * @return 车辆信息
     */
    @GET("/WhhService.asmx/GetCars")
    Observable<GetCarsBean> getCars();

    /**
     * 登录接口
     *
     * @param operatorId 账号
     * @param password   密码
     * @return
     */

    @GET("/WhhService.asmx/LoginIn")
    Observable<Success> loginIn(@Query("operatorId") String operatorId, @Query("password") String password);


    /**
     * 生成路线
     *
     * @param carId      车编号
     * @param driverId   司机编号
     * @param operatorId 操作人员编号
     * @return
     */
    @GET("/WhhService.asmx/BeginRoute")
    Observable<BeginRouteBean> beginRoute(@Query("carId") String carId, @Query("driverId") String driverId, @Query("operatorId") String operatorId);

    /**
     * 上传位置信息
     *
     * @param routeId 路线ID
     * @param x       经度
     * @param y       纬度
     * @return
     */
    @GET("/WhhService.asmx/AddPos")
    Observable<Success> addPos(@Query("routeId") String routeId, @Query("x") String x, @Query("y") String y);


    /**
     * 获取用户信息
     *
     * @param operatorId 用户ID
     * @return
     */

    @GET("/WhhService.asmx/GetUser")
    Observable<GetUsersBean> getUser(@Query("operatorId") String operatorId);

    /**
     * 获取司机
     *
     * @return
     */

    @GET("/WhhService.asmx/GetDrivers")
    Observable<GetDriverBean> getDriver();

    /**
     * 获取废物类型
     *
     * @return
     */

    @GET("/WhhService.asmx/GetTypes")
    Observable<GetTypesBean> getTypes();

    /**
     * 获取公司
     *
     * @return
     */

    @GET("/WhhService.asmx/GetCompanys")
    Observable<GetCarsBean> getCompanys();

    /**
     * 生成废物
     *
     * @param types
     * @param weight
     * @param operatorId
     * @param routeId
     * @param companyId
     * @return
     */
    @GET("/WhhService.asmx/GenRecyle")
    Observable<GenRecyleBean> genRecyle(@Query("imei") String imei,@Query("types") String types, @Query("weight") String weight, @Query("operatorId") String operatorId, @Query("routeId") String routeId, @Query("companyId") String companyId, @Query("filePath") String filePath);

    /**
     * 退出登录
     *
     * @param operatorId 用户ID
     * @return
     */
    @GET("/WhhService.asmx/LogOut")
    Observable<Success> logout(@Query("operatorId") String operatorId);

    /**
     * 结束路线
     *
     * @param routeId 路线ID
     * @return
     */
    @GET("/WhhService.asmx/EndRoute")
    Observable<BeginRouteBean> endRoute(@Query("routeId") String routeId);


    /**
     * 上传文件
     * @param part
     * @return
     */


    @Multipart
    @POST("/servlet/UploadFile")
    Observable<Success> uploadImage(@Part MultipartBody.Part part, @Query("type") String type);

    /**
     * 入库
     * @param recyleCode
     * @param amount
     * @param operatorId
     * @return
     */
    @GET("/WhhService.asmx/InStock")
    Observable<Success> isStock(@Query("recyleCode") String recyleCode,@Query("amount") String amount,@Query("operatorId") String operatorId);

    /**
     * 出库
     * @param recyleCode
     * @param amount
     * @param operatorId
     * @return
     */
    @GET("/WhhService.asmx/OutStock")
    Observable<Success> outStock(@Query("recyleCode") String recyleCode,@Query("amount") String amount,@Query("operatorId") String operatorId);

    /**
     * 销毁
     * @param recyleCode
     * @param operatorId
     * @return
     */
    @GET("/WhhService.asmx/InvalidRecyle")
    Observable<Success> invalidRecyle(@Query("recyleCode") String recyleCode,@Query("operatorId") String operatorId,@Query("filePath") String filePath);


    /**
     * 获取废物信息
     * @param type
     * @return
     */

    @GET("/WhhService.asmx/GetRecyles")
    Observable<DataBean> getRecyles(@Query("type") String type);


    @GET("/WhhService.asmx/GetRoute")
    Observable<GetRouteBean> getRoute(@Query("operatorId") String operatorId);


    @GET("/WhhService.asmx/GetRecyleByCode")
    Observable<Data2Bean> getRecyleByCode(@Query("recyleCode") String recyleCode);

    @GET("/WhhService.asmx/GetPos")
    Observable<GetPosBean> getPos(@Query("recyleCode") String recyleCode);
}

