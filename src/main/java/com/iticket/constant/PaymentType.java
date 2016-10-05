package com.iticket.constant;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

/**
 * 支付方式
 */
public class PaymentType {

	public static final Integer TRANSFER = 1 ; 	// 银行转账
	public static final Integer CHECK = 2 ; 	// 支票
	public static final Integer CASH = 3 ; 		// 现金
	
	//充值选项
	public static final Map<Integer, String> RECHARGE_PAYTYPE = new LinkedHashMap<Integer, String>();

	static {
		RECHARGE_PAYTYPE.put(TRANSFER,	"银行转账");
		RECHARGE_PAYTYPE.put(CHECK,		"支票");
		RECHARGE_PAYTYPE.put(CASH,		"现金");
	}
	
	//出票 支付方式
	public static final Map<String, String> PAYMETHODMAP;
	static{
		Map<String, String> payMethodMap = new LinkedHashMap<String, String>();
		payMethodMap.put("1", "银联");
		payMethodMap.put("2", "支付宝");
		payMethodMap.put("3", "在线网银");
		payMethodMap.put("4", "现金");
		payMethodMap.put("5", "信用卡");
		payMethodMap.put("6", "银行卡");
		payMethodMap.put("7", "会员卡");
		payMethodMap.put("8", "学生证");
		payMethodMap.put("9", "兑换券");
		payMethodMap.put("10", "支票");
		payMethodMap.put("11", "转账");
		payMethodMap.put("12", "未收款");
		payMethodMap.put("13", "储值卡");
		
		PAYMETHODMAP = UnmodifiableMap.decorate(payMethodMap);
	}
}
