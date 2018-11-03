package com.huacheng.huiservers.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class AppUpdate {
	/**
	 * 从服务器下载apk
	 * @param path
	 * @param pd
	 * @param tipHandler 
	 * @throws Exception
	 */
	public static File getFileFromServer(String appurl, ProgressDialog pd, Handler tipHandler) throws Exception{   
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		System.out.println(Environment.getExternalStorageState());
		System.out.println(Environment.MEDIA_MOUNTED);
		if(true){ 
			URL url = new URL(appurl); 
			HttpURLConnection conn =(HttpURLConnection) url.openConnection(); 
			conn.setConnectTimeout(5000); 
			//获取到文件的大小 
			Message msg = new Message();
			msg.arg1 = conn.getContentLength();
			tipHandler.sendMessage(msg);
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();  
			//		File file = new File(Environment.getExternalStorageDirectory(), "ssss"); 
			File file=new File(Environment.getExternalStorageDirectory(), "huishenghuo.apk");
			FileOutputStream fos = new FileOutputStream(file); 
			BufferedInputStream bis = new BufferedInputStream(is); 
			byte[] buffer = new byte[1024]; 
			int len ; 
			int total=0; 
			while((len =bis.read(buffer))!=-1){ 
				fos.write(buffer, 0, len); 
				total+= len; 
				//获取当前下载量
				pd.setProgress(total); 
			} 
			fos.close(); 
			bis.close(); 
			is.close(); 
			return file; 
		}else{ 
			return null; 
		} 
	}

/**
 * 
 *  获取软件版本名称
 * 
 */
	public static String getVersionName(Context mcontext) {
		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		String VersionName_str = null;
		try {
			VersionName_str = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return VersionName_str;
	}
	/**
	 * 
	 * 获取软件版本号
	 * 
	 */
	public static int getVersionCode(Context mcontext) {
		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		int VersionCode_str = 0;
		try {
			VersionCode_str = mcontext.getPackageManager().getPackageInfo(mcontext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return VersionCode_str;
	}
}
