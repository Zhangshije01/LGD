package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.FilePathBean;
import com.lgd.lgdthesis.utils.DownloadManager;
import com.lgd.lgdthesis.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends BaseAdapter {

    List<FilePathBean> list = new ArrayList<FilePathBean>();
    Context context;
    LayoutInflater inflater;
    FilePathBean bean = new FilePathBean();

    public DownloadAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public FilePathBean getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select, parent, false);
            vh = new ViewHolder();
            vh.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            vh.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            vh.imageView = (ImageView) convertView.findViewById(R.id.imageView_download);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final FilePathBean file = getItem(position);
        vh.textView1.setText(file.getName());
        if (!TextUtils.isEmpty(file.getSize())) {
            vh.textView2.setText(file.getSize());
        }
        vh.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("TAG", "url" + file.getFileUrl());
                LGDApplication.getInstance().setFilepath(file.getPath());
                DownloadManager.downloadSong(context, file.getFileUrl(), Util.getlast(file.getName())[0] + "bmob." + Util.getlast(file.getName())[1]);
//                LGDApplication.getInstance().setFirst(true);
            }
        });
        return convertView;
    }

    public void addAll(List<FilePathBean> list, boolean isClear) {
        if (isClear) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageView imageView;
    }

}
