package com.waste.treatment.ui.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.ActivityTestScanBinding;
import com.waste.treatment.ui.BaseActivity;

public class TestScanActivity extends BaseActivity<ActivityTestScanBinding> {
    private String barcodeStr;
    private EditText showScanResult;
    ScanDevice sm;
    private final static String SCAN_ACTION = "scan.rcv.message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        mParentBinding.ilTitle.tvTitle.setText(bundle.getString("titleName"));
        Log.d(WasteTreatmentApplication.TAG, "onCreate: "+bundle.getString("type"));
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        showScanResult = mBinding.edtText;
        sm = new ScanDevice();
        sm.setOutScanMode(0);//启动就是广播模式
        showContentView();
        mBinding.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.openScan();
            }
        });

        mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.closeScan();
            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_test_scan;
    }


    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            byte[] aimid = intent.getByteArrayExtra("aimid");
            barcodeStr = new String(barocode, 0, barocodelen);
            showScanResult.append(barcodeStr);
            showScanResult.append("\n");
            sm.stopScan();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }
    }
}
