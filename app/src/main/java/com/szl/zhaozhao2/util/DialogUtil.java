package com.szl.zhaozhao2.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.szl.zhaozhao2.R;

/**
 * Created by itsdon on 16/6/9.
 */
public class DialogUtil {

    private static Dialog dialog;

    private DialogUtil(){};

    public static void showMoreDialog(Activity activity, final OnMoreDialogItemClickListener listener){
        if(dialog == null){
            dialog = new Dialog(activity, R.style.CustomDialog);
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_more, null);
        view.findViewById(R.id.tv_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCollectButtonClicked(view);
            }
        });

        view.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShareButtonClicked(view);
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    public static void dimissMoreDialog(){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public interface OnMoreDialogItemClickListener {
        void onCollectButtonClicked(View v);
        void onShareButtonClicked(View v);
    }

}
