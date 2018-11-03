package com.huacheng.huiservers.utils;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

	public static final String CACHE_FILE_NAME = "hh";
	private static SharedPreferences mSharedPreferences;

	public static void putBoolean(Context context, String key, boolean value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_PRIVATE);
		}
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static void putString(Context context, String key, String value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_APPEND);
		}
		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_APPEND);
		}
		return mSharedPreferences.getString(key, defValue);
	}

	/**
	 * 保存list
	 * @param context
	 * @param key
	 * @param sKey
	 * @return
	 */
	public static boolean saveArray(Context context, String key,
			List<String> sKey) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_PRIVATE);
		}
		mSharedPreferences.edit().putInt(key, sKey.size()); /* sKey is an array */

		for (int i = 0; i < sKey.size(); i++) {
			mSharedPreferences.edit().remove(key+"_" + i);
			mSharedPreferences.edit().putString(key+"_"+i, sKey.get(i));
		}
		return mSharedPreferences.edit().commit();
	}
	/**
	 * 获取list
	 * @param context
	 * @param key
	 * @return
	 */
    @SuppressWarnings("null")
	public static List<String> loadArray(Context context,String key) {    
    	if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(CACHE_FILE_NAME,
					Context.MODE_PRIVATE);
		}
        List<String> list = null;
        int size = mSharedPreferences.getInt(key, 0);    
      
        for(int i=0;i<size;i++) {  
            list.add(mSharedPreferences.getString(key+"_"+i, null));    
      
        }  
        return list;
    }  
}
