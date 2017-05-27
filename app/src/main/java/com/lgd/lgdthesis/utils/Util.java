package com.lgd.lgdthesis.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	public static String[] getlast(String name){
		String[] a=name.split("\\.");
		if(a !=null && a.length==2){
			return a;
		}
		return null;
	}
	
	/** 
	 * 打开文章
	*/  
	public static Intent getWordFileIntent( String param ){      
	      Intent intent = new Intent("android.intent.action.VIEW");       
	      intent.addCategory("android.intent.category.DEFAULT");       
	      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       
	      Uri uri = Uri.fromFile(new File(param ));       
	      intent.setDataAndType(uri, "application/msword");       
	      return intent;       
	} 
	public static String Time(Date s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s1=sdf.format(s);
		return s1;
	}
	public static boolean checkSdCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
