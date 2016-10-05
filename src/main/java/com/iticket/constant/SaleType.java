package com.iticket.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public class SaleType {
	public static final Integer COMMONE = 0;
	public static final Integer RESERVE = 1;
	public static final Integer WORK = 2;
	public static final Integer LCUSTOMER = 3;
	public static final Integer ORGANIZER = 4;
	public static final Integer PARTNER = 5;
	public static final Integer CONTRACT = 6;

	public static final Map<Integer, String> saleTypeMap = new LinkedHashMap<Integer, String>();

	static {
		saleTypeMap.put(COMMONE, 	"普通售票");
		saleTypeMap.put(RESERVE, 	"保留票");
		saleTypeMap.put(WORK, 		"工作票");
		saleTypeMap.put(LCUSTOMER, 	"大客户");
		saleTypeMap.put(ORGANIZER, 	"主办方");
		saleTypeMap.put(PARTNER, 	"乙方购票");
		saleTypeMap.put(CONTRACT, 	"合同票");
	}
}
