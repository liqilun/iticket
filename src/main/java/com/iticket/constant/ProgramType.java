package com.iticket.constant;

import java.util.HashMap;
import java.util.Map;

public class ProgramType {
	public static Map<Integer, String> typeMap = new HashMap<Integer, String>();
	static {
		typeMap.put(1, "话剧");
		typeMap.put(2, "音乐会/剧");
		typeMap.put(3, "演唱会");
		typeMap.put(4, "电影");
		typeMap.put(5, "歌舞");
		typeMap.put(6, "活动展会");
		typeMap.put(7, "曲艺杂技");
		typeMap.put(8, "体育赛事");
	}
}
