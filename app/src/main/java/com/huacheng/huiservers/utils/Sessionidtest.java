package com.huacheng.huiservers.utils;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.huacheng.huiservers.BaseApplication;
import com.huacheng.huiservers.http.getCookie;

public class Sessionidtest {
	public static Cookie PHPSESSID;
	public Sessionidtest(Context context,String url){

		String phpsessid=CacheUtils.getString(BaseApplication.getApplication(), "PHPSESSID", "");
		getCookie cookie=new getCookie(phpsessid);
		MyCookieStore.cookieStore=null;
		MyCookieStore.cookieStore=cookie.isCookie();
		System.out.println(MyCookieStore.cookieStore);
		List<Cookie> cookies = MyCookieStore.cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie1 = cookies.get(i);
			if (cookie1.getName().equalsIgnoreCase("PHPSESSID")) {
				// 使用一个常量来保存这个cookie，用于做session共享之用
				PHPSESSID = cookie1;
			}
		}
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);

		String cookieString = PHPSESSID.getName()+"="+PHPSESSID.getValue()+";domain="+"m.hui-shenghuo.cn"+ ";path=" + PHPSESSID.getPath();
		System.out.println("ss--------"+cookieString);
		cookieManager.setCookie(url, cookieString);//cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}
}