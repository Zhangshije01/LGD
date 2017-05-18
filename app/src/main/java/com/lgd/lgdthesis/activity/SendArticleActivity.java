package com.lgd.lgdthesis.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.UpdateAdapter;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.FilePathBean;
import com.lgd.lgdthesis.sqlite.DBUtil;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SendArticleActivity extends AppCompatActivity {

    ListView listview;
    List<HashMap<String, Object>> listAll;
    List<String> listname;
    String url;
    Handler handler;
    boolean isFirst;
    ProgressDialog dialog;

    List<FilePathBean> listbean;
    ArrayList<File> list = new ArrayList<File>();
    DBUtil dbUtil = new DBUtil(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_article);
        listview = (ListView) findViewById(R.id.listview_send_arcticle);
        getAllFile();
        seak();
    }

    private void toSave(FilePathBean filePathBean) {
        // TODO Auto-generated method stub
        filePathBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.d("成功");
                }
            }
        });
    }

    private void getAllFile() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        UpdateAdapter adapter = new UpdateAdapter(SendArticleActivity.this);
                        adapter.addAll((List<FilePathBean>) msg.obj, true);
                        listview.setAdapter(adapter);
                        break;
                    case 2:
                        UpdateAdapter adapter2 = new UpdateAdapter(SendArticleActivity.this);
                        adapter2.addAll((List<FilePathBean>) msg.obj, true);
                        listview.setAdapter(adapter2);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private ArrayList<File> toListFile(File[] listFiles) {
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) {
                toListFile(listFiles[i].listFiles());
            }
            if (listFiles[i].isFile()) {
                if (listFiles[i].toString().endsWith("doc") || listFiles[i].toString().endsWith(".docx")) {
                    list.add(listFiles[i]);
                }
            }
        }
        return list;
    }

    //    public void onSelect(View view){
//        Intent intent=new Intent(this,SelectActivity.class);
//        startActivity(intent);
//    }
    public void seak() {
        isFirst = LGDApplication.getInstance().isFirst();
        Log.d("TAG", "isfirst" + isFirst);
        dialog = new ProgressDialog(SendArticleActivity.this);
        dialog.setMessage("正在全盘搜索");
        dialog.show();
        listbean = new ArrayList<FilePathBean>();
        if (isFirst) {
            Log.d("TAG", "time" + Util.Time(new Date()));
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String path = "/storage/emulated/0/";
                    File file = new File(path);
                    ArrayList<File> listfile = new ArrayList<File>();
//					listAll = new ArrayList<HashMap<String,Object>>();

                    FilePathBean filePathBean;
                    listfile = toListFile(file.listFiles());
                    for (File file2 : listfile) {
//						HashMap<String, Object> map = new HashMap<String, Object>();
//						map.put("name", file2.getName());
//						map.put("size", file2.length());
//						map.put("path", file2.getAbsolutePath());
//						listAll.add(map);
//
                        filePathBean = new FilePathBean();

                        filePathBean.setName(file2.getName());
                        filePathBean.setSize(formateFileSize(file2.length()));
                        filePathBean.setPath(file2.getAbsolutePath());
                        filePathBean.setTime(Util.Time(new Date(file2.lastModified())));

                        dbUtil.insertFile(filePathBean);
                        listbean.add(filePathBean);

                    }

//					Collections.sort(listbean, new Comparator<FilePathBean>() {
//						@Override
//						public int compare(FilePathBean lhs, FilePathBean rhs) {
//							// TODO Auto-generated method stub
//							return -1*lhs.getTime().compareTo(rhs.getTime());
//						}
//					});

                    dialog.dismiss();
                    Log.d("TAG", "time2" + Util.Time(new Date()));
                    Message message = new Message();
                    message.what = 1;
                    message.obj = listbean;
                    handler.sendMessage(message);

                    SharedPreferences.Editor editor = getSharedPreferences("wordname", MODE_PRIVATE).edit();
                    editor.putInt("num", listbean.size());
                    for (int i = 0; i < listbean.size(); i++) {
                        editor.putString("name" + i, listbean.get(i).getName());
                        editor.putString("size" + i, listbean.get(i).getSize());
                        editor.putString("path" + i, listbean.get(i).getPath());
                        editor.putString("time" + i, listbean.get(i).getTime());
                    }
                    editor.commit();
                    LGDApplication.getInstance().setFirst(false);
                }
            });
            t.start();
        } else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
//					List<FilePathBean> listbean= new ArrayList<FilePathBean>();
                    FilePathBean filePathBean;
                    SharedPreferences preferences = getSharedPreferences("wordname", MODE_PRIVATE);
                    int snum = preferences.getInt("num", 0);

//					FilePathBean filePathBean;

                    for (int i = 0; i < snum; i++) {
                        String name = preferences.getString("name" + i, null);
                        String size = preferences.getString("size" + i, null);
                        String path = preferences.getString("path" + i, null);
                        String time = preferences.getString("time" + i, null);
                        LogUtils.d("name"+name);
                        filePathBean = new FilePathBean();
                        filePathBean.setName(name);
                        filePathBean.setSize(size);
                        filePathBean.setPath(path);
                        filePathBean.setTime(time);

                        listbean.add(filePathBean);
                    }
                    dialog.dismiss();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = listbean;
                    handler.sendMessage(message);
                }
            });
            thread.start();
        }

    }
    private String formateFileSize(long size){
        return Formatter.formatFileSize(SendArticleActivity.this, size);
    }
}
