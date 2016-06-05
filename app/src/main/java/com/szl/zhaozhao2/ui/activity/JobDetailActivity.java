package com.szl.zhaozhao2.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.view.TitlebarView;

public class JobDetailActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "JobDetailActivity";
    private String jobId;
    private TitlebarView titlebarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

      initView();
    }


    public void initView(){
        jobId = getIntent().getStringExtra("jobId");
        titlebarView = (TitlebarView) findViewById(R.id.titlebar_jobDetail);
        titlebarView.setTitleText("职位信息");
        titlebarView.setRightText("更多");
        titlebarView.getBackView().setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
        }
    }
}
