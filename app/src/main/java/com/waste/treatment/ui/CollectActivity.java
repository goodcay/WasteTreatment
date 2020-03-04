package com.waste.treatment.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.databinding.ActivityCollectBinding;
import com.waste.treatment.util.Utils;
import com.wildma.pictureselector.PictureSelector;

public class CollectActivity extends AppCompatActivity {
    private ActivityCollectBinding mBinding;
    final static int TYPE_ALL = 0;
    final static int TYPE_IMAGE = 1;
    final static int TYPE_VIDEO = 2;
    final static int TYPE_AUDIO = 3;
    final static int CHOOSE_REQUEST = 188;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_collect);
        mBinding.ilTitle.tvTitle.setText(getResources().getString(R.string.collect_title));
        mBinding.ilTitle.ivBack.setVisibility(View.VISIBLE);
        mBinding.ilTitle.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.imgBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(CollectActivity.this,PictureSelector.SELECT_REQUEST_CODE).selectPicture(false, 200, 200, 1, 1);;

            }
        });
        mBinding.imgBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delImg(false,null);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE){
            if (data!=null){
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
               /* RequestOptions requestOptions = RequestOptions
                        .circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);*/
                //Glide.with(this).load(picturePath)/*.apply(requestOptions)*/.into(mBinding.imgPhoto);
                delImg(true,picturePath);
            }
        }
    }

    private void delImg( Boolean bool ,String picturePath){
        if (bool){
            mBinding.imgBtnDel.setVisibility(View.VISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.INVISIBLE);
            Glide.with(this).load(picturePath)/*.apply(requestOptions)*/.into(mBinding.imgPhoto);

        }else {
            mBinding.imgBtnDel.setVisibility(View.INVISIBLE);
            mBinding.imgBtnPhoto.setVisibility(View.VISIBLE);
            mBinding.imgPhoto.setImageBitmap(null);
           // Glide.with(this).load(picturePath)/*.apply(requestOptions)*/.into(mBinding.imgPhoto);
        }

    }

}
