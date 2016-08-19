package com.xxw.student.view.search_history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 公司的表
 */
public class RecordSQLiteOpenHelper2 extends SQLiteOpenHelper {

    private static String name = "temp.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper2(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table recordscom(id integer primary key autoincrement,name varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table recordscom(id integer primary key autoincrement,name varchar(200))");
    }


}