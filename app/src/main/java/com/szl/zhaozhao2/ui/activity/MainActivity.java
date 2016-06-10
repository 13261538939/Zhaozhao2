package com.szl.zhaozhao2.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.ui.fragment.FriendFragment;
import com.szl.zhaozhao2.ui.fragment.JobFragment;
import com.szl.zhaozhao2.ui.fragment.MessageFragment;
import com.szl.zhaozhao2.ui.fragment.MineFragment;
import com.szl.zhaozhao2.ui.fragment.PracticeFragment;
import com.szl.zhaozhao2.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {

	private static final String TAG = "MainActivity";

	private FragmentTabHost fragmentTabHost;
	private LayoutInflater layoutInflater;
	private Class<?>[] fragmentArray = {JobFragment.class,PracticeFragment.class,MessageFragment.class,FriendFragment.class,MineFragment.class};
    private String[] modelTextArray = {"职位","实习圈","消息","好友","我的"};
	private int[] modelImageArray = {R.drawable.job_normal,R.drawable.practice_normal,R.drawable.message_normal,R.drawable.friend_normal,R.drawable.me_normal};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        //hiuuigibiuu
		initView();
	}

	public void initView(){
		layoutInflater = LayoutInflater.from(this);
		initFragmentTabHost();

	}

	 public void initFragmentTabHost(){
		 fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		 fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.fl_container);
		 int tabCount = fragmentArray.length;
		 for(int i = 0; i < tabCount; i++){
			 // 为每一个Tab按钮设置图标、文字和内容
			 TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(modelTextArray[i])
					 .setIndicator(getTabItemView(i));

			 // 将Tab按钮添加进Tab选项卡中
			 fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
			 fragmentTabHost.setTag(i);

		 }

	 }

	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.fragmenttabhost_tab, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(modelImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setText(modelTextArray[index]);
		return view;
	}
}
