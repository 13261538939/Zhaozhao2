package com.szl.zhaozhao2.ui.activity;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.event.LoginFinishEvent;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.GsonHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText account_et;
    private EditText psd_et;
    private ImageView login_iv;
    private TextView localLogin_tv;
    private TextView illustrationForRegist_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);

        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(LoginFinishEvent event){
        LoginActivity.this.finish();
    }

    public void initView() {
        account_et = (EditText) findViewById(R.id.et_account);
        account_et.setText(DApplication.account);
        psd_et = (EditText) findViewById(R.id.et_psd);
        login_iv = (ImageView) findViewById(R.id.iv_login);
        localLogin_tv = (TextView) findViewById(R.id.tv_login_local);
        illustrationForRegist_tv = (TextView) findViewById(R.id.tv_illustrationforregist);

        login_iv.setOnClickListener(this);
        localLogin_tv.setOnClickListener(this);
        illustrationForRegist_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

     switch (v.getId()){
         case R.id.iv_login:
//             if(TextUtils.isEmpty(account_et.getText().toString()) || TextUtils.isEmpty(psd_et.getText().toString())){
//                 CommonUtil.showToast(this,"用户名或密码不能为空");
//             }else{
//                 login("1",account_et.getText().toString().trim(),psd_et.getText().toString().trim());
//             }
             Intent cintent = new Intent(this,MainActivity.class);
             startActivity(cintent);
             break;
         case R.id.tv_login_local:
             if(CommonUtil.isEmpty(DApplication.imei)){
                 CommonUtil.showToast(this,"设备IMEI获取失败，请选择账号登陆");
             }else{
                 login("0","","");
             }
             break;
         case R.id.tv_illustrationforregist:
             Intent intent = new Intent();
             intent.setClass(this,RegisterIllustrationActivity.class);
             startActivity(intent);
             break;
     }
    }

    public void login(String loginType,String userName,String psd){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("loginType",loginType);
            parameters.put("imsi", DApplication.imei);
            if(loginType.equals("1")){
                parameters.put("userName",userName);
                parameters.put("pwd",psd);
            }

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
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                CommonUtil.showToast(LoginActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(LoginActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.Login,TAG);
    }

}
