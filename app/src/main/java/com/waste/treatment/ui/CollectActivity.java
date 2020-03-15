package com.waste.treatment.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetTypesBean;
import com.waste.treatment.databinding.ActivityCollectBinding;
import com.waste.treatment.http.HttpUtils;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;
import com.wildma.pictureselector.PictureSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ActivityCollectBinding mBinding;
    private ProgressDialog waitingDialog;
    Map<Integer, Integer> map = new HashMap<>();
    List<String> aihao = new ArrayList<>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collect);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.collect_title));
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);

        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.imgBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(CollectActivity.this, PictureSelector.SELECT_REQUEST_CODE).selectPicture(false, 200, 200, 1, 1);


            }
        });
        mBinding.imgBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delImg(false, null);
            }
        });


        for (int i = 0; i < aihao.size(); i++) {
            CheckBox cb = new CheckBox(this);
            cb.setText(aihao.get(i));
            cb.setId(i);
            cb.setOnCheckedChangeListener(this);
            mBinding.llZhongnei.addView(cb);
        }


        mBinding.upDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (map.isEmpty()) {
                    Tips.show("请选择种类", Toast.LENGTH_SHORT);
                } else {*/
                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        Log.d(WasteTreatmentApplication.TAG, "选择了: " + entry.getValue());
                    }

                    showWaitingDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitingDialog.cancel();
                            showNormalDialog();
                        }
                    }, 3000);
               /* }*/


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                delImg(true, picturePath);
            }
        }
    }

    /**
     * 选择照片设置
     * @param bool  true 设置照片  false 删除照片
     * @param picturePath 图片地址， 当bool为false 时 填null
     */
    private void delImg(Boolean bool, String picturePath) {
        if (bool) {
            mBinding.imgBtnDel.setVisibility(View.VISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.INVISIBLE);
            Glide.with(this).load(picturePath).into(mBinding.imgPhoto);

        } else {
            mBinding.imgBtnDel.setVisibility(View.INVISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.VISIBLE);
            mBinding.imgPhoto.setImageBitmap(null);
        }

    }

    private void showWaitingDialog() {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        waitingDialog = new ProgressDialog(CollectActivity.this);
        //waitingDialog.setTitle("正在上传");
        waitingDialog.setMessage("正在上传中，请稍等...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CollectActivity.this);
        //  normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("上传成功，是否运输？");
        //normalDialog.setMessage("你要点击哪一个按钮呢?");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSingleChoiceDialog();
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showSingleChoiceDialog();
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    int yourChoice;

    private void showSingleChoiceDialog() {
        HttpUtils.getInstance().geData().getCars().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCarsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetCarsBean getCarsBean) {
                        Log.d(WasteTreatmentApplication.TAG, "getCarsBean: "+getCarsBean.getContent().get(0).getName());
                       final String[] items = new String[getCarsBean.getContent().size()];

                        for (int i=0 ;i<getCarsBean.getContent().size();i++){
                            items[i]=getCarsBean.getContent().get(i).getName();
                        }


                    //    final String[] items = {"川A·88888", "川A·66666", "川A·99999", "川A·12345"};

                        // String[] items = getCarsBean.getContent().toArray(new String[getCarsBean.getContent().size()]);
                        yourChoice = -1;
                        AlertDialog.Builder singleChoiceDialog =
                                new AlertDialog.Builder(CollectActivity.this);
                        singleChoiceDialog.setTitle("请选择运输车辆");
                        // 第二个参数是默认选项，此处设置为0
                        singleChoiceDialog.setSingleChoiceItems(items, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        yourChoice = which;
                                    }
                                });
                        singleChoiceDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (yourChoice != -1) {
                                            Toast.makeText(CollectActivity.this,
                                                    "你选择了" + items[yourChoice],
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        singleChoiceDialog.show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            map.put(buttonView.getId(), buttonView.getId());
            Tips.show("选择了：" + aihao.get(buttonView.getId()));
            //添加到爱好数组
            //   hobbies.add(buttonView.getText().toString().trim());
        } else {
            //从数组中移除
            map.remove(buttonView.getId());
            //  hobbies.remove(buttonView.getText().toString().trim());
            Tips.show("移除了：" + aihao.get(buttonView.getId()));

        }


    }

    private List<String> getTypes (){
        final List<String> typeList = new ArrayList<>();
        HttpUtils.getInstance().geData().getTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypesBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetTypesBean getTypesBean) {
                        if (getTypesBean.getIsSuccess()){
                            for (int i=0;i<getTypesBean.getContent().size();i++){
                                typeList.add(getTypesBean.getContent().get(i).getName());

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return typeList;
    }
}
