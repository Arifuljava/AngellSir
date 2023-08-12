package com.grozziie.grozziie_pdf.SqliteHistorySave;;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//继承SQLiteOpenHelper 必须包含三个 构造函数传递，创建 ，更新
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "historybarcodedata.db";
    private static final int DATABASE_VERSION = 1;

    //创建构造方法 用来传递参数
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //    public SQLiteHepler(Context context)
//    {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        SQLPerson.create(database,"barcodeview","viewinformation");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}