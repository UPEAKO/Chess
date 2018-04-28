package com.example.ubd.chess.music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 构造函数
     */
    MusicDatabaseHelper(Context context, String name, int version){
        super(context,name,null,version);
    }

    /**
     * 创建数据库时调用
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*表生成语句*/
        final String CREATE_TABLE_SQL = "create table musicUrl(id integer primary key,url,musicName)";
        /*此处可以建立自己的数据库的表结构*/
        db.execSQL(CREATE_TABLE_SQL);
    }

    /**
     *更新时调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
