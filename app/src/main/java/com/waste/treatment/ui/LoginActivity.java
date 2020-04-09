package com.waste.treatment.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.GetUsersBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.ActivityLoginBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.service.MyService;
import com.waste.treatment.util.DialogUtil;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding mBinding;
    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        Intent mBootIntent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(mBootIntent);
        } else {
            this.startService(mBootIntent);
        }
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.login_btn_text));
        waitingDialog = DialogUtil.waitingDialog(LoginActivity.this, "正在登陆···");
        mBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.loginNameEdt.getText().toString().trim().isEmpty() || mBinding.loginPwdEdt.getText().toString().trim().isEmpty()) {

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_pwd_null), Toast.LENGTH_LONG).show();

                } else {
                    if (Utils.lacksPermissions(LoginActivity.this, Utils.permissionsREAD)) {
                        ActivityCompat.requestPermissions(LoginActivity.this, Utils.permissionsREAD, 0);
                    } else {
                        waitingDialog.show();
                        HttpClient.getInstance().geData().loginIn(mBinding.loginNameEdt.getText().toString().trim(), mBinding.loginPwdEdt.getText().toString().trim(),Utils.getIMEI(LoginActivity.this))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Success>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Success success) {
                                        Log.d(WasteTreatmentApplication.TAG, "success: " + success.toString());
                                        if (success.getIsSuccess()) {
                                            getUser(mBinding.loginNameEdt.getText().toString().trim());
                                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                        } else {
                                            mBinding.errorLl.setVisibility(View.VISIBLE);
                                            waitingDialog.cancel();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(WasteTreatmentApplication.TAG, "onError: " + e.toString());
                                        waitingDialog.cancel();
                                        Tips.show("登录异常");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                    }


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
        ActivityCompat.requestPermissions(LoginActivity.this, Utils.permissionsREAD, 0);


    }


    private void getUser(String operatorId) {

        HttpClient.getInstance().geData().getUser(operatorId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetUsersBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetUsersBean getUsersBean) {
                        if (getUsersBean.getIsSuccess()) {
                            Log.d(WasteTreatmentApplication.TAG, "getUsersBean: " + getUsersBean.toString());
                            WasteTreatmentApplication.getInstance().setLoginMsg(getUsersBean.getContent().getChineseName(), Integer.toString(getUsersBean.getContent().getID()));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                    Log.d(WasteTreatmentApplication.TAG, "权限设置成功");

                } else {
                    //T.showShort(mContext,"拒绝权限");
                    // 权限被拒绝，弹出dialog 提示去开启权限
                    showPermissions();
                    break;
                }
            }

        }

    }

    //弹出dialog
    private void showPermissions() {
        final Dialog dialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();
        View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_permissions, null);
        dialog.show();
        dialog.setContentView(v);

        Button btn_add = (Button) v.findViewById(R.id.btn_add);
        Button btn_diss = (Button) v.findViewById(R.id.btn_diss);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });

        btn_diss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
