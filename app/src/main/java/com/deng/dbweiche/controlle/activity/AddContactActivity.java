package com.deng.dbweiche.controlle.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deng.dbweiche.R;
import com.deng.dbweiche.model.Modle;
import com.deng.dbweiche.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddContactActivity extends Activity {
    private TextView tv_add_find;
    private EditText et_add_name;
    private RelativeLayout rl_add;
    private TextView tv_add_name;
    private Button bt_add_add;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //初始化view
        initView();

        //初始化监听
        initListener();
    }

    private void initListener() {
        tv_add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find();
            }
        });

        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    //查找按钮处理
    private void find() {
        // 获取输入的用户名
        name = et_add_name.getText().toString();

        // 校验输入的用户名
        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"输入的用户名称不能为空",Toast.LENGTH_SHORT).show();
        }
        // 去服务器判断当前用户名是否存在
        Modle.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去服务器判断当前用户名是否存在
                final UserInfo userInfo = new UserInfo(name);

                //更新UI显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    //添加按钮处理
    private void add() {
        //去环信服务器添加好友
        try {
            EMClient.getInstance().contactManager().addContact(name,"添加好友");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddContactActivity.this,"发送添加好友邀请成功",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (final HyphenateException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddContactActivity.this,"发送添加好友邀请失败" + e.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initView() {
        tv_add_find = findViewById(R.id.tv_add_find);
        tv_add_name = findViewById(R.id.tv_add_name);
        et_add_name = findViewById(R.id.et_add_name);
        rl_add = findViewById(R.id.rl_add);
        bt_add_add = findViewById(R.id.bt_add_add);
    }
}
