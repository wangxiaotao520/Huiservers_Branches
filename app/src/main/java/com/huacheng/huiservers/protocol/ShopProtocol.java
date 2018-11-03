package com.huacheng.huiservers.protocol;

import android.text.TextUtils;

import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.shop.bean.BestpayMerchant;
import com.huacheng.huiservers.shop.bean.CardPayBean;
import com.huacheng.huiservers.shop.bean.CateBean;
import com.huacheng.huiservers.shop.bean.ConfirmBean;
import com.huacheng.huiservers.shop.bean.DataBean;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.shop.bean.ShopMainBean;
import com.huacheng.huiservers.shop.bean.XGBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.libraryservice.utils.NullUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShopProtocol {

    public List<CateBean> getlistCateTwo(String json) {
        List<CateBean> info = new ArrayList<CateBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    CateBean adinfo = new CateBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setParent_id(obj.getString("parent_id"));
                    adinfo.setCate_name(obj.getString("cate_name"));
                    adinfo.setIcon(obj.getString("icon"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 分类二级数据
    public List<CateBean> getcateTwo(String json) {
        List<CateBean> info = new ArrayList<CateBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    CateBean adinfo = new CateBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setCate_name(obj.getString("cate_name"));
                    String sub_arr = obj.getString("sub_arr");
                    if (!TextUtils.isEmpty(sub_arr)) {
                        JSONArray array_two = new JSONArray(sub_arr);
                        List<CateBean> info_two = new ArrayList<CateBean>();
                        for (int j = 0; j < array_two.length(); j++) {
                            CateBean adinfo_two = new CateBean();
                            JSONObject obj_two = array_two.getJSONObject(j);
                            adinfo_two.setId(obj_two.getString("id"));
                            adinfo_two.setCate_name(obj_two.getString("cate_name"));
                            adinfo_two.setIcon(obj_two.getString("icon"));
                            adinfo_two.setIs_limit(obj_two.getString("is_limit"));
                            info_two.add(adinfo_two);
                        }
                        adinfo.setSub_arr(info_two);

                    }
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 首页底部商品列表接口
    public List<CateBean> getcateOne(String json) {
        List<CateBean> info = new ArrayList<CateBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    CateBean adinfo = new CateBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setCate_name(obj.getString("cate_name"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 圈子列表
    public List<BannerBean> getSocialList(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                if (!TextUtils.isEmpty(data)) {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        BannerBean adinfo = new BannerBean();
                        JSONObject obj = array.getJSONObject(i);
                        adinfo.setId(obj.getString("id"));
                        adinfo.setUid(obj.getString("uid"));
                        adinfo.setAdmin_id(obj.getString("admin_id"));
                        adinfo.setCommunity_name(obj.getString("community_name"));
                        adinfo.setC_id(obj.getString("c_id"));
                        adinfo.setC_name(obj.getString("c_name"));
                        adinfo.setTitle(obj.getString("title"));
                        adinfo.setContent(obj.getString("content"));
                        adinfo.setClick(obj.getString("click"));
                        adinfo.setReply_num(obj.getString("reply_num"));
                        adinfo.setAddtime(obj.getString("addtime"));
                        adinfo.setAvatars(obj.getString("avatars"));
                        adinfo.setNickname(obj.getString("nickname"));
                        String Img_list = obj.getString("img_list");
                        if (!NullUtil.isStringEmpty(Img_list)) {
                            JSONArray array_img = new JSONArray(Img_list);
                            if (array_img.length() > 0) {
                                List<BannerBean> beans = new ArrayList<BannerBean>();
                                for (int j = 0; j < array_img.length(); j++) {
                                    JSONObject obj1 = array_img.getJSONObject(j);
                                    BannerBean bean = new BannerBean();
                                    bean.setId(obj1.getString("id"));
                                    bean.setImg(obj1.getString("img"));
                                    bean.setImg_size(obj1.getString("img_size"));
                                    beans.add(bean);
                                }
                                adinfo.setImg_list(beans);
                            } else {
                                adinfo.setImg_list(null);
                            }

                        } else {
                            adinfo.setImg_list(null);
                        }
                        adinfo.setTotal_Pages(obj.getInt("total_Pages"));
                        info.add(adinfo);
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public List<BannerBean> getWoSocial(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                if (!TextUtils.isEmpty(data)) {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        BannerBean adinfo = new BannerBean();
                        JSONObject obj = array.getJSONObject(i);
                        adinfo.setId(obj.getString("id"));
                        adinfo.setUid(obj.getString("uid"));
                        adinfo.setAdmin_id(obj.getString("admin_id"));
                        adinfo.setC_id(obj.getString("c_id"));
                        adinfo.setC_name(obj.getString("c_name"));
                        adinfo.setTitle(obj.getString("title"));
                        adinfo.setContent(obj.getString("content"));
                        adinfo.setClick(obj.getString("click"));
                        adinfo.setReply_num(obj.getString("reply_num"));
                        adinfo.setAddtime(obj.getString("addtime"));
                        adinfo.setCommunity_name(obj.getString("community_name"));
                        adinfo.setAvatars(obj.getString("avatars"));
                        adinfo.setNickname(obj.getString("nickname"));
                        String Img_list = obj.getString("img_list");
                        if (!TextUtils.isEmpty(Img_list)) {
                            JSONArray array_img = new JSONArray(Img_list);
                            List<BannerBean> beans = new ArrayList<BannerBean>();
                            for (int j = 0; j < array_img.length(); j++) {
                                JSONObject obj1 = array_img.getJSONObject(j);
                                BannerBean bean = new BannerBean();
                                bean.setImg(obj1.getString("img"));
                                beans.add(bean);
                            }
                            adinfo.setImg_list(beans);
                        } else {
                            adinfo.setImg_list(null);
                        }
                        String replystr = obj.getString("reply");
                        if (!TextUtils.isEmpty(replystr)) {
                            JSONObject jsonReply = new JSONObject(replystr);
                            BannerBean reply = new BannerBean();
                            String replynum = jsonReply.getString("new_reply_num");
                            if (!replynum.equals("0")) {
                                reply.setNew_reply_num(replynum);
                                reply.setContent(jsonReply.getString("content"));
                                reply.setAvatars(MyCookieStore.URL + jsonReply.getString("avatars"));
                                reply.setNickname(jsonReply.getString("nickname"));
                                reply.setAddtime(jsonReply.getString("addtime"));
                                adinfo.setReply(reply);
                            } else {
                                adinfo.setReply(null);
                            }

                        } else {
                            adinfo.setReply(null);
                        }

                        adinfo.setTotal_Pages(obj.getInt("total_Pages"));
                        info.add(adinfo);
                    }

                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 首页活动
    public List<BannerBean> getActivitys(String json, String tag) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);

                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean adinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setPicture(MyCookieStore.URL
                            + obj.getString("picture"));
                    adinfo.setPicture_size(obj.getString("picture_size"));
                    adinfo.setTitle(obj.getString("title"));
                    if (tag.equals("1")) {//首页活动
                        adinfo.setId(obj.getString("id"));
                    } else {
                        adinfo.setA_id(obj.getString("a_id"));
                    }
                    adinfo.setOutside_url(obj.getString("outside_url"));
                    adinfo.setC_name(obj.getString("c_name"));
                    adinfo.setStatus(obj.getString("status"));
                    adinfo.setTotal_Pages(obj.getInt("total_Pages"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 广告位信息
    public List<BannerBean> bannerInfo(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean adinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setImg(MyCookieStore.URL + obj.getString("img"));
                    adinfo.setUrl(obj.getString("url"));
                    adinfo.setUrl_type(obj.getString("url_type"));
                    adinfo.setType_name(obj.getString("type_name"));
                    adinfo.setAdv_url(obj.getString("adv_url"));
                    adinfo.setAdv_inside_url(obj.getString("adv_inside_url"));
                    adinfo.setImg_size(obj.getString("img_size"));
                    adinfo.setUrl_type_cn(obj.getString("url_type_cn"));
                    info.add(adinfo);
                }
            } else {
                info = null;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 商品详情
    public ShopDetailBean getDetail(String json) {
        ShopDetailBean info = new ShopDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                // 详情轮播商品图片
                String img = obj.getString("imgs");
                System.out.println("imgs===" + img);
                if (img.equals("null") || img == null || img.equals("")) {
                } else {
                    JSONArray imgarray = new JSONArray(img);
                    List<BannerBean> imglist = new ArrayList<BannerBean>();
                    for (int k = 0; k < imgarray.length(); k++) {
                        JSONObject imgObj = imgarray.getJSONObject(k);
                        BannerBean imginfos = new BannerBean();
                        imginfos.setImg(imgObj.getString("img"));
                        imglist.add(imginfos);
                    }
                    info.setImgs(imglist);
                }
                info.setId(obj.getString("id"));
                info.setTitle(obj.getString("title"));
                info.setOriginal(obj.getString("original"));
                info.setDescription(obj.getString("description"));
                info.setInventory(obj.getString("inventory"));
                info.setSend_shop(obj.getString("send_shop"));
                info.setTagid(obj.getString("tagid"));
                info.setTagname(obj.getString("tagname"));
                info.setPrice(obj.getString("price"));
                info.setUnit(obj.getString("unit"));
                info.setTitle_img(obj.getString("title_img"));
                info.setCart_num(obj.getString("cart_num"));
                info.setExist_hours(obj.getString("exist_hours"));
                info.setAdvertorial(obj.getString("advertorial"));
                info.setShop_cate_etime(obj.getString("shop_cate_etime"));
                info.setShop_cate_stime(obj.getString("shop_cate_stime"));

                info.setDiscount(obj.getString("discount"));
                info.setDistance_start(obj.getString("distance_start"));
                info.setDistance_end(obj.getString("distance_end"));
                // 商家保证
                String add = obj.getString("goods_tag");
                List<BannerBean> tagBean = new ArrayList<BannerBean>();
                JSONArray tagobj = new JSONArray(add);
                for (int i = 0; i < tagobj.length(); i++) {
                    JSONObject addobj = tagobj.getJSONObject(i);
                    BannerBean tag = new BannerBean();
                    tag.setC_name(addobj.getString("c_name"));
                    tag.setC_img(MyCookieStore.URL + addobj.getString("c_img"));
                    tagBean.add(tag);
                }
                info.setGoods_tag(tagBean);
                //商品评价
                String pjShop = obj.getString("score");
                if (pjShop.equals("null") || pjShop == null || pjShop.equals("")) {
                    info.setScore(null);
                } else {
                    JSONArray tjarray = new JSONArray(pjShop);
                    List<ShopMainBean> tjList = new ArrayList<ShopMainBean>();
                    for (int j = 0; j < tjarray.length(); j++) {
                        JSONObject tjObj = tjarray.getJSONObject(j);
                        ShopMainBean tjinfos = new ShopMainBean();
                        tjinfos.setUsername(tjObj.getString("username"));
                        tjinfos.setDescription(tjObj.getString("description"));
                        tjinfos.setScore(tjObj.getString("score"));
                        tjList.add(tjinfos);
                    }
                    info.setScore(tjList);
                }

            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 获取商品标签信息
    public List<ShopDetailBean> getTag(String json) {
        List<ShopDetailBean> info = new ArrayList<ShopDetailBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    ShopDetailBean adinfo = new ShopDetailBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setTagname(obj.getString("tagname"));
                    adinfo.setOriginal(obj.getString("original"));
                    adinfo.setPrice(obj.getString("price"));
                    // adinfo.setTagid(obj.getString("tagid"));
                    adinfo.setInventory(obj.getString("inventory"));
                    adinfo.setUnit(obj.getString("unit"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 获取购物车数量
    public ShopDetailBean getCartNum(String json) {
        ShopDetailBean info = new ShopDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                info.setCart_num(obj.getString("cart_num"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 设置订单状态
    public String setShop(String json) {
        String str = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            System.out.println("json---------------======" + json);
            if (StringUtils.isEquals(status, "1")) {
                str = "1";
            } else {
                str = msg;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return str;
    }

    // 购物车列表
    public ShopDetailBean getShopList(String json) {
        ShopDetailBean info = new ShopDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                // 商家保证
                String add = obj.getString("list");
                System.out.println("list-----" + add);
                List<DataBean> listBean = new ArrayList<DataBean>();
                if (add != null && !add.equals("")) {
                    JSONArray tagobj = new JSONArray(add);
                    for (int i = 0; i < tagobj.length(); i++) {
                        JSONObject addobj = tagobj.getJSONObject(i);
                        DataBean list = new DataBean();
                        list.setId(Integer.valueOf(addobj.getString("id")));
                        list.setTagid(addobj.getString("tagid"));
                        list.setNumber(Integer.valueOf(addobj.getString("number")));
                        list.setP_id(addobj.getString("p_id"));
                        list.setTitle(addobj.getString("title"));
                        list.setTitle_img(addobj.getString("title_img"));
                        list.setTitle_thumb_img(addobj.getString("title_thumb_img"));
                        list.setPrice(Float.valueOf(addobj.getString("price")));
                        list.setTagname(addobj.getString("tagname"));
                        list.setInventory(addobj.getString("inventory"));
                        listBean.add(list);
                    }
                    info.setList(listBean);
                }
                // 推荐商品
                /*String tjShop = obj.getString("recommend");
                System.out.println("list-----" + tjShop);
                if (!tjShop.equals("null")) {
                    JSONArray tjarray = new JSONArray(tjShop);
                    List<BannerBean> tjList = new ArrayList<BannerBean>();
                    for (int j = 0; j < tjarray.length(); j++) {
                        JSONObject tjObj = tjarray.getJSONObject(j);
                        BannerBean tjinfos = new BannerBean();
                        tjinfos.setId(tjObj.getString("id"));
                        //tjinfos.setInventory(tjObj.getString("inventory"));
                        tjinfos.setTitle_img(tjObj.getString("title_img"));
                        tjinfos.setTitle(tjObj.getString("title"));
                        tjinfos.setOriginal(tjObj.getString("original"));
                        tjinfos.setMin_price(tjObj.getString("min_price"));
                        tjinfos.setExist_hours(tjObj.getString("exist_hours"));
                        tjList.add(tjinfos);
                    }
                    info.setRecommend(tjList);
                }*/
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 确认订单信息
    public ShopDetailBean getShopOrder(String json) {
        ShopDetailBean info = new ShopDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                String pro_data = obj.getString("pro_data");
                JSONArray proarray = new JSONArray(pro_data);
                List<ConfirmBean> confirmBeans = new ArrayList<ConfirmBean>();
                for (int i = 0; i < proarray.length(); i++) {
                    ConfirmBean confirmBean = new ConfirmBean();
                    JSONObject proObj = proarray.getJSONObject(i);
                    confirmBean.setMerchant_id(proObj.getString("merchant_id"));
                    confirmBean.setMerchant_name(proObj.getString("merchant_name"));
                    confirmBean.setMerchant_address(proObj.getString("merchant_address"));
                    confirmBean.setMerchant_telphone(proObj.getString("merchant_telphone"));
                    confirmBean.setHalf_amount(proObj.getString("half_amount"));
                    confirmBean.setNumber(proObj.getString("number"));
                    // 确认商品的图片
                    String img = proObj.getString("img");
                    JSONArray imgarray = new JSONArray(img);
                    List<BannerBean> imglist = new ArrayList<BannerBean>();
                    for (int k = 0; k < imgarray.length(); k++) {
                        JSONObject imgObj = imgarray.getJSONObject(k);
                        BannerBean imginfos = new BannerBean();
                        imginfos.setOne_img(imgObj.getString("one_img"));
                        imglist.add(imginfos);
                    }
                    confirmBean.setImg(imglist);
                    confirmBeans.add(confirmBean);
                }
                info.setPro_data(confirmBeans);
                info.setPro_num(obj.getString("pro_num"));
                info.setAddress(obj.getString("address"));
                info.setAddress_id(obj.getString("address_id"));
                info.setContact(obj.getString("contact"));
                info.setMobile(obj.getString("mobile"));
                info.setAmount(obj.getString("amount"));
                info.setSend_amount(obj.getString("send_amount"));
                info.setIs_coupon(obj.getString("is_coupon"));
                info.setShop_id_str(obj.getString("shop_id_str"));
                info.setAmount(obj.getString("amount"));

            } else {
                UIUtils.showToastSafe(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 确认订单生成
    public ShopDetailBean getShopConfirm(String json) {
        ShopDetailBean info = new ShopDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                info.setOrder_id(obj.getString("order_id"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }


    // 查看所有确认订单商品列表
    public List<BannerBean> getAllOrder(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean allinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    allinfo.setTagid(obj.getString("tagid"));
                    allinfo.setP_id(obj.getString("p_id"));
                    allinfo.setP_title_img(obj.getString("p_title_img"));
                    allinfo.setP_title(obj.getString("p_title"));
                    allinfo.setPrice(obj.getString("price"));
                    allinfo.setNumber(obj.getString("number"));
                    info.add(allinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 商品评价列表
    public List<ShopMainBean> getpingjia(String json) {
        List<ShopMainBean> info = new ArrayList<ShopMainBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    ShopMainBean getfinfo = new ShopMainBean();
                    JSONObject obj = array.getJSONObject(i);
                    getfinfo.setScore(obj.getString("score"));
                    getfinfo.setUsername(obj.getString("username"));
                    getfinfo.setDescription(obj.getString("description"));
                    getfinfo.setTotal_Pages(obj.getString("total_Pages"));
                    info.add(getfinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 翼支付Bean
    public BestpayMerchant getBestpayMerchant(String json) {
        BestpayMerchant info = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject jsonData = new JSONObject(data);
                info = new BestpayMerchant();
                DecimalFormat df = new DecimalFormat("######0.00");
                double num1 = Double.valueOf(jsonData.getString("ORDERAMT"));

                info.setMERCHANTID(jsonData.getString("MERCHANTID"));
                info.setORDERSEQ(jsonData.getString("ORDERSEQ"));
                info.setORDERREQTRANSEQ(jsonData.getString("ORDERREQTRANSEQ"));
                info.setORDERTIME(jsonData.getString("ORDERREQTIME"));
                // info.set(jsonData.getString("TRANSCODE"));
                info.setORDERAMOUNT(df.format(num1).toString());
                info.setCURTYPE(jsonData.getString("CURTYPE"));
                info.setPRODUCTID(jsonData.getString("PRODUCTID"));
                info.setPRODUCTDESC(jsonData.getString("PRODUCTDESC"));
                // info.set(jsonData.getString("ENCODETYPE"));
                info.setPRODUCTAMOUNT(df.toString());
                info.setATTACHAMOUNT("0.00");
                info.setCUSTOMERID("");
                info.setBUSITYPE("04");
                info.setSWTICHACC(jsonData.getString("SWTICHACC"));
                info.setSUBJECT(jsonData.getString("PRODUCTDESC"));
                // info.setMERCHANTID(jsonData.getString("MAC"));
                MyCookieStore.RISKCONTROLINFO = jsonData.getString("RISKCONTROLINFO");
                info.setBACKMERCHANTURL(jsonData.getString("BGURL"));
                MyCookieStore.YI_KEY = jsonData.getString("KEY");
                info.setMERCHANTPWD(jsonData.getString("MERCHANTPWD"));
            } else {
                return null;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public CardPayBean getHCCard(String json) {
        CardPayBean info = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject jsonData = new JSONObject(data);
                info = new CardPayBean();
            /*
            info.setService(jsonData.getString("service"));
			info.setPartner(jsonData.getString("partner"));
			info.setInput_charset(jsonData.getString("input_charset"));
			info.setNotify_url(jsonData.getString("notify_url"));
			info.setOut_trade_no(jsonData.getString("out_trade_no"));
			info.setSubject(jsonData.getString("subject"));
			info.setPayment_type(jsonData.getString("payment_type"));
			info.setSeller_id(jsonData.getString("seller_id"));
			info.setTotal_fee(jsonData.getString("total_fee"));
			info.setSign(jsonData.getString("sign"));
			info.setSign_type(jsonData.getString("sign_type"));*/
                info.setKey(jsonData.getString("key"));
            } else {
                return null;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 确认订单生成
    public XGBean getAGNum(String json) {
        XGBean info = new XGBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                info.setLimit(obj.getString("limit"));
                info.setCart_num(obj.getString("cart_num"));
                info.setOrder_num(obj.getString("order_num"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 获取圈子分类
    public List<BannerBean> getSocialCategorys(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);

                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean adinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setC_name(obj.getString("c_name"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 获取我的装修
    public List<BannerBean> getMyRenvation(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);

                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean adinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setId(obj.getString("id"));
                    adinfo.setUid(obj.getString("uid"));
                    adinfo.setContent(obj.getString("content"));
                    adinfo.setUptime(obj.getString("uptime"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 商城新增分类导航
    public ShopIndexBean getShop3_2_index(String json) {
        ShopIndexBean info = new ShopIndexBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                //获取我的分类导航
                String catelist = obj.getString("cate_list");
                if (!TextUtils.isEmpty(catelist)) {
                    JSONArray array = new JSONArray(catelist);
                    List<ShopIndexBean> lists = new ArrayList<ShopIndexBean>();
                    for (int i = 0; i < array.length(); i++) {
                        ShopIndexBean cate_list = new ShopIndexBean();
                        JSONObject objlist = array.getJSONObject(i);
                        cate_list.setId(objlist.getString("id"));
                        cate_list.setParent_id(objlist.getString("parent_id"));
                        cate_list.setCate_name(objlist.getString("cate_name"));
                        cate_list.setIcon(objlist.getString("icon"));
                        lists.add(cate_list);
                    }
                    info.setCate_list(lists);
                } else {
                    info.setCate_list(null);
                }

                //获取我的所有限时抢购商品
                ShopIndexBean limit = new ShopIndexBean();
                String pro_discount_list = obj.getString("pro_discount_list");
                if (!TextUtils.isEmpty(pro_discount_list)) {
                    JSONObject json_pro = new JSONObject(pro_discount_list);
                    /*List<ShopIndexBean> lists_pro = new ArrayList<ShopIndexBean>();
                    for (int i = 0; i < array_pro.length(); i++) {
                        ShopIndexBean pro_list = new ShopIndexBean();
                        JSONObject objlist = array_pro.getJSONObject(i);
                        *//*pro_list.setId(objlist.getString("id"));
                        pro_list.setCate_name(objlist.getString("cate_name"));*//*
                    }
                    info.setPro_list(lists_pro);*/

                    limit.setCate_img(json_pro.getString("cate_img"));
                    String list_pro_item = json_pro.getString("list");
                    if (TextUtils.isEmpty(list_pro_item)) {
                        limit.setList(null);
                    } else {
                        JSONArray array_pro_item = new JSONArray(list_pro_item);
                        List<ShopIndexBean> lists_pro_item = new ArrayList<ShopIndexBean>();
                        for (int k = 0; k < array_pro_item.length(); k++) {
                            ShopIndexBean pro_list_item = new ShopIndexBean();
                            JSONObject objlist_item = array_pro_item.getJSONObject(k);
                            pro_list_item.setId(objlist_item.getString("id"));
                            pro_list_item.setTitle(objlist_item.getString("title"));
                            pro_list_item.setTitle_img(objlist_item.getString("title_img"));
//                                pro_list_item.setTitle_thumb_img(objlist_item.getString("title_thumb_img"));
                            pro_list_item.setCate_tag_id(objlist_item.getString("cate_tag_id"));
                            pro_list_item.setShop_cate_stime(objlist_item.getString("shop_cate_stime"));
                            pro_list_item.setShop_cate_etime(objlist_item.getString("shop_cate_etime"));
//                                pro_list_item.setSend_shop_id(objlist_item.getString("send_shop_id"));
                            pro_list_item.setAddtime(objlist_item.getString("addtime"));
                            pro_list_item.setPrice(objlist_item.getString("price"));
                            pro_list_item.setTagid(objlist_item.getString("tagid"));
                            pro_list_item.setInventory(objlist_item.getString("inventory"));
                            pro_list_item.setTagname(objlist_item.getString("tagname"));
                            pro_list_item.setOriginal(objlist_item.getString("original"));
                            pro_list_item.setUnit(objlist_item.getString("unit"));
                            pro_list_item.setExist_hours(objlist_item.getString("exist_hours"));
                            pro_list_item.setIs_hot(objlist_item.getString("is_hot"));
                            pro_list_item.setIs_new(objlist_item.getString("is_new"));
                            pro_list_item.setDiscount(objlist_item.getString("discount"));
                            pro_list_item.setDistance_start(objlist_item.getString("distance_start"));
                            pro_list_item.setDistance_end(objlist_item.getString("distance_end"));

                            String goods_tag = objlist_item.getString("goods_tag");
                            List<ShopIndexBean> tagBean = new ArrayList<ShopIndexBean>();
                            JSONArray tagobj = new JSONArray(goods_tag);
                            for (int m = 0; m < tagobj.length(); m++) {
                                JSONObject addobj = tagobj.getJSONObject(m);
                                ShopIndexBean tag = new ShopIndexBean();
                                tag.setC_name(addobj.getString("c_name"));
                                tag.setC_img(addobj.getString("c_img"));
                                tagBean.add(tag);
                            }
                            pro_list_item.setGoods_tag(tagBean);
                            lists_pro_item.add(pro_list_item);
                        }
                        limit.setList(lists_pro_item);
                    }
                    info.setPro_discount_list(limit);

                } else {
                    info.setPro_discount_list(null);
                }

                //获取我的商品列表
                String prolist = obj.getString("pro_list");
                if (!TextUtils.isEmpty(prolist)) {
                    JSONArray array_pro = new JSONArray(prolist);
                    List<ShopIndexBean> lists_pro = new ArrayList<ShopIndexBean>();
                    for (int i = 0; i < array_pro.length(); i++) {
                        ShopIndexBean pro_list = new ShopIndexBean();
                        JSONObject objlist = array_pro.getJSONObject(i);
                        pro_list.setId(objlist.getString("id"));
                        pro_list.setCate_name(objlist.getString("cate_name"));
                        pro_list.setCate_img(objlist.getString("cate_img"));
                        String list_pro_item = objlist.getString("list");
                        if (TextUtils.isEmpty(list_pro_item)) {
                            pro_list.setList(null);
                        } else {
                            JSONArray array_pro_item = new JSONArray(list_pro_item);
                            List<ShopIndexBean> lists_pro_item = new ArrayList<ShopIndexBean>();
                            for (int k = 0; k < array_pro_item.length(); k++) {
                                ShopIndexBean pro_list_item = new ShopIndexBean();
                                JSONObject objlist_item = array_pro_item.getJSONObject(k);
                                pro_list_item.setId(objlist_item.getString("id"));
                                pro_list_item.setTitle(objlist_item.getString("title"));
                                pro_list_item.setTitle_img(objlist_item.getString("title_img"));
                                pro_list_item.setTitle_thumb_img(objlist_item.getString("title_thumb_img"));
                                pro_list_item.setCate_tag_id(objlist_item.getString("cate_tag_id"));
                                pro_list_item.setSend_shop_id(objlist_item.getString("send_shop_id"));
                                pro_list_item.setAddtime(objlist_item.getString("addtime"));
                                pro_list_item.setPrice(objlist_item.getString("price"));
                                pro_list_item.setTagid(objlist_item.getString("tagid"));
                                pro_list_item.setInventory(objlist_item.getString("inventory"));
                                pro_list_item.setTagname(objlist_item.getString("tagname"));
                                pro_list_item.setOriginal(objlist_item.getString("original"));
                                pro_list_item.setUnit(objlist_item.getString("unit"));
                                pro_list_item.setExist_hours(objlist_item.getString("exist_hours"));
                                pro_list_item.setIs_hot(objlist_item.getString("is_hot"));
                                pro_list_item.setIs_new(objlist_item.getString("is_new"));
                                pro_list_item.setDiscount(objlist_item.getString("discount"));

                                String goods_tag = objlist_item.getString("goods_tag");
                                List<ShopIndexBean> tagBean = new ArrayList<ShopIndexBean>();
                                JSONArray tagobj = new JSONArray(goods_tag);
                                for (int m = 0; m < tagobj.length(); m++) {
                                    JSONObject addobj = tagobj.getJSONObject(m);
                                    ShopIndexBean tag = new ShopIndexBean();
                                    tag.setC_name(addobj.getString("c_name"));
                                    tag.setC_img(addobj.getString("c_img"));
                                    tagBean.add(tag);
                                }
                                pro_list_item.setGoods_tag(tagBean);
                                lists_pro_item.add(pro_list_item);
                            }
                            pro_list.setList(lists_pro_item);
                        }
                        lists_pro.add(pro_list);
                    }
                    info.setPro_list(lists_pro);

                } else {
                    info.setPro_list(null);
                }

            } else {
                info = null;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    public List<ShopIndexBean> getProList(String json) {
        List<ShopIndexBean> productList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray ary = new JSONArray(data);
                for (int i = 0; i < ary.length(); i++) {
                    ShopIndexBean info = new ShopIndexBean();
                    JSONObject obj = ary.getJSONObject(i);
                    info.setId(obj.getString("id"));
                    info.setTitle(obj.getString("title"));
                    info.setTitle_img(obj.getString("title_img"));
                    info.setShop_cate_stime(obj.getString("shop_cate_stime"));
                    info.setShop_cate_etime(obj.getString("shop_cate_etime"));
                    String add = obj.getString("goods_tag");

                    String discount = obj.getString("discount");
                    String distance_start = obj.getString("distance_start");
                    String distance_end = obj.getString("distance_end");
                    info.setDiscount(discount);
                    info.setDistance_start(distance_start);
                    info.setDistance_end(distance_end);

                    if (TextUtils.isEmpty(add)) {
                        info.setGoods_tag(null);
                    } else {
                        List<ShopIndexBean> tagBean = new ArrayList<ShopIndexBean>();
                        JSONArray tagobj = new JSONArray(add);
                        for (int j = 0; j < tagobj.length(); j++) {
                            JSONObject addobj = tagobj.getJSONObject(j);
                            ShopIndexBean tag = new ShopIndexBean();
                            tag.setC_name(addobj.getString("c_name"));
                            tag.setC_img(MyCookieStore.URL + addobj.getString("c_img"));
                            tagBean.add(tag);
                        }
                        info.setGoods_tag(tagBean);
                    }
                    info.setTagid(obj.getString("tagid"));
                    info.setTagname(obj.getString("tagname"));
                    info.setPrice(obj.getString("price"));
                    info.setOriginal(obj.getString("original"));
                    info.setInventory(obj.getString("inventory"));
                    info.setUnit(obj.getString("unit"));
                    info.setExist_hours(obj.getString("exist_hours"));
                    info.setIs_hot(obj.getString("is_hot"));
                    info.setIs_new(obj.getString("is_new"));
                    info.setOrder_num(obj.getString("order_num"));
                    info.setTotal_Pages(obj.getString("total_Pages"));
                    productList.add(info);
                }

            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return productList;
    }

    public List<ShopIndexBean> getProSearchList(String json) {
        List<ShopIndexBean> productList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray ary = new JSONArray(data);
                for (int i = 0; i < ary.length(); i++) {
                    ShopIndexBean info = new ShopIndexBean();
                    JSONObject obj = ary.getJSONObject(i);
                    info.setId(obj.getString("id"));
                    info.setTitle(obj.getString("title"));
                    info.setTitle_img(MyCookieStore.URL + obj.getString("title_img"));

                    String add = obj.getString("goods_tag");
                    List<ShopIndexBean> tagBean = new ArrayList<ShopIndexBean>();
                    JSONArray tagobj = new JSONArray(add);
                    for (int j = 0; j < tagobj.length(); j++) {
                        JSONObject addobj = tagobj.getJSONObject(j);
                        ShopIndexBean tag = new ShopIndexBean();
                        tag.setC_name(addobj.getString("c_name"));
                        tag.setC_img(MyCookieStore.URL + addobj.getString("c_img"));
                        tagBean.add(tag);
                    }
                    info.setGoods_tag(tagBean);
                    info.setTagid(obj.getString("tagid"));
                    info.setTagname(obj.getString("tagname"));
                    info.setPrice(obj.getString("price"));
                    info.setOriginal(obj.getString("original"));
                    info.setInventory(obj.getString("inventory"));
                    info.setUnit(obj.getString("unit"));
                    info.setExist_hours(obj.getString("exist_hours"));
                    info.setIs_hot(obj.getString("is_hot"));
                    info.setIs_new(obj.getString("is_new"));
                    info.setOrder_num(obj.getString("order_num"));
                    info.setTotal_Pages(obj.getString("total_Pages"));
                    productList.add(info);
                }

            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return productList;
    }


}
