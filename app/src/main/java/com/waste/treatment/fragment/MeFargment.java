package com.waste.treatment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.Success;
import com.waste.treatment.databinding.FragmentSetBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.ui.ChangePasswordActivity;
import com.waste.treatment.ui.LoginActivity;
import com.waste.treatment.ui.PrintSetActivity;
import com.waste.treatment.ui.RouteActivity;
import com.waste.treatment.util.Tips;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

         mBinding.tvIdName.setText(WasteTreatmentApplication.instance.getUserName());
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
        mBinding.llSclx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RouteActivity.class));

            }
        });
        mBinding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpClient.getInstance().geData().logout(WasteTreatmentApplication.instance.getUserId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Success>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Success success) {
                                Log.d(WasteTreatmentApplication.TAG, "onNext: "+success.getIsSuccess());
                                if (success.getIsSuccess()){
                                    WasteTreatmentApplication.instance.setRouteId(null,null,null);
                                    WasteTreatmentApplication.instance.setLoginMsg(null,null);
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    Objects.requireNonNull(getActivity()).finish();
                                }else {
                                    Tips.show("退出异常");
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(WasteTreatmentApplication.TAG, "onError: "+e.toString());
                                Tips.show("退出异常");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });
    }


}
