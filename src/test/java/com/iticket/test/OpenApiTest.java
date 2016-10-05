package com.iticket.test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.iticket.util.ApiUtil;
import com.iticket.util.DateUtil;
import com.iticket.web.util.HttpResult;
import com.iticket.web.util.HttpUtils;

public class OpenApiTest {
	public static void main(String[] args) throws IOException {
		TreeMap<String, String> serviceMap = new TreeMap<String, String>();
		//serviceMap.put("venueId", "13");
		//serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		//reqMap("com.iticket.stadium.venueAreaList", serviceMap);
		
		/*String xml = FileUtils.readFileToString(new File("E:\\aliworkspace\\iticket\\src\\main\\test\\com\\iticket\\test\\1.xml"), "utf-8");
		serviceMap.put("venueAreaId", "17");
		serviceMap.put("seatBody", xml);
		serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		reqMap("com.iticket.stadium.addVenueAreaSeat", serviceMap);*/
		
		
		/*serviceMap.put("venueAreaId", "17");
		serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		reqMap("com.iticket.stadium.venueAreaSeatList", serviceMap);*/
		
		/*serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		reqMap("com.iticket.program.programTypeList", serviceMap);*/
		
		
		/*serviceMap.put("venueId", "13");
		serviceMap.put("cnName", "测试项目");
		serviceMap.put("code", "29389293");
		serviceMap.put("startTime", "2015-12-10 00:00:00");
		serviceMap.put("endTime", "2015-12-31 00:00:00");
		serviceMap.put("typeId", "1");
		serviceMap.put("status", "Y");
		
		serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		reqMap("com.iticket.program.addProgram", serviceMap);*/
		
		serviceMap.put("memberKey", "2@a04b5582e026543028df6d29331ba627");
		reqMap("com.iticket.program.programList", serviceMap);
		
		
	}
	public static TreeMap getCommonMap(){
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("timestamp", DateUtil.getCurFullTimestampStr());
		treeMap.put("v", "1.0");
		treeMap.put("appkey", "1");
		treeMap.put("format", "xml");
		return treeMap;
	}
	public static void reqMap(String method, Map<String, String> serviceMap){
		TreeMap<String, String> treeMap = getCommonMap();
		treeMap.put("method", method);
		if(serviceMap!=null){
			treeMap.putAll(serviceMap);
		}
		String sign = ApiUtil.signMD5(treeMap, "ijcp2xgtkt4ydrnjrhu7jabl3t4ztqug");
		treeMap.put("signMethod", "md5");
		treeMap.put("sign", sign);
		//String url = "http://114.215.107.90:8080/iticket/showapi/router/rest";
		String url = "http://localhost:8080/iticket/showapi/router/rest";
		//System.out.println(url+"?"+treeMap.toString().replace("{", "").replace("}", "").replace(", ", "&"));
		HttpResult result = HttpUtils.postUrlAsString(url, treeMap, null, "utf-8");
		System.out.println(result.getResponse());
	}
}
