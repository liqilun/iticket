package com.iticket.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
import com.iticket.service.Ticket4StandService;
import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.iticket.vo.TicketVo;
import com.iticket.xml.XHelp;
import com.iticket.xml.XLockStand;
import com.iticket.xml.XLockStandList;

@Service("ticket4StandService")
public class Ticket4StandServiceImpl extends TicketBaseServiceImpl implements Ticket4StandService{
	@Override
	public List<TicketVo> sell(ClientMember member, Long scheduleId,
			String standIds, String pzType, String ticketType,
			String disType, String showPrice, Long customerId,
			String customerName, String customerMobile, String customerAddress,
			String payMethod, Integer saleType) throws IException {
		Schedule schedule = validateSellData(member, scheduleId, pzType, ticketType, disType, showPrice);
		List<Long> idList = BeanUtil.getIdList(standIds, ",");
		List<ScheduleStand> standList = baseDao.getObjectList(ScheduleStand.class, idList);
		if(standList.size()==0){
			throw new IException("站票数据不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(standList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			throw new IException("你选择的站票不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			throw new IException("你选择的站票所在的场次，和输入的场次不匹配");
		}
		ScheduleVenueArea area = null;
		Map<Long, SchedulePrice> spriceMap = new HashMap<Long, SchedulePrice>();
		Map<Long, Integer> quantityMap = new HashMap<Long, Integer>();
		double standAmount = 0;
		for(ScheduleStand stand : standList){
			if(stand.getStatus().equals(ScheduleStand.STATUS_SOLD)){
				throw new IException("选择的站票已被卖出，请重新操作");
			}
			if(area==null){
				area = baseDao.getObject(ScheduleVenueArea.class, stand.getScheduleVenueAreaId());
			}
			Long priceId = stand.getPriceId();
			SchedulePrice sprice = spriceMap.get(priceId);
			if(sprice==null){
				sprice = baseDao.getObject(SchedulePrice.class, priceId);
				spriceMap.put(priceId, sprice);
				quantityMap.put(priceId, 1);
			}else {
				quantityMap.put(priceId, quantityMap.get(priceId)+1);
			}
			standAmount = standAmount + sprice.getPrice();
		}
		Voucher voucher = new Voucher(member, standList, schedule);
		Program program = baseDao.getObject(Program.class, schedule.getProgramId());
		Venue venue = baseDao.getObject(Venue.class, program.getVenueId());
		copyData2Voucher(member, voucher, program, venue, area, showPrice, pzType, disType, ticketType, standAmount, payMethod, saleType);
		voucher.setPayStatus("Y");
		voucher.setReserveStatus("N");
		voucher.setRefundStatus("N");
		voucher.setDiscount(getDiscount(standAmount, disType));
		baseDao.saveObject(voucher);
		List<VoucherDetail> vdetailList = new ArrayList<VoucherDetail>();
		for(ScheduleStand stand : standList){
			VoucherDetail vdetail = new VoucherDetail(stand, voucher.getId(), voucher.getPzType(), spriceMap.get(stand.getPriceId()));
			vdetail.setScheduleVenueAreaId(area.getId());
			vdetail.setAreaCnName(area.getCnName());
			vdetailList.add(vdetail);
			stand.setStatus(ScheduleSeat.STATUS_SOLD);
		}
		for(Long priceId : quantityMap.keySet()){
			SchedulePrice sprice = spriceMap.get(priceId);
			sprice.addSold(quantityMap.get(priceId));
			baseDao.saveObject(sprice);
		}
		baseDao.saveObjectList(standList);
		baseDao.saveObjectList(vdetailList);
		VoucherCustomer vcustomer = addVoucherCustomer(member, voucher, customerId, customerName, customerMobile, customerAddress);
		VoucherLog voucherLog = new VoucherLog(voucher, member, "sell");
		baseDao.saveObject(voucherLog);
		List<TicketVo> voList = new ArrayList<TicketVo>();
		Stadium stadium = baseDao.getObject(Stadium.class, schedule.getStadiumId());
		for(ScheduleStand stand : standList){
			TicketVo vo = new TicketVo(stadium, venue, area, program, schedule, spriceMap.get(stand.getPriceId()), stand, voucher, vcustomer);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public Long reserve(ClientMember member, Long scheduleId, 
			String standIds,  Long customerId, String customerName,
			String customerMobile, String customerAddress,
			Timestamp releaseTime, Timestamp tipTime, String disType)
			throws IException {
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
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		if(StringUtils.isBlank(customerName)){
			throw new IException("请输入预留客户姓名");
		}
		if(StringUtils.isBlank(customerMobile)){
			throw new IException("请输入预留客户手机号");
		}
		List<Long> idList = BeanUtil.getIdList(standIds, ",");
		List<ScheduleStand> standList = baseDao.getObjectList(ScheduleStand.class, idList);
		if(standList.size()==0){
			throw new IException("站票数据不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(standList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			throw new IException("你选择的站票在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			throw new IException("你选择的站票所在的场次，和输入的场次不匹配");
		}
		for(ScheduleStand stand : standList){
			if(stand.getStatus()!=ScheduleSeat.STATUS_LOCKED){
				dbLogger.warn(stand.getStatus()+","+ScheduleSeat.STATUS_FREE);
				throw new IException("站票不处于空闲状态，不能预留");
			}
		}
		ScheduleVenueArea area = null;
		Map<Long, SchedulePrice> spriceMap = new HashMap<Long, SchedulePrice>();
		double standAmount = 0;
		for(ScheduleStand stand : standList){
			if(area==null){
				area = baseDao.getObject(ScheduleVenueArea.class, stand.getScheduleVenueAreaId());
			}
			SchedulePrice sprice = spriceMap.get(stand.getPriceId());
			if(sprice==null){
				sprice = baseDao.getObject(SchedulePrice.class, stand.getPriceId());
				spriceMap.put(stand.getPriceId(), sprice);
			}
			standAmount = standAmount + sprice.getPrice();
		}
		Voucher voucher = new Voucher(member, standList, schedule);
		Program program = baseDao.getObject(Program.class, schedule.getProgramId());
		Venue venue = baseDao.getObject(Venue.class, program.getVenueId());
		copyData2Voucher(member, voucher, program, venue, area, null, VoucherType.PZTYPE_YL, disType, null, standAmount, null, null);
		voucher.setPayStatus("N");
		voucher.setReserveStatus("Y");
		voucher.setRefundStatus("N");
		voucher.setReleaseTime(releaseTime);
		voucher.setTipTime(tipTime);
		baseDao.saveObject(voucher);
		List<VoucherDetail> vdetailList = new ArrayList<VoucherDetail>();
		for(ScheduleStand stand : standList){
			VoucherDetail vdetail = new VoucherDetail(stand, voucher.getId(), voucher.getPzType(), spriceMap.get(stand.getPriceId()));
			vdetail.setScheduleVenueAreaId(area.getId());
			vdetail.setAreaCnName(area.getCnName());
			vdetailList.add(vdetail);
			stand.setStatus(ScheduleSeat.STATUS_RESERVE);
		}
		baseDao.saveObjectList(standList);
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

	@Override
	public List<Long> lockStand(ClientMember member, Long scheduleId, Long scheduleVenueAreaId,
			String lockStandBody) throws IException {
		if(scheduleId==null){
			throw new IException("缺少scheduleId");
		}
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在！");
		}
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ResultCode<XLockStandList> code = XHelp.getBean(lockStandBody, "lockStandList");
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		List<XLockStand> lockStandList = code.getRetval().getLockStandList();
		if(lockStandList.size()==0){
			throw new IException("解析数据为空！");
		}
		List<ScheduleStand> allStandList = new ArrayList<ScheduleStand>();
		for(XLockStand lockStand : lockStandList){
			int quantity = lockStand.getQuantity();
			DetachedCriteria query = DetachedCriteria.forClass(ScheduleStand.class);
			query.add(Restrictions.eq("scheduleId", scheduleId));
			query.add(Restrictions.eq("priceId", lockStand.getPriceId()));
			query.add(Restrictions.eq("scheduleVenueAreaId", scheduleVenueAreaId));
			query.add(Restrictions.eq("status", ScheduleStand.STATUS_FREE));
			List<ScheduleStand> standList = baseDao.findByCriteria(query, 0, quantity);
			int size = standList.size();
			if(size<quantity){
				SchedulePrice sprice = baseDao.getObject(SchedulePrice.class, lockStand.getPriceId());
				throw new IException(sprice.getPrice()+"元剩余" + size + "个，不能卖出" + quantity + "个");
			}
			allStandList.addAll(standList);
		}
		for(ScheduleStand stand : allStandList){
			stand.setStatus(ScheduleStand.STATUS_LOCKED);
		}
		baseDao.saveObjectList(allStandList);
		List<Long> standIdList = BeanUtil.getBeanPropertyList(allStandList, Long.class, "id", false);
		return standIdList;
	}

}
