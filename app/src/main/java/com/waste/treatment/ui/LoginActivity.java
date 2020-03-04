package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityLoginBinding;
import com.waste.treatment.util.Utils;

public class LoginActivity extends AppCompatActivity {
     private ActivityLoginBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.login_btn_text));
        mBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.loginNameEdt.getText().toString().trim().isEmpty()||mBinding.loginPwdEdt.getText().toString().trim().isEmpty()){

                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_pwd_null),Toast.LENGTH_LONG).show();

                }else {
                    if (mBinding.loginNameEdt.getText().toString().trim().equals("abc")&&mBinding.loginPwdEdt.getText().toString().trim().equals("123")){

                        startActivity(new Intent(LoginActivity.this, TestFramgeActivity.class));
                        finish();
                    }else {
                        mBinding.errorLl.setVisibility(View.VISIBLE);
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


    }
}
