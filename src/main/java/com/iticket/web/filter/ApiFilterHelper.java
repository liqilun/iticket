package com.iticket.web.filter;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

public class ApiFilterHelper {
	
	public static void writeErrorResponse(HttpServletResponse res, String code, String message){
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/xml; charset=UTF-8");
		try {
			PrintWriter writer = res.getWriter();
			writer.write("<?xml  version=\"1.0\" encoding=\"UTF-8\" ?><data>");
			writer.write("<code>" + code + "</code>");
			writer.write("<error><![CDATA[" + message + "]]></error></data>");
			res.flushBuffer();
		} catch (Exception e) {
		} 
	}
	public static void writeErrorResponse(HttpServletResponse res, String message){
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/xml; charset=UTF-8");
		try {
			PrintWriter writer = res.getWriter();
			writer.write("<?xml  version=\"1.0\" encoding=\"UTF-8\" ?><data>");
			writer.write("<code>" + 9999 + "</code>");
			writer.write("<error><![CDATA[" + message + "]]></error></data>");
			res.flushBuffer();
		} catch (Exception e) {
		} 
	}
	public static TreeMap<String, String> getTreeMap(HttpServletRequest request) {
		Map<String, String[]> requestParams = request.getParameterMap();
		TreeMap<String, String> params = new TreeMap<String, String>();
		for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
			String[] values = entry.getValue();
			StringBuilder vb = new StringBuilder();
			for (String v : values) {
				vb.append(v + ",");
			}
			params.put(entry.getKey(),StringUtils.removeEnd(vb.toString(), ","));
		}
		if(ServletFileUpload.isMultipartContent(request)){
			params.remove("FileData");
		}
		params.remove("_asynch_forward_uri");
		return params;
	}

}
