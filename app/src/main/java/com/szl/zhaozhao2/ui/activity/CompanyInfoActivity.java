package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.view.TitlebarView;

public class CompanyInfoActivity extends BaseFragmentActivity implements View.OnClickListener{

    private TitlebarView titlebarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        initView();
    }

    public void initView(){
        titlebarView = (TitlebarView) findViewById(R.id.titlebar_companyInfo);
        titlebarView.setTitleText("公司信息");
        titlebarView.getBackView().setOnClickListener(this);
        titlebarView.setRightText("更多");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
        }

    }
}
