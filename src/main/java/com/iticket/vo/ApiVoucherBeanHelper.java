package com.iticket.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.iticket.constant.VoucherType;
import com.iticket.model.ticket.Voucher;

public class ApiVoucherBeanHelper {
	public static Map<String, Object> getVoucher(Voucher voucher, String seatText){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("voucherId", voucher.getId());
		resMap.put("voucherNo", voucher.getVoucherNo());
		resMap.put("scheduleId", voucher.getScheduleId());
		resMap.put("programId", voucher.getProgramId());
		resMap.put("seatAmount", voucher.getSeatAmount());
		resMap.put("seatNum", voucher.getSeatNum());
		resMap.put("addTime", voucher.getAddTime());
		resMap.put("ticketType", voucher.getTicketType());
		resMap.put("pzType", voucher.getPzType());
		resMap.put("disType", voucher.getDisType());
		resMap.put("discount", voucher.getDiscount());
		resMap.put("disTypeText", voucher.gainDisTypeText());
		resMap.put("ticketTypeText", voucher.gainTicketTypeText());
		resMap.put("pzTypeText", voucher.gainTypeText());
		resMap.put("scheduleCnName", voucher.getScheduleCnName());
		resMap.put("scheduleEnName", voucher.getScheduleEnName());
		resMap.put("programCnName", voucher.getProgramCnName());
		resMap.put("programEnName", voucher.getProgramEnName());
		resMap.put("scheduleCnName", voucher.getScheduleCnName());
		resMap.put("scheduleEnName", voucher.getScheduleEnName());
		resMap.put("venueCnName", voucher.getVenueCnName());
		resMap.put("venueEnName", voucher.getVenueEnName());
		resMap.put("venueAreaCnName", voucher.getVenueAreaCnName());
		resMap.put("venueAreaEnName", voucher.getVenueAreaEnName());
		resMap.put("playTime", voucher.getPlayTime());
		if(StringUtils.equals(voucher.getPzType(), VoucherType.PZTYPE_YL)){
			resMap.put("releaseTime", voucher.getReleaseTime());
			resMap.put("tipTime", voucher.getTipTime());
			resMap.put("reserveStatus", voucher.getReserveStatus());
		}
		resMap.put("payStatus", voucher.getPayStatus());
		resMap.put("payStatusText", voucher.gainPayStatusText());
		if(StringUtils.isNotBlank(seatText)){
			resMap.put("seatText", seatText);
		}else {
			resMap.put("seatText", voucher.getSeatText());
		}
		resMap.put("addTime", voucher.getAddTime());
		return resMap;
	}
}
