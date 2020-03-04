package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityDaiBinding;

public class DaiActivity extends AppCompatActivity {
    private ActivityDaiBinding mBinding;
    private RecyclerView d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_dai);
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
    }
}
