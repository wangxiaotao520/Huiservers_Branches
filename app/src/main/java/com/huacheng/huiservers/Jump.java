package com.huacheng.huiservers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.RadioGroup;

import com.huacheng.huiservers.center.ALLWebviewActivity;
import com.huacheng.huiservers.center.AboutActivity;
import com.huacheng.huiservers.center.Coupon40ListActivity;
import com.huacheng.huiservers.center.Coupon40ToshopUseActivity;
import com.huacheng.huiservers.cricle.Circle2DetailsActivity;
import com.huacheng.huiservers.dialog.IsCallDialog;
import com.huacheng.huiservers.dialog.WaitDIalog;
import com.huacheng.huiservers.facepay.FacepayIndexActivity;
import com.huacheng.huiservers.geren.CableTelIndexActivity;
import com.huacheng.huiservers.geren.ServiceXiaDanActivity;
import com.huacheng.huiservers.geren.bean.GerenBean;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.house.HouseFamilyBillActivity;
import com.huacheng.huiservers.house.HouseGuideActivity;
import com.huacheng.huiservers.house.HouseSelectActivity;
import com.huacheng.huiservers.house.New_House_HandbookActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.huodong.EducationActivity2;
import com.huacheng.huiservers.huodong.EducationListActivity;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.oldhome.OldHomeActivity;
import com.huacheng.huiservers.openDoor.OpenLanActivity;
import com.huacheng.huiservers.property.PropertyNewActivity;
import com.huacheng.huiservers.protocol.GerenProtocol;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.servicenew.ui.shop.ServiceStoreActivity;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.huacheng.huiservers.shop.ShopListActivity;
import com.huacheng.huiservers.shop.ShopWBActivity;
import com.huacheng.huiservers.shop.ShopXS4TimeListActivity;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.PermissionUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.TDevice;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huacheng.huiservers.utils.UIUtils.startActivity;

public class Jump {

    public static final int PHONE_STATE_REQUEST_CODE = 101;//获取读取手机权限，回调在 A中
    private Context mContext;
    private String url_type, login_uid, phone, urlh5, urlh5Btn, type;
    private IsCallDialog isCallDialog;
    private GerenProtocol protocol = new GerenProtocol();
    private GerenBean bean = new GerenBean();
    @ViewInject(R.id.rg_content_fragment)
    public static RadioGroup mRadioGroup;// 下面的radioGroup
    private SharePrefrenceUtil sharePrefrenceUtil;
    private SharedPreferences preferencesLogin;
    private String login_type, is_wuye, id, title, content, img, call_link, login_mobile, push_id;
    ShopDetailBean detailBean = new ShopDetailBean();
    ShopProtocol shopprotocol = new ShopProtocol();


