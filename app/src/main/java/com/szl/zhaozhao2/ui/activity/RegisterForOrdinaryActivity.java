package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.event.LoginFinishEvent;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.GsonHelper;
import com.szl.zhaozhao2.view.TitlebarView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterForOrdinaryActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "RegisterForOrdinaryActivity";
    private TitlebarView titlebarView;
    private EditText userName_et;
    private EditText psd_et;
    private EditText confirmPsd_et;
    private EditText phoneNum_et;
    private EditText confirmCode_et;
    private TextView getCode_tv;
    private ImageView register_iv;
    private RadioButton apply_rb;
    private RadioButton recruit_rb;
    private String ckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_ordinary);

        initView();
    }

    public void initView(){
        titlebarView = (TitlebarView) findViewById(R.id.titlebar_registerForOrdinary);
        titlebarView.setTitleText("普通用户注册");
        titlebarView.hiddenRightButton();
        titlebarView.getBackView().setOnClickListener(this);
        userName_et = (EditText) findViewById(R.id.et_userName_ordinary);
        psd_et = (EditText) findViewById(R.id.et_psd_ordinary);
        confirmPsd_et = (EditText) findViewById(R.id.et_psd_confirm_ordinary);
        phoneNum_et = (EditText) findViewById(R.id.et_phone_ordinary);
        confirmCode_et = (EditText) findViewById(R.id.et_cofirmCode_ordinary);
        getCode_tv = (TextView) findViewById(R.id.tv_getCode);
        register_iv = (ImageView) findViewById(R.id.iv_registerForOrdinary);
        apply_rb = (RadioButton) findViewById(R.id.rb_apply);
        recruit_rb = (RadioButton) findViewById(R.id.rb_recruit);
        getCode_tv.setOnClickListener(this);
        register_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.iv_registerForOrdinary:
                String userName = userName_et.getText().toString().trim();
                String psd = psd_et.getText().toString().trim();
                String confirmPsd = confirmPsd_et.getText().toString().trim();
                String phoneNum = phoneNum_et.getText().toString().trim();
                String code = confirmCode_et.getText().toString().trim();
                boolean isApply = apply_rb.isChecked();
                boolean isRecruit = recruit_rb.isChecked();
                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(psd) || TextUtils.isEmpty(confirmPsd) ||
                    TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(code) || !isApply && !isRecruit){
                    CommonUtil.showToast(this,"信息不能为空");
                }else if(!psd.equals(confirmPsd)){
                    CommonUtil.showToast(this,"两次输入的密码不一致");
                }else{
                    register();
                }
                break;
             case R.id.tv_getCode:
                 if(TextUtils.isEmpty(phoneNum_et.getText().toString())){
                     CommonUtil.showToast(this,"手机号不能为空");
                 }else{
                     getConfirmCode();
                 }
                break;
        }
    }

    public void getConfirmCode(){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("imsi", DApplication.imei);
            parameters.put("phone",phoneNum_et.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject meta = jsonObject.optJSONObject("meta");
                String result = meta.optString("s");
                String message = meta.optString("m");
                LogPrinter.v("Don","====="+jsonObject.toString());
                if(result.equals("1")){
                    CommonUtil.showToast(RegisterForOrdinaryActivity.this,"验证码获取成功");
                    ckId = jsonObject.optString("ckid");
                }
                CommonUtil.showToast(RegisterForOrdinaryActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(RegisterForOrdinaryActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.SendCode,TAG);
    }


    public void register(){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("type",apply_rb.isChecked() ? "4" : "5");
            parameters.put("ckId",ckId);
            parameters.put("sendCode",confirmCode_et.getText().toString().trim());
            parameters.put("imsi", DApplication.imei);
            parameters.put("userName",userName_et.getText().toString().trim());
            parameters.put("phone",phoneNum_et.getText().toString().trim());
            parameters.put("pwd",psd_et.getText().toString().trim());

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
                    DApplication.user = GsonHelper.parse(jsonObject.optJSONObject("user").toString(),User.class);
                    DApplication.isLogined = true;
                    DApplication.account = DApplication.user.userName;
                    DApplication.smId = jsonObject.optString("smId");
                    if(DApplication.user.type.equals("5")){
                        Intent intent = new Intent(RegisterForOrdinaryActivity.this,CompleteCompanyActivity.class);
                        intent.putExtra("smId",DApplication.smId);
                        startActivity(intent);
                        finish();
                    }else{
                        EventBus.getDefault().post(new LoginFinishEvent());
                        Intent intent = new Intent(RegisterForOrdinaryActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                CommonUtil.showToast(RegisterForOrdinaryActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(RegisterForOrdinaryActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.UserRegister2,TAG);
    }

}
