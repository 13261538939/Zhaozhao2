package com.szl.zhaozhao2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szl.zhaozhao2.R;

/**
 * Created by itsdon on 16/5/15.
 */
public class TitlebarView extends LinearLayout {
    // private Context context;
    private ImageView back_iv, icon1_iv, icon2_iv;
    private TextView title_tv;
    private Button right_btn;

    public TitlebarView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public TitlebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LinearLayout
                .inflate(context, R.layout.titlebar, null);
        back_iv = (ImageView) view.findViewById(R.id.iv_back_titlebar);
        title_tv = (TextView) view.findViewById(R.id.tv_title_titlebar);
       right_btn = (Button) view.findViewById(R.id.btn_right_titlebar);
        icon1_iv = (ImageView) view.findViewById(R.id.iv_icon1_titlebar);
        icon2_iv = (ImageView) view.findViewById(R.id.iv_icon2_titlebar);
        addView(view);
    }

    public void setBackImage(int sourceId) {
        back_iv.setImageResource(sourceId);
    }

    public void setTitleText(String str) {
        title_tv.setText(str);
    }

    public void setTitleText(int sourceId) {
        title_tv.setText(sourceId);
    }

    public void setRightText(int sourceId) {
        right_btn.setText(sourceId);
    }

    public void setRightText(String str) {
        right_btn.setText(str);
    }

    public ImageView getBackView() {
        return back_iv;
    }

    public TextView getTitleView() {
        return title_tv;
    }

    public ImageView getIcon1View() {
        return icon1_iv;
    }

    public ImageView getIcon2View() {
        return icon2_iv;
    }

    public Button getRightButton() {
        return right_btn;
    }

    public void hiddenBackView() {
        back_iv.setVisibility(View.GONE);
    }

    public void hiddenRightButton() {
        right_btn.setVisibility(View.GONE);
    }

    public void showBackView() {
        back_iv.setVisibility(View.VISIBLE);
    }

    public void showRightButton() {
        right_btn.setVisibility(View.VISIBLE);
    }

    public void showIcon1View() {
        icon2_iv.setVisibility(View.VISIBLE);
    }

    public void showIcon2View() {
        icon2_iv.setVisibility(View.VISIBLE);
    }
}
