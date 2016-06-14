package com.szl.zhaozhao2.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.adapter.MessageAdapter;
import com.szl.zhaozhao2.ui.activity.MainActivity;
import com.szl.zhaozhao2.ui.activity.MessageDetailActivity;
import com.szl.zhaozhao2.view.TitlebarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itsdon on 16/5/13.
 */
public class MessageFragment extends BaseFragment {

    public View view;
    private TitlebarView titleBar_message;
    private ListView list_message;
    private TextView tv_empty_message;
    private List<String> data;
    private MessageAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_message,container,false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化listview 的数据
     */
    private void initData() {
        data.clear();
       data.add("端午节放假通知1");
       data.add("端午节放假通知2");
       data.add("端午节放假通知3");
        adapter.updateData();

    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view) {
        titleBar_message = (TitlebarView) view.findViewById(R.id.titleBar_message);
        list_message = (ListView) view.findViewById(R.id.list_message);
        tv_empty_message = (TextView) view.findViewById(R.id.tv_empty_message);
        list_message.setEmptyView(tv_empty_message);
        titleBar_message.setTitleText("通知");
        adapter = new MessageAdapter(getActivity(),data);
        list_message.setAdapter(adapter);
        list_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"title="+data.get(position),Toast.LENGTH_LONG).show();
                Intent intent  = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra("title",data.get(position));
                startActivity(intent);
            }
        });
    }

}
