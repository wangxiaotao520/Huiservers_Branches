package com.huacheng.huiservers.protocol;

import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.geren.bean.AddressBean;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceProtocol {
	public AddressBean getAddress(String json) {
		AddressBean address = new AddressBean();
		try {
			JSONObject jsonObject = new JSONObject(json);
			String status = jsonObject.getString("status");
			String data = jsonObject.getString("data");
			if (StringUtils.isEquals(status, "1")) {
				JSONObject obj =new JSONObject(data);
				address.setAddress_id(obj.getString("address_id"));
				address.setAddress(obj.getString("address"));
				address.setContact(obj.getString("contact"));
				address.setMobile(obj.getString("mobile"));
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return address;
	}

	//设置订单状态
	public String setType(String json) {
		String str = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String status = jsonObject.getString("status");
			String msg = jsonObject.getString("msg");
			System.out.println("json---------------======"+json);
			if (StringUtils.isEquals(status, "1")) {
				str="1";
			}else{
				str=msg;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return str;
	} 

	//参与服务菜单列表
	public List<ListBean> getServceMenu(String json){
		List<ListBean> menuList=new ArrayList<ListBean>();
		try {
			JSONObject jsonObject= new JSONObject(json);
			String status = jsonObject.getString("status");
			String data = jsonObject.getString("data");
			if (StringUtils.isEquals(status, "1")) {
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					ListBean info = new ListBean();
					JSONObject obj = array.getJSONObject(i);
					info.setId(obj.getString("id"));
					info.setName(obj.getString("name"));
					info.setLink(obj.getString("link"));
					info.setType(obj.getString("type"));
					menuList.add(info);
				}
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return menuList;

	}
}
