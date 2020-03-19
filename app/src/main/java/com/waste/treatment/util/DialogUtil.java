package com.waste.treatment.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.waste.treatment.ui.CollectActivity;


public class DialogUtil {
    public static ProgressDialog waitingDialog(Context context,String text) {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */

        ProgressDialog waitingDialog = new ProgressDialog(context);
        //waitingDialog.setTitle("正
        //
        // 在上传");
        waitingDialog.setMessage(text);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        return waitingDialog;
    }


}
