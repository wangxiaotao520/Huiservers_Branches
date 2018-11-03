package com.huacheng.huiservers.utils;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.huacheng.huiservers.shop.bean.ConfirmMapBean;
import com.huacheng.huiservers.wuye.bean.WuYeBean;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

public class MyCookieStore {

    public static CookieStore cookieStore = null;
    public static String YI_KEY;
    public static String RISKCONTROLINFO;
    public static String type = null;//微信支付返回类型
    public static String o_id = null;//微信支付返回订单id
    //public static String coupon_id = null;//微信支付返回优惠券id
    public static int is_notify = 0;//返回刷新
    public static int home_notify = 0;//首页下拉刷新
    public static int Activity_notity = 0;//活动微信返回刷新
    public static int Index_notify = 0;//商城选择id
    public static int Choose_id = 0;//圈子选择id
    public static int service_id = 0;//服务选择id
    public static int add_YZ = 0;//添加住宅成员返回刷新
    public static int Shop_notify = 0;//商城选择id
    public static int Circle_notify = 0;//圈子刷新
    public static int CircleOwn_notify = 0;//圈子刷新0
    public static int My_notify = 0;//我的刷新
    public static int shopcar_notify = 0;//列表页每次加载的条数

    public static Bitmap bitmap;//我的刷新

    public static Bitmap cirClebitmap;//我的详情底部头像

    public static int WX_dialog = 0;//weixin
    public static int Wu_notify = 0;//圈子刷新
    public static int Sh_notify = 0;//收货返回列表刷新
    public static int WyJf_notify = 0;//物业返回列表刷新
    public static int SC_notify = 0;//删除返回列表刷新
    public static int WX_notify = 0;//微信支付成功返回刷新
    public static int WY_notify = 0;//物业住宅绑定返回刷新
    public static int My_info = 0;//更改个人头像成功返回刷新
    public static int fw_delete = 0;//房屋人员删除以及添加返回刷新

    //    public static int Circle_refresh = 0;//圈子刷新（新）
    public static String item_delete_id = null;//微信支付成功返回刷新
    public static Cookie cookie = null;
    public static String password = null;
    public static String token = null;
    public static int width = 0;
    public static int height = 0;
    public static boolean updataTag = true;//判断是否调用检查新版本功能
    public static int everyPageNum = 10;//列表页每次加载的条数


    public static Dialog waitDialogc;
    public static boolean isMlinkSkipDownTimer = false;

//    正式
//    public static String SERVERADDRESS = "http://m.hui-shenghuo.cn/apk41/";
//    public static String URL = "http://img.hui-shenghuo.cn/";
    //测试
    public static String SERVERADDRESS = "http://test.hui-shenghuo.cn/apk41/";
    public static String URL = "http://img.hui-shenghuo.cn/";
//    public static String WL_URL = "http://test.hui-shenghuo.cn/apk40/";//物流配送接口域名

    public static List<ConfirmMapBean> Confirmlist = new ArrayList<ConfirmMapBean>();
    public static WuYeBean ConfirmWuye = new WuYeBean();
//    public static String ConfirmIsWaterElec = null;


}
