package com.szl.zhaozhao2.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szl.zhaozhao2.R;

/**
 * Created by itsdon on 16/5/15.
 */
public class LoadMoreView extends RelativeLayout {


    private TextView mTextView;
    private ProgressBar mProgressBar;
    protected LinearLayout mLy;
    protected static final int LINEARLAYOUT_ID = 0x0000001;

    public LoadMoreView(Context context, int textsize) {
        super(context);

        mLy = new LinearLayout(context);

        mLy.setOrientation(LinearLayout.HORIZONTAL);
        mLy.setGravity(Gravity.CENTER);
//        mLy.setId(LINEARLAYOUT_ID);
        setLyLayoutParams();
        addView(mLy);

        mProgressBar = new ProgressBar(context);
        int width = context.getResources().getDimensionPixelSize(
                R.dimen.progressbar_width);
        int height = context.getResources().getDimensionPixelSize(
                R.dimen.progressbar_height);
        LayoutParams lp1 = new LayoutParams(width, height);

        mProgressBar.setIndeterminate(false);
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(
                R.drawable.progressbar));
        mProgressBar.setLayoutParams(lp1);

        mLy.addView(mProgressBar);

        mTextView = new TextView(context);
        LayoutParams textViewParams = new LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, context.getResources()
                .getDimensionPixelSize(R.dimen.sta_height));
        mTextView.setLayoutParams(textViewParams);
        mTextView.setTextSize(textsize);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setText(R.string.more);
        mLy.addView(mTextView);

        setNormalMode();
    }

    protected void setLyLayoutParams() {
        if (mLy != null) {
            LayoutParams params = new LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    getResources().getDimensionPixelSize(
                            R.dimen.loadmore_item_height));
            mLy.setLayoutParams(params);
        }
    }

    protected void setLyLayoutParams(LayoutParams params) {
        if (params != null) {
            mLy.setLayoutParams(params);
        }
    }

    public LoadMoreView(Context context) {
        this(context, 14);
    }

    public void setText(int strId) {
        mTextView.setText(strId);
    }

    // 加载中状态
    public void setLoadingMode() {
        mTextView.setText(R.string.doing_update);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    // 正常状态,显示更多
    public void setNormalMode() {
        mTextView.setText(R.string.more);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    // 网络没有数据状态
    public void setNoDataMode() {
        mTextView.setText(R.string.no_new_content);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    // 空白状态
    public void setBlankMode() {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.GONE);
    }
    // 显示状态
    public void setshowMode(){
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }
}
