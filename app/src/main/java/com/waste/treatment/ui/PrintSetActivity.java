package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.ActivityPrintSetBinding;
import com.waste.treatment.util.SharedPreferencesUtil;
import com.waste.treatment.util.Utils;

import hardware.print.printer;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_IN;
import static android.util.TypedValue.COMPLEX_UNIT_MM;
import static android.util.TypedValue.COMPLEX_UNIT_PT;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class PrintSetActivity extends AppCompatActivity {
    ActivityPrintSetBinding mBinding;
    private int gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_print_set);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.print_set));
        mBinding.ilTitle.tvRightText.setText(getResources().getString(R.string.save));
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.spinnerGray.setSelection((int)SharedPreferencesUtil.getInstance(this).getSP("grayLevel",0));
        mBinding.printHEdt.setText((String)SharedPreferencesUtil.getInstance(this).getSP("printHigh",""));
        mBinding.printWEdt.setText((String)SharedPreferencesUtil.getInstance(this).getSP("printWidth",""));
        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.spinnerGray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gray =position;

                Log.d(WasteTreatmentApplication.TAG,"cccccc"+gray);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.ilTitle.tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("printHigh",mBinding.printHEdt.getText().toString().trim());
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("printWidth",mBinding.printWEdt.getText().toString().trim());
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("grayLevel",gray);
                Toast.makeText(PrintSetActivity.this,"保存成功",Toast.LENGTH_SHORT).show();

            }
        });

        mBinding.printReviseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printer.Open() == 0) {
                    printer.Step((byte) 0x1f);
                }else {
                    Toast.makeText(PrintSetActivity.this,getResources().getString(R.string.print_error),Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.printReviseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

    }
}
