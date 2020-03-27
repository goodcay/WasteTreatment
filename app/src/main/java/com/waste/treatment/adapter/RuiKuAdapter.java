package com.waste.treatment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.waste.treatment.R;
import com.waste.treatment.WasteTreatmentApplication;
import com.waste.treatment.bean.Data1Bean;
import com.waste.treatment.bean.RuiKuBean;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.List;

public class RuiKuAdapter extends BaseQuickAdapter<Data1Bean, BaseViewHolder> {
    Context context;

    public RuiKuAdapter(int layoutResId) {
        super(layoutResId);
    }

    public RuiKuAdapter(int layoutResId, List<Data1Bean> data, Context context) {
        super(layoutResId, data);
        this.context =context;
        addChildClickViewIds(R.id.text_01,
                R.id.text_02, R.id.text_03,R.id.ck);

     /*   addChildLongClickViewIds(R.id.text_01,
                R.id.text_02, R.id.text_03);*/
    }



    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Data1Bean s) {
       TextView gongsi = baseViewHolder.getView((R.id.tv_gongsi));
       TextView zhongliang = baseViewHolder.getView((R.id.tv_zhongliang));
       TextView type = baseViewHolder.getView((R.id.tv_leixing));
       TextView code = baseViewHolder.getView((R.id.text_code));
       TextView chepai = baseViewHolder.getView((R.id.text_chepai));
       TextView sj_time = baseViewHolder.getView((R.id.text_sj_time));
       TextView siji= baseViewHolder.getView((R.id.tv_siji));
       TextView soujiren = baseViewHolder.getView((R.id.tv_shoujiren));
       gongsi.setText(s.getCompany().getName());
       zhongliang.setText(s.getWeight()+"Kg");
       type.setText(s.getName());
       code.setText(s.getCode());
       sj_time.setText(Utils.timeToTime1(s.getRecyleTime()));
       siji.setText(s.getRouteId().getDriver().getChineseName());
       chepai.setText(s.getRouteId().getCarId().getName());
       soujiren.setText(s.getRouteId().getBeginOperator().getChineseName());



    }


}
