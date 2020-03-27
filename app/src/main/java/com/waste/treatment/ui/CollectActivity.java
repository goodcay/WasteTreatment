package com.waste.treatment.ui;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.printer.sdk.PosFactory;
import android.printer.sdk.bean.BarCodeBean;
import android.printer.sdk.bean.TextData;
import android.printer.sdk.bean.enums.ALIGN_MODE;
import android.printer.sdk.constant.BarCode;
import android.printer.sdk.interfaces.IPosApi;
import android.printer.sdk.interfaces.OnPrintEventListener;
import android.printer.sdk.util.PowerUtils;
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
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.ActivityCollect1Binding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.DialogUtil;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pda.serialport.SerialDriver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CollectActivity extends BaseActivity<ActivityCollect1Binding> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Map<Integer, Integer> types = new HashMap<>();
    private List<String> typeList;
    private boolean getCompanyIsSucceed = false;
    private boolean getTypeSucceed = false;
    private boolean isImage = false;
    private List<String> companys;
    private ArrayAdapter<String> adapter;
    private String company;
    private IPosApi mPosApi;
    private String strTypes="";
    private ProgressDialog waitingDialog;
    private String filePath=null;


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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBinding.imgBtnPhoto.setOnClickListener(this);
        mBinding.imgBtnDel.setOnClickListener(this);
        mBinding.upDataBtn.setOnClickListener(this);
        initPos();
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
                uploadImg(picturePath);
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
                                CheckBox cb = new CheckBox(CollectActivity.this);
                                cb.setText(typeList.get(i));
                                cb.setId(i + 1);
                                cb.setOnCheckedChangeListener(CollectActivity.this);
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
                            adapter = new ArrayAdapter<String>(CollectActivity.this, android.R.layout.simple_spinner_item, companys);
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
                PictureSelector.create(CollectActivity.this, PictureSelector.SELECT_REQUEST_CODE).selectPicture(false, 200, 200, 1, 1);
                break;
            case R.id.img_btn_del:
                loadImg(false, null);
                break;
            case R.id.up_data_btn:
                if (isFillOut()) {
                    StringBuilder type = new StringBuilder();
                    for (Map.Entry<Integer, Integer> entry : types.entrySet()) {
                        Log.d(WasteTreatmentApplication.TAG, "strTypes: "+strTypes);
                        strTypes = strTypes+typeList.get(entry.getValue()-1)+"、";
                        type.append(Integer.toString(entry.getValue())).append(";");
                    }
                    if (WasteTreatmentApplication.instance.getRouteId() != null) {

                        genRecyle(type.substring(0, type.length() - 1), mBinding.etZhongliang.getText().toString().trim(), WasteTreatmentApplication.instance.getUserId(), WasteTreatmentApplication.instance.getRouteId(), company, filePath);
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
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        try {
            Date date = sdf.parse( " 2008-07-10 19:20:00 " );

        }catch (Exception e){

        }

        if (types.isEmpty()) {
            Toast.makeText(CollectActivity.this, "请选择种类", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (mBinding.etZhongliang.getText().toString().trim().isEmpty()) {
                Toast.makeText(CollectActivity.this, "请填写重量", Toast.LENGTH_SHORT).show();
                // Log.d(WasteTreatmentApplication.TAG, "请填写重量");
                return false;
            } else {
                if (isImage) {
                    return true;
                } else {
                    Toast.makeText(CollectActivity.this, "请拍照", Toast.LENGTH_SHORT).show();
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
    private void genRecyle(String types, final String weight, String operatorId, String routeId, final String companyId, String filePath) {
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
                        Log.d(WasteTreatmentApplication.TAG, "content: " + success.getContent());
                        if (success.getIsSuccess()) {
                            //print(companys.get(Integer.parseInt(companyId)-1),strTypes.substring(0, strTypes.length() - 1),WasteTreatmentApplication.instance.getChepai(),weight,WasteTreatmentApplication.instance.getSiji(),WasteTreatmentApplication.instance.getUserName(), Utils.timeToTime(success.getContent().getRecyleTime()),success.getContent().getCode());
                            print(success.getContent().getCompany().getName(),success.getContent().getName(),success.getContent().getRouteId().getCarId().getName(),success.getContent().getWeight(),success.getContent().getRouteId().getDriver().getChineseName(),success.getContent().getRouteId().getBeginOperator().getChineseName(),success.getContent().getRecyleTime(),success.getContent().getCode());
                            cleanMsg();
                            Tips.show("上传成功");
                        } else {
                            Tips.show(success.getErrorMsg());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "onError: "+e.toString());
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

        HttpClient.getInstance().geData1().uploadImage(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Success>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        waitingDialog = DialogUtil.waitingDialog(CollectActivity.this, "正在上传图片，请稍等···");
                        waitingDialog.show();

                    }

                    @Override
                    public void onNext(Success s) {
                        waitingDialog.cancel();

                        if (s.getIsSuccess()){
                            isImage = true;
                            Tips.show("上传成功！");
                        filePath= s.getContent();
                        }else {
                            Tips.show("失败！");

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        waitingDialog.cancel();
                        Tips.show("上传失败！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


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
        strTypes="";
        filePath=null;
        loadImg(false,null);
    }

    public void initPos() {
        PowerUtils.powerOnOrOff(1, "1");
        PosFactory.registerCommunicateDriver(this, new SerialDriver()); // 注册串口类 Register serial driver
        mPosApi = PosFactory.getPosDevice(); // 获取打印机实例 get printer driver
        mPosApi.setPrintEventListener(onPrintEventListener);
        mPosApi.openDev("/dev/ttyS2", 115200, 0);
        mPosApi.setPos()  //设置打印机
                .setAutoEnableMark(true)//开启黑标
                .setEncode(-1)  //设置编码  1 为UNICODE编码  2为UFT-8编码 3 为 CODEPAGE 编码 默认-1
                .setLanguage(15) // 0 为英语 15简体中文 39 阿拉伯语 21 俄语  默认-1
                .setPrintSpeed(-1) //  设置打印速度
                .setMarkDistance(-1) //检测到黑标后走的距离
                .init();// 初始化打印机 init printer

        //mPosApi.addFeedPaper(true,60); //设置走空纸   最大956mm
    }

    private void print(String danwei,String types,String chepai ,String zhongliang ,String siji, String shoujiren ,String time ,String code){
        TextData textData1 = new TextData();
        textData1.addConcentration(25);
        textData1.addFont(BarCode.FONT_ASCII_12x24);
        textData1.addTextAlign(BarCode.ALIGN_LEFT);
        textData1.addFontSize(BarCode.NORMAL);
        textData1.addText("单位："+danwei);
        textData1.addText("\n");
               /* textData1.addText("标号：2020031900000039");
                textData1.addText("\n");*/
        textData1.addText("名称："+types);
        textData1.addText("\n");
        textData1.addText("车牌："+chepai+"  重量："+zhongliang+"Kg");
        textData1.addText("\n");
        textData1.addText("司机："+siji+"  收集人："+shoujiren);
        textData1.addText("\n");
        textData1.addText("时间："+time);
        mPosApi.addText(textData1);
        BarCodeBean barCodeBean = new BarCodeBean();
        barCodeBean.setConcentration(25);
        barCodeBean.setHeight(60);
        barCodeBean.setWidth(2);// 条码宽度1-4; Width value 1 2 3 4
        barCodeBean.setText(code);
        barCodeBean.setBarType (BarCode.CODE128);
        mPosApi.addBarCode(barCodeBean, ALIGN_MODE.ALIGN_CENTER);
        mPosApi.addMark();
        //  mPosApi.addFeedPaper(true, 2);
        mPosApi.printStart();


    }

    public OnPrintEventListener onPrintEventListener=new OnPrintEventListener () {
        @Override
        public void onPrintState(int state) {
            switch (state) {
                case BarCode.ERR_POS_PRINT_SUCC:
                    Tips.show("打印成功");
                    //showToastShort (getString (R.string.toast_print_success));
                    break;
                case BarCode.ERR_POS_PRINT_FAILED:
                    Tips.show("打印错误");
                    //  showToastShort (getString (R.string.toast_print_error));
                    break;
                case BarCode.ERR_POS_PRINT_HIGH_TEMPERATURE:
                    Tips.show("温度过高");
                    // showToastShort (getString (R.string.toast_high_temperature));
                    break;
                case BarCode.ERR_POS_PRINT_NO_PAPER:
                    Tips.show("没有纸张");
                    //showToastShort (getString (R.string.toast_no_paper));
                    break;
                case 4:
                    break;
            }
        }
    };
}
