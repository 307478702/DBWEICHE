package com.deng.dbweiche.controlle.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import com.deng.dbweiche.R;
import com.deng.dbweiche.controlle.adapter.InviteAdapter;
import com.deng.dbweiche.model.Modle;
import com.deng.dbweiche.model.bean.InvationInfo;
import com.deng.dbweiche.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

//邀请信息列表页面
public class InviteActivity extends Activity {
    private ListView lv_invite;
    private InviteAdapter inviteAdapter;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refresh();
        }
    };
    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(final InvationInfo invationInfo) {
            // 通知环信服务器，点击了接受按钮
            Modle.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invationInfo.getUser().getHxid());

                        //数据库更新
                        Modle.getInstance().getDbManager().getInviteTableDao().updateInvitationStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT,invationInfo.getUser().getHxid());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面发生变化
                                Toast.makeText(InviteActivity.this,"接受了邀请",Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {

                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面发生变化
                                Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }

        @Override
        public void onReject(final InvationInfo invationInfo) {
            Modle.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invationInfo.getUser().getHxid());

                        //数据库变化
                        Modle.getInstance().getDbManager().getInviteTableDao().removeInvitation(invationInfo.getUser().getHxid());

                        //页面变化
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝成功了",Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this,"拒绝失败了",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        // 接受邀请按钮
        @Override
        public void onInviteAccept(InvationInfo invationInfo) {
            try {
                //告诉环信服务器接受了邀请
                EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());

                //本地数据库更新
                invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                //内存数据的变化
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"接受邀请成功",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });

            } catch (HyphenateException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });
            }
        }

        // 拒绝邀请按钮
        @Override
        public void onInviteReject(InvationInfo invationInfo) {
            try {
                //告诉环信服务器拒绝了邀请
                EMClient.getInstance().groupManager().declineInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝邀请");

                //本地数据库更新
                invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                //内存数据的变化
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"拒绝邀请",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });

            } catch (HyphenateException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"拒绝邀请失败",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });
            }

        }

        // 接受申请按钮
        @Override
        public void onApplicationAccept(InvationInfo invationInfo) {
            try {
                //告诉环信服务器接受了申请
                EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson());

                //本地数据库更新
                invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                //内存数据的变化
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"接受申请",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });

            } catch (HyphenateException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"接受申请失败",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });
            }
        }

        //拒绝申请按钮
        @Override
        public void onApplicationReject(InvationInfo invationInfo) {
            try {
                //告诉环信服务器接受了申请
                EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvatePerson(),"拒绝申请");

                //本地数据库更新
                invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

                //内存数据的变化
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"拒绝申请",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });

            } catch (HyphenateException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InviteActivity.this,"拒绝申请失败",Toast.LENGTH_SHORT).show();

                        //刷新页面
                        refresh();
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initView();

        initData();
        
    }

    private void initData() {
        //初始化ListView
        inviteAdapter = new InviteAdapter(this,mOnInviteListener);

        lv_invite.setAdapter(inviteAdapter);

        //刷新方法
        refresh();

        //注册邀请信息变化的广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }

    private void refresh() {
        //获取数据库中所以邀请信息
        List<InvationInfo> invitations = Modle.getInstance().getDbManager().getInviteTableDao().getInvitations();

        //刷新适配器
        inviteAdapter.refresh(invitations);
    }

    private void initView() {
        lv_invite = findViewById(R.id.lv_invite);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(InviteChangedReceiver);
    }
}