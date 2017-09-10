package com.deng.dbweiche.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deng.dbweiche.model.bean.UserInfo;
import com.deng.dbweiche.model.db.UserAccountDB;

/**
 * Created by 厚贤 on 2017/8/6.
 */
//用户账号数据库的操作类
public class UserAccountDao {

    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB(context);
    }

    //添加用户账号到数据库
    public void addAccound(UserInfo user){
        //获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行添加操作
        ContentValues Values = new ContentValues();
        Values.put(UserAccountTable.COL_HXID,user.getHxid());
        Values.put(UserAccountTable.COL_NAME,user.getName());
        Values.put(UserAccountTable.COL_NICK,user.getNick());
        Values.put(UserAccountTable.COL_PHOTO,user.getPhoto());

        db.replace(UserAccountTable.TAB_NAME,null,Values);
    }

    //根据环信ID获取所有用户信息
    public UserInfo getAccountByHxId(String hxid){
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + UserAccountTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxid});

        UserInfo userInfo = null;
        if (cursor.moveToNext()){
            userInfo = new UserInfo();

            //封装对象
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }

        //关闭资源
        cursor.close();

        //返回数据
        return userInfo;
    }
}
