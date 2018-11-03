package com.huacheng.huiservers.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import android.util.Log;

import com.huacheng.huiservers.shop.bean.BestpayMerchant;
import com.huacheng.huiservers.yipay.CryptUtil;

public class ParamsUtil {
	/**
	 * 下单参数，订单金额传分
	 * 
	 * @return
	 */
	public static String buildOrderParams(BestpayMerchant model, String merchantKey, String riskControlInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERCHANTID").append("=").append(model.getMERCHANTID()).append("&")
				.append("ORDERAMT").append("=").append(model.getORDERAMOUNT())
				.append("&").append("ORDERSEQ").append("=").append(model.getORDERSEQ()).append("&")
				.append("ORDERREQTRANSEQ").append("=").append(model.getORDERREQTRANSEQ())
				.append("&").append("ORDERREQTIME").append("=").append(model.getORDERTIME())
				.append("&").append("TRANSCODE").append("=").append("01").append("&")
				.append("DIVDETAILS").append("=").append("").append("&").append("ORDERREQTIME")
				.append("=").append("").append("&").append("SERVICECODE").append("=").append("05")
				.append("&").append("PRODUCTDESC").append("=").append("").append("&")
				.append("BGURL").append("=").append(model.getBACKMERCHANTURL()).append("&")
				.append("ENCODETYPE").append("=").append("1").append("&").append("RISKCONTROLINFO")
				.append("=").append(riskControlInfo).append("&").append("MAC").append("=")
				.append(getMac(model, merchantKey, riskControlInfo));
		Log.e("buildOrderParams", sb.toString());
		return sb.toString();
	}

	/**
	 * 将单位为元的金额转化成分
	 * 
	 * @param yuan
	 * @return
	 */
	private static String yuan2cent(String yuan) {
		BigDecimal bigDecimalYuan = new BigDecimal(yuan);
		return bigDecimalYuan.multiply(new BigDecimal(100)).toString();
	}

	/**
	 * 获取下单加密串防止参数被篡改
	 * 
	 * @param model
	 * @param merchantKey
	 * @return
	 */
	private static String getMac(BestpayMerchant model, String merchantKey, String riskControlInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("MERCHANTID").append("=").append(model.getMERCHANTID()).append("&")
				.append("ORDERSEQ").append("=").append(model.getORDERSEQ()).append("&")
				.append("ORDERREQTRANSEQ").append("=").append(model.getORDERREQTRANSEQ())
				.append("&").append("ORDERREQTIME").append("=").append(model.getORDERTIME())
				.append("&").append("RISKCONTROLINFO").append("=").append(riskControlInfo)
				.append("&").append("KEY").append("=").append(merchantKey);
		String mac = "";
		try {
			mac = CryptUtil.md5Digest(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	/**
	 * 进入收银台的参数拼接
	 * 
	 * @return
	 */
	public static String buildPayParams(BestpayMerchant model) {
		StringBuilder paramsSb = new StringBuilder();
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			String attribute = name.substring(0, 1).toUpperCase() + name.substring(1);
			Method m;
			String value = "";
			try {
				m = model.getClass().getMethod("get" + attribute);
				value = (String) m.invoke(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			paramsSb.append(name).append("=").append(value).append("&");
		}
		Log.e("BestpayMerchant", paramsSb.toString().substring(0, paramsSb.length() - 1));
		return paramsSb.toString().substring(0, paramsSb.length() - 1);
	}

	/**
	 * 获取进入收银台的加密串，防止参数进入收银台的途中被篡改
	 * 
	 * @param model
	 * @return
	 */
	public static String getSign(BestpayMerchant model, String merchantKey) {
		StringBuilder sb = new StringBuilder();
		sb.append("SERVICE=").append(model.getSERVICE()).append("&MERCHANTID=")
				.append(model.getMERCHANTID()).append("&MERCHANTPWD=")
				.append(model.getMERCHANTPWD()).append("&SUBMERCHANTID=")
				.append(model.getSUBMERCHANTID()).append("&BACKMERCHANTURL=")
				.append(model.getBACKMERCHANTURL()).append("&ORDERSEQ=")
				.append(model.getORDERSEQ()).append("&ORDERREQTRANSEQ=")
				.append(model.getORDERREQTRANSEQ()).append("&ORDERTIME=")
				.append(model.getORDERTIME()).append("&ORDERVALIDITYTIME=")
				.append(model.getORDERVALIDITYTIME()).append("&CURTYPE=")
				.append(model.getCURTYPE()).append("&ORDERAMOUNT=").append(model.getORDERAMOUNT())
				.append("&SUBJECT=").append(model.getSUBJECT()).append("&PRODUCTID=")
				.append(model.getPRODUCTID()).append("&PRODUCTDESC=")
				.append(model.getPRODUCTDESC()).append("&CUSTOMERID=")
				.append(model.getCUSTOMERID()).append("&SWTICHACC=").append(model.getSWTICHACC())
				.append("&KEY=").append(merchantKey);
		Log.i("TAG", "sign加密前" + sb.toString());
		String sign = "";
		try {
			sign = CryptUtil.md5Digest(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}
}
