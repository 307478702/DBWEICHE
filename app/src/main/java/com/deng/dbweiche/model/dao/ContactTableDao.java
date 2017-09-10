package com.deng.dbweiche.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deng.dbweiche.model.bean.UserInfo;
import com.deng.dbweiche.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 厚贤 on 2017/8/10.
 */
//联系人表的操作类
public class ContactTableDao  {
    private DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }

    //获取所有联系人
    public List<UserInfo> getContacts(){
        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);

        List<UserInfo> users = new ArrayList<>();

        while (cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

            users.add(userInfo);
        }

        //关闭资源
        cursor.close();

        //返回数据
        return users;
    }

    //通过环信ID获取联系人单个信息
    public UserInfo getContactByHx(String hxId){
        if(hxId == null){
            return null;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
        }

        //关闭资源
        cursor.close();

        //返回数据
        return userInfo;
    }

    //通过环信ID获取用户联系人信息
    public List<UserInfo> getContactByHx(List<String> hxIds){
        if(hxIds == null||hxIds.size()<=0){
            return null;
        }

        List<UserInfo> contacts = new ArrayList<>();

        //遍历hxIds，来查找
        for (String hxId : hxIds){
            UserInfo contact = getContactByHx(hxId);

            contacts.add(contact);
        }

        //返回数据
        return contacts;

    }

    //保存单个联系人
    public void saveContact(UserInfo user,boolean isMyContact){
        if(user == null){
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行保存语句
        ContentValues Values = new ContentValues();
        Values.put(ContactTable.COL_HXID,user.getHxid());
        Values.put(ContactTable.COL_NAME,user.getName());
        Values.put(ContactTable.COL_NICK,user.getNick());
        Values.put(ContactTable.COL_PHOTO,user.getPhoto());
        Values.put(ContactTable.COL_IS_CONTACT,isMyContact ? 1:0);

        db.replace(ContactTable.TAB_NAME,null,Values);

    }

    //保存联系人信息
    public void saveContacts(List<UserInfo>contacts,boolean isMyContact){
        if(contacts == null || contacts.size()<=0){
            return;
        }

        for (UserInfo contant : contacts){
            saveContact(contant,isMyContact);
        }
    }

    //删除联系人信息
    public void deleteContactByHxId(String hxId){
        if(hxId == null){
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行删除语句
        db.delete(ContactTable.TAB_NAME,ContactTable.COL_HXID+"=?",new String[]{hxId});
    }

}
