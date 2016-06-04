package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.view.TitlebarView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterForCompanyActivity extends BaseFragmentActivity implements View.OnClickListener{


    private static final String TAG = "RegisterForCompanyActivity";
    private TitlebarView titlebarView;
     private EditText companyName_et;
    private EditText contacts_et;
    private EditText phone_et;
    private ImageView register_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_company);
        initView();
    }

    public void initView(){
        titlebarView = (TitlebarView) findViewById(R.id.titlebar_registerForCompany);
        titlebarView.setTitleText("单位用户注册");
        titlebarView.hiddenRightButton();
        titlebarView.getBackView().setOnClickListener(this);
        companyName_et = (EditText) findViewById(R.id.et_companyName);
        contacts_et = (EditText) findViewById(R.id.et_contacts);
        phone_et = (EditText) findViewById(R.id.et_phone);
        register_iv = (ImageView) findViewById(R.id.iv_registerForCompany);
        register_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.iv_registerForCompany:
                if(TextUtils.isEmpty(companyName_et.getText()) || TextUtils.isEmpty(contacts_et.getText())
                        || TextUtils.isEmpty(phone_et.getText())){
                    CommonUtil.showToast(RegisterForCompanyActivity.this,"信息不能为空");
                }else{
                    register();
                }

                break;
        }
    }

    public void register(){
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("unitName",companyName_et.getText().toString().trim());
            parameters.put("phone",phone_et.getText().toString().trim());
            parameters.put("linkMan",contacts_et.getText().toString().trim());
            parameters.put("imsi", DApplication.imei);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
               JSONObject meta = jsonObject.optJSONObject("meta");
                String result = meta.optString("s");
                String message = meta.optString("m");
                if(result.equals("1")){
                    CommonUtil.showToast(RegisterForCompanyActivity.this,"注册成功，请等待客服人员与您联系");
                    Intent intent = new Intent(RegisterForCompanyActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(RegisterForCompanyActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.ApplyForUser,TAG);
    }
}
