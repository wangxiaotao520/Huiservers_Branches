package com.huacheng.huiservers.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import com.huacheng.huiservers.utils.MyCookieStore;

public class getCookie {
	Cookie cookie;
	public getCookie(final String phpsessionid) {
		cookie=new Cookie() {
			
			@Override
			public boolean isSecure() {
				return false;
			}
			
			@Override
			public boolean isPersistent() {
				return false;
			}
			
			@Override
			public boolean isExpired(Date date) {
				return false;
			}
			
			@Override
			public int getVersion() {
				return 0;
			}
			
			@Override
			public String getValue() {
				return phpsessionid;
			}
			
			@Override
			public int[] getPorts() {
				return null;
			}
			
			@Override
			public String getPath() {
				return "/";
			}
			
			@Override
			public String getName() {
				return "PHPSESSID";
			}
			
			@Override
			public Date getExpiryDate() {
				return null;
			}
			
			@Override
			public String getDomain() {
				return "http://m.hui-shenghuo.cn";
			}
			
			@Override
			public String getCommentURL() {
				return null;
			}
			
			@Override
			public String getComment() {
				return null;
			}
		};
	}
	
 public CookieStore isCookie()
	{
	 MyCookieStore.cookieStore=new CookieStore() {
		
		@Override
		public List<Cookie> getCookies() {
			List<Cookie> cookies=new ArrayList<Cookie>();
			cookies.add(cookie);
			return cookies;
		}
		
		@Override
		public boolean clearExpired(Date date) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void clear() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void addCookie(Cookie cookie) {
			// TODO Auto-generated method stub
			
		}
	};
	 
		return  MyCookieStore.cookieStore;
	}
}
