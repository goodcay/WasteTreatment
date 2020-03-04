package com.waste.treatment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityImgBinding;
import com.wildma.pictureselector.PictureSelector;

public class ImgActivity extends AppCompatActivity {
    private ActivityImgBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_img);
        mBinding.photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureSelector.create(ImgActivity.this,PictureSelector.SELECT_REQUEST_CODE).selectPicture(true, 200, 200, 1, 1);;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE){
            if (data!=null){
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
                RequestOptions requestOptions = RequestOptions
                        .circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(this).load(picturePath).apply(requestOptions).into(mBinding.imgIv);
            }
        }
    }
}
