package com.deng.dbweiche.controlle.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.deng.dbweiche.R;
import com.deng.dbweiche.controlle.fragment.ChatFragment;
import com.deng.dbweiche.controlle.fragment.ContactListFragment;
import com.deng.dbweiche.controlle.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_mian;

    private SettingFragment settingFragment;
    private ContactListFragment contactListFragment;
    private ChatFragment chatfragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        initListener();
    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_mian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            Fragment fragment = null;

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    //会话列表页面
                    case R.id.rb_chat:
                        fragment = chatfragment;
                        break;

                    //联系人列表页面
                    case R.id.rb_contant:
                        fragment = contactListFragment;
                        break;

                    //设置列表页面
                    case R.id.rb_setting:
                        fragment = settingFragment;
                        break;
                }

                // 实现fragment切换的方法
                switchFragment(fragment);
            }
        });

        //默认选择会话列表页面
        rg_mian.check(R.id.rb_chat);
    }

    // 实现fragment切换的方法
    private void switchFragment(Fragment fragment) {
        FragmentManager FragmentManager = getSupportFragmentManager();
        FragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit();
    }

    private void initData() {
        //创建三个fragment对象
        chatfragment = new ChatFragment();

        contactListFragment = new ContactListFragment();

        settingFragment = new SettingFragment();

    }

    private void initView() {
        rg_mian = (RadioGroup) findViewById(R.id.rg_mian);
    }
}
