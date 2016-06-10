package com.szl.zhaozhao2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.view.RoundImageView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by itsdon on 16/6/2.
 */
public class ResumeListAdapter extends BaseAdapter {

    private Context context;

    public ResumeListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_position,null);
            if(holder == null){
                holder = new ViewHolder();
            }
            holder.avatar_iv = (RoundImageView) convertView.findViewById(R.id.iv_applyeeAvatar);
            holder.applyeeName_tv = (TextView) convertView.findViewById(R.id.tv_applyeeName);
            holder.checkResume_tv = (TextView) convertView.findViewById(R.id.tv_checkResume);
            holder.applyDate_tv = (TextView) convertView.findViewById(R.id.tv_applyDate);
            holder.collect_tv = (TextView) convertView.findViewById(R.id.tv_collectResume);
            holder.delete_tv = (TextView) convertView.findViewById(R.id.tv_deleteResume);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }

    class ViewHolder{
        private RoundImageView avatar_iv;
        private TextView applyeeName_tv;
        private TextView checkResume_tv;
        private TextView applyDate_tv;
        private TextView collect_tv;
        private TextView delete_tv;
    }
}
