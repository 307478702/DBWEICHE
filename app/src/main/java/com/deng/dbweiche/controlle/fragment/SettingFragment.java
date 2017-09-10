package com.deng.dbweiche.controlle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.deng.dbweiche.R;
import com.deng.dbweiche.controlle.activity.LoginActivity;
import com.deng.dbweiche.model.Modle;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by 厚贤 on 2017/8/7.
 */
//设置页面
public class SettingFragment extends Fragment {
    private Button bt_setting_out;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,null);

        initview(view);
        return view;
    }

    private void initview(View view) {
        bt_setting_out = view.findViewById(R.id.bt_setting_out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        //在Button上显示当前用户名称
        bt_setting_out.setText("退出登录（"+ EMClient.getInstance().getCurrentUser() + ")");

        //退出登录的业务逻辑
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //登录环信服务器退出登录
                Modle.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                // 关闭DBHelper
                                Modle.getInstance().getDbManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新UI显示
                                        Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();

                                        //回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);

                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });

                            }

                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新UI显示
                                        Toast.makeText(getActivity(),"退出失败" + s,Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });
            }
        });
    }
}
