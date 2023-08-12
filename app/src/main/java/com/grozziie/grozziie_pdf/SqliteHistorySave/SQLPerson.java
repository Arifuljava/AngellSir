package com.grozziie.grozziie_pdf.SqliteHistorySave;;

import android.database.sqlite.SQLiteDatabase;

public class SQLPerson {
    public int _id;
    public String _name;
    public String _info;
    public static void create(SQLiteDatabase database,String TableName,String TableName2)//创建数据库表
    {
        //"create table user(name varchar(20))"
        String createTable="create table if not exists "+TableName+"(_id integer primary key autoincrement,_barcodenumber varchar(50) not null,_barcodename varchar(50) not null,_barcodetime varchar(50) not null,_barcodeinformation varchar(100) not null,_barcodeimage BLOB,_barcodebackgroundimage BLOB)";
        database.execSQL(createTable);
        String createTable2="create table if not exists "+TableName2+"(_ids integer primary key autoincrement,_barcodenumbers varchar(50) not null,_viewflag varchar(50) not null,_viewinformation varchar(100) not null,_viewtext varchar(100) not null,_viewimage BLOB)";
        database.execSQL(createTable2);

    }
    public static void drop(SQLiteDatabase database,String TableName)//删除数据库表
    {
        String dropTable="drop table if exists "+TableName;
        database.execSQL(dropTable);
    }
}
