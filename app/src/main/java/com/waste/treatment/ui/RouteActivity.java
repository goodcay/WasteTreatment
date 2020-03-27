package com.waste.treatment.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.BeginRouteBean;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.GetDriverBean;
import com.waste.treatment.databinding.ActivityRouteBinding;
import com.waste.treatment.http.HttpClient;
import com.waste.treatment.util.Tips;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.waste.treatment.WasteTreatmentApplication.TAG;

public class RouteActivity extends BaseActivity<ActivityRouteBinding> {
    private List<String> drives;
    private List<String> cars;
    private ArrayAdapter<String> adapterCar;
    private ArrayAdapter<String> adapterDra;
    private boolean getCarIsSucceed = false;
    private boolean getDriveSucceed = false;
    private int carId;
    private int driveId;
    private String driveName;
    private List<Integer> drivesId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mParentBinding.ilTitle.tvTitle.setText("生成路线");
        isVis(WasteTreatmentApplication.instance.getRouteId()==null);
        mBinding.btnSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginRoute();
            }
        });
        mBinding.spChepai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carId =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.spSj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                driveId =position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.endRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient.getInstance().geData().endRoute(WasteTreatmentApplication.instance.getRouteId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BeginRouteBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(BeginRouteBean beginRouteBean) {
                                Log.d(TAG, "EndRoute: " +beginRouteBean.getIsSuccess());
                                if (beginRouteBean.getIsSuccess()){

                                    WasteTreatmentApplication.instance.setRouteId(null,null,null);
                                    isVis(true);
                                    Tips.show("路线结束成功");
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                Tips.show("结束路线失败");

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_route;
    }


    private void getDate() {
        HttpClient.getInstance().geData().getDriver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetDriverBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetDriverBean getDriverBean) {
                        if (getDriverBean.getIsSuccess()) {
                            drives = new ArrayList<>();
                            drivesId = new ArrayList<>();
                            for (int i = 0; i < getDriverBean.getContent().size(); i++) {
                                drives.add(getDriverBean.getContent().get(i).getChineseName());
                                drivesId.add(getDriverBean.getContent().get(i).getID());
                            }
                            adapterDra = new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_spinner_item, drives);
                            adapterDra.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mBinding.spSj.setAdapter(adapterDra);
                            getDriveSucceed = true;
                            isShowContentView();
                        } else {
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
        HttpClient.getInstance().geData().getCars()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCarsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetCarsBean getCarsBean) {
                        if (getCarsBean.getIsSuccess()) {
                            cars = new ArrayList<>();
                            for (int i = 0; i < getCarsBean.getContent().size(); i++) {
                                cars.add(getCarsBean.getContent().get(i).getName());
                            }
                            adapterCar = new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_spinner_item, cars);
                            adapterCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mBinding.spChepai.setAdapter(adapterCar);
                            getCarIsSucceed = true;
                            isShowContentView();

                        } else {
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

    /**
     * 数据加载完毕后 显示
     */
    private void isShowContentView() {
        if (getCarIsSucceed && getDriveSucceed) {
            showContentView();
        }
    }
    private void beginRoute(){
        Log.d(TAG, "SC:carId: "+carId+"   driveId: "+driveId+"    userId: "+WasteTreatmentApplication.instance.getUserId());

        HttpClient.getInstance().geData().beginRoute(Integer.toString(carId),Integer.toString(driveId),WasteTreatmentApplication.instance.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeginRouteBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BeginRouteBean beginRouteBean) {
                        Log.d(TAG, "drives: "+drives.get(driveId)  +"    cars:"+cars.get(carId));
                        if (beginRouteBean.getIsSuccess()){
                            WasteTreatmentApplication.getInstance().setRouteId(Integer.toString(beginRouteBean.getContent().getOid()),drives.get(driveId),cars.get(carId));
                            Tips.show("已生成路线");
                            isVis(WasteTreatmentApplication.instance.getRouteId().isEmpty());
                        }else {
                            Tips.show(beginRouteBean.getErrorMsg());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Throwable: "+e.toString());


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void isVis(boolean bool){
        if (bool) {
            getDate();
            mBinding.llBangding.setVisibility(View.VISIBLE);
            mBinding.llWc.setVisibility(View.GONE);
        } else {
            mBinding.llBangding.setVisibility(View.GONE);
            mBinding.llWc.setVisibility(View.VISIBLE);
            showContentView();
        }
    }
}
