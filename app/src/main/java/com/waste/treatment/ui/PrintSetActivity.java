package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.printer.sdk.PosFactory;
import android.printer.sdk.constant.BarCode;
import android.printer.sdk.interfaces.IPosApi;
import android.printer.sdk.interfaces.OnPrintEventListener;
import android.printer.sdk.util.PowerUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityPrintSetBinding;
import com.waste.treatment.util.SharedPreferencesUtil;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import cn.pda.serialport.SerialDriver;


public class PrintSetActivity extends AppCompatActivity {
    ActivityPrintSetBinding mBinding;
    private int gray;
    private IPosApi mPosApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_print_set);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.print_set));
        mBinding.ilTitle.tvRightText.setText(getResources().getString(R.string.save));
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.spinnerGray.setSelection((int) SharedPreferencesUtil.getInstance(this).getSP("grayLevel", 0));
        mBinding.printHEdt.setText((String) SharedPreferencesUtil.getInstance(this).getSP("printHigh", ""));
        mBinding.printWEdt.setText((String) SharedPreferencesUtil.getInstance(this).getSP("printWidth", ""));
        initPos();
        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.spinnerGray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gray = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.ilTitle.tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("printHigh", mBinding.printHEdt.getText().toString().trim());
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("printWidth", mBinding.printWEdt.getText().toString().trim());
                SharedPreferencesUtil.getInstance(PrintSetActivity.this).putSP("grayLevel", gray);
                Toast.makeText(PrintSetActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

            }
        });

        mBinding.printReviseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosApi.addMark();
                //  mPosApi.addFeedPaper(true, 2);
                mPosApi.printStart();
              /*  if (printer.Open() == 0) {
                    printer.Step((byte) 0x1f);
                }else {
                    Toast.makeText(PrintSetActivity.this,getResources().getString(R.string.print_error),Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPosApi.closeDev();
      //  mPosApi.closePos();
        // printer.Close();
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

    public OnPrintEventListener onPrintEventListener = new OnPrintEventListener() {
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
