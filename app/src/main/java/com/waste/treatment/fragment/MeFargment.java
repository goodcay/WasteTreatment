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
import com.waste.treatment.databinding.FragmentSetBinding;
import com.waste.treatment.ui.ChangePasswordActivity;
import com.waste.treatment.ui.PrintSetActivity;

public class MeFargment extends Fragment {
    FragmentSetBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_set,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.me));
        mBinding.llChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));

            }
        });
        mBinding.llPrintSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PrintSetActivity.class));

            }
        });
    }
}
