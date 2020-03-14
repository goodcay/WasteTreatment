package com.waste.treatment.ui;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetUsersBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.ActivityLoginBinding;
import com.waste.treatment.http.HttpUtils;
import com.waste.treatment.util.DialogUtil;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;

public class LoginActivity extends AppCompatActivity {
     private ActivityLoginBinding mBinding;
    private ProgressDialog waitingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.login_btn_text));
        waitingDialog = DialogUtil.showWaitingDialog(LoginActivity.this ,"正在登陆");
        mBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.loginNameEdt.getText().toString().trim().isEmpty()||mBinding.loginPwdEdt.getText().toString().trim().isEmpty()){

                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_pwd_null),Toast.LENGTH_LONG).show();

                }else {
                    Log.d(WasteTreatmentApplication.TAG, "onClick: "+Utils.lacksPermissions(LoginActivity.this,Utils.permissionsREAD));
                   /* startActivity(new Intent(LoginActivity.this, MainActivity.class));

                   waitingDialog.show();
*/
                   /* HttpUtils.getInstance().geData().loginIn(mBinding.loginNameEdt.getText().toString().trim(),mBinding.loginPwdEdt.getText().toString().trim())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Success>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Success success) {
                                    Log.d(WasteTreatmentApplication.TAG, "success: "+success.toString());
                                    if (success.getIsSuccess()){
                                        getUser(mBinding.loginNameEdt.getText().toString().trim());
                                    }else {
                                        mBinding.errorLl.setVisibility(View.VISIBLE);
                                       // waitingDialog.cancel();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    waitingDialog.cancel();
                                    Tips.show("登录异常");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });*/

                }
            }
        });
        mBinding.loginPwdEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.errorLl.setVisibility(View.INVISIBLE);


            }
        });
        mBinding.loginNameEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.errorLl.setVisibility(View.INVISIBLE);


            }
        });

        Utils.getPermission(LoginActivity.this);
        //getPermission();
       /* HiPermission.create(LoginActivity.this)
                .animStyle(R.style.PermissionAnimFade)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.d(WasteTreatmentApplication.TAG, "onClose: ");

                    }

                    @Override
                    public void onFinish() {
                        Log.d(WasteTreatmentApplication.TAG, "onFinish: ");

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.d(WasteTreatmentApplication.TAG, "onDeny: ");

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        Log.d(WasteTreatmentApplication.TAG, "onGuarantee: ");

                    }
                });*/

    }


    private void getUser(String operatorId){

        HttpUtils.getInstance().geData().getUser(operatorId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetUsersBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetUsersBean getUsersBean) {
                        if (getUsersBean.getIsSuccess()){
                            Log.d(WasteTreatmentApplication.TAG, "getUsersBean: "+getUsersBean.toString());
                            WasteTreatmentApplication.getInstance().setLoginMsg(getUsersBean.getContent().getChineseName(),Integer.toString(getUsersBean.getContent().getID()));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           waitingDialog.cancel();
                            finish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getPermission() {

        //添加这下面的一部分
        //动态申请权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(WasteTreatmentApplication.TAG, "onRequestPermissionsResult: "+"requestCode:"+requestCode+"   permissions:"+permissions.toString() +"  grantResults:"+grantResults.toString());
    }
}
