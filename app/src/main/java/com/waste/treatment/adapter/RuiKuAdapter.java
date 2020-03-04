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
import com.waste.treatment.bean.RuiKuBean;
import com.waste.treatment.ui.RuiKuActivity;
import com.waste.treatment.util.Tips;
import com.waste.treatment.util.Utils;

import java.util.List;

public class RuiKuAdapter extends BaseQuickAdapter<RuiKuBean , BaseViewHolder> {
    Context context;

    public RuiKuAdapter(int layoutResId) {
        super(layoutResId);
    }

    public RuiKuAdapter(int layoutResId, List<RuiKuBean> data, Context context) {
        super(layoutResId, data);
        this.context =context;
        addChildClickViewIds(R.id.text_01,
                R.id.text_02, R.id.text_03,R.id.ck);

     /*   addChildLongClickViewIds(R.id.text_01,
                R.id.text_02, R.id.text_03);*/
    }



    @Override
    protected void convert(BaseViewHolder baseViewHolder, final RuiKuBean s) {
       CheckBox cb = baseViewHolder.getView(R.id.ck);
       cb.setChecked(s.isCheck());


    }


}
