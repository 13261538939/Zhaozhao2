package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
import com.szl.zhaozhao2.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

public class PositionActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "PositionActivity";
    private TitlebarView titlebarView;
    private PullToRefreshListView ptrListView;
    private ListView listView;
    private String jobId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        initView();
        loadPositionApplyeeData();
    }

    public void initView(){

        jobId = getIntent().getStringExtra("jobId");

        titlebarView = (TitlebarView) findViewById(R.id.titlebar_position);
        titlebarView.getBackView().setOnClickListener(this);
        titlebarView.setRightText("更多");
        titlebarView.setTitleText("职位信息");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;

        }
    }

    public void loadPositionApplyeeData(){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("smId", DApplication.smId);
            parameters.put("mId",DApplication.user.mId);
            parameters.put("jobId",jobId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject meta = jsonObject.optJSONObject("meta");
                String result = meta.optString("s");
                String message = meta.optString("m");
                LogPrinter.v("Don","职位申请情况====="+jsonObject.toString());
                if(result.equals("1")){
//                    updateView(jsonObject);
                }
                CommonUtil.showToast(PositionActivity.this,message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(PositionActivity.this,
                        getString(R.string.no_connect));
            }
        }, Contants.DeliveryList,TAG);
    }

}
