package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    private static final String name = "database1.db";
    private static final int version =6;

    DataBase(Context context)
    {
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table charge(name text primary key,type text,pay text,price integer not null,months integer not null,days integer not null)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("drop table if exists charge");
        onCreate(db);
    }
}
