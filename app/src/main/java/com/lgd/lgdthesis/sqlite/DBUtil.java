package com.lgd.lgdthesis.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lgd.lgdthesis.bean.FilePathBean;

public class DBUtil {
	public FileOpenhelper fileOpenhelper;
	public DBUtil(Context context) {
		// TODO Auto-generated constructor stub
		if (fileOpenhelper == null) {
			fileOpenhelper = new FileOpenhelper(context);
		}
	}
	public void insertFile(FilePathBean bean){
		SQLiteDatabase db = fileOpenhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("name",bean.getName());
		contentValues.put("fileurl", bean.getFileUrl());
		contentValues.put("path", bean.getPath());
		contentValues.put("size", bean.getSize());
		contentValues.put("time", bean.getTime());
		db.insert("fileinfo", null, contentValues);
		Log.d("TAG", "插入成功");
	}
	public void select(){
		SQLiteDatabase database = fileOpenhelper.getWritableDatabase();
		
		Cursor cursor = database.rawQuery("select * from fileinfo", null);
		FilePathBean bean = new FilePathBean();
		while(cursor.moveToNext()){
			bean.setName(cursor.getString(cursor.getColumnIndex("name")));
			bean.setFileUrl(cursor.getString(cursor.getColumnIndex("fileurl")));
			bean.setPath(cursor.getString(cursor.getColumnIndex("path")));
			bean.setSize(cursor.getString(cursor.getColumnIndex("size")));
			bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
		}
		Log.d("TAG", bean.toString());
		cursor.close();
	}

}
