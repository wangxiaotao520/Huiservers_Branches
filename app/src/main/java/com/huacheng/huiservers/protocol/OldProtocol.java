package com.huacheng.huiservers.protocol;

import android.text.TextUtils;

import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.oldhome.bean.Oldbean;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.libraryservice.utils.NullUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OldProtocol {

    // 商城新增分类导航
    public Oldbean getOldHome(String json) {
        Oldbean info = new Oldbean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                System.out.println("obj====" + jsonObject);
                JSONObject obj = new JSONObject(data);
                //获取我的分类导航
                String catelist = obj.getString("s_list");
                if (!TextUtils.isEmpty(catelist)) {
                    JSONArray array = new JSONArray(catelist);
                    List<BannerBean> beanss = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        BannerBean adinfo = new BannerBean();
                        JSONObject obj1 = array.getJSONObject(i);
                        adinfo.setId(obj1.getString("id"));
                        adinfo.setUid(obj1.getString("uid"));
                        adinfo.setAdmin_id(obj1.getString("admin_id"));
                        adinfo.setCommunity_name(obj1.getString("community_name"));
                        adinfo.setCommunity_id(obj1.getString("community_id"));
                        adinfo.setC_id(obj1.getString("c_id"));
                        adinfo.setC_name(obj1.getString("c_name"));
                        adinfo.setTitle(obj1.getString("title"));
                        adinfo.setContent(obj1.getString("content"));
                        adinfo.setClick(obj1.getString("click"));
                        adinfo.setReply_num(obj1.getString("reply_num"));
                        adinfo.setAddtime(obj1.getString("addtime"));
                        adinfo.setAvatars(obj1.getString("avatars"));
                        adinfo.setNickname(obj1.getString("nickname"));
                        String Img_list = obj1.getString("img_list");
                        if (!TextUtils.isEmpty(Img_list)) {
                            JSONArray array_img = new JSONArray(Img_list);
                            List<BannerBean> beans = new ArrayList<BannerBean>();
                            for (int j = 0; j < array_img.length(); j++) {
                                JSONObject obj2 = array_img.getJSONObject(j);
                                BannerBean bean = new BannerBean();
                                bean.setId(obj2.getString("id"));
                                bean.setImg(obj2.getString("img"));
                                bean.setImg_size(obj2.getString("img_size"));
                                beans.add(bean);
                            }
                            adinfo.setImg_list(beans);
                        } else {
                            adinfo.setImg_list(null);
                        }
                        adinfo.setTotal_Pages(obj1.getInt("total_Pages"));
                        beanss.add(adinfo);
                    }
                    info.setS_list(beanss);
                }else {
                    info.setS_list(null);
                }

                ///解析s_url
                String s_url = obj.getString("s_url");
                if(!NullUtil.isStringEmpty(s_url)){
                    Oldbean oldbeans_url = new Oldbean();
                    JSONObject jsons_url = new JSONObject(s_url);
                    oldbeans_url.setPath(jsons_url.getString("path"));
                    String param = jsons_url.getString("param");
                    JSONObject jsons_param = new JSONObject(param);
                    Oldbean oldbeans_param = new Oldbean();
                    oldbeans_param.setC_id(jsons_param.getString("c_id"));
                    oldbeans_param.setCommunity_id(jsons_param.getString("community_id"));
                    oldbeans_url.setParam(oldbeans_param);

                    info.setS_url(oldbeans_url);
                }

                ///解析p_url
                String p_url = obj.getString("p_url");
                if(!NullUtil.isStringEmpty(p_url)){
                    Oldbean oldbeanp_url = new Oldbean();
                    JSONObject jsonp_url = new JSONObject(p_url);
                    oldbeanp_url.setPath(jsonp_url.getString("path"));
                    String paramp = jsonp_url.getString("param");
                    JSONObject jsons_para_pm = new JSONObject(paramp);
                    Oldbean oldbeans_param_p = new Oldbean();
                    oldbeans_param_p.setId(jsons_para_pm.getString("id"));
                    oldbeanp_url.setParam(oldbeans_param_p);

                    info.setP_url(oldbeanp_url);
                }

                //
                //获取我的所有分类商品
                String prolist = obj.getString("p_list");
                if (!TextUtils.isEmpty(prolist)) {
                    JSONArray array_pro = new JSONArray(prolist);
                    List<ShopIndexBean> lists_pro = new ArrayList<ShopIndexBean>();
                    for (int i = 0; i < array_pro.length(); i++) {
                        ShopIndexBean pro_list_item = new ShopIndexBean();
                        JSONObject objlist_item = array_pro.getJSONObject(i);
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
                        pro_list_item.setOrder_num(objlist_item.getString("order_num"));
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
                        lists_pro.add(pro_list_item);
                    }
                    info.setP_list(lists_pro);

                } else {
                    info.setP_list(null);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

}
