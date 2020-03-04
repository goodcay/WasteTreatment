package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.waste.treatment.R;
import com.waste.treatment.databinding.ActivityGenerateBinding;
import com.waste.treatment.qrcode.BGAQRCodeUtil;
import com.waste.treatment.qrcode.QRCodeEncoder;

public class CollectActivity extends AppCompatActivity {
    private ActivityGenerateBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_generate);
        mBinding.qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createChineseQRCode(mBinding.qrBtn.getText().toString());
                mBinding.qrImg.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(mBinding.qrEdt.getText().toString(), BGAQRCodeUtil.dp2px(CollectActivity.this, 150)));

            }
        });

    }
  /*  @SuppressLint("StaticFieldLeak")
    private void createChineseQRCode(final String s){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(s, BGAQRCodeUtil.dp2px(GenerateActivity.this, 150));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    mBinding.qrImg.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(GenerateActivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }*/
}
