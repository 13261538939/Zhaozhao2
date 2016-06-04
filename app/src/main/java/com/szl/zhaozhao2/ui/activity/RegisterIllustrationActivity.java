package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.szl.zhaozhao2.R;

public class RegisterIllustrationActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView noticeTitle_tv;
    private WebView webView;
    private ImageView registerForTrainee_iv;
    private ImageView registerForCompany_iv;
    private ImageView registerForOrdinary_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_illustration);

        initView();
    }

    public void initView(){
        noticeTitle_tv = (TextView) findViewById(R.id.tv_noticeTitle);
        webView = (WebView) findViewById(R.id.webView);
        registerForCompany_iv = (ImageView) findViewById(R.id.iv_registerForCompany_ill);
        registerForTrainee_iv = (ImageView) findViewById(R.id.iv_registerForTrainee_ill);
        registerForOrdinary_iv = (ImageView) findViewById(R.id.iv_registerForOrdinary_ill);
        registerForCompany_iv.setOnClickListener(this);
        registerForTrainee_iv.setOnClickListener(this);
        registerForOrdinary_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
      switch (v.getId()){
          case R.id.iv_registerForCompany_ill:
                 intent.setClass(this,RegisterForCompanyActivity.class);
              break;
          case R.id.iv_registerForTrainee_ill:
//              intent.setClass(this,RegisterForTraineeActivity.class);
              intent.setClass(this,MipCaptureActivity.class);
              break;
          case R.id.iv_registerForOrdinary_ill:
              intent.setClass(this,RegisterForOrdinaryActivity.class);
              break;
      }
        startActivity(intent);
        finish();
    }
}