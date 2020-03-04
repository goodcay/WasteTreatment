package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.LinearLayoutBindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.waste.treatment.R;
import com.waste.treatment.adapter.TestListViewAdapter;
import com.waste.treatment.databinding.ActivityListViewBinding;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    private TestListViewAdapter adapter;
    private List<String> testData;
    private ActivityListViewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list_view);
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
        adapter = new TestListViewAdapter(R.layout.rev_item, getData(), this);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Tips.show(position + "--:::" + view.getId());

            }
        });
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Tips.show(position + "-----" + view.getId());

            }
        });
        mBinding.rvList.setAdapter(adapter);
    }


    private List<String> getData() {
        testData = new ArrayList<String>();
        testData.add("a");
        testData.add("b");
        testData.add("c");
        testData.add("d");
        testData.add("e");
        testData.add("f");
        testData.add("g");
        testData.add("h");
        testData.add("j");
        testData.add("k");
        testData.add("l");
        testData.add("m");
        return testData;


    }
}
