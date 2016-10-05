package com.iticket.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public abstract class VoucherType {
	//优惠类型
	public static final String DISTYPE_NONE = "none"; 
	public static final String DISTYPE_9D = "9D"; 
	public static final String DISTYPE_8D = "8D"; 
	public static final String DISTYPE_7D = "7D"; 
	//出票类型
	public static final String CHUPIAO_CHUPIAO = "1"; 	//出票
	public static final String CHUPIAO_TIPIAO = "2"; 	//赠票
	public static final String CHUPIAO_ZENGPIAO = "3";	//提票
	public static final String CHUPIAO_TUIPIAO = "4";	//退票
	
	//凭证类型
	public static final String PZTYPE_CP = "S";	//出票凭证：S
	public static final String PZTYPE_YL = "L";	//预留凭证：L
	public static final String PZTYPE_CD = "C";	//重打凭证：C
	public static final String PZTYPE_TP = "T";	//退票凭证：T

	//状态
	public static final String V_STATUS_N = "N"; 	//初始状态
	public static final String V_STATUS_D = "D"; 	//未批准
	public static final String V_STATUS_Y = "Y"; 	//已经批准

	public static final String VD_STATUS_N = "N"; 	//初始状态
	public static final String VD_STATUS_T = "T";	//申请退票，取消重打
	public static final String VD_STATUS_Y = "Y"; 	//票已经退票或者重打
	
	
	public static final Map<String, String> DISTYPE_MAP;
	public static final Map<String, String> TICKETTYPEMAP;
	public static final Map<String, String> WULIUMAP;
	public static final Map<String, String> VOUCHERTYPE;
	public static final Map<String, String> PZTYPENAMEMAP;
	
	static{
		Map<String, String> ticketTypeMap = new LinkedHashMap<String, String>();
		ticketTypeMap.put(CHUPIAO_CHUPIAO, "出票");
		ticketTypeMap.put(CHUPIAO_TIPIAO, "赠票");
		ticketTypeMap.put(CHUPIAO_ZENGPIAO, "提票");
		TICKETTYPEMAP = UnmodifiableMap.decorate(ticketTypeMap);
		
		Map<String, String> wuliuMap = new LinkedHashMap<String, String>();
		wuliuMap.put("1", "电子票");
		wuliuMap.put("2", "物流快递");
		WULIUMAP = UnmodifiableMap.decorate(wuliuMap);
		
		Map<String, String> voucherType = new LinkedHashMap<String, String>();
		voucherType.put("1", PZTYPE_CP);
		voucherType.put("2", PZTYPE_YL);
		voucherType.put("3", PZTYPE_TP);
		voucherType.put("4", PZTYPE_CD);
		VOUCHERTYPE = UnmodifiableMap.decorate(voucherType);
		
		Map<String, String> typeNameMap = new LinkedHashMap<String, String>();
		typeNameMap.put(PZTYPE_CP, "出票凭证");
		typeNameMap.put(PZTYPE_YL, "预留凭证");
		typeNameMap.put(PZTYPE_TP, "退票凭证");
		typeNameMap.put(PZTYPE_CD, "重打凭证");
		PZTYPENAMEMAP = UnmodifiableMap.decorate(typeNameMap);
		
		
		Map<String, String> disMap = new LinkedHashMap<String, String>();
		disMap.put(DISTYPE_NONE, "无优惠");
		disMap.put(DISTYPE_9D, "九折");
		disMap.put(DISTYPE_8D, "八折");
		disMap.put(DISTYPE_7D, "七折");
		DISTYPE_MAP = UnmodifiableMap.decorate(disMap);
	}
}
