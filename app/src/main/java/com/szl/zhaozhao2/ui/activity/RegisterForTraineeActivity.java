
package com.szl.zhaozhao2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.GsonHelper;
import com.szl.zhaozhao2.view.TitlebarView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterForTraineeActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "RegisterForTraineeActivity";
    private TitlebarView titlebarView;
    private EditText userName_et;
    private EditText psd_et;
    private EditText psdConfirm_et;
    private EditText confirmCode_et;
    private ImageView register_iv;
    private String ckid;
    private String aaa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_trainee);

        initView();
    }

    public void initView(){

        ckid = getIntent().getStringExtra("ckid");
        aaa = getIntent().getStringExtra("aaa");

        titlebarView = (TitlebarView) findViewById(R.id.titlebar_registerForTrainee);
        titlebarView.getBackView().setOnClickListener(this);
        titlebarView.hiddenRightButton();
        titlebarView.setTitleText("实习生注册");
        userName_et = (EditText) findViewById(R.id.et_userName_trainee);
        psd_et = (EditText) findViewById(R.id.et_psd_trainee);
        psdConfirm_et = (EditText) findViewById(R.id.et_psd_confirm_trainee);
        confirmCode_et = (EditText) findViewById(R.id.et_cofirmCode);
        register_iv = (ImageView) findViewById(R.id.iv_registerForTrainee);
        register_iv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.iv_back_titlebar:
              finish();
              break;
          case R.id.iv_registerForTrainee:
               if(TextUtils.isEmpty(userName_et.getText()) || TextUtils.isEmpty(psd_et.getText())
                       || TextUtils.isEmpty(psdConfirm_et.getText()) || TextUtils.isEmpty(confirmCode_et.getText())){
                    CommonUtil.showToast(RegisterForTraineeActivity.this,"信息不能为空");
              }else if(!psdConfirm_et.getText().toString().equals(psd_et.getText().toString())){
                   CommonUtil.showToast(RegisterForTraineeActivity.this,"两次输入密码不一致");
               }else{
                   register(userName_et.getText().toString().trim(),
                           psd_et.getText().toString().trim(),
                           confirmCode_et.getText().toString().trim());
               }

              break;
      }
    }

    public void register(String userName,String psd,String code){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("aaa",aaa);
            parameters.put("ckId",ckid);
            parameters.put("sendCode",code);
            parameters.put("imsi", DApplication.imei);
            parameters.put("userName",userName);
            parameters.put("pwd",psd);

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
                    Intent intent = new Intent(RegisterForTraineeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                CommonUtil.showToast(RegisterForTraineeActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(RegisterForTraineeActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.UserRegister,TAG);
    }


}
