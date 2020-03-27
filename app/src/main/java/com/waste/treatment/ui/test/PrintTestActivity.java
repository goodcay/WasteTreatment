package com.waste.treatment.ui.test;

import androidx.appcompat.app.AppCompatActivity;

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

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.ActivityPrintTestBinding;
import com.waste.treatment.ui.BaseActivity;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import cn.pda.serialport.SerialDriver;

public class PrintTestActivity extends BaseActivity<ActivityPrintTestBinding> {
    private IPosApi mPosApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentBinding.ilTitle.tvTitle.setText("打印测试");
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
       /* initPos();*/
        showContentView();
        mBinding.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // print("四川金达动物研究所","老鼠、兔子、猴子","川A·88888","6.3","王金龙","张阿婆","2020-03-20 18:21:56","20200302362514");
                Log.d(WasteTreatmentApplication.TAG, "AAAonClick: "+ Utils.timeToTime("2016-09-03T00:00:00.000+08:00 "));
            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_print_test;
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