    public Jump(final Context context, String type, String url, String img, String name) {//url 不能为空

        this.mContext = context;
        this.url_type = type;
        sharePrefrenceUtil = new SharePrefrenceUtil(context);
        preferencesLogin = context.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        login_uid = preferencesLogin.getString("login_uid", "");
        is_wuye = preferencesLogin.getString("is_wuye", "");
        login_mobile = preferencesLogin.getString("login_username", "");
        //没有绑定跳转绑定界面
        if (!url.equals("")) {
            phone = url;
            if (!type.equals("26")) {
                url = MyCookieStore.SERVERADDRESS + url;
            }

            if (type.equals("index")) {//跳转到app首页
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else if (type.equals("1")) {//跳转到商城列表
               /* Intent intent = new Intent(mContext, ShopZhuanquActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                intent.putExtra("isbool", "zhuanqu");
                mContext.startActivity(intent);*/
                if (url.indexOf("is_limit") != -1) {//接口中包含限购字段
                    System.out.println("包含");
                    String cateid = url.substring(url.lastIndexOf("/") + 1, url.length());
                    System.out.println("jump1*******" + cateid);
                    Intent intent = new Intent(mContext, ShopXS4TimeListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cateID", cateid);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else {
                    String cateid = url.substring(url.lastIndexOf("/") + 1, url.length());
                    System.out.println("不包含");
                    Intent intent = new Intent(mContext, ShopListActivity.class);
                    intent.putExtra("cateID", cateid);
                    mContext.startActivity(intent);
                }


            } else if (type.equals("2")) {//跳转到商城详情
                //跳转到详情页
                final String strurl = url;
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
                //String url = MyCookieStore.SERVERADDRESS + "shop/goods_details/id/" + mList.get(position).getId();

            } else if (type.equals("3")) {//跳转到活动列表
                /*url = url + "/m_id/" + sharePrefrenceUtil.getXiaoQuId();
                Intent intent = new Intent();
                intent.setClass(mContext, CampaignListActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                intent.putExtra("temp", "1");
                mContext.startActivity(intent);*/
                String urlnew = url;
                url = url + "/m_id/" + sharePrefrenceUtil.getXiaoQuId();
                Intent intent = new Intent();
                intent.setClass(mContext, EducationListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                intent.putExtra("urlnew", urlnew);
                intent.putExtra("name", name);
                mContext.startActivity(intent);
            } else if (type.equals("4")) {//跳转到活动详情
                Intent intent = new Intent();
                intent.setClass(mContext, EducationActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                mContext.startActivity(intent);
            } else if (type.equals("5")) { //跳转到维修
                preferencesLogin = mContext.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginVerifyCode1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    //  mContext.startActivity(new Intent(mContext, LoginVerifyCode1Activity.class));
                } else {
                    Intent intent = new Intent(mContext, ServiceXiaDanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", name);
                    mContext.startActivity(intent);
                }
            } else if (type.equals("6")) {//跳转到优惠券
                urlh5 = url;
                id = url.substring(url.lastIndexOf("/") + 1, url.length());
                getShare(context, id, "2");

            } else if (type.equals("7")) {//跳转到拨打电话
                new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"
                                    + phone));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")
              /*  isCallDialog = new IsCallDialog((Activity) context, phone);
                isCallDialog.show();*/
            } else if (type.equals("8")) {//个人中心优惠券列表
                preferencesLogin = mContext.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginVerifyCode1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    //  mContext.startActivity(new Intent(mContext, LoginVerifyCode1Activity.class));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Coupon40ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", url);
                    intent.putExtra("tag", "jump");
                    mContext.startActivity(intent);
                }
            } else if (type.equals("9")) {//跳转个人中心优惠券详情
                preferencesLogin = mContext.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginVerifyCode1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    //  mContext.startActivity(new Intent(mContext, LoginVerifyCode1Activity.class));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Coupon40ToshopUseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    id = url.substring(url.lastIndexOf("/") + 1, url.length());
                    intent.putExtra("coupon_id", id);
                    mContext.startActivity(intent);
                }
            } else if (type.equals("10")) {//跳转到软文
                urlh5 = url;
                id = url.substring(url.lastIndexOf("/") + 1, url.length());
                getShare(context, id, "1");

            } else if (type.equals("11")) {
                // TODO: 2018/8/20  现在不做任何作用
               /* id = url.substring(url.lastIndexOf("/") + 1, url.length());
                Intent intent = new Intent(mContext, ZhengWuDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("tag", "wuye");
                intent.putExtras(bundle);
                mContext.startActivity(intent);*/

            } else if (type.equals("12")) {//圈子列表
                // CircleFragment5 fragment=new CircleFragment5();
               /* FragmentManager fmanger=getSupportFragmentManager();
                FragmentTransaction transaction=fmanger.beginTransaction();
                transaction.replace(R.id.rb_content_fragment_quanzi,new CircleFragment5());
                transaction.commit();*/

            } else if (type.equals("13")) {//圈子详情
                id = url.substring(url.lastIndexOf("/") + 1, url.length());
                Intent intent = new Intent(mContext, Circle2DetailsActivity.class);
                Bundle bundle = new Bundle();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("id", id);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else if (type.equals("20")) {  //广告位跳转至东森易购

                if (!PermissionUtils.checkPermissionGranted(context, Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_REQUEST_CODE);
                } else {
                    String uuid = "";
                    uuid = TDevice.getIMEI(mContext);
                    preferencesLogin = mContext.getSharedPreferences("login", 0);
                    login_type = preferencesLogin.getString("login_type", "");
                    if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                        Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                        mContext.startActivity(intent);
                    } else {
                        if (login_type.equals("1")) {//个人
                            String sign = "hshObj";
                            Intent intent = new Intent(context, AboutActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("tag", "dsyg");
                            bundle.putString("strHouse",
                                    "http://www.dsyg42.com/ec/app_index?username=" + login_mobile + "&sign=" + sign + "&uuid=" + uuid);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        } else {
                            XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else if (type.equals("22")) {//服务模块_服务列表
                String cateid = "";
                String i_id = "";
                Intent intent = new Intent();
                int i_idLastIndex = url.lastIndexOf("/i_id/");
                int cateLastIndex = url.lastIndexOf("category/");

                if (i_idLastIndex >= 0 && cateLastIndex >= 0) {
                    cateid = url.substring(cateLastIndex + 9, i_idLastIndex);
                    if (!NullUtil.isStringEmpty(cateid)) {
                        intent.putExtra("sub_id", cateid);
                    }
                    i_id = url.substring(i_idLastIndex + 6, url.length());
                    if (!NullUtil.isStringEmpty(i_id)) {
                        intent.putExtra("store_id", i_id);
                    }
                } else {
                    if (i_idLastIndex >= 0) {
                        i_id = url.substring(i_idLastIndex + 6, url.length());
                        if (!NullUtil.isStringEmpty(i_id)) {
                            intent.putExtra("store_id", i_id);
                        }
                    }
                    if (cateLastIndex >= 0) {
                        cateid = url.substring(cateLastIndex + 9, url.length());
                        if (!NullUtil.isStringEmpty(cateid)) {
                            intent.putExtra("sub_id", cateid);
                        }
                    }

                }

                intent.setClass(mContext, MerchantServiceListActivity.class);
                intent.putExtra("tabType", "service");
                mContext.startActivity(intent);

            } else if (type.equals("23")) {//服务模块_服务详情
                String cateid = url.substring(url.lastIndexOf("/") + 1, url.length());
                Intent intent = new Intent();
                intent.setClass(mContext, ServiceDetailActivity.class);
                intent.putExtra("service_id", cateid);
                mContext.startActivity(intent);

            } else if (type.equals("24")) {//服务模块_商家列表
                String cateid = url.substring(url.lastIndexOf("category/") + 9, url.length());
                Intent intent = new Intent();
                intent.setClass(mContext, MerchantServiceListActivity.class);
                intent.putExtra("tabType", "");
                if (!NullUtil.isStringEmpty(cateid)) {
                    intent.putExtra("sub_id", cateid);
                }

                mContext.startActivity(intent);

            } else if (type.equals("25")) {//服务模块_商家详情
                String cateid = url.substring(url.lastIndexOf("/") + 1, url.length());
                Intent intent = new Intent(mContext, ServiceStoreActivity.class);
                intent.putExtra("store_id", cateid);
                mContext.startActivity(intent);
            } else if (type.equals("26")) {//所有直接展示webview的

                Intent intent = new Intent();
                intent.setClass(mContext, ALLWebviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                mContext.startActivity(intent);

            }
        }

    }

    public Jump(final Context context, String type, String url, String name) {//不需要接口值，根据type值判断
        this.mContext = context;
        this.url_type = type;
        sharePrefrenceUtil = new SharePrefrenceUtil(context);
        preferencesLogin = context.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        login_uid = preferencesLogin.getString("login_uid", "");
        is_wuye = preferencesLogin.getString("is_wuye", "");
        login_mobile = preferencesLogin.getString("login_username", "");
        if (type.equals("14")) {//紫光物联

            if (isZGWL(context, "com.uiot.smarthome.mobile")) {
                doStartApplicationWithPackageName(context, "com.uiot.smarthome.mobile");
            } else {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(MyCookieStore.SERVERADDRESS + "site/uiot");
                intent.setData(content_url);
                mContext.startActivity(intent);
            }

        } else if (type.equals("15")) { //有线缴费

            if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else if (login_type.equals("1")) {
                Intent intent = new Intent(mContext, CableTelIndexActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
            }

        } else if (type.equals("16")) {//物业缴费
            if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                startActivity(intent);
            } else {
                if (login_type.equals("1")) {//个人
                    //获取我的住宅列表来判断住宅有几条
                    //getHouseList("2");//2 为物业交费  家庭账单
                    // openPopupWindolw(v);
                    // }
                    startActivity(new Intent(mContext, PropertyNewActivity.class));

                } else {//
                    XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
            }

        } else if (type.equals("17")) {//一健开门
            if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                mContext.startActivity(intent);
            } else {
                if (login_type.equals("1")) {//个人
                    //获取我的住宅列表来判断住宅有几条
                    getHouseList("1");//1 为一健开门
                    // openPopupWindow(v);
                    // }
                } else {//
                    XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
            }

        } else if (type.equals("18")) {//当面付
            if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                if (login_type.equals("1")) {//当面付
                    Intent intent = new Intent(mContext, FacepayIndexActivity.class);
                    mContext.startActivity(intent);
                } else {//
                    XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
            }

        } else if (type.equals("19")) {//居家养老

            Intent intent = new Intent(mContext, OldHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);


        } else if (type.equals("20")) {//东森易购

            if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                mContext.startActivity(intent);
            } else {
                if (login_type.equals("1")) {//个人
                    String sign = "hshObj";
                    String uuid = "";
                    uuid = TDevice.getIMEI(mContext);
                    Intent intent = new Intent(context, AboutActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "dsyg");
                    bundle.putString("strHouse",
                            "http://www.dsyg42.com/ec/app_index?username=" + login_mobile + "&sign=" + sign + "&uuid=" + uuid);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else {
                    XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }
            }

        } else if (type.equals("21")) {//新房手册
            Intent intent = new Intent(mContext, New_House_HandbookActivity.class);
            mContext.startActivity(intent);

        } else if (type.equals("26")) {//所有直接展示webview的
            Intent intent = new Intent();
            intent.setClass(mContext, ALLWebviewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("url", url);
            intent.putExtra("name", name);
            mContext.startActivity(intent);

        }
    }


    // 判断是否安装紫光物联
    public static boolean isZGWL(Context context, String packagename) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packagename);
    }

    private void doStartApplicationWithPackageName(Context mcontext, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mcontext.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mcontext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            mcontext.startActivity(intent);
        }
    }

    //外链
    public Jump(Context context, String url) {
        this.mContext = context;
        /*Intent intent = new Intent();
        intent.setClass(mContext, ShopWBActivity.class);
		intent.putExtra("url", url); 
		intent.putExtra("uid", uid);
		mContext.startActivity(intent);*/
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException a) {
            a.getMessage();
        }

    }

    //软文链接
    public Jump(Context context, String urlh5, String type) {
        this.type = type;//1为软文  2为优惠券
        this.mContext = context;
        this.urlh5 = MyCookieStore.SERVERADDRESS + urlh5;
        preferencesLogin = context.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");

        id = urlh5.substring(urlh5.lastIndexOf("/") + 1, urlh5.length());
        if(!StringUtils.isEmpty(id)){
            getShare(context, id, type);

        }

    }

    //软新房链接详情
    public Jump(Context context, String h5, int i) {
        this.mContext = context;
        preferencesLogin = context.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        Intent intent = new Intent(context, AboutActivity.class);
        Bundle bundle = new Bundle();
        if (i == 333) {
            bundle.putString("tag", "activity");
        } else {
            bundle.putString("tag", "house");
        }
        bundle.putString("strHouse", h5);
        System.out.println("h5******" + h5);
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }

    //分享
    private void getShare(Context activity, String id, final String type) {
        Url_info info = new Url_info(activity);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("type", type);
        HttpHelper hh = new HttpHelper(info.share_return, params, activity) {

            @Override
            protected void setData(String json) {
                JSONObject jsonObject, jsonData;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    if (StringUtils.isEquals(status, "1")) {
                        jsonData = new JSONObject(data);
                        title = jsonData.getString("title");
                        content = jsonData.getString("content");
                        img = jsonData.getString("img");
                        call_link = jsonData.getString("call_link");
                        Intent intent = new Intent();
                        intent.setClass(mContext, ShopWBActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("url", urlh5);
                        intent.putExtra("title", title);
                        intent.putExtra("content", content);
                        intent.putExtra("img", img);
                        intent.putExtra("call_link", call_link);
                        intent.putExtra("type", type);
                        mContext.startActivity(intent);
                    } else if (StringUtils.isEquals(status, "0")) {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
//                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    HouseProtocol mHouseProtocol = new HouseProtocol();
    List<HouseBean> mHouseBeanList = new ArrayList<>();
    Dialog WaitDialog;

    private void getHouseList(final String type_str) {//获取我的住宅
        WaitDialog = WaitDIalog.createLoadingDialog(mContext, "正在加载...");
        Url_info info = new Url_info(mContext);
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.binding_community, params, mContext) {

            @Override
            protected void setData(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        mHouseBeanList = mHouseProtocol.getHouseList(json);
                        if (mHouseBeanList.size() == 1) {//当数量为1时直接跳转到家庭成员信息界面

                            getResult(mHouseBeanList.get(0).getRoom_id(), type_str);

                        } else {//否则跳转选择房屋绑定界面
                            //getResult(mHouseBeanList.get(0).getRoom_id(), type_str, "22");
                            Intent intent1 = new Intent(mContext, HouseSelectActivity.class);
                            if (type_str.equals("1")) {//蓝牙
                                intent1.putExtra("wuye_type", "fw_lanya");//为fw_invite时 选择小区完成后跳转到蓝牙
                            } else {
                                intent1.putExtra("wuye_type", "family");//为family时 选择小区完成后跳转到家庭账单
                            }
                            startActivity(intent1);
                        }

                    } else {
                        //如果状态为0   直接跳未绑定界面让其绑定,缓存值为1
                        //临时文件存储
                        SharedPreferences preferences1 = mContext.getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = preferences1.edit();
                        editor.putString("is_wuye", "1");
                        editor.commit();
                        Intent intent = new Intent(mContext, HouseGuideActivity.class);
                        startActivity(intent);
                    }
                    WaitDIalog.closeDialog(WaitDialog);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                WaitDIalog.closeDialog(WaitDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private String room_id, mobile, community, building, room_code;

    private void getResult(final String room_id, final String tag_str) {
        Url_info info = new Url_info(mContext);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(info.checkIsAjb, params, mContext) {


            @Override
            protected void setData(String json) {
                JSONObject jsonObject, jsonData;
                try {
                    jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        jsonData = new JSONObject(data);
                        mobile = jsonData.getString("mobile");
                        community = jsonData.getString("community");
                        building = jsonData.getString("building");
                        room_code = jsonData.getString("room_code");
                        if (tag_str.equals("1")) {
                            Intent intent;
                            intent = new Intent(mContext, OpenLanActivity.class);
                            intent.putExtra("room_id", room_id);

                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, HouseFamilyBillActivity.class);
                            intent.putExtra("room_id", room_id);
                            startActivity(intent);
                        }

                    } else {
                        XToast.makeText(mContext, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

   /* public void getDetail(String url, final String img) {//详情数据
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(url, params, mContext) {
            @Override
            protected void setData(String json) {
                bean = protocol.geRenManger(json, mContext);
                if (bean.getTopclass() != null && !TextUtils.isEmpty(bean.getTopclass().get(0).getId())) {
                    Intent intent = new Intent(mContext, ServiceXiaDanActivity.class);
                    Bundle bundle = new Bundle();
//					bundle.putString("id", bean.getTopclass().get(0).getId());
                    bundle.putString("name", bean.getTopclass().get(0).getName());
                    *//*bundle.putString("pay", bean.getPrepay());
                    bundle.putString("img", MyCookieStore.URL+bean.getTopclass().get(0).getIcon());
					if(bean.getAddress()!=null){
						bundle.putString("address", bean.getAddress().getRegion_cn()+
								bean.getAddress().getCommunity_cn()+
								bean.getAddress().getDoorplate());
						bundle.putString("conanct", bean.getAddress().getConsignee_name());
						bundle.putString("phone", bean.getAddress().getConsignee_mobile());
					}*//*
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        };
    }*/


}
