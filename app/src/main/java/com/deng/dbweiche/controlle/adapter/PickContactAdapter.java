package com.deng.dbweiche.controlle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.deng.dbweiche.R;
import com.deng.dbweiche.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 厚贤 on 2017/8/19.
 */
//选择联系人的页面适配器
public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks = new ArrayList<>();
    private List<String> mExistMembers = new ArrayList<>();

    public PickContactAdapter(Context context, List<PickContactInfo> picks,List<String> exisMembers) {
        mContext = context;

        if(picks != null && picks.size() >= 0){
            mPicks.clear();
            mPicks.addAll(picks);
        }

        //加载已存在的群成员集合
        mExistMembers.clear();
        mExistMembers.addAll(exisMembers);
    }

    @Override
    public int getCount() {
        return mPicks == null? 0:mPicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        //创建或获取ViewHolder
        ViewHolder holder = null;
        if(converView == null){
            holder = new ViewHolder();
            converView = View.inflate(mContext, R.layout.item_pick,null);
            holder.cb = converView.findViewById(R.id.cb_pick);
            holder.tv_name = converView.findViewById(R.id.tv_pick_name);

            converView.setTag(holder);
        }else {
            holder = (ViewHolder) converView.getTag();
        }

        // 获取当前item数据
        PickContactInfo pickContactInfo = mPicks.get(position);

        // 显示数据
        holder.tv_name.setText(pickContactInfo.getUser().getName());
        holder.cb.setChecked(pickContactInfo.isChecked());

        //判断
        if(mExistMembers.contains(pickContactInfo.getUser().getHxid())){
            holder.cb.setChecked(true);
            pickContactInfo.setChecked(true);
        }

        return converView;
    }

    //获取选择的联系人
    public List<String> getPickContances() {

        List<String> picks = new ArrayList<>();

        for (PickContactInfo pick : mPicks){

            //判断是否选中
            if(pick.isChecked()){
                picks.add(pick.getUser().getName());
            }
        }


        return  picks;
    }

    private class ViewHolder{
        private CheckBox cb;
        private TextView tv_name;
    }

}
