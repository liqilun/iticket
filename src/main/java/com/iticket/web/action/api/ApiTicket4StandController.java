package com.iticket.web.action.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.PaymentType;
import com.iticket.constant.SaleType;
import com.iticket.constant.VoucherType;
import com.iticket.model.schedule.BaseSeatOrStand;
import com.iticket.model.schedule.Price2Stand;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.service.IException;
import com.iticket.service.ProgramService;
import com.iticket.service.ScheduleService;
import com.iticket.service.Ticket4StandService;
import com.iticket.util.BeanUtil;
import com.iticket.vo.ApiScheduleBeanHelp;
import com.iticket.vo.TicketVo;

@Controller
public class ApiTicket4StandController extends BaseApiController{
	@Autowired@Qualifier("programService")
	private ProgramService programService;
	@Autowired@Qualifier("scheduleService")
	private ScheduleService scheduleService;
	@Autowired@Qualifier("ticket4StandService")
	private Ticket4StandService ticket4StandService;
	@RequestMapping("/inner/api/ticket/getSchedulePriceList.xhtml")
	public String scheduleStandPriceList(Long scheduleId, Long scheduleVenueAreaId, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		if(scheduleVenueAreaId==null){
			return getErrorXmlView(model, "缺少scheduleVenueAreaId");
		}
		List<SchedulePrice> priceList = daoService.getObjectListByField(SchedulePrice.class, "scheduleId", scheduleId);
		Map<Long, SchedulePrice> priceMap = BeanUtil.beanListToMap(priceList, "id");
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		List<Price2Stand> p2StandList = daoService.findByHql("from Price2Stand where scheduleVenueAreaId=?", scheduleVenueAreaId);
		for(Price2Stand p2s : p2StandList){
			Map<String,Object> resMap = ApiScheduleBeanHelp.getSchedulePrice(priceMap.get(p2s.getPriceId()), p2s);
			resMap.put("remain", getSelledCount(p2s));
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "schedulePriceList,schedulePrice", model, request);
	}
	@RequestMapping("/inner/api/ticket/scheduleVenueAreaSoldCount.xhtml")
	public String scheduleVenueAreaSoldCount(Long scheduleId, Long scheduleVenueAreaId, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		if(scheduleVenueAreaId==null){
			return getErrorXmlView(model, "缺少scheduleVenueAreaId");
		}
		ScheduleVenueArea area = daoService.getObject(ScheduleVenueArea.class, scheduleVenueAreaId);
		Map<Long, Integer> totalMap = new HashMap<Long, Integer>();
		Map<Long, Integer> soldMap = new HashMap<Long, Integer>();
		List<BaseSeatOrStand> ssList = new ArrayList<BaseSeatOrStand>();
		if(area.hasSeat()){
			List<ScheduleSeat> seatList = daoService.getObjectListByField(ScheduleSeat.class, "scheduleVenueAreaId", scheduleVenueAreaId);
			ssList.addAll(seatList);
		}else {
			List<ScheduleStand> standList = daoService.getObjectListByField(ScheduleStand.class, "scheduleVenueAreaId", scheduleVenueAreaId);
			ssList.addAll(standList);
		}
		for(BaseSeatOrStand ss : ssList){
			Integer total = totalMap.get(ss.getPriceId());
			if(total==null){
				total = 0;
			}
			totalMap.put(ss.getPriceId(), total+1);
			Integer sold = soldMap.get(ss.getPriceId());
			if(sold==null){
				sold = 0;
				soldMap.put(ss.getPriceId(), sold);
			}
			if(ss.getStatus().equals(BaseSeatOrStand.STATUS_SOLD)){
				soldMap.put(ss.getPriceId(), sold+1);
			}
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		for(Long priceId : totalMap.keySet()){
			SchedulePrice sp = daoService.getObject(SchedulePrice.class, priceId);
			Map<String, Object> resMap = ApiScheduleBeanHelp.getSchedulePrice(sp);
			if(!area.hasSeat()){
				Price2Stand p2s = daoService.getObject(Price2Stand.class, priceId + "_" + scheduleVenueAreaId);
				resMap = ApiScheduleBeanHelp.getSchedulePrice(sp, p2s);
			}
			resMap.put("snum", totalMap.get(priceId));
			resMap.put("sold", soldMap.get(priceId));
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "schedulePriceList,schedulePrice", model, request);
	}
	private Long getSelledCount(Price2Stand p2s){
		String hql = "select count(*) from ScheduleStand where priceId=? and scheduleVenueAreaId=? and status=?";
		List list = daoService.findByHql(hql, p2s.getPriceId(), p2s.getScheduleVenueAreaId(), ScheduleStand.STATUS_FREE);
		return Long.valueOf(list.get(0)+"");
	}
	@RequestMapping("/inner/api/ticket/toSoldOrReserve4Stand.xhtml")
	public String toSoldOrReserve4Stand(Long scheduleId, Long scheduleVenueAreaId, String lockStandBody, ModelMap model, HttpServletRequest request) {
		List<Long> standIdList = null;
		try {
			standIdList = ticket4StandService.lockStand(getClientMember(request), scheduleId, scheduleVenueAreaId, lockStandBody);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("ticketType", getMap2Str(VoucherType.TICKETTYPEMAP));
		resMap.put("payType", getMap2Str(PaymentType.PAYMETHODMAP));
		resMap.put("logisticsType", getMap2Str(VoucherType.WULIUMAP));
		resMap.put("saleType", getMap2Str(SaleType.saleTypeMap));
		resMap.put("disType", getMap2Str(VoucherType.DISTYPE_MAP));
		resMap.put("standIds", StringUtils.join(standIdList, ","));
		return getOpenApiXmlDetail(resMap, "IType", model, request);
	}
	@RequestMapping("/inner/api/ticket/unLock4Stand.xhtml")
	public String unLockSeat(Long scheduleId, String standIds, ModelMap model) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		List<Long> idList = BeanUtil.getIdList(standIds, ",");
		List<ScheduleStand> standList = daoService.getObjectList(ScheduleStand.class, idList);
		if(standList.size()==0){
			return getErrorXmlView(model, "坐票不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(standList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			return getErrorXmlView(model, "你选择的座位在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			return getErrorXmlView(model, "你选择的座位说在的场次，和输入的场次不匹配");
		}
		List<ScheduleStand> updList = new ArrayList<ScheduleStand>();
		for(ScheduleStand stand : standList){
			if(stand.getStatus().equals(ScheduleSeat.STATUS_LOCKED)){
				stand.setStatus(ScheduleSeat.STATUS_FREE);
				updList.add(stand);
			}
		}
		daoService.saveObjectList(updList);
		return getSuccessXmlView(model);
	}
	//正常出票
	@RequestMapping("/inner/api/ticket/sell4Sand.xhtml")
	public String sell(Long scheduleId, String standIds, String ticketType/*出票类型*/, String showPrice, String disType,
			Long customerId, String customerName, String customerMobile, String customerAddress, 
			String payMethod, Integer saleType, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		try {
			String pzType = VoucherType.PZTYPE_CP;
			List<TicketVo> voList = ticket4StandService.sell(getClientMember(request), scheduleId, standIds, pzType, ticketType, disType, showPrice, customerId, customerName, customerMobile, customerAddress, payMethod, saleType);
			resMapList = BeanUtil.copyToListMap(voList);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getOpenApiXmlList(resMapList, "ticketVoList,ticketVo", model, request);
	}
	//预留
	@RequestMapping("/inner/api/ticket/reserve4Stand.xhtml")
	public String reserve(Long scheduleId, String standIds, Long customerId, String customerName, String customerMobile, String customerAddress, String disType, Timestamp releaseTime, Timestamp tipTime, ModelMap model, HttpServletRequest request){
		try {
			Long voucherId = ticket4StandService.reserve(getClientMember(request), scheduleId, standIds, customerId, customerName, customerMobile, customerAddress, releaseTime, tipTime, disType);
			return getSingleResultXmlView(model, voucherId);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
	}
	
}
