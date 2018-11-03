package com.huacheng.libraryservice.http;

/**
 * Description: 路径
 * created by wangxiaotao
 * 2018/7/27 0027 下午 4:39
 */
public class ApiHttpClient {
    //测试
    public static String API_URL = "http://test.hui-shenghuo.cn/apk41/";

    public static String IMG_URL = "http://img.hui-shenghuo.cn/";
    public static String API_URL_SHARE = "http://test.hui-shenghuo.cn/";//分享
    //正式
//    public static String API_URL = "http://m.hui-shenghuo.cn/apk40/";
//    public static String IMG_URL = "http://img.hui-shenghuo.cn/";
//    public static String API_URL_SHARE = "http://m.hui-shenghuo.cn/";

    //服务模块
    public static String API_SERVICE_URL = "http://test.hui-shenghuo.cn/service/";
    public static String IMG_SERVICE_URL = "http://img.hui-shenghuo.cn/";
    //正式
//    public static String API_SERVICE_URL = "http://m.hui-shenghuo.cn/service/";
//    public static String IMG_SERVICE_URL = "http://img.hui-shenghuo.cn/";

    public static String TOKEN;
    public static String TOKEN_SECRET;

    public static void setTokenInfo(String token, String tokenSecret) {
        TOKEN = token;
        TOKEN_SECRET = tokenSecret;
    }

    //推送接口
    public static String PUSH_INS = API_URL + "Jpush/service_order_toIns_push";

    //3.3
    //微信登录
    public static String WX_LOGIN = API_URL + "/site/wx_login";
    //登录用户绑定
    public static String WX_BIND = API_URL + "/site/wx_bind";
    //提交小区id
    public static String SELECT_COMMUNITY = API_URL + "/site/select_community";
    //定位获取小区
    public static String GET_COMMUNITY_BYCITY = API_URL + "/site/getCommunityByCity";
    //搜索关键字获取小区
    public static String SERCH_COMMUNITY = API_URL + "/site/searchCommunity";
    //新版物业缴费流程 根据房间号验证并获取业主信息
    public static String PRO_PERSONAL_INFO = API_URL + "/property/getPersonalInfo";
    //新版物业缴费流程 根据房间号获物业账单
//    public static String PRO_BILLBYROOM = API_URL_41 + "/property/getBillByRoom";//

    public static String GET_ROOM_BILL = API_URL + "property/get_room_bill";
    //新版物业缴费流程 完成绑定
    public static String PRO_BIND_USER = API_URL + "/property/pro_bind_user";
    //新版 缴费记录
    public static String GET_USER_BILL = API_URL + "property/getUserBill";


    //服务首页
    public static String GET_SERVICE_INDEX = API_SERVICE_URL + "index/serviceindex";
    //服务首页广告
    public static String GET_ADVERTISING = API_URL + "site/get_Advertising/";
    // 订单获取统计接口
    public static String GET_ORDER_COUNT = API_SERVICE_URL + "order/myOrderCount";
    // 我的订单
    public static String GET_MY_ORDER = API_SERVICE_URL + "order/myorder";
    //店铺详情
    public static String GET_SHOP_DETAIL = API_SERVICE_URL + "institution/merchantDetails";
    //获取店铺服务列表
    public static String GET_SHOP_SERVICE_LIST = API_SERVICE_URL + "/institution/merchantService";
    //获取店铺评论列表
    public static String GET_SHOP_COMMENT_LIST = API_SERVICE_URL + "institution/merchantComments";
    //服务详情
    public static String GET_SSERVICE_DETAIL = API_SERVICE_URL + "service/serviceDetails";
    //服务所有评论
    public static String GET_SSERVICE_SCORELIST = API_SERVICE_URL + "service/scoreList";
    //立即预约
    public static String GET_SSERVICE_RESERVE = API_SERVICE_URL + "service/serviceReserve";
    //获取订单详情
    public static String GET_ORDER_DETAIL = API_SERVICE_URL + "order/OrderDetail";
    //投诉，取消订单
    public static String ABORT_LIST = API_SERVICE_URL + "order/abortList";
    //提交评价
    public static String COMMIT_PINGJIA = API_SERVICE_URL + "order/critial";
    //提交取消服务订单
    public static String COMMIT_CANCEL = API_SERVICE_URL + "order/abortSave";
    //提交举报服务订单
    public static String COMMIT_JUBAO = API_SERVICE_URL + "order/reportSave";

    //服务首页-更多服务
    public static String GET_SERVICEMORE = API_SERVICE_URL + "index/serviceMore";
    //搜索,服务分类
    public static String SERVICE_CLASSIF = API_SERVICE_URL + "index/serviceClassif";
    //搜索关键字
    public static String GET_SERVICEKEYS = API_SERVICE_URL + "index/serviceKeys";
    //商家列表
    public static String GET_MERCHANTLIST = API_SERVICE_URL + "institution/merchantList";
    //服务列表
    public static String GET_SERVICELIST = API_SERVICE_URL + "service/serviceList";
    //付款成功后极光推送
    public static String PAY_SERVICE_SUCCESS = API_URL + "Jpush/service_order_toAmountWorker_push";
    //商城订单列表
    public static String GET_SHOP_ORDER_LIST = API_URL + "userCenter/shopping_order";
    //商城订单列表待付款删除
    public static String GET_SHOP_ORDER_DEL = API_URL + "userCenter/del_shopping_order";

    //0.4.1物业
    //选择房产类型
    public static String GET_HOUSETYPE = API_URL + "property/get_pro_housesType";
    //选择抄表类型 //useless
    public static String GET_CHAOBIAOTYPE = API_URL + "";
    //选择楼号
    public static String GET_FLOOR = API_URL + "property/get_pro_building";
    //选择单元号
    public static String GET_UNIT = API_URL + "property/get_pro_unit";
    //选择房间号
    public static String GET_ROOM = API_URL + "property/get_pro_room";
    //选择商铺
    public static String GET_SHOPS = API_URL + "property/get_pro_shop";

}
