package com.deng.dbweiche.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deng.dbweiche.model.dao.ContactTable;
import com.deng.dbweiche.model.dao.InviteTable;

/**
 * Created by 厚贤 on 2017/8/10.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人的表
        db.execSQL(ContactTable.CREATE_TAB);

        //创建邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
