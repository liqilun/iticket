package com.iticket.vo;

import java.util.HashMap;
import java.util.Map;

import com.iticket.model.api.ClientMember;
import com.iticket.model.layout.Layout;

public class ApiLayoutBeanHelp {
	public static Map<String, Object> getLayout(Layout layout, ClientMember member){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("layoutId", layout.getId());
		resMap.put("title", layout.getTitle());
		resMap.put("layoutData", layout.getLayoutData());
		resMap.put("addTime", layout.getAddTime());
		resMap.put("memberName", member.getMemberName());
		return resMap;
	}
}
