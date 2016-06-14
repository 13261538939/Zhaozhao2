package com.szl.zhaozhao2.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.adapter.PracticeAdapter;
import com.szl.zhaozhao2.ui.activity.MainActivity;
import com.szl.zhaozhao2.view.TitlebarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itsdon on 16/5/13.
 */
public class PracticeFragment extends BaseFragment {

    public View view;
    private TitlebarView titleBar_practice;
    private ImageView bg_practice;
    private ImageView iv_head_practice;
    private ListView list_practice;
    private TextView tv_empty;
    private Context context;
    private List<String> data;
    private PracticeAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MainActivity)getActivity();
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_practice,container,false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        data.clear();
        data.add("小花");
        data.add("小红");
        data.add("小丽");
        data.add("小雨");
        adapter.updateData();
    }

    private void initView(View view) {
        titleBar_practice = (TitlebarView) view.findViewById(R.id.titlebar_practice);
        bg_practice = (ImageView) view.findViewById(R.id.iv_bg_practice);
        iv_head_practice = (ImageView) view.findViewById(R.id.iv_head_practice);
        list_practice = (ListView) view.findViewById(R.id.list_practice);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty_practice);
        list_practice.setEmptyView(tv_empty);
        titleBar_practice.setTitleText(R.string.title_practice);
        titleBar_practice.getIcon2View().setImageResource(R.drawable.icon_takephoto);
        titleBar_practice.showIcon2View();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleBar_practice.getIcon2View().getLayoutParams();
        params.setMargins(0,0,30,0);
        adapter = new PracticeAdapter(context,data);
        list_practice.setAdapter(adapter);
    }

}
