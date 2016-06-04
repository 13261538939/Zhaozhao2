package com.szl.zhaozhao2.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szl.zhaozhao2.R;

/**
 * Created by itsdon on 16/5/25.
 */
public class CustomLinearLayout extends LinearLayout{

    private TextView tv_lable_customlinearlayout;
    private EditText et_lableContent_customlinearlayout;
    private View view_line_cll;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayoutView);
        typedArray.getString(R.styleable.CustomLinearLayoutView_lable);
        View view = LayoutInflater.from(context).inflate(R.layout.customlinearlayout,null);
        tv_lable_customlinearlayout = (TextView) view.findViewById(R.id.tv_lable_customlinearlayout);
        et_lableContent_customlinearlayout = (EditText) view.findViewById(R.id.et_lableContent_customlinearlayout);
        view_line_cll = (View) view.findViewById(R.id.view_line_cll);


    }


    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
