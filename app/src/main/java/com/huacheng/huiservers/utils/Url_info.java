package com.huacheng.huiservers.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.huacheng.libraryservice.http.ApiHttpClient;
import com.umeng.socialize.sina.helper.MD5;

/**
 * 类：
 * 时间：2018/1/12 16:18
 * 功能描述:Huiservers
 */
public class Url_info {
    private Context context;
    public static String str_md5;

    public Url_info(Context mContext) {
        this.context = mContext;
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("login", 0);
        String user = mSharedPreferences.getString("login_username", "");
        String uid = mSharedPreferences.getString("login_uid", "");
        System.out.println("uid&&&&&&&&&&" + uid);
        str_md5 = MD5.hexdigest(uid + user);
    //    System.out.println("str_md5********" + str_md5);

    }

    /**
     * userCenter
     * 个人中心
     */
    //TODO
    //访客邀请
    public static final String checkIsAjb = MyCookieStore.SERVERADDRESS + "property/checkIsAjb/" + str_md5+"/";
    //我的装修
    public static final String my_renvation = MyCookieStore.SERVERADDRESS + "userCenter/my_decoration/" ;
    //意见反馈
    public static final String yijian = MyCookieStore.SERVERADDRESS + "userCenter/my_opinion/";
    //关于我们
    public static final String about = MyCookieStore.SERVERADDRESS + "userCenter/about_us/";
    //修改个人信息生日,头像,性别,昵称
    public static final String edit_center = MyCookieStore.SERVERADDRESS + "userCenter/edit_center/";
    //个人中心消费记录
    public static final String my_wallet = MyCookieStore.SERVERADDRESS + "userCenter/my_wallet/";
    //修改密码
    public static final String edit_pass = MyCookieStore.SERVERADDRESS + "userCenter/account/";

    //用户协议
    public static final String user_agreement = MyCookieStore.SERVERADDRESS + "userCenter/user_agreement/";
    //4.0优惠券列表
    public static final String my_coupon_40 = MyCookieStore.SERVERADDRESS + "userCenter/my_coupon_40/";
    //4.0优惠券-领取优惠券
    public static final String coupon_add = MyCookieStore.SERVERADDRESS + "userCenter/coupon_add/";
    //4.0个人中心优惠券详情
    public static final String coupon_details_40 = MyCookieStore.SERVERADDRESS + "userCenter/coupon_details_40/";
    //4.0个人中心已使用优惠券记录列表
    public static final String coupon_over_40 = MyCookieStore.SERVERADDRESS + "userCenter/coupon_over_40/";
    //个人中心优惠券详情页开启使用
    public static final String use_coupon = MyCookieStore.SERVERADDRESS + "userCenter/use_coupon/";


    //个人中心个人信息
    public static final String center_index = MyCookieStore.SERVERADDRESS + "userCenter/index_40/";
    //获取修改个人信息
    public static final String get_person_index = MyCookieStore.SERVERADDRESS + "userCenter/index/" ;
    //个人中心 添加/修改 收货地址
    public static final String add_user_address = MyCookieStore.SERVERADDRESS + "userCenter/add_user_address/";
    //个人中心获取收货地址列表
    public static final String get_user_address = MyCookieStore.SERVERADDRESS + "userCenter/get_user_address/";//apk_token/" + str_md5 + "/
    //个人中心删除收货地址
    public static final String del_user_address = MyCookieStore.SERVERADDRESS + "userCenter/del_user_address/";
    //个人中心购物订单详情
    public static final String shop_order_details = MyCookieStore.SERVERADDRESS + "userCenter/shopping_order_details/";
    //个人中心购物删除订单
    public static final String del_order = MyCookieStore.SERVERADDRESS + "userCenter/del_shopping_order/";
    //个人中心购物确认收货
    public static final String shop_order_accept = MyCookieStore.SERVERADDRESS + "userCenter/order_accept/";
    //个人中心购物订单列表
    public static final String shop_order_list = MyCookieStore.SERVERADDRESS + "userCenter/shopping_order/";
    //个人中心服务订单导航栏数据
    public static final String service_order_menu = MyCookieStore.SERVERADDRESS + "userCenter/my_service_menu/";
    //支付  商城支付/一卡通发送验证码
    public static final String pay_shopping_order = MyCookieStore.SERVERADDRESS + "userCenter/pay_shopping_order/";
    //支付成功掉区回调接口验证是否成功
    public static final String confirm_order_payment = MyCookieStore.SERVERADDRESS + "userCenter/confirm_order_payment/";
    //支付  一卡通支付信息订单
    public static final String pay_shopping_check = MyCookieStore.SERVERADDRESS + "userCenter/pay_shopping_check/";
    //提交购物订单评价信息
    public static final String shopping_order_score = MyCookieStore.SERVERADDRESS + "userCenter/shopping_order_score/";
    //提交购物订单退款信息
    public static final String shop_refund = MyCookieStore.SERVERADDRESS + "userCenter/refund/";

