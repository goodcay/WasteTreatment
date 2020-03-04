package com.waste.treatment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.waste.treatment.R;

import java.util.List;

public class TestListViewAdapter extends BaseQuickAdapter<String , BaseViewHolder> {
    Context context;

    public TestListViewAdapter(int layoutResId) {
        super(layoutResId);
    }

    public TestListViewAdapter(int layoutResId, List<String> data,Context context) {
        super(layoutResId, data);
        this.context =context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        final String b=s;
        TextView a =baseViewHolder.getView(R.id.text_tiaoma);
       /* a.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context,b,Toast.LENGTH_SHORT).show();
           }
       });*/
        //a.setText(s);

    }
}
