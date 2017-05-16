package com.lgd.lgdthesis.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FileOpenhelper extends SQLiteOpenHelper{

	public static FileOpenhelper fileOpenhelper = null;
	public synchronized static FileOpenhelper getinstance(Context context){
		if(fileOpenhelper == null){
			fileOpenhelper = new FileOpenhelper(context);
		}
		return fileOpenhelper;
	}
	
	public FileOpenhelper(Context context) {
		super(context, "files.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("create table fileinfo ( ");
		sb.append("id integer primary key autoincrement , ");
		sb.append("name varchar(20) , ");
		sb.append("fileurl varchar(60) ,");
		sb.append("path varchar(60) , ");
		sb.append("size varchar(30) , ");
		sb.append("time varchar(30)");
		sb.append(")");
		try {
			db.execSQL(sb.toString());
			Log.d("TAG", "创建数据库表成功");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS fileinfo");
		onCreate(db);
	}

}
