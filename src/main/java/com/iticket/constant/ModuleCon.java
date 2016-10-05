package com.iticket.constant;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ModuleCon {
	public static LinkedHashMap<String, String> moduleMap = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> adminModuleMap = new LinkedHashMap<String, String>();
	static {
		moduleMap.put("stadium", "场馆建设");
		moduleMap.put("layout", "票版制作");
		moduleMap.put("program", "演出建设");
		moduleMap.put("ticket", "票务操作");
		moduleMap.put("report", "报表管理");
		moduleMap.put("auth", "用户管理");
		
		adminModuleMap.put("common", "通用模块");
		adminModuleMap.put("stadium", "场馆建设");
		adminModuleMap.put("layout", "票版制作");
		adminModuleMap.put("program", "演出建设");
		adminModuleMap.put("ticket", "票务操作");
		adminModuleMap.put("report", "报表管理");
		adminModuleMap.put("auth", "用户管理");
	}

	public static List<String> getAllModuleList() {
		return Arrays.asList("stadium", "layout", "program", "ticket",
				"report", "auth");
	}
}
