package com.waste.treatment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.waste.treatment.R;
import com.waste.treatment.adapter.RuiKuAdapter;
import com.waste.treatment.databinding.FragmentHomeBinding;
import com.waste.treatment.ui.CollectActivity;
import com.waste.treatment.ui.ListViewActivity;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.util.Utils;

public class HomeFragment extends Fragment {
    FragmentHomeBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.tvName.setText(Utils.getShangOrXia()+"好，Admin");
        mBinding.tvTime.setText(Utils.getMonthDay());
        mBinding.llYfsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });
        mBinding.llYfck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListViewActivity.class));
            }
        });
        mBinding.llYfrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RuiKuActivity.class));
            }
        });

    }
}