    /**
     * shop
     * 商城
     */
    //商城确认订单选择优惠券
    public static final String select_poupon = MyCookieStore.SERVERADDRESS + "shop/select_coupon/";
    //商城商品限购数量
    public static final String shop_limit = MyCookieStore.SERVERADDRESS + "shop/check_shop_limit/";
    //商城商品加入购物车
    //TODO 没法加smalldialog
    public static final String add_shop_cart = MyCookieStore.SERVERADDRESS + "shop/add_shopping_cart/";
    //获取购物车商品数量
    public static final String cart_num = MyCookieStore.SERVERADDRESS + "shop/cart_num/";
    //商城首页数据（限时商品以及精选商品）
    public static final String shop_index = MyCookieStore.SERVERADDRESS + "shop/shop_index/";
    //商城首页数据（限时商品以及精选商品）3.2
    public static final String shop_index_new = MyCookieStore.SERVERADDRESS + "shop/shop_index_new/";
    //居家养老首页数据3.2
    public static final String old_index_new = MyCookieStore.SERVERADDRESS + "shop/old_index_new/";
    //商城列表限时商品正在抢购以及准备活动3.2
    public static final String pro_list = MyCookieStore.SERVERADDRESS + "shop/pro_list/";
    //商城首页数据加载商品信息3.2
    public static final String pro_list_more = MyCookieStore.SERVERADDRESS + "shop/pro_list_more/";
    //确认订单信息
    public static final String submit_order_before = MyCookieStore.SERVERADDRESS + "shop/submit_order_before/";
    //提交订单信息
    public static final String submit_order = MyCookieStore.SERVERADDRESS + "shop/submit_order/";
    //确认订单信息查看商品信息
    public static final String order_info = MyCookieStore.SERVERADDRESS + "shop/order_info/";
    //查看商品全部评价
    public static final String goods_review = MyCookieStore.SERVERADDRESS + "shop/goods_review/";
    //搜索商品
    public static final String goods_search = MyCookieStore.SERVERADDRESS + "shop/goods_search/";
    //商城搜索，搜索关键字
    public static final String goods_search_keys = MyCookieStore.SERVERADDRESS + "shop/goods_search_keys/";
    //获取购物车列表
    public static final String shopping_cart = MyCookieStore.SERVERADDRESS + "shop/shopping_cart/";
    //删除购物车列表商品信息
    public static final String del_shopping_cart = MyCookieStore.SERVERADDRESS + "shop/del_shopping_cart/";
    //修改购物车数量校对
    public static final String edit_shopping_cart = MyCookieStore.SERVERADDRESS + "shop/edit_shopping_cart/";
    //一级分类
    public static final String area_topclass = MyCookieStore.SERVERADDRESS + "shop/area_topclass/";
    //3.2商城 商品列表分类数据 接口
    public static final String pro_list_cate = MyCookieStore.SERVERADDRESS + "shop/pro_list_cate/";

    //二三级分类
    public static final String area_category = MyCookieStore.SERVERADDRESS + "shop/area_category/";
    //商品详情
    public static final String goods_details = MyCookieStore.SERVERADDRESS + "shop/goods_details/";
    //商品标签
    public static final String goods_tags = MyCookieStore.SERVERADDRESS + "shop/goods_tags/";
    //商品专区列表
    public static final String category_goods = MyCookieStore.SERVERADDRESS + "shop/category_goods/";

