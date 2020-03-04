package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.olc.scan.ScanManager;
import com.waste.treatment.R;

public class ScanActivity extends AppCompatActivity {
    private ScanManager sm;
    private TextView tv;
    String  m_Broadcastname = "com.barcode.sendBroadcast";
    MyCodeReceiver receiver = new MyCodeReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        tv = findViewById(R.id.text111);
        sm = (ScanManager) getSystemService("olc_service_scan");
        sm.setScanSwitchLeft(true);
        sm.setScanSwitchMiddle(true);//（如果有中扫描键需设置）
        sm.setScanSwitchRight(true);
        sm.setBarcodeReceiveModel(2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
    }


    public void registerBroadcast() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);
    }

    public class MyCodeReceiver extends BroadcastReceiver
    {
        private static final String TAG = "MycodeReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(m_Broadcastname)) {
                String str = intent.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    tv.setText(str);
                    Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
