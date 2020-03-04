package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.adapter.RuiKuAdapter;
import com.waste.treatment.bean.RuiKuBean;
import com.waste.treatment.databinding.ActivityRuiKuBinding;
import com.waste.treatment.dialog.RuiKuDialog;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class RuiKuActivity extends AppCompatActivity implements RuiKuDialog.OnCenterItemClickListener {
    private ActivityRuiKuBinding mBinding;
    private RuiKuAdapter adapter;
    public List<RuiKuBean> testData;
    private RuiKuDialog mDialog;
    private String abc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mDialog = new RuiKuDialog(this,R.layout.dialog_ruiku,new int []{R.id.dialog_queren,R.id.dialog_quxiao});
        mDialog.setOnCenterItemClickListener(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rui_ku);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvList.setLayoutManager(layoutManager);
        mBinding.ilTitle.tvTitle.setText("入库废物");
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new RuiKuAdapter(R.layout.item_ruiku, getData(), this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Tips.show("item:"+position);

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
        refreshTotal();
        mBinding.allCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.allCb.isChecked()) {
                    for (int i = 0; i < testData.size(); i++) {
                        testData.get(i).setCheck(true);
                    }
                } else {
                    for (int i = 0; i < testData.size(); i++) {
                        testData.get(i).setCheck(false);
                    }
                }
                refreshTotal();
                adapter.notifyDataSetChanged();
            }
        });

        mBinding.btnRk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < testData.size(); i++) {
                    testData.get(i).setCheck(true);
                }
            }
        });
        mBinding.btnRk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();

            }
        });


    }

    private List<RuiKuBean> getData() {
        testData = new ArrayList<RuiKuBean>();

        for (int i = 0; i < 30; i++) {
            RuiKuBean bean = new RuiKuBean();

            if (i % 2 == 1) {
                bean.setCheck(false);


            } else {
                bean.setCheck(true);
            }
            bean.setAbc("a:" + i);
            testData.add(bean);
        }
        return testData;

    }

    private void refreshTotal(){
        int t =0;

        for (int i=0;i<testData.size();i++){

            if (testData.get(i).isCheck()){
                t++;
            }
        }
        mBinding.tvTotal.setText("合计："+t);

    }

    @Override
    public void OnCenterItemClick(RuiKuDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_queren:
                TextView tv = (TextView)dialog.findViewById(R.id.text_total);
                Tips.show(tv.getText().toString());
                break;
            case R.id.dialog_quxiao:
                break;
            case R.id.rui_spinner:
                break;
            default:
                break;
        }

    }
}
