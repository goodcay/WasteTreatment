package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.ilTitle.tvTitle.setText("功能选择");
        mainBinding.ilTitle.llTitle.setBackgroundColor(getResources().getColor(R.color.color_btn));
        mainBinding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
        });
        mainBinding.gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));

            }
        });
        mainBinding.qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CollectActivity.class));

            }
        });
        mainBinding.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestPrintActivity.class));

            }
        });
        mainBinding.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImgActivity.class));

            }
        });
        mainBinding.imgBtnLuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CollectActivity.class));

            }
        });


    }
}
