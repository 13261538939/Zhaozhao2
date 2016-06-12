package com.szl.zhaozhao2.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.manager.imageloader.DisplayImageOptionsManager;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.User;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.DialogUtil;
import com.szl.zhaozhao2.util.GsonHelper;
import com.szl.zhaozhao2.view.RoundImageView;
import com.szl.zhaozhao2.view.TitlebarView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class JobDetailActivity extends BaseFragmentActivity implements View.OnClickListener{

    private static final String TAG = "JobDetailActivity";
    private String jobId;
    private TitlebarView titlebarView;
    // job info
    private TextView positionName_tv;
    private ImageView traineeTag_iv;
    private TextView salary_tv;
    private TextView companyLocation_tv;
    private TextView jobType_tv;
    private TextView eduRequire_tv;
    private TextView applyeeCount_tv;
    // applyer info
    private  RoundImageView applyerAvatar_iv;
    private TextView applyerName_tv;
    private TextView applyerPosition_tv;
    // job description
    private TextView jobDescShow_tv;
    private TextView jobDescHide_tv;
    private TextView showAllDesc_tv;
    private boolean allDescShowing = false;
    // company info
    private TextView companyScale_tv;
    private TextView companyWebsite_tv;
    private TextView companyStatus_tv;
    private TextView companyAddress_tv;
    // bottom button
    private ImageView talkNow_iv;
    private ImageView sendResume_iv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        initView();
        loadJobDetail();
    }


    public void initView(){
        jobId = getIntent().getStringExtra("jobId");
        // titlebar
        titlebarView = (TitlebarView) findViewById(R.id.titlebar_jobDetail);
        titlebarView.setTitleText("职位信息");
        titlebarView.setRightText("更多");
        titlebarView.getRightButton().setOnClickListener(this);
        titlebarView.getBackView().setOnClickListener(this);
        // job info
        positionName_tv = (TextView) findViewById(R.id.tv_positionName);
        traineeTag_iv = (ImageView) findViewById(R.id.iv_traineeTag);
        salary_tv = (TextView) findViewById(R.id.tv_salary);
        companyLocation_tv = (TextView) findViewById(R.id.tv_companyLocation);
        jobType_tv = (TextView) findViewById(R.id.tv_jobType);
        eduRequire_tv = (TextView) findViewById(R.id.tv_eduRequire);
        applyeeCount_tv = (TextView) findViewById(R.id.tv_applyeeCount);
        // applyer info
        applyerAvatar_iv = (RoundImageView) findViewById(R.id.iv_applyerAvatar);
        applyerName_tv = (TextView) findViewById(R.id.tv_applyerName_jobDetail);
        applyerPosition_tv = (TextView) findViewById(R.id.tv_applyerPosition_jobDetail);
        //  job description
        jobDescShow_tv = (TextView) findViewById(R.id.tv_jobDesc_show);
        jobDescHide_tv = (TextView) findViewById(R.id.tv_jobDesc_hide);
        showAllDesc_tv = (TextView) findViewById(R.id.tv_more);
        // company info
        companyScale_tv = (TextView) findViewById(R.id.tv_companyScale);
        companyWebsite_tv = (TextView) findViewById(R.id.tv_companyWebsite);
        companyAddress_tv = (TextView) findViewById(R.id.tv_companyAddress);
        companyStatus_tv = (TextView) findViewById(R.id.tv_companyStatus);
        // bottom button
        talkNow_iv = (ImageView) findViewById(R.id.iv_talkNow);
        sendResume_iv = (ImageView) findViewById(R.id.iv_sendResume);

        showAllDesc_tv.setOnClickListener(this);
        findViewById(R.id.rl_jobInfo_jobDetail).setOnClickListener(this);
        findViewById(R.id.rl_applyerInfo).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_titlebar:
                finish();
                break;
            case R.id.tv_more:
                 jobDescHide_tv.setVisibility(allDescShowing ? View.GONE : View.VISIBLE);
                 allDescShowing = allDescShowing ? false : true;
                 showAllDesc_tv.setText(allDescShowing ? "收起" : "显示全部");
                break;
            case R.id.rl_jobInfo_jobDetail:
                 Intent intent = new Intent(this,PositionActivity.class);
                   intent.putExtra("jobId",jobId);
                startActivity(intent);
                break;
            case R.id.btn_right_titlebar:
                DialogUtil.showMoreDialog(this, new DialogUtil.OnMoreDialogItemClickListener() {
                    @Override
                    public void onCollectButtonClicked(View v) {
                        DialogUtil.dimissMoreDialog();
                        collectPositon();
                    }

                    @Override
                    public void onShareButtonClicked(View v) {
                        CommonUtil.showToast(JobDetailActivity.this,"此功能待开发");
                        DialogUtil.dimissMoreDialog();
                    }
                });
                break;
            case R.id.rl_applyerInfo:
                   Intent mintent = new Intent(this,CompanyInfoActivity.class);
                startActivity(mintent);
                break;
        }
    }

    public void loadJobDetail(){
            JSONObject parameters = new JSONObject();
            RequestManager.getInstance().getData(Request.Method.GET, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    JSONObject meta = jsonObject.optJSONObject("meta");
                    String result = meta.optString("s");
                    String message = meta.optString("m");
                    LogPrinter.v("Don","职位详情====="+jsonObject.toString());
                    if(result.equals("1")){
                        updateView(jsonObject);
                    }
                    CommonUtil.showToast(JobDetailActivity.this,message);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CommonUtil.showToast(JobDetailActivity.this,
                            getString(R.string.no_connect));
                }
            }, Contants.GetJobInfo+"/"+jobId,TAG);
    }

    public void updateView(JSONObject resutl){
        JSONObject job = resutl.optJSONObject("job");
        JSONObject user = resutl.optJSONObject("user");
        JSONObject office = resutl.optJSONObject("office");
        positionName_tv.setText(job.optString("position"));
        salary_tv.setText(job.optString("remuneration"));
        companyLocation_tv.setText(job.optString("workPlace"));
        jobType_tv.setText(CommonUtil.getJobType(Integer.parseInt(job.optString("jobType"))));
        eduRequire_tv.setText(job.optString("eduRequ"));
        //applyeeCount_tv.setText("已有"+);

        if(CommonUtil.isNotEmpty(user.optString("avata"))){
            ImageLoader.getInstance().displayImage(user.optString("avata"),applyerAvatar_iv, DisplayImageOptionsManager.getCommenOptions());
        }
        applyerName_tv.setText(user.optString("cUser"));
        String post = user.optString("post");
        if(CommonUtil.isNotEmpty(post)){
            applyerPosition_tv.setText(post);
        }

        companyAddress_tv.setText("工作地址："+office.optString("location"));
        companyWebsite_tv.setText("公司主页："+office.optString("webSite"));

    }

  public void collectPositon(){
      JSONObject parameters = new JSONObject();
      try {
          parameters.put("jobId",jobId);
          parameters.put("smId",DApplication.smId);
          parameters.put("mId",DApplication.user.mId);
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
                  CommonUtil.showToast(JobDetailActivity.this,"收藏成功");
              }else{
                  CommonUtil.showToast(JobDetailActivity.this,message);
              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {
              CommonUtil.showToast(JobDetailActivity.this,
                      getString(R.string.no_connect));
          }
      }, Contants.JobFavo,TAG);
  }


}
