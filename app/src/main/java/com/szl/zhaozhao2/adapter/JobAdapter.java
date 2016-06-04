package com.szl.zhaozhao2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.manager.imageloader.DisplayImageOptionsManager;
import com.szl.zhaozhao2.model.JobModel;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.view.RoundImageView;

import java.util.List;

/**
 * Created by itsdon on 16/6/2.
 */
public class JobAdapter extends BaseAdapter {

    private Context context;
    private List<JobModel> datas;

    public JobAdapter(Context context,List<JobModel> datas){
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_job,null);
            if(holder == null){
                holder = new ViewHolder();
            }
            holder.positionName_tv = (TextView) convertView.findViewById(R.id.tv_positionName);
            holder.traineeTag_iv = (ImageView) convertView.findViewWithTag(R.id.iv_traineeTag);
            holder.salary_tv = (TextView) convertView.findViewById(R.id.tv_salary);
            holder.companyLocation_tv = (TextView) convertView.findViewById(R.id.tv_companyLocation);
            holder.jobType_tv = (TextView) convertView.findViewById(R.id.tv_jobType);
            holder.eduRequire_tv = (TextView) convertView.findViewById(R.id.tv_eduRequire);
            holder.companyLogo_iv = (RoundImageView) convertView.findViewById(R.id.iv_companyLogo);
            holder.applyerPosition_tv = (TextView) convertView.findViewById(R.id.tv_applyerPosition);
            holder.applyerCompany_tv = (TextView) convertView.findViewById(R.id.tv_applyerCompany);
            holder.companyProperty_tv = (TextView) convertView.findViewById(R.id.tv_companyProperty);
            holder.companyScale_tv = (TextView) convertView.findViewById(R.id.tv_companyScale);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

         JobModel item = datas.get(position);
        if(CommonUtil.isNotEmpty(item)){
            holder.positionName_tv.setText(item.getPosition());
            holder.salary_tv.setText(item.getRemuneration());
            holder.companyLocation_tv.setText(item.getWorkPlace());
            holder.eduRequire_tv.setText(item.getEduRequ());
            holder.applyerPosition_tv.setText(item.getcUser());
            holder.applyerCompany_tv.setText(item.getOfficeName());
//            holder.companyProperty_tv.setText("");
//             holder.companyScale_tv.setText("");
//              holder.traineeTag_iv.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage("",holder.companyLogo_iv, DisplayImageOptionsManager.getCommenOptions());
            String jobType = ""; //1实习、2全职，3 兼职,4、项目
            switch(Integer.parseInt(item.getJobType())){
                case 1:
                    jobType = "实习";
                    break;
                case 2:
                    jobType = "全职";
                    break;
                case 3:
                    jobType = "兼职";
                    break;
                case 4:
                    jobType = "项目";
                    break;
            }
            holder.jobType_tv.setText(jobType);

        }


        return convertView;
    }

    class ViewHolder{
      private TextView positionName_tv;
        private ImageView traineeTag_iv;
        private TextView salary_tv;
        private TextView companyLocation_tv;
        private TextView jobType_tv;
        private TextView eduRequire_tv;
        private RoundImageView companyLogo_iv;
        private TextView applyerPosition_tv;
        private TextView applyerCompany_tv;
        private TextView companyProperty_tv;
        private TextView companyScale_tv;


    }
}