    /**
     * property
     * 服务/物业
     */
    //个人中心维修订单详情
    public static final String repair_info = MyCookieStore.SERVERADDRESS + "property/my_property_repair_info/";
    //下单获取维修服务地址
    public static final String submit_repair_before = MyCookieStore.SERVERADDRESS + "property/submit_repair_before/";
    //提交维修订单
    public static final String submit_repair = MyCookieStore.SERVERADDRESS + "property/submit_repair/";
    //提交维修分类
    public static final String repair_type = MyCookieStore.SERVERADDRESS + "property/repair_type/";
    //维修支付接口
    public static final String pay_property_order = MyCookieStore.SERVERADDRESS + "property/pay_property_order/";
    //物业绑定小区选择楼号
    public static final String get_pro_building = MyCookieStore.SERVERADDRESS + "property/get_pro_building/";
    //物业绑定小区选择单元
    public static final String get_pro_unit = MyCookieStore.SERVERADDRESS + "property/get_pro_unit/";
    //物业绑定小区选择房间
    public static final String get_pro_room = MyCookieStore.SERVERADDRESS + "property/get_pro_room/";
    //我的房屋信息 接口
    public static final String room_info = MyCookieStore.SERVERADDRESS + "property/room_info/";
    //我的住宅
    public static final String binding_community = MyCookieStore.SERVERADDRESS + "property/binding_community/";
    //我的住宅添加修改成员
    public static final String house_member_save = MyCookieStore.SERVERADDRESS + "property/house_member_save/";
    //我的住宅获取/移除成员列表信息
    public static final String house_member = MyCookieStore.SERVERADDRESS + "property/house_member/";
    //我的住宅恢复/移除操作
    public static final String set_bind_status = MyCookieStore.SERVERADDRESS + "property/set_bind_status/";
    //业主认证
    public static final String bind_send_sms = MyCookieStore.SERVERADDRESS + "property/bind_send_sms";
    //查询验证是否有信息
    public static final String check_bind_sms = MyCookieStore.SERVERADDRESS + "property/check_bind_sms";
    //业主认证下一步
    public static final String check_property_mobile = MyCookieStore.SERVERADDRESS + "property/check_property_mobile/";
    //property_bind_user完成绑定
    public static final String property_bind_user = MyCookieStore.SERVERADDRESS + "property/property_bind_user/";
    //完成绑定
    public static final String pro_bind_user = MyCookieStore.SERVERADDRESS + "property/pro_bind_user/";
    //获取物业未缴费账单
    public static final String get_user_bill = MyCookieStore.SERVERADDRESS + "property/get_user_bill/";
    //获取物业已缴费的记录
    public static final String get_property_order = MyCookieStore.SERVERADDRESS + "property/get_property_order/";
    //确认物业支付订单
    public static final String make_property_order = MyCookieStore.SERVERADDRESS + "property/make_property_order/";
    //获取物业小区
    public static final String get_pro_com = MyCookieStore.SERVERADDRESS + "property/get_pro_com/";
    //通告详情
    public static final String notice_details = MyCookieStore.SERVERADDRESS + "property/notice_details/";
    //获取家庭账单
    public static final String getRoomBill = MyCookieStore.SERVERADDRESS + "property/get_room_bill/";
    //获取我的房屋手册
    public static final String get_new_house = MyCookieStore.SERVERADDRESS + "memorandum/get_new_house/";
    //获取我的备忘录
    public static final String get_memorandum = MyCookieStore.SERVERADDRESS + "memorandum/get_memorandum/";
    //添加我的备忘录
    public static final String set_memorandum = MyCookieStore.SERVERADDRESS + "memorandum/set_memorandum/";

    //获取用户水/电表详情
    public static final String property_getinfo = MyCookieStore.SERVERADDRESS + "property/get_info/";
    //输入金额 支付 水 / 电表费用（初始订单）
    public static final String property_createorder = MyCookieStore.SERVERADDRESS + "property/create_order/";

    //当面付分类 接口
    public static final String getFacePayCate = MyCookieStore.SERVERADDRESS + "property/face_pay_cate/";
    //当面付 支付订单
    public static final String pay_face_order = MyCookieStore.SERVERADDRESS + "property/pay_face_order/";
    //生成当面付订单
    public static final String addFacePayOrder = MyCookieStore.SERVERADDRESS + "property/add_face_pay_order/";
    //当面付 订单列表(历史记录) 接口
    public static final String face_order_list = MyCookieStore.SERVERADDRESS + "property/face_order_list/";
    //房间号 获取业主信息  接口
    public static final String getRoomPersonalInfo = MyCookieStore.SERVERADDRESS + "property/get_room_personal_info/";
    //商户列表
    public static final String merchant_list = MyCookieStore.SERVERADDRESS + "property/merchant_list/";

