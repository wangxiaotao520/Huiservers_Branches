package com.huacheng.huiservers.center;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseApplication;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.HomeActivity;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.PayInfoBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.AppUpdate;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class SetActivity extends BaseUI implements OnClickListener {
    ShopProtocol protocol2 = new ShopProtocol();
    PayInfoBean infoBean = new PayInfoBean();
    CenterProtocol protocol = new CenterProtocol();
    private String apkpath;
    private TextView title_name, tv_call_number, txt_verson;
    private LinearLayout lin_left;
    private RelativeLayout rel_zhanghao, rel_about, rel_siteout,
            rel_gengxin, rl_changepwd, rel_yijian, rel_hezuo, rel_dial_number;


    private Handler myHandler = new myHandler();

    class myHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
                    // 安装Apk
                    System.out.println("========");
                    Intent intent = new Intent();
                    // 执行动作
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    // 执行的数据类型
                    intent.setDataAndType(Uri.fromFile((File) msg.obj), "application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
                    startActivity(intent);
                    break;
            }
        }
    }

    /*
     * 从服务器中下载APK
     */
    @SuppressLint("HandlerLeak")
    protected void downLoadApk() {
        final ProgressDialog pds; // 进度条对话框
        pds = new ProgressDialog(SetActivity.this);
        pds.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pds.setCancelable(false);
        final Handler tipHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                double mLengTh = msg.arg1 / 1024f / 1024f;
                System.out.println("-----00000000000");
                DecimalFormat df = new DecimalFormat("#0.00");
                System.out.println("aok_size------" + mLengTh);
                pds.setMessage("正在下载更新-文件大小" + df.format(mLengTh) + "M");
                pds.show();
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("apkpath=======" + apkpath);
                    File file = AppUpdate.getFileFromServer(apkpath, pds, tipHandler);
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = file;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {

                } finally {
                    pds.dismiss();// 结束掉进度条对话框
                }
            }
        }.start();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.set_info);
        //     SetTransStatus.GetStatus(this);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        title_name = (TextView) findViewById(R.id.title_name);
        txt_verson = (TextView) findViewById(R.id.txt_verson);
        rl_changepwd = (RelativeLayout) findViewById(R.id.rl_changepwd);// 修改密码
        rel_dial_number = (RelativeLayout) findViewById(R.id.rel_dial_number);//拨打电话
        tv_call_number = (TextView) findViewById(R.id.tv_call_number);//

        title_name.setText("设置");
        txt_verson.setText("当前版本号：v" + AppUpdate.getVersionName(SetActivity.this));
        rel_about = (RelativeLayout) findViewById(R.id.rel_about);// 关于我们
        rel_gengxin = (RelativeLayout) findViewById(R.id.rel_gengxin);// 更新
        rel_siteout = (RelativeLayout) findViewById(R.id.rel_siteout);// 退出登陆
        rel_yijian = (RelativeLayout) findViewById(R.id.rel_yijian);// 意见反馈
        rel_hezuo = (RelativeLayout) findViewById(R.id.rel_hezuo);


        Set<String> set = new HashSet<>();
        set.add("15535406024");
        set.add("99906501020101");
        JPushInterface.resumePush(this);
        JPushInterface.setTags(getApplicationContext(), set, null);

        rel_dial_number.setOnClickListener(this);
        rel_about.setOnClickListener(this);
        rel_siteout.setOnClickListener(this);
        rel_gengxin.setOnClickListener(this);
        rl_changepwd.setOnClickListener(this);
        rel_yijian.setOnClickListener(this);
        rel_hezuo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.rel_yijian:// 意见反馈
                // KeepAliveService.connectServer(this,"99906501020101");
                startActivity(new Intent(SetActivity.this, YiJianActivity.class));
                break;
            case R.id.rel_hezuo:// 商务合作
                startActivity(new Intent(SetActivity.this, HeZuoActivity.class));
                break;
            case R.id.rel_dial_number:// 拨打电话
                new CommomDialog(this, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"
                                    + tv_call_number.getText().toString().trim()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")
                break;


            case R.id.rel_about:// 关于我们
                intent = new Intent(this, AboutActivity.class);
                bundle.putString("tag", "about");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rel_siteout:// 注销登陆
                new CommomDialog(this, R.style.my_dialog_DimEnabled, "确定退出登录？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            getsiteout();
                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")

                break;
            case R.id.rel_gengxin:// 更新版本
                getResult();// 获取是否有新版本
                break;
            case R.id.rl_changepwd:
                intent = new Intent(this, ChangePwdVerfityActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getResult() {// 版本更新接口
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("version", "v" + AppUpdate.getVersionName(SetActivity.this));
        System.out.println("version-------" + "v" + AppUpdate.getVersionName(SetActivity.this));
        params.addBodyParameter("type", "1");
        HttpHelper hh = new HttpHelper(info.version_update, params, SetActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                infoBean = protocol.getApk(json);
                System.out.println("info-------" + infoBean);
                System.out.println("infos-------" + infoBean.getPath());
                if (infoBean.getPath() != null) {
                    System.out.println("77777777777");
                    apkpath = infoBean.getPath();
                    new CommomDialog(SetActivity.this, R.style.my_dialog_DimEnabled, "发现有新版本，是否立即更新？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                downLoadApk();
                                dialog.dismiss();
                            }

                        }
                    }).show();//.setTitle("提示")
                } else {
                    XToast.makeText(SetActivity.this, "当前已是最新版本", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getsiteout() {// 退出登陆
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.site_logout, params, SetActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol2.setShop(json);
                if (str.equals("1")) {
                    // 清除登陆保存的值
                    SharedPreferences preferences1 = SetActivity.this.getSharedPreferences("login", 0);
                    preferences1.edit().clear().commit();
                    HomeActivity.instant.finish();
                    BaseApplication.removeALLActivity_();
                    ApiHttpClient.setTokenInfo(null,null);
                    HttpHelper.tokenSecret=null;
                    HttpHelper.token=null;
                    Intent intent = new Intent(SetActivity.this, HomeActivity.class);
                    startActivity(intent);
                    XToast.makeText(SetActivity.this, "退出登录", XToast.LENGTH_SHORT).show();
                   /* new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //execute the task
                            finish();
                        }
                    }, 2000);*/

                } else {
                    XToast.makeText(SetActivity.this, str, XToast.LENGTH_SHORT).show();
                    System.out.println("str=========" + str);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
