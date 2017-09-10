package com.deng.dbweiche.controlle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deng.dbweiche.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 厚贤 on 2017/8/17.
 */
//群组列表的适配器
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mGroups = new ArrayList<>();

    public GroupListAdapter(Context context) {
        mContext = context;
    }

    //刷新方法
    public void refresh(List<EMGroup> groups){

        if(groups != null && groups.size() >= 0){
            mGroups.clear();

            mGroups.addAll(groups);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mGroups == null ? 0:mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        // 创建或获取viewholder
        ViewHolder holder = null;
        if(converView == null){
            holder = new ViewHolder();

            converView = View.inflate(mContext, R.layout.item_grouplist,null);

            holder.name = converView.findViewById(R.id.tv_grouplist_name);

            converView.setTag(holder);
        }else {
            holder = (ViewHolder) converView.getTag();
        }

        // 获取当前item数据
        EMGroup emGroup = mGroups.get(position);

        // 显示数据
        holder.name.setText(emGroup.getGroupName());

        // 返回数据
        return converView;
    }

    private class ViewHolder{
        TextView name;
    }
}
