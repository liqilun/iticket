package com.iticket.util;

import java.sql.Timestamp;

public class TicketUtil {
	public static String getVoucherNo(Timestamp curtime){
		if(curtime==null){
			curtime = DateUtil.getCurFullTimestamp();
		}
		return DateUtil.format(curtime,"yyMMddHHmmss") + StringUtil.getDigitalRandomString(4);
	}
}
