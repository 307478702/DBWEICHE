package com.deng.dbweiche.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deng.dbweiche.model.dao.UserAccountTable;

/**
 * Created by 厚贤 on 2017/8/6.
 */

public class UserAccountDB extends SQLiteOpenHelper{
    //构造
    public UserAccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    //数据库创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表语句
        db.execSQL(UserAccountTable.CREATE_TABLE);
    }

    //数据库更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
