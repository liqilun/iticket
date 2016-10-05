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
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.ticket.ReserveMessage;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.model.ticket.VoucherDetail;
import com.iticket.model.ticket.VoucherLog;
import com.iticket.service.IException;
import com.iticket.service.TicketService;
import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.iticket.util.DateUtil;
import com.iticket.util.TicketUtil;
import com.iticket.vo.TicketVo;
@Service("ticketService")
public class TicketServiceImpl extends TicketBaseServiceImpl implements TicketService{
	@Override
	public List<TicketVo> reserveSell(ClientMember member, Long voucherId, String ticketType, String payMethod, String showPrice, Integer saleType, String disType) throws IException {
		Voucher voucher = baseDao.getObject(Voucher.class, voucherId);
		if(!voucher.getPzType().equals(VoucherType.PZTYPE_YL)){
			throw new IException("非预留凭证，不能出票");
		}
		if(!voucher.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		if(voucher.getReleaseTime()!=null || curtime.after(voucher.getReleaseTime())){
			throw new IException("预留的信息已被释放，不能出票");
		}
		Schedule schedule = baseDao.getObject(Schedule.class, voucher.getScheduleId());
		ResultCode code = schedule.booking();
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		Voucher newVoucher = new Voucher();
		BeanUtil.copyProperties(newVoucher, voucher);
		newVoucher.setId(null);
		newVoucher.setVoucherNo(TicketUtil.getVoucherNo(curtime));
		newVoucher.setShowPrice(showPrice);
		newVoucher.setPzType(VoucherType.PZTYPE_CP);
		newVoucher.setDisType(disType);
		voucher.setDiscount(getDiscount(newVoucher.getSeatAmount(), disType));
		newVoucher.setTicketType(ticketType);
		newVoucher.setMemberId(member.getId());
		newVoucher.setMemberName(member.getMemberName());
		newVoucher.setReserveId(voucher.getId());
		newVoucher.setAddTime(curtime);
		baseDao.saveObject(newVoucher);
		List<VoucherDetail> vdetailList = new ArrayList<VoucherDetail>();
		List<ScheduleSeat> seatList = new ArrayList<ScheduleSeat>();
		List<ScheduleStand> standList = new ArrayList<ScheduleStand>();
		Map<Long, SchedulePrice> xpriceMap = new HashMap<Long, SchedulePrice>();
		if(voucher.hasSeat()){
			seatList = getScheduleSeatListByVoucherId(voucherId);
			for(ScheduleSeat seat : seatList){
				if(!seat.getStatus().equals(ScheduleSeat.STATUS_RESERVE)){
					throw new IException("座位状态非预留状态，不能出票");
				}
				SchedulePrice xprice = xpriceMap.get(seat.getPriceId());
				if(xprice==null){
					xprice = baseDao.getObject(SchedulePrice.class, seat.getPriceId());
					xpriceMap.put(seat.getPriceId(), xprice);
				}
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, seat.getScheduleVenueAreaId());
				VoucherDetail vdetail = new VoucherDetail(area, seat, newVoucher.getId(), newVoucher.getPzType(), xprice.getPrice());
				vdetail.setScheduleVenueAreaId(area.getId());
				vdetail.setAreaCnName(area.getCnName());
				vdetailList.add(vdetail);
				seat.setStatus(ScheduleSeat.STATUS_SOLD);
			}
			baseDao.saveObjectList(seatList);
		}else {
			standList = getScheduleStandListByVoucherId(voucherId);
			for(ScheduleStand stand : standList){
				if(!stand.getStatus().equals(ScheduleSeat.STATUS_RESERVE)){
					throw new IException("状态非预留状态，不能出票");
				}
				SchedulePrice price = xpriceMap.get(stand.getPriceId());
				if(price==null){
					price = baseDao.getObject(SchedulePrice.class, stand.getPriceId());
					xpriceMap.put(stand.getPriceId(), price);
				}
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, stand.getScheduleVenueAreaId());
				VoucherDetail vdetail = new VoucherDetail(stand, newVoucher.getId(), newVoucher.getPzType(), xpriceMap.get(stand.getPriceId()));
				vdetail.setScheduleVenueAreaId(area.getId());
				vdetail.setAreaCnName(area.getCnName());
				vdetailList.add(vdetail);
				stand.setStatus(ScheduleSeat.STATUS_SOLD);
			}
			baseDao.saveObjectList(standList);
		}
		voucher.setPayStatus("Y");
		baseDao.saveObject(voucher);
		baseDao.saveObjectList(vdetailList);
		
