package com.waste.treatment.ui;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.adapter.RuiKuAdapter;
import com.waste.treatment.bean.Data1Bean;
import com.waste.treatment.bean.DataBean;
import com.waste.treatment.bean.RuiKuBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.ActivityRuiKu1Binding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.Tips;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RuiKuActivity extends BaseActivity<ActivityRuiKu1Binding> {
    private RuiKuAdapter adapter;
    public List<Data1Bean> data = new ArrayList<>();
    private String type;
    private String barcodeStr;
    private String hint;
    ScanDevice sm;
    private int state;
    private final static String SCAN_ACTION = "scan.rcv.message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        state = bundle.getInt("state");
        hint =bundle.getString("hint");
        mParentBinding.ilTitle.tvTitle.setText(bundle.getString("titleName"));
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        if (state!=3){
            mBinding.textHint.setText(hint);
        }else {
            mBinding.textHint.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvList.setLayoutManager(layoutManager);
        adapter = new RuiKuAdapter(R.layout.rev_item, data, this);
        mBinding.rvList.setAdapter(adapter);

        sm = new ScanDevice();
        sm.setOutScanMode(0);//启动就是广播模式
        sm.openScan();
        getData();

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_rui_ku1;
    }

    private void getData() {
        HttpClient.getInstance().geData().getRecyles(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        Log.d(WasteTreatmentApplication.TAG, "data: " + dataBean.getContent());
                        if (dataBean.getIsSuccess()) {
                            data.clear();
                            if (dataBean.getContent() != null) {
                                data.addAll(dataBean.getContent());
                            }
                            adapter.notifyDataSetChanged();
                            showContentView();
                        } else {
                            data.clear();
                            if (dataBean.getContent() != null) {
                                data.addAll(dataBean.getContent());
                            }
                            adapter.notifyDataSetChanged();
                            showError();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
        onRefresh();
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            byte[] aimid = intent.getByteArrayExtra("aimid");
            barcodeStr = new String(barocode, 0, barocodelen);
            update(state, barcodeStr, "1", WasteTreatmentApplication.instance.getUserId());
            sm.stopScan();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }*/
    }

    @Override
    protected void onRefresh() {
        getData();
    }



    private void update(int i, String recyleCode, String amount, String operatorid) {
        switch (i) {
            case 0:
                HttpClient.getInstance().geData().isStock(recyleCode, amount, operatorid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Success>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Success success) {
                                if (success.getIsSuccess()){
                                    Tips.show("入库成功");
                                }else {
                                    Tips.show("入库失败");
                                }

                                onRefresh();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(WasteTreatmentApplication.TAG, "onError: " + e.toString());
                                Tips.show("入库失败");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case 1:
                HttpClient.getInstance().geData().outStock(recyleCode, amount, operatorid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Success>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Success success) {
                                if (success.getIsSuccess()){
                                    Tips.show("出库成功");
                                }else {
                                    Tips.show("出库失败");
                                }
                                onRefresh();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Tips.show("出库失败");

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case 2:
                //利用Intent实现跳转
                Intent intent = new Intent(RuiKuActivity.this,InvalidRecyleActivity.class);
                //利用Bundle携带数据,类似于Map集合,携带数据有很多种这里主要介绍这种
                Bundle bundle = new Bundle();
                bundle.putString("code", recyleCode);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }

}
