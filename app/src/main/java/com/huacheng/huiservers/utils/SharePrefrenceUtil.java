package com.huacheng.huiservers.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefrenceUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharePrefrenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("hui", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //登录类型
    public String getLoginType() {
        return sharedPreferences.getString("loginType", "0");
    }

    public void setLoginType(String loginType) {
        editor.putString("loginType", loginType);
        editor.commit();
    }


    //cookie
    public String getCookie() {
        return sharedPreferences.getString("cookie", "");
    }

    public void setCookie(String cookie) {
        editor.putString("cookie", cookie);
        editor.commit();
    }

    public SharePrefrenceUtil() {
        sharedPreferences = context.getSharedPreferences("hui", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    //首页选择的小区id
    public String getXiaoQuId() {
        return sharedPreferences.getString("xiaoqu_id", "1");
    }

    public void setXiaoQuId(String xiaoqu_id) {
        editor.putString("xiaoqu_id", xiaoqu_id);
        editor.commit();
    }

    //首页选择的小区id
    public String getXiaoQuIdF() {
        return sharedPreferences.getString("xiaoqu_id_f", "1");
    }

    public void setXiaoQuIdF(String xiaoqu_id_f) {
        editor.putString("xiaoqu_id_f", xiaoqu_id_f);
        editor.commit();
    }

    //首页选择的小区名字
    public String getXiaoQuNanme() {
        return sharedPreferences.getString("xiaoqu_name", "迎宾合作社区");
    }

    public void setXiaoQuName(String xiaoqu_name) {
        editor.putString("xiaoqu_name", xiaoqu_name);
        editor.commit();
    }

    //免密accessToken
    public String getAcceessToken() {
        return sharedPreferences.getString("accessToken", "");
    }

    public void setAcceessToken(String accessToken) {
        editor.putString("accessToken", accessToken);
        editor.commit();
    }

    //免密refreshToken
    public String getRefreshToken() {
        return sharedPreferences.getString("refreshToken", "");
    }

    public void setRefreshToken(String refreshToken) {
        editor.putString("refreshToken", refreshToken);
        editor.commit();
    }

    //免密openId
    public String getOpenId() {
        return sharedPreferences.getString("openId", "");
    }

    public void setOpenId(String openId) {
        editor.putString("openId", openId);
        editor.commit();
    }

    //免密timeStamp
    public String getTimeStamp() {
        return sharedPreferences.getString("timeStamp", "");
    }

    public void setTimeStamp(String timeStamp) {
        editor.putString("timeStamp", timeStamp);
        editor.commit();
    }

    //免密tel
    public String getTel() {
        return sharedPreferences.getString("tel", "");
    }

    public void setTel(String tel) {
        editor.putString("tel", tel);
        editor.commit();
    }

    //选择小区
    public String getIsChooseXiaoqu() {
        return sharedPreferences.getString("isChooseXiaoqu", "0");
    }

    public void setIsChooseXiaoqu(String isChooseXiaoqu) {
        editor.putString("isChooseXiaoqu", isChooseXiaoqu);
        editor.commit();
    }

    //首页选择的小区id
    public String getIsNew() {
        return sharedPreferences.getString("is_new", "0");
    }

    public void setIsNew(String isNew) {
        editor.putString("is_new", isNew);
        editor.commit();
    }

}
