package com.iticket.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.iticket.constant.VoucherType;
import com.iticket.model.api.ClientMember;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.ticket.ReserveMessage;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.model.ticket.VoucherDetail;
import com.iticket.model.ticket.VoucherLog;
import com.iticket.service.IException;
import com.iticket.service.Ticket4SeatService;
import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.iticket.vo.TicketVo;
@Service("ticket4SeatService")
public class Ticket4SeatServiceImpl extends TicketBaseServiceImpl implements Ticket4SeatService{
	@Override
	public List<TicketVo> sell(ClientMember member, Long scheduleId, String seatIds, String pzType, String ticketType, String disType,
			String showPrice, Long customerId, String customerName, String customerMobile, String customerAddress, 
			String payMethod, Integer saleType) throws IException {
		Schedule schedule = validateSellData(member, scheduleId, pzType, ticketType, disType, showPrice);
		List<Long> idList = BeanUtil.getIdList(seatIds, ",");
		List<ScheduleSeat> seatList = baseDao.getObjectList(ScheduleSeat.class, idList);
		if(seatList.size()==0){
			throw new IException("座位不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(seatList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			throw new IException("你选择的座位在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			throw new IException("你选择的座位所在的场次，和输入的场次不匹配");
		}
		for(ScheduleSeat seat : seatList){
			if(seat.getStatus().equals(ScheduleSeat.STATUS_SOLD)){
				throw new IException(seat.getSeatLabel()+"已被售出，卖出失败");
			}
		}
		double seatAmount = 0;
		Map<Long, SchedulePrice> priceMap = new HashMap<Long, SchedulePrice>();
		Map<Long, Integer> quantityMap = new HashMap<Long, Integer>();
		ScheduleVenueArea area = null;
		for(ScheduleSeat seat : seatList){
			SchedulePrice price = priceMap.get(seat.getPriceId());
			if(price==null){
				price = baseDao.getObject(SchedulePrice.class, seat.getPriceId());
				priceMap.put(seat.getPriceId(), price);
				quantityMap.put(price.getId(), 1);
			}else {
				quantityMap.put(price.getId(), quantityMap.get(price.getId())+1);
			}
			seatAmount = seatAmount + price.getPrice();
			if(area==null){
				area = baseDao.getObject(ScheduleVenueArea.class, seat.getScheduleVenueAreaId());
			}
		}
		Voucher voucher = new Voucher(member, schedule, seatList);
		Program program = baseDao.getObject(Program.class, schedule.getProgramId());
		Venue venue = baseDao.getObject(Venue.class, program.getVenueId());
		copyData2Voucher(member, voucher, program, venue, area, showPrice, pzType, disType, ticketType, seatAmount, payMethod, saleType);
		voucher.setPayStatus("Y");
		voucher.setReserveStatus("N");
		voucher.setRefundStatus("N");
		baseDao.saveObject(voucher);
		List<VoucherDetail> vdetailList = new ArrayList<VoucherDetail>();
		for(ScheduleSeat seat : seatList){
			Double price = priceMap.get(seat.getPriceId()).getPrice();
			VoucherDetail vdetail = new VoucherDetail(area, seat, voucher.getId(), voucher.getPzType(), price);
			vdetail.setAreaCnName(area.getCnName());
			vdetailList.add(vdetail);
			seat.setStatus(ScheduleSeat.STATUS_SOLD);
		}
		for(Long priceId : quantityMap.keySet()){
			SchedulePrice sprice = priceMap.get(priceId);
			sprice.addSold(quantityMap.get(priceId));
			baseDao.saveObject(sprice);
		}
		baseDao.saveObjectList(seatList);
		baseDao.saveObjectList(vdetailList);
		VoucherCustomer vcustomer = addVoucherCustomer(member, voucher, customerId, customerName, customerMobile, customerAddress);
		VoucherLog voucherLog = new VoucherLog(voucher, member, "sell");
		baseDao.saveObject(voucherLog);
		List<TicketVo> voList = new ArrayList<TicketVo>();
		Stadium stadium = baseDao.getObject(Stadium.class, schedule.getStadiumId());
		for(ScheduleSeat seat : seatList){
			SchedulePrice price = priceMap.get(seat.getPriceId());
			TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, price, seat, voucher, vcustomer);
			voList.add(vo);
		}
		return voList;
	}
	@Override
	public Long reserve(ClientMember member, Long scheduleId, String seatIds, Long customerId, String customerName, String customerMobile, String customerAddress, 
			Timestamp releaseTime, Timestamp tipTime, String disType) throws IException{
		if(scheduleId==null){
			throw new IException("缺少scheduleId");
		}
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在");
		}
		ResultCode code = schedule.booking();
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		if(StringUtils.isBlank(customerName)){
			throw new IException("请输入预留客户姓名");
		}
		if(StringUtils.isBlank(customerMobile)){
			throw new IException("请输入预留客户手机号");
		}
		List<Long> idList = BeanUtil.getIdList(seatIds, ",");
		List<ScheduleSeat> seatList = baseDao.getObjectList(ScheduleSeat.class, idList);
		if(seatList.size()==0){
			throw new IException("座位不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(seatList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			throw new IException("你选择的座位在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			throw new IException("你选择的座位所在的场次，和输入的场次不匹配");
		}
		for(ScheduleSeat seat : seatList){
			if(seat.getStatus()!=ScheduleSeat.STATUS_LOCKED){
				dbLogger.warn(seat.getStatus()+","+ScheduleSeat.STATUS_FREE);
				throw new IException(seat.getSeatLabel()+"座位不处于空闲状态，不能预留");
			}
		}
		ScheduleVenueArea area = null;
		double seatAmount = 0;
		Map<Long, SchedulePrice> priceMap = new HashMap<Long, SchedulePrice>();
		for(ScheduleSeat seat : seatList){
			SchedulePrice price = priceMap.get(seat.getPriceId());
			if(price==null){
				price = baseDao.getObject(SchedulePrice.class, seat.getPriceId());
				priceMap.put(seat.getPriceId(), price);
			}
			seatAmount = seatAmount + price.getPrice();
			if(area==null){
				area = baseDao.getObject(ScheduleVenueArea.class, seat.getScheduleVenueAreaId());
			}
		}
		Voucher voucher = new Voucher(member, schedule, seatList);
		Program program = baseDao.getObject(Program.class, schedule.getProgramId());
		Venue venue = baseDao.getObject(Venue.class, program.getVenueId());
		copyData2Voucher(member, voucher, program, venue, area, null, VoucherType.PZTYPE_YL, disType, null, seatAmount, null, null);
		voucher.setPayStatus("N");
		voucher.setReserveStatus("Y");
		voucher.setRefundStatus("N");
		voucher.setReleaseTime(releaseTime);
		voucher.setTipTime(tipTime);
		baseDao.saveObject(voucher);
		List<VoucherDetail> vdetailList = new ArrayList<VoucherDetail>();
		for(ScheduleSeat seat : seatList){
			Double price =  priceMap.get(seat.getPriceId()).getPrice();
			VoucherDetail vdetail = new VoucherDetail(area, seat, voucher.getId(), voucher.getPzType(), price);
			vdetail.setScheduleVenueAreaId(area.getId());
			vdetail.setAreaCnName(area.getCnName());
			vdetailList.add(vdetail);
			seat.setStatus(ScheduleSeat.STATUS_RESERVE);
		}
		baseDao.saveObjectList(seatList);
		baseDao.saveObjectList(vdetailList);
		addVoucherCustomer(member, voucher, customerId, customerName, customerMobile, customerAddress);
		VoucherLog voucherLog = new VoucherLog(voucher, member, "reserve");
		baseDao.saveObject(voucherLog);
		if(voucher.getTipTime()!=null){
			ReserveMessage message = new ReserveMessage(voucher);
			baseDao.saveObject(message);
		}
		return voucher.getId();
	}
}
