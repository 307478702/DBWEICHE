package com.deng.dbweiche.controlle.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.deng.dbweiche.R;
import com.deng.dbweiche.controlle.adapter.PickContactAdapter;
import com.deng.dbweiche.model.Modle;
import com.deng.dbweiche.model.bean.PickContactInfo;
import com.deng.dbweiche.model.bean.UserInfo;
import com.deng.dbweiche.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

//选择联系人页面
public class PickContactActivity extends Activity {
    private TextView tv_pick_save;
    private ListView lv_pick;
    private List<PickContactInfo> mPicks;
    private PickContactAdapter pickContactAdapter;
    private List<String> mExisMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        
        initView();

        //获取传递过来的数据
        getData();

        initData();

        initListener();
    }

    private void getData() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);

        if(groupId != null){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);

            //获取群中已经存在的所有群成员
            mExisMembers = group.getMembers();
        }

        if(mExisMembers == null){
            mExisMembers = new ArrayList<>();
        }
    }

    private void initListener() {
        lv_pick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //checkbox的切换
                CheckBox cb_pick = view.findViewById(R.id.cb_pick);
                cb_pick.setChecked(!cb_pick.isChecked());

                //修改数据
                PickContactInfo pickContactInfo = mPicks.get(position);
                pickContactInfo.setChecked(cb_pick.isChecked());

                //刷新页面
                pickContactAdapter.notifyDataSetChanged();
            }
        });

        //保存按钮的点击事件
        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到已选择的联系人
                List<String> names = pickContactAdapter.getPickContances();

                //给启动页面返回数据
                Intent intent = new Intent();

                intent.putExtra("members",names.toArray(new String[0]));

                //设置返回的结果码
                setResult(RESULT_OK,intent);

                //结束当前页面
                finish();
            }
        });
    }

    private void initData() {
        //从本地数据库中获取所有的联系人信息
        List<UserInfo> contacts = Modle.getInstance().getDbManager().getContactTableDao().getContacts();

        mPicks = new ArrayList<>();

        if(contacts != null && contacts.size() >= 0){
            //转换
            for(UserInfo contact : contacts){
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                mPicks.add(pickContactInfo);
            }
        }

        //初始化listview
        pickContactAdapter = new PickContactAdapter(this,mPicks,mExisMembers);

        lv_pick.setAdapter(pickContactAdapter);
    }

    private void initView() {
        tv_pick_save = findViewById(R.id.tv_pick_save);
        lv_pick = findViewById(R.id.lv_pick);
    }

}
