package com.huacheng.huiservers.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OSUtils {
	public static String SYSTEM_HUAWEI = "HUAWEI";
	public static String SYSTEM_XIAOMI = "XIAOMI";
	// MIUI标识
	private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
	private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
	private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
	// EMUI标识
	private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
	private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
	private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

	public static String getSystemBrand() {
		String SYS = null;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
			if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
					|| prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
				SYS = SYSTEM_XIAOMI;// 小米
			} else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null || prop.getProperty(KEY_EMUI_VERSION, null) != null
					|| prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
				SYS = SYSTEM_HUAWEI;// 华为
			} /*else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
				SYS = SYSTEM_MEIZU;//魅族  
				}*/
		} catch (IOException e) {
			e.printStackTrace();
			return SYS;
		}
		return SYS;
	}
}
