package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.GsonHelper;
import com.szl.zhaozhao2.view.TitlebarView;

import org.json.JSONException;
import org.json.JSONObject;

public class CompleteCompanyActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "CompleteCompanyActivity";
    private TitlebarView titlebarView;
    private EditText shortForm_et;
    private EditText psd_et;
    private EditText fullName_et;
    private  EditText address_et;
    private EditText website_et;
    private EditText industry_et;
    private  EditText scale_et;
    private EditText shortIntro_et;
    private  EditText picture_et;
    private ImageView selectCompany_iv;
    private ImageView saveInfo_iv;
    private String smId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_company);


         initView();
    }

    public void initView(){
        smId = getIntent().getStringExtra("smId");

        // titlebar view
        titlebarView = (TitlebarView) findViewById(R.id.titleBar_CompleteCompany);
        titlebarView.hiddenRightButton();
        titlebarView.setTitleText("选择或完善单位信息");
        titlebarView.getBackView().setOnClickListener(this);

        //info view
        shortForm_et = (EditText) findViewById(R.id.et_shorterForm_CompleteCompany);
        psd_et = (EditText) findViewById(R.id.et_password_CompleteCompany);
        fullName_et  = (EditText) findViewById(R.id.et_fullName_CompleteCompany);
        address_et = (EditText) findViewById(R.id.et_address_CompleteCompany);
        website_et = (EditText) findViewById(R.id.et_website_CompleteCompany);
        industry_et = (EditText) findViewById(R.id.et_industry_CompleteCompany);
        scale_et = (EditText) findViewById(R.id.et_scale_CompleteCompany);
        shortIntro_et = (EditText) findViewById(R.id.et_briefIntroduction_CompleteCompany);
        picture_et = (EditText) findViewById(R.id.et_picture_CompleteCompany);
        selectCompany_iv = (ImageView) findViewById(R.id.iv_selectCompany);
        saveInfo_iv = (ImageView) findViewById(R.id.iv_saveCompanyInfo_CompleteCompany);
        saveInfo_iv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.iv_saveCompanyInfo_CompleteCompany:
                if(CommonUtil.isEmpty(shortForm_et.getText()) || CommonUtil.isEmpty(fullName_et.getText()) ||
                        CommonUtil.isEmpty(psd_et.getText()) || CommonUtil.isEmpty(address_et.getText())){
                    CommonUtil.showToast(CompleteCompanyActivity.this,"公司简称、全称、密码和地址不能为空");
                }else{
                    saveCompanyInfo();
                }
                break;
        }
    }

    public void saveCompanyInfo(){
        JSONObject parameters = new JSONObject();
        try {

            parameters.put("type","2");
            parameters.put("mId",DApplication.user.mId);
            parameters.put("smId",smId);
            parameters.put("officeName", fullName_et.getText().toString().trim());
            parameters.put("officeSname",shortForm_et.getText().toString().trim());
            parameters.put("location",address_et.getText().toString());
            parameters.put("lng","");
            parameters.put("lat","");
            parameters.put("brief",shortIntro_et.getText().toString());
            parameters.put("scale",scale_et.getText().toString());
            parameters.put("website",website_et.getText().toString());
            parameters.put("industry",industry_et.getText().toString());
            parameters.put("selfPass",psd_et.getText().toString());
            parameters.put("img","");

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
                    DApplication.user.officeId = jsonObject.optString("officeId");
                    Intent intent = new Intent(CompleteCompanyActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                CommonUtil.showToast(CompleteCompanyActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(CompleteCompanyActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.OfficeInfo,TAG);
    }

}