		Program program = baseDao.getObject(Program.class, schedule.getProgramId());
		Venue venue = baseDao.getObject(Venue.class, program.getVenueId());
		List<TicketVo> voList = new ArrayList<TicketVo>();
		Stadium stadium = baseDao.getObject(Stadium.class, schedule.getStadiumId());
		VoucherCustomer vcustomer = getVoucherCustomerByVoucherId(voucherId);
		if(voucher.hasSeat()){
			for(ScheduleSeat seat : seatList){
				SchedulePrice price = xpriceMap.get(seat.getPriceId());
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, seat.getScheduleVenueAreaId());
				TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, price, seat, voucher, vcustomer);
				voList.add(vo);
			}
		}else {
			for(ScheduleStand stand : standList){
				SchedulePrice price = xpriceMap.get(stand.getPriceId());
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, stand.getScheduleVenueAreaId());
				TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, price, stand, voucher, vcustomer);
				voList.add(vo);
			}
		}
		VoucherLog voucherLog = new VoucherLog(newVoucher, member, "reserveSell");
		baseDao.saveObject(voucherLog);
		if(voucher.getTipTime()!=null){
			List<ReserveMessage> messageList = baseDao.getObjectListByField(ReserveMessage.class, "voucherId", voucherId);
			baseDao.removeObjectList(messageList);
		}
		return voList;
	}
	
	@Override
	public void reserveCancel(ClientMember member, Long voucherId) throws IException {
		Voucher voucher = baseDao.getObject(Voucher.class, voucherId);
		if(!voucher.getPzType().equals(VoucherType.PZTYPE_YL)){
			throw new IException("非预留凭证，不能取消");
		}
		if(!member.isSysMember() && !voucher.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		List<ScheduleSeat> seatList = new ArrayList<ScheduleSeat>();
		List<ScheduleStand> standList = new ArrayList<ScheduleStand>();
		if(voucher.hasSeat()){
			seatList = getScheduleSeatListByVoucherId(voucherId);
			for(ScheduleSeat seat : seatList){
				if(!seat.getStatus().equals(ScheduleSeat.STATUS_RESERVE)){
					throw new IException("座位状态非预留状态，不能取消");
				}
			}
		}else {
			standList = getScheduleStandListByVoucherId(voucherId);
			for(ScheduleStand stand : standList){
				if(!stand.getStatus().equals(ScheduleSeat.STATUS_RESERVE)){
					throw new IException("站票状态非预留状态，不能取消");
				}
			}
		}
		voucher.setReserveStatus("C");
		baseDao.saveObject(voucher);
		if(voucher.hasSeat()){
			for(ScheduleSeat seat : seatList){
				seat.setStatus(ScheduleSeat.STATUS_FREE);
			}
		}else {
			for(ScheduleStand stand : standList){
				stand.setStatus(ScheduleSeat.STATUS_FREE);
			}
		}
		baseDao.saveObjectList(seatList);
		baseDao.saveObjectList(standList);
		VoucherLog voucherLog = new VoucherLog(voucher, member, "reserveCancel");
		baseDao.saveObject(voucherLog);
		if(voucher.getTipTime()!=null){
			List<ReserveMessage> messageList = baseDao.getObjectListByField(ReserveMessage.class, "voucherId", voucherId);
			baseDao.removeObjectList(messageList);
		}
	}
	@Override
	public List<TicketVo> repeatPrint(Stadium stadium, ClientMember member, Long voucherId, String reprintPass)throws IException{
		Voucher voucher = baseDao.getObject(Voucher.class, voucherId);
		if(voucher==null){
			throw new IException("凭证不存在！");
		}
		if(!StringUtils.equalsIgnoreCase(reprintPass, stadium.getReprintPass())){
			throw new IException("重新打印密码不正确！");
		}
		if(!StringUtils.equals(voucher.getPayStatus(), "Y")){
			throw new IException("未付款的订单不能重复打印！");
		}
		if(!voucher.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		Schedule schedule = baseDao.getObject(Schedule.class, voucher.getScheduleId());
		Venue venue = baseDao.getObject(Venue.class, voucher.getVenueId());
		Program program = baseDao.getObject(Program.class, voucher.getProgramId());
		List<TicketVo> voList = new ArrayList<TicketVo>();
		Map<Long, SchedulePrice> priceMap = new HashMap<Long, SchedulePrice>();
		VoucherCustomer vcustomer = getVoucherCustomerByVoucherId(voucherId);
		if(voucher.hasSeat()){
			List<ScheduleSeat> seatList = getScheduleSeatListByVoucherId(voucherId);
			for(ScheduleSeat seat : seatList){
				SchedulePrice price = priceMap.get(seat.getPriceId());
				if(price==null){
					price = baseDao.getObject(SchedulePrice.class, seat.getPriceId());
					priceMap.put(seat.getPriceId(), price);
				}
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, seat.getScheduleVenueAreaId());
				TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, price, seat, voucher, vcustomer);
				voList.add(vo);
			}
		}else {
			List<ScheduleStand> standList = getScheduleStandListByVoucherId(voucherId);
			for(ScheduleStand stand : standList){
				SchedulePrice price = priceMap.get(stand.getPriceId());
				if(price==null){
					price = baseDao.getObject(SchedulePrice.class, stand.getPriceId());
					priceMap.put(stand.getPriceId(), price);
				}
				ScheduleVenueArea area = baseDao.getObject(ScheduleVenueArea.class, stand.getScheduleVenueAreaId());
				TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, price, stand, voucher, vcustomer);
				voList.add(vo);
			}
		}
		VoucherLog voucherLog = new VoucherLog(voucher, member, "repeatPrint");
		baseDao.saveObject(voucherLog);
		return voList;
	}
	@Override
	public void refund(Stadium stadium, ClientMember member, Long voucherId, String refundPass)throws IException{
		Voucher voucher = baseDao.getObject(Voucher.class, voucherId);
		if(voucher==null){
			throw new IException("凭证不存在！");
		}
		if(!voucher.getStadiumId().equals(stadium.getId())){
			throw new IException("非法操作！");
		}
		if(!StringUtils.equalsIgnoreCase(refundPass, stadium.getRefundPass())){
			throw new IException("退款密码不正确！");
		}
		if(!StringUtils.equals(voucher.getPayStatus(), "Y")){
			throw new IException("未付款的订单不能退款！");
		}
		if(StringUtils.equals(voucher.getRefundStatus(), "Y")){
			throw new IException("已退款的订单，不能重复操作！");
		}
		if(!voucher.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		Map<Long, SchedulePrice> priceMap = new HashMap<Long, SchedulePrice>();
		Map<Long, Integer> quantityMap = new HashMap<Long, Integer>();
		if(voucher.hasSeat()){
			List<ScheduleSeat> seatList = getScheduleSeatListByVoucherId(voucherId);
			for(ScheduleSeat seat : seatList){
				seat.setStatus(ScheduleSeat.STATUS_FREE);
				baseDao.saveObject(seat);
				SchedulePrice price = priceMap.get(seat.getPriceId());
				if(price==null){
					price = baseDao.getObject(SchedulePrice.class, seat.getPriceId());
					priceMap.put(seat.getPriceId(), price);
					quantityMap.put(price.getId(), 1);
				}else {
					quantityMap.put(price.getId(), quantityMap.get(price.getId())+1);
				}
			}
		}else {
			List<ScheduleStand> standList = getScheduleStandListByVoucherId(voucherId);
			for(ScheduleStand stand : standList){
				stand.setStatus(ScheduleSeat.STATUS_FREE);
				baseDao.saveObject(stand);
				SchedulePrice price = priceMap.get(stand.getPriceId());
				if(price==null){
					price = baseDao.getObject(SchedulePrice.class, stand.getPriceId());
					priceMap.put(stand.getPriceId(), price);
					quantityMap.put(price.getId(), 1);
				}else {
					quantityMap.put(price.getId(), quantityMap.get(price.getId())+1);
				}
			}
		}
		for(Long priceId : quantityMap.keySet()){
			SchedulePrice sprice = priceMap.get(priceId);
			sprice.addSold(-quantityMap.get(priceId));
			baseDao.saveObject(sprice);
		}
		voucher.setRefundStatus("Y");
		baseDao.saveObject(voucher);
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		Voucher newVoucher = new Voucher();
		BeanUtil.copyProperties(newVoucher, voucher);
		newVoucher.setId(null);
		newVoucher.setVoucherNo(TicketUtil.getVoucherNo(curtime));
		newVoucher.setPzType(VoucherType.PZTYPE_TP);
		newVoucher.setMemberId(member.getId());
		newVoucher.setMemberName(member.getMemberName());
		newVoucher.setAddTime(curtime);
		baseDao.saveObject(newVoucher);
		VoucherLog voucherLog = new VoucherLog(newVoucher, member, "refund");
		baseDao.saveObject(voucherLog);
	}
	private List<ScheduleSeat> getScheduleSeatListByVoucherId(Long voucherId){
		List<VoucherDetail> voucherDetaiList = baseDao.getObjectListByField(VoucherDetail.class, "voucherId", voucherId);
		List<Long> seatidList = BeanUtil.getBeanPropertyList(voucherDetaiList, Long.class, "seatid", true);
		List<ScheduleSeat> seatList = baseDao.getObjectList(ScheduleSeat.class, seatidList);
		return seatList;
	}
	private List<ScheduleStand> getScheduleStandListByVoucherId(Long voucherId){
		List<VoucherDetail> voucherDetaiList = baseDao.getObjectListByField(VoucherDetail.class, "voucherId", voucherId);
		List<Long> seatidList = BeanUtil.getBeanPropertyList(voucherDetaiList, Long.class, "seatid", true);
		List<ScheduleStand> seatList = baseDao.getObjectList(ScheduleStand.class, seatidList);
		return seatList;
	}
	private VoucherCustomer getVoucherCustomerByVoucherId(Long voucherId){
		List<VoucherCustomer> vcustomerList = baseDao.getObjectListByField(VoucherCustomer.class, "voucherId", voucherId);
		VoucherCustomer vcustomer = null;
		if(vcustomerList.size()>0){
			vcustomer = vcustomerList.get(0);
		}
		return vcustomer;
	}
	@Override
	public String getSeatLabelsByVoucherId(Voucher voucher){
		List<VoucherDetail> voucherDetaiList = baseDao.getObjectListByField(VoucherDetail.class, "voucherId", voucher.getId());
		List<String> seatLabelList = BeanUtil.getBeanPropertyList(voucherDetaiList, "seatLabel", false);
		return StringUtils.join(seatLabelList, ",");
	}
}
