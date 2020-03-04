package com.waste.treatment.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.waste.treatment.R;

public class PrintDialog extends Dialog {
    private Context context;

    public PrintDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    public PrintDialog(@NonNull Context context, int themeResId, String abc) {
        super(context, themeResId);
        this.context = context;
    }

    protected PrintDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, String abc) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_print, null);
        setContentView(view);
        // initView(view);
        // initListener();
    }
}
