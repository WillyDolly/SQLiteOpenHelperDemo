package com.popland.pop.sqliteopenhelperdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Blob;

/**
 * Created by hai on 21/05/2017.
 */
public class MySQlite extends SQLiteOpenHelper{

    MySQlite(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    void queryData(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    Cursor getData(String sql){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sql,null);
    }
//why not use queryData: 1. to Insert Blob   2. safe data input
    void Insert_Data(String id,String keyword,byte[] pic){
        SQLiteDatabase db = getWritableDatabase();
        String insert_command = "INSERT INTO NuIm VALUES(null,?,?,?)";
        SQLiteStatement statement = db.compileStatement(insert_command);
        statement.clearBindings();

        statement.bindString(1,id);
        statement.bindString(2,keyword);
        statement.bindBlob(3,pic);
        statement.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
