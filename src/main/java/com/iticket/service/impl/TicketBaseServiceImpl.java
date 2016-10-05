package com.iticket.service.impl;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.iticket.constant.VoucherType;
import com.iticket.model.api.ClientMember;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Venue;
import com.iticket.model.ticket.Customer;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.service.IException;
import com.iticket.support.ResultCode;
import com.iticket.util.DateUtil;
import com.iticket.util.TicketUtil;

public class TicketBaseServiceImpl extends BaseServiceImpl{
	protected Double getDiscount(Double amount, String disType){
		if(StringUtils.isBlank(disType) && StringUtils.equalsIgnoreCase(disType, "NONE")){
			return 0D;
		}
		if(StringUtils.equals(disType, VoucherType.DISTYPE_9D)){
			return amount*0.1;
		}else if(StringUtils.equals(disType, VoucherType.DISTYPE_8D)){
			return amount*0.2;
		}else if(StringUtils.equals(disType, VoucherType.DISTYPE_7D)){
			return amount*0.3;
		}
		return 0D;
	}
	protected Schedule validateSellData(ClientMember member, Long scheduleId,String pzType, String ticketType,
			String disType, String showPrice) throws IException{
		if(scheduleId==null){
			throw new IException("缺少scheduleId");
		}
		if(StringUtils.isBlank(ticketType)){
			throw new IException("缺少出票类型ticketType");
		}
		if(StringUtils.isBlank(showPrice)){
			throw new IException("缺少showPrice");
		}
		if(StringUtils.isBlank(disType)){
			throw new IException("缺少disType");
		}
		if(StringUtils.isBlank(pzType)){
			throw new IException("缺少凭证类型pzType");
		}
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在");
		}
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ResultCode code = schedule.booking();
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		return schedule;
	}
	protected void copyData2Voucher(ClientMember member, Voucher voucher, Program program, Venue venue, ScheduleVenueArea area, 
			String showPrice, String pzType, String disType, String ticketType, Double seatAmount, String payMethod, Integer saleType){
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		String otherNums = ""+RandomUtils.nextInt(10000000);
		voucher.setVoucherNo(TicketUtil.getVoucherNo(curtime));
		voucher.setPasswd(StringUtils.leftPad(otherNums, 7, '0'));
		voucher.setVenueCnName(venue.getCnName());
		voucher.setVenueEnName(venue.getEnName());
		voucher.setVenueAreaCnName(area.getCnName());
		voucher.setVenueAreaEnName(area.getEnName());
		voucher.setShowPrice(showPrice);
		voucher.setPzType(pzType);
		voucher.setDisType(disType);
		voucher.setTicketType(ticketType);
		voucher.setMemberId(member.getId());
		voucher.setMemberName(member.getMemberName());
		voucher.setSeatAmount(seatAmount);
		voucher.setProgramCnName(program.getCnName());
		voucher.setProgramEnName(program.getEnName());
		voucher.setPayMethod(payMethod);
		voucher.setSaleType(saleType);
		voucher.setDiscount(getDiscount(seatAmount, disType));
	}
	protected VoucherCustomer addVoucherCustomer(ClientMember member, Voucher voucher, Long customerId, String customerName, String customerMobile, String customerAddress){
		VoucherCustomer vcustomer = null;
		if(StringUtils.isNotBlank(customerName)){
			vcustomer = new VoucherCustomer();
			vcustomer.setVoucherId(voucher.getId());
			vcustomer.setCustomerName(customerName);
			vcustomer.setCustomerMobile(customerMobile);
			vcustomer.setCustomerAddress(customerAddress);
			baseDao.saveObject(vcustomer);
			if(customerId==null){
				Customer customer = new Customer(member);
				customer.setAddress(customerAddress);
				customer.setMobile(customerMobile);
				customer.setName(customerName);
				baseDao.saveObject(customer);
			}
		}
		return vcustomer;
	}
}	
