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
    ScanDevice sm;
    private int state;
    private final static String SCAN_ACTION = "scan.rcv.message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        state = bundle.getInt("state");
        mParentBinding.ilTitle.tvTitle.setText(bundle.getString("titleName"));
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvList.setLayoutManager(layoutManager);

        adapter = new RuiKuAdapter(R.layout.rev_item, data, this);
        mBinding.rvList.setAdapter(adapter);

        sm = new ScanDevice();
        sm.setOutScanMode(0);//启动就是广播模式
        sm.openScan();
        getData();


        //  refreshTotal();
/*        mBinding.allCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.allCb.isChecked()) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setCheck(true);
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setCheck(false);
                    }
                }
                refreshTotal();
                adapter.notifyDataSetChanged();
            }
        });*/

  /*      mBinding.btnRk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < testData.size(); i++) {
                    testData.get(i).setCheck(true);
                }
            }
        });*/
        mBinding.btnRk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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

/*
    private void refreshTotal(){
        int t =0;

        for (int i=0;i<testData.size();i++){

            if (testData.get(i).isCheck()){
                t++;
            }
        }
        mBinding.tvTotal.setText("合计："+t);

    }
*/
   /*  private void adapterSet(){

        adapter = new RuiKuAdapter(R.layout.rev_item, data, this);
       adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               // Tips.show("item:"+position);
            }
        });

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()==R.id.ck){
                    CheckBox cb = (CheckBox) view;
                    if (cb.isChecked()){
                        testData.get(position).setCheck(true);
                    }else {
                        testData.get(position).setCheck(false);
                    }
                    refreshTotal();
                }
            }
        });
        mBinding.rvList.setAdapter(adapter);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
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
        if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }
    }

    @Override
    protected void onRefresh() {
        getData();
    }

    private void update(int i, String recyleCode, String amount, String operatorid) {

        Log.i(WasteTreatmentApplication.TAG, "入库: int" + i + "  recyleCode:" + recyleCode + "  amount:" + amount + "    operatorid:" + operatorid);
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
                HttpClient.getInstance().geData().invalidRecyle(recyleCode, operatorid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Success>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Success success) {
                                if (success.getIsSuccess()){
                                    Tips.show("销毁成功");
                                }else {
                                    Tips.show("销毁失败");
                                }

                                onRefresh();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Tips.show("销毁失败");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }

    }

}
