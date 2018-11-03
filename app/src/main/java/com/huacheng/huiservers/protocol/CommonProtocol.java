package com.huacheng.huiservers.protocol;

import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/4/25.
 */

public class CommonProtocol {

    public String getResult(String json) {
        String str = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
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
}
