package com.huacheng.huiservers.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.XToast;

import java.io.File;
import java.net.URLEncoder;

public class UpdateService extends Service{
	public static final int APP_VERSION_LATEST = 0;
	public static final int APP_VERSION_OLDER = 1;
	public int titleId;
	private NotificationManager mNotificationManager = null;
	private Notification mNotification = null;
	private PendingIntent mPendingIntent = null;
	private File destDir = null;
	private File destFile = null;
	private String url;
	private static final int DOWNLOAD_FAIL = -1;
	private static final int DOWNLOAD_SUCCESS = 0;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				XToast.makeText(UpdateService.this,"成功", XToast.LENGTH_SHORT).show();
				install(destFile);
				break;
			case DOWNLOAD_FAIL:
				XToast.makeText(UpdateService.this, "失败", XToast.LENGTH_SHORT).show();
				mNotificationManager.cancel(titleId);
				destFile.delete();
				break;
			default:
				break;
			}
		}
	};
	private DownloadUtils.DownloadListener downloadListener = new DownloadUtils.DownloadListener() {
		@Override
		public void downloading(int progress,String DownloadSpeed,int lasttime,String fileTotalSize) {
			if (progress%10==0) {
				mNotification.contentView.setTextViewText(R.id.tv_sizes, DownloadSpeed+"/s");
				//将秒转换成分钟
				mNotification.contentView.setTextViewText(R.id.tv_time,"剩余时间："+StringUtils.changeTime(lasttime));
				mNotification.contentView.setTextViewText(R.id.fileSize,fileTotalSize);
				mNotification.contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
				mNotificationManager.notify(titleId, mNotification);
			}
		}
		@Override
		public void downloaded() {
			mNotification.contentView.setViewVisibility(R.id.tv_sizes, View.GONE);
			mNotification.defaults = Notification.DEFAULT_SOUND;
			mNotification.contentIntent = mPendingIntent;
			mNotification.contentView.setTextViewText(R.id.notificationTitle, "下载完成。");
			mNotificationManager.notify(titleId, mNotification);
			if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
				Message msg = mHandler.obtainMessage();
				msg.what = DOWNLOAD_SUCCESS;
				mHandler.sendMessage(msg);
			}
			mNotificationManager.cancel(titleId);
		}
	};
	private String version;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 获取传值
		titleId = intent.getIntExtra("titleId", 0);
		url=intent.getStringExtra("url");
		version = intent.getStringExtra("version");
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/sdcard/crash/");
			if (destDir.exists()) {
				File destFile = new File(destDir.getPath() + "/" + URLEncoder.encode(url));
				if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
					install(destFile);
					stopSelf();
					return super.onStartCommand(intent, flags, startId);
				}
			}
		}else {
			return super.onStartCommand(intent, flags, startId);
		}
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.notification_item);
		Intent completingIntent = new Intent();
		completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		completingIntent.setClass(getApplication().getApplicationContext(), UpdateService.class);
		mPendingIntent = PendingIntent.getActivity(UpdateService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.icon = R.drawable.logo;
		mNotification.contentIntent = mPendingIntent;
		mNotification.contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		mNotification.contentView.setTextViewText(R.id.tv_sizes, "0k/s");
		mNotification.contentView.setTextViewText(R.id.tv_time, "剩余时间：");
		mNotification.contentView.setTextViewText(R.id.notificationTitle,"慧生活_"+version);
		mNotificationManager.cancel(titleId);
		mNotificationManager.notify(titleId, mNotification);
		new AppUpgradeThread().start();
		return super.onStartCommand(intent, flags, startId);
	}
	class AppUpgradeThread extends Thread{
		@Override
		public void run() {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				if (destDir == null) {
					destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/sdcard/crash/");
				}
				if (destDir.exists() || destDir.mkdirs()) {
					PackageManager manager = getPackageManager();
					          PackageInfo info = null;
							try {
								info = manager.getPackageInfo(getPackageName(), 0);
							} catch (NameNotFoundException e1) {
								e1.printStackTrace();
							}
					destFile = new File(destDir.getPath() + "/慧生活"+version+".apk");
					if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
						destFile.delete();
						//	install(destFile);
						try { 
							DownloadUtils.download(url, destFile, false, downloadListener);
						} catch (Exception e) {
							Message msg = mHandler.obtainMessage();
							msg.what = DOWNLOAD_FAIL;
							mHandler.sendMessage(msg);
							e.printStackTrace();
						}
					} else {
						try { 
							DownloadUtils.download(url, destFile, false, downloadListener);
						} catch (Exception e) {
							Message msg = mHandler.obtainMessage();
							msg.what = DOWNLOAD_FAIL;
							mHandler.sendMessage(msg);
							e.printStackTrace();
						}
					}
				}
			}
			stopSelf();
		}
	}
	public boolean checkApkFile(String apkFilePath) {
		boolean result = false;
		try{
			PackageManager pManager = getPackageManager();
			PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
			if (pInfo == null) {
				result = false;
			} else {
				result = true;
			}
		} catch(Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
	public void install(File apkFile){
		if(apkFile.getName().endsWith(".apk")){
			Intent install = new Intent();
			install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			install.setAction(Intent.ACTION_VIEW);
			install.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
			startActivity(install);
			
		}
	}
}

