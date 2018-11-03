package com.huacheng.huiservers.protocol;

import android.text.TextUtils;

import com.huacheng.huiservers.cricle.bean.CircleDetail4Bean;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwh on 2017/11/3.
 */

public class CircleProtocol {

    /**
     * 圈子分类
     * @param json
     * @return
     */
    public List<BannerBean> getSocialCategory(String json) {
        List<BannerBean> info = new ArrayList<BannerBean>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    BannerBean adinfo = new BannerBean();
                    JSONObject obj = array.getJSONObject(i);
                    adinfo.setC_name(obj.getString("c_name"));
                    adinfo.setId(obj.getString("id"));
                    adinfo.setIs_pro(obj.getInt("is_pro"));
                    info.add(adinfo);
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 圈子详情4.0接口
    public CircleDetail4Bean getSocialDetail(String json) {
        CircleDetail4Bean info = new CircleDetail4Bean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                if (!TextUtils.isEmpty(data)) {
                    System.out.println("obj====" + jsonObject);
                    JSONObject obj = new JSONObject(data);
                    info.setId(obj.getString("id"));
                    info.setUid(obj.getString("uid"));
                    info.setAdmin_id(obj.getString("admin_id"));
                    info.setC_name(obj.getString("c_name"));
                    info.setContent(obj.getString("content"));
                    info.setClick(obj.getString("click"));
                    info.setReply_num(obj.getString("reply_num"));
                    info.setAddtime(obj.getString("addtime"));
                    info.setAvatars(obj.getString("avatars"));
                    info.setNickname(obj.getString("nickname"));
                    info.setIs_observe(obj.getString("is_observe"));
                    String Img_list = obj.getString("img_list");
                    if (!TextUtils.isEmpty(Img_list)) {
                        JSONArray array_img = new JSONArray(Img_list);
                        List<CircleDetail4Bean> beans = new ArrayList<CircleDetail4Bean>();
                        for (int j = 0; j < array_img.length(); j++) {
                            JSONObject obj1 = array_img.getJSONObject(j);
                            CircleDetail4Bean bean = new CircleDetail4Bean();
                            bean.setImg(obj1.getString("img"));
                            bean.setImg_size(obj1.getString("img_size"));
                            beans.add(bean);
                        }
                        info.setImg_list(beans);
                    } else {
                        info.setImg_list(null);
                    }
                    String reply_list = obj.getString("reply_list");
                    if (!TextUtils.isEmpty(reply_list)) {
                        JSONArray array_img = new JSONArray(reply_list);
                        List<CircleDetail4Bean> beans = new ArrayList<CircleDetail4Bean>();
                        for (int j = 0; j < array_img.length(); j++) {
                            JSONObject obj1 = array_img.getJSONObject(j);
                            CircleDetail4Bean bean = new CircleDetail4Bean();
                            bean.setId(obj1.getString("id"));
                            bean.setUid(obj1.getString("uid"));
                            bean.setSocial_id(obj1.getString("social_id"));
                            bean.setContent(obj1.getString("content"));
                            bean.setAddtime(obj1.getString("addtime"));
                            bean.setR_avatars(obj1.getString("r_avatars"));
                            bean.setR_nickname(obj1.getString("r_nickname"));
                            beans.add(bean);
                        }
                        info.setReply_list(beans);
                    } else {
                        info.setReply_list(null);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return info;
    }

    // 获取我的圈子数量
    public CircleDetail4Bean getMyCircleNum(String json) {
        CircleDetail4Bean circleNum = new CircleDetail4Bean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            if (StringUtils.isEquals(status, "1")) {
                JSONObject obj = new JSONObject(data);
                circleNum.setSocial_num(obj.getString("social_num"));
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return circleNum;
    }
}
