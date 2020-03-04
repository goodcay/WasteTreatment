package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityChangePasswordBinding;
import com.waste.treatment.util.Utils;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.change_pwd));
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