    //有限电视提交
    public static final String add_wired_order = MyCookieStore.SERVERADDRESS + "property/add_wired_order/";
    //有限电视支付接口
    public static final String pay_wired_order = MyCookieStore.SERVERADDRESS + "property/pay_wired_order/";
    //有线缴费记录
    public static final String wired_order_list = MyCookieStore.SERVERADDRESS + "property/wired_order_list/";
    //3.3服务支付
    public static final String service_new_pay = ApiHttpClient.API_SERVICE_URL + "order/pay_service_order/";
    /**
     * activity
     * 活动
     */
    //社区活动支付接口
    public static final String pay_activity_order = MyCookieStore.SERVERADDRESS + "activity/pay_activity_order/";
    //社区活动详情
    public static final String activity_details = MyCookieStore.SERVERADDRESS + "activity/activity_details/";
    //社区活动报名
    public static final String activity_enroll = MyCookieStore.SERVERADDRESS + "activity/activity_enroll/";
    //活动报名详情
    public static final String my_activity_info = MyCookieStore.SERVERADDRESS + "activity/my_activity_info/";
    /**
     * site
     * 登录注册
     */
    //版本更新
    public static final String version_update = MyCookieStore.SERVERADDRESS + "site/version_update/";
    //退出登录  不需要传值  需finish缓存
    public static final String site_logout = MyCookieStore.SERVERADDRESS + "site/logout/";
    // TODO: 2018/8/18
    //账号密码登录
    public static final String site_login = MyCookieStore.SERVERADDRESS + "site/login/";
    //免密登录
    public static final String free_login = MyCookieStore.SERVERADDRESS + "site/free_login/";
    //验证码登录
    public static final String login_verify = MyCookieStore.SERVERADDRESS + "site/login_verify/";
    //登录获取验证码登录
    public static final String reg_send_sms = MyCookieStore.SERVERADDRESS + "site/reg_send_sms/";
    //获取用户信息 -> 电信免密登录
    public static final String get_userInfo = MyCookieStore.SERVERADDRESS + "site/get_userInfo/";
    //注册
    public static final String register = MyCookieStore.SERVERADDRESS + "site/register/";
    //订单完成推送
    public static final String merchant_push = MyCookieStore.SERVERADDRESS + "site/merchant_push";
    //获取广告
    public static final String get_Advertising = MyCookieStore.SERVERADDRESS + "site/get_Advertising/";
    //首页3.2
    public static final String index_32 = MyCookieStore.SERVERADDRESS + "index/index_32/";
    //服务获取导航
    public static final String getAppMenuMore = MyCookieStore.SERVERADDRESS + "site/getAppMenuMore/";
    //获取支付类型列表
    public static final String payment_list = MyCookieStore.SERVERADDRESS + "site/payment_list/";

    //分享
    public static final String share_return = MyCookieStore.SERVERADDRESS + "site/share_return/";
    /**
     * 邻里
     * social
     */
    //邻里列表
    public static final String getSocialList = MyCookieStore.SERVERADDRESS + "social/get_social_list/";
    //我的邻里
    public static final String get_user_Social = MyCookieStore.SERVERADDRESS + "social/get_user_social_list/";
    //邻里首页右上角接口
    public static final String getSocialNum = MyCookieStore.SERVERADDRESS + "social/social_num/";
    //邻里分类
    public static final String getSocialCategorys = MyCookieStore.SERVERADDRESS + "social/getSocialCategory";
    //邻里点 赞/踩
    public static final String social_zan_cai = MyCookieStore.SERVERADDRESS + "social/SocialThumbs/";
    //邻里举报
    public static final String social_Report = MyCookieStore.SERVERADDRESS + "social/SocialReport/";


    //邻里详情4.0接口
    public static final String get_social = MyCookieStore.SERVERADDRESS + "social/get_social/";
    //邻里分类
    public static final String getSocialCategory = MyCookieStore.SERVERADDRESS + "social/getSocialCategory/";
    //邻里详情评论4.0
    public static final String social_reply = MyCookieStore.SERVERADDRESS + "social/social_reply/";
    //邻里删除4.0
    public static final String SocialDel = MyCookieStore.SERVERADDRESS + "social/SocialDel/";
    //邻里详情删除
    public static final String social_reply_del = MyCookieStore.SERVERADDRESS + "social/social_reply_del/";
    /**
     * 物流配送
     * social
     */
    public static final String distribution = MyCookieStore.SERVERADDRESS + "Jpush/distribution_push/";

    //限时抢购3.2
    public static final String pro_discount_list = MyCookieStore.SERVERADDRESS + "shop/pro_discount_list/";
}
