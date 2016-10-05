package com.iticket.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class ApiUtil {
	public static String signMD5(Map<String, String> params, String secretCode){
		if(params == null || params.isEmpty())
			return "";
		if(params instanceof java.util.TreeMap){
			return signMD5Inner((TreeMap<String, String>) params, secretCode);
		}else{
			TreeMap<String, String> treeMap = new TreeMap<String, String>();
			treeMap.putAll(params);
			return signMD5Inner(treeMap, secretCode);
		}
	}
	private static String signMD5Inner(TreeMap<String, String> param, String secretCode){
		return DigestUtils.md5Hex(signStr(param, secretCode)).toUpperCase();
	}
	private static String signStr(TreeMap<String, String> param, String secretCode){
		StringBuilder orgin = new StringBuilder();
		String value = "";
		for(String name : param.keySet()){
			//参与签名的值不包括参数中的签名值和签名方法
			if(!StringUtils.equalsIgnoreCase(name, "sign")
					&& !StringUtils.equalsIgnoreCase(name, "signmethod")){
				value = param.get(name);
				if(StringUtils.isEmpty(value)){
					value = "";
				}
				orgin.append(name).append("=").append(value).append("&");
			}
		}
		return StringUtils.substringBeforeLast(orgin.toString(), "&") + secretCode;
	}
}
