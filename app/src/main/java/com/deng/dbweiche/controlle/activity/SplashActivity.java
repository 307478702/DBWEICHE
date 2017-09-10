package com.deng.dbweiche.controlle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.deng.dbweiche.R;
import com.deng.dbweiche.model.Modle;
import com.deng.dbweiche.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

//欢迎页面
public class SplashActivity extends Activity {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果当期activity已经退出，那么我就不处理handler中的消息
            if (isFinishing()){
                return;
            }
            //判断进入主界面还是登陆界面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin() {
        Modle.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断当前账号是否登陆过
                if(EMClient.getInstance().isLoggedInBefore()){//登录过

                    //获取当前用户信息
                    UserInfo account = Modle.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());

                    if(account == null){
                        //跳转到登录页面
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else {
                        //登录成功后的方法
                        Modle.getInstance().loginSuccess(account);

                        //跳转到主页面
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }else {//没登录过
                    //跳转到登录页面
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                //结束当前页面
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //发送2秒钟的延迟消息
        handler.sendMessageDelayed(Message.obtain(),2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}
