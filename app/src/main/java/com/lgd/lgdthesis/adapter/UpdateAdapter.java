package com.lgd.lgdthesis.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.FilePathBean;
import com.lgd.lgdthesis.utils.Util;

public class UpdateAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private List<FilePathBean> filelist = new ArrayList<FilePathBean>();
	ProgressDialog dialog;
	public UpdateAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filelist.size();
	}

	@Override
	public FilePathBean getItem(int position) {
		// TODO Auto-generated method stub
		return filelist.get(position);
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_updatefile, parent,false);
			vh = new ViewHolder();
			vh.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			vh.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			vh.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			vh.imageView = (ImageView) convertView.findViewById(R.id.imageView_update);
			vh.imageView2 = (ImageView) convertView.findViewById(R.id.imageView_seak);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		final FilePathBean bean = getItem(position);
		vh.textView1.setText(bean.getName());
		vh.textView2.setText(String.valueOf(bean.getSize()));
		vh.textView3.setText(bean.getTime());
		vh.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = new ProgressDialog(context);
				dialog.setMessage("正在上传");
				dialog.show();
				String path = bean.getPath();
				final BmobFile bmobFile = new BmobFile(new File(path));
				bmobFile.upload(new UploadFileListener() {
					@Override
					public void done(BmobException e) {
						if(e == null){
							String url = bmobFile.getFileUrl();
							Log.d("TAG", "上传成功");
							toSave(new FilePathBean(bean.getName(), bmobFile, url, bean.getSize(), bean.getPath() , bean.getTime()));
						}
					}
				});
			}
		});
		vh.imageView2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String path = bean.getPath();
				Intent intent = Util.getWordFileIntent(path);
	            context.startActivity(intent);  
			}
		});
		return convertView;
	}
	
	public void addAll(List<FilePathBean> list,boolean isClear){
		if(isClear){
			filelist.clear();
		}
		filelist.addAll(list);
		notifyDataSetChanged();
	}
	
	public class ViewHolder{
		TextView textView1;
		TextView textView2;
		TextView textView3;
		ImageView imageView;
		ImageView imageView2;
		
	}
	private void toSave(FilePathBean filePathBean) {
		// TODO Auto-generated method stub
		filePathBean.save(new SaveListener<String>() {
			@Override
			public void done(String s, BmobException e) {
				if(e == null){
					Log.d("TAG", "保存成功");
					dialog.dismiss();
					Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
 
}
