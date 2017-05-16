package com.lgd.lgdthesis.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.lgd.lgdthesis.app.LGDApplication;

@SuppressLint("NewApi") public class DownloadManager {

		public static void sendNotification(Context context, String ticker, String title, String content) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification.Builder builder = new Notification.Builder(context);
			builder.setTicker(ticker);
			builder.setContentTitle(title);
			builder.setContentText(content);
			builder.setSmallIcon(android.R.drawable.dialog_holo_light_frame);
			
			
			Intent apkIntent = new Intent();
			apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			apkIntent.setAction(Intent.ACTION_VIEW);
			String apk_path = LGDApplication.getInstance().getFilepath();
			Uri uri = Uri.fromFile(new File(apk_path));
			apkIntent.setDataAndType(uri, "application/msword");
			// context.startActivity(intent);
			PendingIntent contextIntent = PendingIntent.getActivity(context, 0,apkIntent, 0);
			builder.setContentIntent(contextIntent);
			
			Notification notification = builder.build();
			notification.defaults = Notification.DEFAULT_VIBRATE;
			manager.notify(100, notification);
			
		}
		

		public static void downloadSong(final Context context, String url, String name) {
			String downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
					.getAbsolutePath();
			final File file = new File("/mnt/sdcard", name);

			if(file.exists()){
				file.delete();
			}
			
			
			new AsyncTask<String, Void, File>() {
				protected void onPreExecute() {
					sendNotification(context, "׼正在", "׼下载", "׼请稍等......");
				};

				@Override
				protected File doInBackground(String... params) {
					String path = params[0];

					try {
						URL url = new URL(path);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setConnectTimeout(5000);

						connection.setDoInput(true);
						int responsecode = connection.getResponseCode();
						if (responsecode == 200) {
							InputStream is = connection.getInputStream();
							BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
							byte[] buffer = new byte[1024];

							OutputStream os = new FileOutputStream(file);
							BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);

							int downloadcount = 0;//
							int len = 0;//
							int contentLength = connection.getContentLength();
							Log.d("TAG", "contentLength"+contentLength);
							while ((len = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
								downloadcount += len;
								sendNotification(context, "正在", "下载", (downloadcount * 100 / contentLength) + "%");
								bufferedOutputStream.write(buffer);
							}
							ToastUtils.show("下载完成");
							bufferedOutputStream.flush();
							bufferedOutputStream.close();
							bufferedInputStream.close();

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return file;
				}
				protected void onPostExecute(File result) {
					sendNotification(context, "文章", result.getName(), "已下载完成");
				};
			}.execute(url);
		}

}
