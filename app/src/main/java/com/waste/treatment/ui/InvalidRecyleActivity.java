package com.waste.treatment.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.Data2Bean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.ActivityInvalidRecyleBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.DialogUtil;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class InvalidRecyleActivity extends BaseActivity<ActivityInvalidRecyleBinding> {
    private String code;
    private boolean isImage = false;

    private ProgressDialog waitingDialog;
    private String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentBinding.ilTitle.tvTitle.setText("销毁");
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        code = bundle.getString("code");
        mBinding.imgBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(InvalidRecyleActivity.this, PictureSelector.SELECT_REQUEST_CODE).selectPicture(false, 200, 200, 1, 1);

            }
        });

        mBinding.upDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isImage) {
                    HttpClient.getInstance().geData().invalidRecyle(code, WasteTreatmentApplication.instance.getUserId(),filePath)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Success>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Success success) {
                                    if (success.getIsSuccess()) {
                                        Tips.show("销毁成功");
                                        InvalidRecyleActivity.this.finish();
                                    } else {
                                        Tips.show("销毁失败");
                                    }

                                    onRefresh();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Tips.show("销毁失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    Tips.show("请先上传照片");
                }
            }
        });
        mBinding.imgBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImg(false, null);

            }
        });

        getData();

    }

    private void getData() {

        HttpClient.getInstance().geData().getRecyleByCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data2Bean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Data2Bean data2Bean) {
                        if (data2Bean.getIsSuccess()) {
                            Log.d(WasteTreatmentApplication.TAG, "onNext: " + data2Bean.getContent().toString());
                            mBinding.tvGsmc.setText(data2Bean.getContent().getCompany().getName());
                            mBinding.tvFwbm.setText(data2Bean.getContent().getCode());
                            mBinding.tvFwzl.setText(data2Bean.getContent().getName());
                            mBinding.tvFwzhongliang.setText(data2Bean.getContent().getWeight().toString());
                            if (data2Bean.getContent().getStatus()!=2){
                                Tips.show("该废物已消费");
                                InvalidRecyleActivity.this.finish();
                            }
                           // mBinding.tvSjsj.setText(Utils.timeToTime1(data2Bean.getContent().getRecyleTime()));
                           // mBinding.tvYscp.setText(data2Bean.getContent().getRouteId().getCarId().getName());
                           // mBinding.tvYssj.setText(data2Bean.getContent().getRouteId().getDriver().getChineseName());
                           // mBinding.tvSjr.setText(data2Bean.getContent().getRouteId().getBeginOperator().getChineseName());
                            showContentView();

                        } else {
                            Log.d(WasteTreatmentApplication.TAG, "onNext:error ");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "onError: " + e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_invalid_recyle;
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

        HttpClient.getInstance().geData1().uploadImage(parts, "2") //代表上传到销毁文件夹
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Success>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        waitingDialog = DialogUtil.waitingDialog(InvalidRecyleActivity.this, "正在上传图片，请稍等···");
                        waitingDialog.show();

                    }

                    @Override
                    public void onNext(Success s) {
                        waitingDialog.cancel();

                        if (s.getIsSuccess()) {
                            isImage = true;
                            Tips.show("上传成功！");
                            filePath = s.getContent();
                        } else {
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

    private void startIntent(Class<?> cl,String titleName,String type,int state,String hint){
        //利用Intent实现跳转
        Intent intent = new Intent(this, cl);
//利用Bundle携带数据,类似于Map集合,携带数据有很多种这里主要介绍这种
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        bundle.putString("type", type);
        bundle.putInt("state",state);
        bundle.putString("hint",hint);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
