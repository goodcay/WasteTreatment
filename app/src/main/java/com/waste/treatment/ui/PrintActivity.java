package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityPrintBinding;

import hardware.print.printer;

public class PrintActivity extends AppCompatActivity {
    private ActivityPrintBinding mBinding;
    printer mPrinter = new printer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_print);
        mBinding.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int result = mPrinter.Open();
               if (result==0){
                   mPrinter.PrintLineInit(50);
                   mPrinter.PrintLineStringByType(mBinding.printEdt.getText().toString(),40, printer.PrintType.Centering,false);
                   mPrinter.PrintLineEnd();
                //   mPrinter.Close();
               }

            }
        });
    }
}
