package com.lgd.lgdthesis.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.DownloadAdapter;
import com.lgd.lgdthesis.bean.FilePathBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SeakActivity extends AppCompatActivity {
    ListView listView;
    List<FilePathBean> list = new ArrayList<FilePathBean>();
    FilePathBean bean = new FilePathBean();
    DownloadAdapter adapter;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seak);
        listView = (ListView) findViewById(R.id.listView_down);
        adapter = new DownloadAdapter(SeakActivity.this);
        getWord();
    }
    private void getWord() {
        // TODO Auto-generated method stub
        BmobQuery<FilePathBean> query = new BmobQuery<FilePathBean>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在搜索");
        dialog.show();
        query.findObjects(new FindListener<FilePathBean>() {
            @Override
            public void done(List<FilePathBean> list, BmobException e) {
                if(e == null){
                    Log.d("TAG", "查找完成");
                    adapter.addAll(list, true);
                    listView.setAdapter(adapter);
                    dialog.dismiss();
                }
            }
        });
    }
}
