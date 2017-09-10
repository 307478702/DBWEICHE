package com.deng.dbweiche.model;

import android.content.Context;

import com.deng.dbweiche.model.bean.UserInfo;
import com.deng.dbweiche.model.dao.UserAccountDao;
import com.deng.dbweiche.model.db.DBManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 厚贤 on 2017/8/5.
 */
//数据模型层全局类
public class Modle {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();
    private DBManager dbManager;

    //创建对象
    private static Modle model = new Modle();
    private static UserAccountDao userAccountDao;

    //私有化构造
    private Modle(){

    }

    //获取单例对象
    public static Modle getInstance(){

        return model;
    }

    //初始化的方法
    public void init(Context context){
        mContext = context;
        
        //创建用户数据库的操作对象类
        userAccountDao = new UserAccountDao(mContext);

        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }

    //获取全局线程池对象
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    //用户成功后的处理方法
    public void loginSuccess(UserInfo account) {

        //校验
        if(account == null){
            return;
        }

        if(dbManager != null){
            dbManager.close();
        }

        dbManager = new DBManager(mContext,account.getName());
    }

    public DBManager getDbManager(){
        return dbManager;
    }

    //获取用户账号数据库的操作对象类
    public UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }

}
