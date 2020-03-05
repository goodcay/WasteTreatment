package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.DialogPrintBinding;
import com.waste.treatment.qrcode.BGAQRCodeUtil;
import com.waste.treatment.qrcode.QRCodeEncoder;
import com.waste.treatment.util.Utils;

import hardware.print.printer;

public class TestPrintActivity extends AppCompatActivity {
    printer mPrinter;
    private DialogPrintBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_print);
        mBinding.edtTime.setText(Utils.getNowTime());
        mPrinter = new printer();
        mBinding.btnScqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.qrCodeImg.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(mBinding.edtId.getText().toString(), BGAQRCodeUtil.dp2px(TestPrintActivity.this, 150)));
            }
        });
        mBinding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.edtLx.clearFocus();
                mBinding.edtCll.clearFocus();
                mBinding.edtId.clearFocus();
                mBinding.edtName.clearFocus();
                mBinding.edtTime.clearFocus();
                String w = mBinding.edtW.getText().toString().trim();
                String h = mBinding.edtH.getText().toString().trim();
                Log.d(WasteTreatmentApplication.TAG,"H:"+h+"        W:"+w);
                Bitmap bitmap = Utils.getViewToBitmap(mBinding.rlPrint, Integer.parseInt(w), Integer.parseInt(h));
                if (printer.Open() == 0) {
                    mBinding.img111.setImageBitmap(bitmap);
                    if (bitmap!=null){
                        mPrinter.PrintBitmap(bitmap);
                        printer.Step((byte) 0x3f);
                        mPrinter.PrintLineEnd();
                    }

                }
            }
        });

        mBinding.btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WasteTreatmentApplication.TAG,"dddddd");
                if (printer.Open() == 0) {
                printer.Step((byte) 0x1f);
                }
            }
        });


    }

}
