package com.szl.zhaozhao2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.adapter.JobAdapter;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.log.LogPrinter;
import com.szl.zhaozhao2.manager.request.RequestManager;
import com.szl.zhaozhao2.model.JobModel;
import com.szl.zhaozhao2.ui.activity.JobDetailActivity;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.util.Contants;
import com.szl.zhaozhao2.util.GsonHelper;
import com.szl.zhaozhao2.view.TitlebarView;
import com.szl.zhaozhao2.view.pulltorefresh.PullToRefreshListView;
import com.szl.zhaozhao2.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itsdon on 16/5/13.
 */
public class JobFragment extends BaseFragment implements OnRefreshListener,View.OnClickListener{

    private static final String TAG = "JobFragment";
    private  View view;
    private TitlebarView titlebarView;
    private PullToRefreshListView ptrListView;
    private ListView listView;
    private JobAdapter adapter;
    private List<JobModel> datas = new ArrayList<>();
    private String jobType = "2"; //工作类型：1实习、2全职，3 兼职,4、项目
    private TextView fullTime_tv;
    private TextView partTime_tv;
    private TextView trainee_tv;
    private TextView project_tv;
    private List<TextView> jobViewList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_job,container,false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            parent.removeView(view);
        }

        initView();
        loadJobList();

        return view;
    }

    public void initView(){
        titlebarView = (TitlebarView) view.findViewById(R.id.titlebar_job);
        titlebarView.setTitleText("职位");
        titlebarView.hiddenRightButton();

        titlebarView.getBackView().setImageResource(R.drawable.icon_search_white);
        titlebarView.showIcon2View();
        titlebarView.getIcon2View().setImageResource(R.drawable.icon_more);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titlebarView.getIcon2View().getLayoutParams();
        params.setMargins(0,0,30,0);

        ptrListView = (PullToRefreshListView) view.findViewById(R.id.ptr_job);
        ptrListView.setRefreshing();
        ptrListView.setOnRefreshListener(this);
        listView = ptrListView.getRefreshableView();
        adapter = new JobAdapter(getActivity(),datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), JobDetailActivity.class);
                intent.putExtra("jobId",datas.get(position-1).getJobId());
                startActivity(intent);
            }
        });

        initJobTypeViewList();

    }

    public void initJobTypeViewList(){
        fullTime_tv = (TextView) view.findViewById(R.id.tv_fullTime);
        partTime_tv = (TextView) view.findViewById(R.id.tv_partTime);
        trainee_tv = (TextView) view.findViewById(R.id.tv_trainee);
        project_tv = (TextView) view.findViewById(R.id.tv_project);

        fullTime_tv.setOnClickListener(this);
        partTime_tv.setOnClickListener(this);
        trainee_tv.setOnClickListener(this);
        project_tv.setOnClickListener(this);

        jobViewList = new ArrayList<>();
        jobViewList.add(trainee_tv);
        jobViewList.add(fullTime_tv);
        jobViewList.add(partTime_tv);
        jobViewList.add(project_tv);
    }

    public void updateJobTypeView(int index){
        for(int i = 0; i < jobViewList.size(); i++){
            jobViewList.get(i).setTextColor( i == index ? 0xff47C1B6 : 0xff333333);
            jobViewList.get(i).setTextSize(i == index ? 16 : 14);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_trainee:
                update(0);
                break;
            case R.id.tv_fullTime:
                update(1);
                break;
            case R.id.tv_partTime:
                update(2);
                break;
            case R.id.tv_project:
                update(3);
                break;
        }
    }
    public void update(int index){
        updateJobTypeView(index);
        jobType = ""+(index+1);
        loadJobList();
    }

    public void loadJobList(){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("key","");
            parameters.put("p","0");
            parameters.put("pS","10");
            parameters.put("oby","t");
            parameters.put("smId",DApplication.smId);
            parameters.put("mId",DApplication.user.mId);
            parameters.put("lng","");
            parameters.put("lat","");
            parameters.put("jobType",jobType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestManager.getInstance().getData(Request.Method.POST, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject meta = jsonObject.optJSONObject("meta");
                String result = meta.optString("s");
                String message = meta.optString("m");
                LogPrinter.v("Don","======"+jsonObject.toString());
                if(result.equals("1")){
                    datas.clear();
                    datas.addAll(GsonHelper.parseList(jsonObject.optJSONObject("data").optJSONArray("d").toString(),JobModel.class));
                    adapter.notifyDataSetChanged();
                }
                ptrListView.onRefreshComplete();
                CommonUtil.showToast(getActivity(),message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtil.showToast(getActivity(),
                        getString(R.string.no_connect));
            }
        }, Contants.JobList,TAG);
    }

    @Override
    public void onRefresh() {

        loadJobList();
    }


}
