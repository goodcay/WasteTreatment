package com.waste.treatment.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.GenRecyleBean;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetTypesBean;
import com.waste.treatment.databinding.ActivityCollect1Binding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.Tips;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CollectActivity1 extends BaseActivity<ActivityCollect1Binding> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Map<Integer, Integer> types = new HashMap<>();
    private List<String> typeList;
    private boolean getCompanyIsSucceed = false;
    private boolean getTypeSucceed = false;
    private boolean isImage = false;
    private List<String> companys;
    private ArrayAdapter<String> adapter;
    private String company;


    @Override
    protected int setLayout() {
        return R.layout.activity_collect1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentBinding.ilTitle.tvTitle.setText("医废收集");
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company = Integer.toString(position + 1);
                Log.d(WasteTreatmentApplication.TAG, "onItemSelected: " + position + "   ID:" + id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBinding.imgBtnPhoto.setOnClickListener(this);
        mBinding.imgBtnDel.setOnClickListener(this);
        mBinding.upDataBtn.setOnClickListener(this);

        getData();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            types.put(buttonView.getId(), buttonView.getId());
        } else {
            //从数组中移除
            types.remove(buttonView.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                loadImg(true, picturePath);
            }
        }
    }

    /**
     * 选择照片设置
     *
     * @param bool        true 设置照片  false 删除照片
     * @param picturePath 图片地址， 当bool为false 时 填null
     */
    private void loadImg(Boolean bool, String picturePath) {
        if (bool) {
            mBinding.imgBtnDel.setVisibility(View.VISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.INVISIBLE);
            Glide.with(this).load(picturePath).into(mBinding.imgPhoto);
            isImage = true;
        } else {
            mBinding.imgBtnDel.setVisibility(View.INVISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.VISIBLE);
            mBinding.imgPhoto.setImageBitmap(null);
            isImage = false;
        }

    }

    /**
     * 获取数据
     */
    private void getData() {

        HttpClient.getInstance().geData().getTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypesBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetTypesBean getTypesBean) {
                        Log.d(WasteTreatmentApplication.TAG, "getTypesBean: " + getTypesBean.toString());

                        typeList = new ArrayList<>();
                        if (getTypesBean.getIsSuccess()) {
                            for (int i = 0; i < getTypesBean.getContent().size(); i++) {
                                typeList.add(getTypesBean.getContent().get(i).getName());
                            }
                            for (int i = 0; i < typeList.size(); i++) {
                                CheckBox cb = new CheckBox(CollectActivity1.this);
                                cb.setText(typeList.get(i));
                                cb.setId(i + 1);
                                cb.setOnCheckedChangeListener(CollectActivity1.this);
                                mBinding.llZhongnei.addView(cb);
                            }
                            getTypeSucceed = true;
                            Log.d(WasteTreatmentApplication.TAG, "getTypeSucceed: " + getTypeSucceed);
                            isShowContentView();
                        } else {
                            showError();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        HttpClient.getInstance().geData().getCompanys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCarsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetCarsBean getCarsBean) {
                        Log.d(WasteTreatmentApplication.TAG, "getCarsBean: " + getCarsBean.toString());

                        if (getCarsBean.getIsSuccess()) {
                            companys = new ArrayList<>();
                            for (int i = 0; i < getCarsBean.getContent().size(); i++) {
                                companys.add(getCarsBean.getContent().get(i).getName());
                            }
                            adapter = new ArrayAdapter<String>(CollectActivity1.this, android.R.layout.simple_spinner_item, companys);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mBinding.spCompany.setAdapter(adapter);
                            getCompanyIsSucceed = true;
                            Log.d(WasteTreatmentApplication.TAG, "getCompanyIsSucceed: " + getCompanyIsSucceed);
                            isShowContentView();

                        } else {

                            showError();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "eComm: " + e.toString());

                        showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 数据加载完毕后 显示
     */
    private void isShowContentView() {
        if (getCompanyIsSucceed && getTypeSucceed) {
            Log.d(WasteTreatmentApplication.TAG, "getCompanyIsSucceed: " + getCompanyIsSucceed + "  getTypeSucceed: " + getTypeSucceed);
            showContentView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_btn_photo:
                PictureSelector.create(CollectActivity1.this, PictureSelector.SELECT_REQUEST_CODE).selectPicture(false, 200, 200, 1, 1);
                break;
            case R.id.img_btn_del:
                loadImg(false, null);
                break;
            case R.id.up_data_btn:
                if (isFillOut()) {
                    StringBuilder type = new StringBuilder();
                    for (Map.Entry<Integer, Integer> entry : types.entrySet()) {
                        type.append(Integer.toString(entry.getValue())).append(";");
                    }
                    if (WasteTreatmentApplication.instance.getRouteId() != null) {
                        Log.d(WasteTreatmentApplication.TAG, "getRoute>类型： " + type.substring(0, type.length() - 1) + "  重量：" + mBinding.etZhongliang.getText().toString().trim() + " 用户号：" + WasteTreatmentApplication.instance.getUserId() + " 公司id:" + company + "  文件地址：" + "c:file/image/001.jpg");
                        genRecyle(type.substring(0, type.length() - 1), mBinding.etZhongliang.getText().toString().trim(), WasteTreatmentApplication.instance.getUserId(), WasteTreatmentApplication.instance.getRouteId(), company, "c:file/image/001.jpg");
                    } else {
                        Tips.show("路线没生成，请生成路线后再上传！");
                    }
                }
                break;
            default:
                break;
        }

    }


    /**
     * 判断是否信息填全
     *
     * @return true 为填写完全
     */
    private boolean isFillOut() {

        if (types.isEmpty()) {
            Toast.makeText(CollectActivity1.this, "请选择种类", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (mBinding.etZhongliang.getText().toString().trim().isEmpty()) {
                Toast.makeText(CollectActivity1.this, "请填写重量", Toast.LENGTH_SHORT).show();
                // Log.d(WasteTreatmentApplication.TAG, "请填写重量");
                return false;
            } else {
                if (isImage) {
                    return true;
                } else {
                    Toast.makeText(CollectActivity1.this, "请拍照", Toast.LENGTH_SHORT).show();
                    return false;

                }
            }

        }

    }

    /**
     * 上传废物
     *
     * @param types      种类
     * @param weight     重量
     * @param operatorId 操作员
     * @param routeId    路线
     * @param companyId  公司
     */
    private void genRecyle(String types, String weight, String operatorId, String routeId, String companyId, String filePath) {
        HttpClient.getInstance().geData().genRecyle(types, weight, operatorId, routeId, companyId, filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GenRecyleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GenRecyleBean success) {
                        Log.d(WasteTreatmentApplication.TAG, "success: " + success.getIsSuccess());
                        if (success.getIsSuccess()) {
                            cleanMsg();
                            //TODO 打印条码
                            Tips.show("上传成功");
                        } else {
                            Tips.show(success.getErrorMsg());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Tips.show("上传出现异常");

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void uploadImg(String file1Location) {
        //创建文件(你需要上传到服务器的文件)
        //file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
        File file = new File(file1Location);
        //设置文件的格式;两个文件上传在这里添加
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        //添加文件(uploadfile就是你服务器中需要的文件参数)
        // builder.addFormDataPart("uploadfile", file.getName(), imageBody);
        //builder.addFormDataPart("uploadfile1", file1.getName(), imageBody1);
        //生成接口需要的list
        MultipartBody.Part parts = MultipartBody.Part.createFormData("uploadfile", file.getName(), imageBody);

        HttpClient.getInstance().geData().uploadImage(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(WasteTreatmentApplication.TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
        private void upLoadingMethod( String file1Location) {

            //创建文件(你需要上传到服务器的文件)
            //file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
            File file = new File(file1Location);

            //创建表单map,里面存储服务器本接口所需要的数据;
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //在这里添加服务器除了文件之外的其他参数
                    .addFormDataPart("参数1", "值1")
                    .addFormDataPart("参数2", "值2");


            //设置文件的格式;两个文件上传在这里添加
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            //添加文件(uploadfile就是你服务器中需要的文件参数)
            builder.addFormDataPart("uploadfile", file.getName(), imageBody);
            //builder.addFormDataPart("uploadfile1", file1.getName(), imageBody1);
            //生成接口需要的list
            List<MultipartBody.Part> parts = builder.build().parts();
            //创建设置OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    //允许失败重试
                    .retryOnConnectionFailure(true)
                    .build();
            //创建retrofit实例对象
            Retrofit retrofit = new Retrofit.Builder()
                    //设置基站地址(基站地址+描述网络请求的接口上面注释的Post地址,就是要上传文件到服务器的地址,
                    // 这只是一种设置地址的方法,还有其他方式,不在赘述)
                    .baseUrl("你的基站地址")
                    //设置委托,使用OKHttp联网,也可以设置其他的;
                    .client(okHttpClient)
                    //设置数据解析器,如果没有这个类需要添加依赖:
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置支持rxJava
                    // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            //实例化请求接口,把表单传递过去;
            Call<BaseBean> call = retrofit.create(RetrofitHttpClient.class).upLoading(parts);
            //开始请求
            call.enqueue(new Callback<BaseBean>() {
                @Override
                public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                    //联网有响应或有返回数据
                    System.out.println(response.body().toString());
                }

                @Override
                public void onFailure(Call<BaseBean> call, Throwable t) {
                    //连接失败,多数是网络不可用导致的
                    System.out.println("网络不可用");
                }
            });

        }
    */

    /**
     * 上传成功 清楚信息
     */
    @SuppressLint("ResourceType")
    public void cleanMsg() {
        for (int i = 0; i < typeList.size(); i++) {
            CheckBox cb = mBinding.llZhongnei.findViewById(i + 1);
            cb.setChecked(false);
        }
        mBinding.etZhongliang.setText("");
        loadImg(false,null);

        

    }
}
