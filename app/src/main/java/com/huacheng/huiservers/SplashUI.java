package com.huacheng.huiservers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huacheng.huiservers.center.XiaoquActivity;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.utils.CacheUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.ThreadManager;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;
import com.lidroid.xutils.HttpUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 *
 * @author
 */
public class SplashUI extends BaseUI implements View.OnClickListener {

    private String login_verifytype, login_uid;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String IS_FIRST_OPEN = "is_first_open";
    HttpUtils utils = new HttpUtils();
    private SharePrefrenceUtil sharePrefrenceUtil;

    private boolean isFirstOpen;
    private String regId, login_type, is_wuye, zwStr, login_username, login_password;
    SharedPreferences preferencesLogin;

    private int is_ruan;
    String number;
    public static SplashUI intents;


    private String token;
    private String tokenSecret;
    private Timer timer_goOn;

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
      getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

    }


    @Override
    protected void init() {
        //   super.init();
        LoadingTask task = new LoadingTask();
        ThreadManager.getLongPool().execute(task);
       /* //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);*/
        intents = this;
        isFirstOpen = CacheUtils.getBoolean(SplashUI.this, IS_FIRST_OPEN, true);
        setContentView(R.layout.splash);
      //  SetTransStatus.GetStatus(SplashUI.this);//系统栏默认为黑色


        goinit();


    }

    private void goinit() {
        sharePrefrenceUtil = new SharePrefrenceUtil(SplashUI.this);
        preferencesLogin = SplashUI.this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        is_wuye = preferencesLogin.getString("is_wuye", "");
        login_uid = preferencesLogin.getString("login_uid", "");
        login_username = preferencesLogin.getString("login_username", "");
        login_password = preferencesLogin.getString("login_password", "");
        login_verifytype = preferencesLogin.getString("login_verifytype", "");

        token = preferencesLogin.getString("token", "");
        tokenSecret = preferencesLogin.getString("tokenSecret", "");


        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        //获取极光注册id并保存  开启推送
        JPushInterface.init(SplashUI.this);
        JPushInterface.resumePush(SplashUI.this);
        regId = JPushInterface.getRegistrationID(SplashUI.this);
        SharedPreferences preferences1 = SplashUI.this.getSharedPreferences(
                "jpush_id", 0);
        Editor editor = preferences1.edit();
        editor.putString("reg_id", regId);
        editor.commit();


        is_ruan = 0;
        goOn(isFirstOpen);
    }


    private void goOn(final boolean isFirstOpen) {

        if(isFirstOpen){
            Intent intent = new Intent(SplashUI.this, GuideUI.class);
            startActivity(intent);
            finish();
        }else{
            timer_goOn = new Timer();
            TimerTask tast = new TimerTask() {
                @Override
                public void run() {
                    if (login_type.equals("")) {

                        if (sharePrefrenceUtil.getIsChooseXiaoqu().equals("1")) {
                            if (is_ruan == 0) {
                                Intent intent = new Intent(SplashUI.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent = new Intent(SplashUI.this, XiaoquActivity.class);
                            intent.putExtra("type", "splash");
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // 改用token和tokensecert的方式
                        if (hasLoginUser()){
                            if ("1".equals(login_type)) {//个人登陆成功后获取物业验证字段。。目前现在没有首页暂不获取
                                Intent intent = new Intent(SplashUI.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                XToast.makeText(SplashUI.this, "当前账号不是个人账号,请重新登录", XToast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashUI.this, LoginVerifyCode1Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            // 到时候看看，这里是进首页还是进登录页
                            SharedPreferences preferences1 = getSharedPreferences("login", 0);
                            preferences1.edit().clear().commit();
                            if ("1".equals(sharePrefrenceUtil.getIsChooseXiaoqu())) {
                                if (is_ruan == 0) {
                                    Intent intent = new Intent(SplashUI.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Intent intent = new Intent(SplashUI.this, XiaoquActivity.class);
                                intent.putExtra("type", "splash");
                                startActivity(intent);
                                finish();
                            }
//                        Intent intent = new Intent(SplashUI.this, LoginVerifyCode1Activity.class);
//                        startActivity(intent);
//                        finish();
                        }
                    }
                    timer_goOn.cancel();
                }
            };
            timer_goOn.schedule(tast, 1000);
        }

    }

    private boolean hasLoginUser() {
        if (!NullUtil.isStringEmpty(token)&&!NullUtil.isStringEmpty(tokenSecret)){
            ApiHttpClient.TOKEN=token;
            ApiHttpClient.TOKEN_SECRET=tokenSecret;
            return  true;
        }
        return false;
    }

    @Override

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer_goOn!=null){
            timer_goOn.cancel();
        }
    }
}
