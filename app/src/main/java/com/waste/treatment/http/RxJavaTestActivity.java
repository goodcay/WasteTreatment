package com.waste.treatment.http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.CarsBean;
import com.waste.treatment.bean.GetCarsBean;
import com.waste.treatment.bean.Success;
import com.waste.treatment.bean.newsBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxJavaTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);
        testJuhePost();

        HttpUtils.getInstance().geData().getCars().map(new Function<GetCarsBean, List<CarsBean>>() {

            @Override
            public List<CarsBean> apply(GetCarsBean getCarsBean) throws Exception {
                return getCarsBean.getContent();
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CarsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(WasteTreatmentApplication.TAG, "onSubscribe");

                    }

                    @Override
                    public void onNext(List<CarsBean> carsBeans) {

                        Log.d(WasteTreatmentApplication.TAG, "onNext" + carsBeans.get(0).getName());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "error1:"+ e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d(WasteTreatmentApplication.TAG, "onComplete");

                    }
                });

        HttpUtils.getInstance().geData().addPos(1,"1.3","1.6").subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Success>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Success success) {
                Log.d(WasteTreatmentApplication.TAG, "onNextppos" + success.toString());

            }

            @Override
            public void onError(Throwable e) {
                Log.d(WasteTreatmentApplication.TAG, "pos:"+ e.getMessage()+new BigDecimal(3.5));

            }

            @Override
            public void onComplete() {

            }
        });

       /*
        OkHttpUtils.get().url("http://192.168.121.59/WhhService.asmx/GetCars").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(WasteTreatmentApplication.TAG,"ERROR OKHTTP");

            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(WasteTreatmentApplication.TAG," OKHTTP:"+response);


            }
        });*/




    }


    private void testRxJava() {

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

            }
        });


        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer instant) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };


/*
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
*/


        observable.subscribe(observer);

    }


    private void testJuhePost(){

        HttpUtils.getInstance().getmJuHeClient().getTouTiao("f9141352fe34615b0806e9fd943f9dbd","top").subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<newsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(newsBean newsBean) {
                       String nnn = newsBean.getResult().getData().get(0).getTitle();
                        Log.d(WasteTreatmentApplication.TAG, "JUHE:"+nnn);


                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.d(WasteTreatmentApplication.TAG, "juhe:"+ e.getMessage()+new BigDecimal(3.5));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
