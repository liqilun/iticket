package com.iticket.web.action.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.api.ApiUser;
import com.iticket.model.layout.Layout;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Price2Stand;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Venue;
import com.iticket.service.IException;
import com.iticket.service.ProgramService;
import com.iticket.service.ScheduleService;
import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.iticket.vo.ApiScheduleBeanHelp;
import com.iticket.web.filter.OpenApiAuth;
import com.iticket.xml.XHelp;
import com.iticket.xml.XPrice2Seat;
import com.iticket.xml.XPrice2SeatList;

@Controller
public class ApiScheduleController extends BaseApiController{
	@Autowired@Qualifier("programService")
	private ProgramService programService;
	@Autowired@Qualifier("scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping("/inner/api/schedule/addSchedule.xhtml")
	public String addSchedule(Long scheduleId, String seat, Long venueId, String venueBackground, String fileType, ModelMap model, HttpServletRequest request) {
		Schedule schedule = null;
		try {
			OpenApiAuth auth = getOpenApiAuth(request);
			ApiUser user = auth.getApiUser();
			schedule = scheduleService.addSchedule(getClientMember(request), user.getStadiumId(), scheduleId, venueId, seat, venueBackground, fileType, request);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}catch (Exception e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMessage());
		}
		if(schedule==null){
			return getErrorXmlView(model, "创建场次异常");
		}
		return getOpenApiXmlDetail(ApiScheduleBeanHelp.getSchedule(schedule), "schedule", model, request);
	}
	@RequestMapping("/inner/api/schedule/delSchedule.xhtml")
	public String delSchedule(Long scheduleId, ModelMap model, HttpServletRequest request) {
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		schedule.setDelStatus("Y");
		daoService.saveObject(schedule);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/schedule/setStatus.xhtml")
	public String setStatus(Long scheduleId, String status, ModelMap model, HttpServletRequest request) {
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		schedule.setStatus(status);
		daoService.saveObject(schedule);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/schedule/setActive.xhtml")
	public String setActive(Long scheduleId, String active, ModelMap model, HttpServletRequest request) {
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		schedule.setActive(active);
		daoService.saveObject(schedule);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/schedule/scheduleList.xhtml")
	public String scheduleList(Long programId, Long venueId, Timestamp playTime, Timestamp playEndTime, ModelMap model, HttpServletRequest request) {
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		DetachedCriteria query = DetachedCriteria.forClass(Schedule.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		if(programId!=null){
			query.add(Restrictions.eq("programId", programId));
		}
		if(venueId!=null){
			query.add(Restrictions.eq("venueId", venueId));
		}
		if(playTime!=null){
			query.add(Restrictions.ge("playTime", playTime));
		}
		if(playEndTime!=null){
			query.add(Restrictions.le("playEndTime", playEndTime));
		}
		query.add(Restrictions.eq("delStatus", "N"));
		query.addOrder(Order.asc("playTime"));
		List<Schedule> scheduleList = daoService.findByCriteria(query);
		Map<Long, Venue> venueMap = new HashMap<Long, Venue>();
		Map<Long, Program> programMap = new HashMap<Long, Program>();
		for(Schedule schedule : scheduleList){
			Program program = programMap.get(schedule.getProgramId());
			if(program==null){
				program = daoService.getObject(Program.class, schedule.getProgramId());
				programMap.put(schedule.getProgramId(), program);
			}
			if(StringUtils.equals(program.getDelStatus(), "Y")){
				continue;
			}
			Venue venue = venueMap.get(schedule.getVenueId());
			if(venue==null){
				venue = daoService.getObject(Venue.class, schedule.getVenueId());
				venueMap.put(schedule.getVenueId(), venue);
			}
			Map<String,Object> resMap = ApiScheduleBeanHelp.getSchedule(schedule);
			resMap.put("venueCnName", venue.getCnName());
			resMap.put("programCnName", program.getCnName());
			resMap.put("venueEnName", venue.getEnName());
			resMap.put("programEnName", program.getEnName());
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "scheduleList,schedule", model, request);
	}
	
	@RequestMapping("/inner/api/schedule/addSchedulePrice.xhtml")
	public String addSchedulePrice(Long scheduleId, String priceBody, ModelMap model, HttpServletRequest request) {
		try {
			scheduleService.addSchedulePrice(getClientMember(request), scheduleId, priceBody);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getSuccessXmlView(model);
	}
	
	@RequestMapping("/inner/api/schedule/schedulePriceList.xhtml")
	public String schedulePriceList(Long scheduleId, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		List<SchedulePrice> priceList = daoService.findByHql("from SchedulePrice where scheduleId=?", scheduleId);
		for(SchedulePrice price : priceList){
			Map<String,Object> resMap = ApiScheduleBeanHelp.getSchedulePrice(price);
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "schedulePriceList,schedulePrice", model, request);
	}
	@RequestMapping("/inner/api/schedule/scheduleStandPriceList.xhtml")
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
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "schedulePriceList,schedulePrice", model, request);
	}
	@RequestMapping("/inner/api/schedule/price2Stand.xhtml")
	public String addPrice2Stand(Long scheduleId, Long scheduleVenueAreaId, String p2sBody, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		if(scheduleVenueAreaId==null){
			return getErrorXmlView(model, "缺少scheduleVenueAreaId");
		}
		try {
			scheduleService.addPrice2Stand(getClientMember(request), scheduleId, scheduleVenueAreaId, p2sBody);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/schedule/scheduleSeat.xhtml")
	public String scheduleSeat(Long scheduleId, Long scheduleVenueAreaId, ModelMap model, HttpServletRequest request) {
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		DetachedCriteria query = DetachedCriteria.forClass(ScheduleSeat.class);
		query.add(Restrictions.eq("scheduleId", scheduleId));
		if(scheduleVenueAreaId!=null){
			query.add(Restrictions.eq("scheduleVenueAreaId", scheduleVenueAreaId));
		}
		query.addOrder(Order.asc("x"));
		query.addOrder(Order.asc("y"));
		List<ScheduleSeat> seatList = daoService.findByCriteria(query);
		for(ScheduleSeat seat : seatList){
			Map<String,Object> resMap = ApiScheduleBeanHelp.getScheduleSeat(seat);
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "scheduleSeatList,scheduleSeat", model, request);
	}
	@RequestMapping("/inner/api/schedule/price2Seat.xhtml")
	public String price2Seat(Long scheduleId, String price2SeatBody, ModelMap model) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少场次id");
		}
		ResultCode<XPrice2SeatList> code = XHelp.getBean(price2SeatBody, "price2SeatList");
		if(!code.isSuccess()){
			return getErrorXmlView(model, code.getMsg());
		}
		List<ScheduleSeat> updSeatList = new ArrayList<ScheduleSeat>();
		List<XPrice2Seat> price2SeatList = code.getRetval().getPrice2SeatList();
		for(XPrice2Seat price2Seat : price2SeatList){
			List<Long> seatIdList = BeanUtil.getIdList(price2Seat.getSeatIds(), ",");
			List<ScheduleSeat> seatList = daoService.getObjectList(ScheduleSeat.class, seatIdList);
			for(ScheduleSeat seat : seatList){
				if(!scheduleId.equals(seat.getScheduleId())){
					return getErrorXmlView(model, "座位对应的场次和对应的场次id不匹配");
				}
				if(!seat.getStatus().equals(ScheduleSeat.STATUS_FREE)){
					return getErrorXmlView(model, seat.getSeatLabel()+"非空闲状态不能设置价格");
				}
				seat.setPriceId(price2Seat.getPriceId());
			}
			updSeatList.addAll(seatList);
		}
		daoService.saveObjectList(updSeatList);
		scheduleService.updScheudlePriceSnum(scheduleId);
		return getSuccessXmlView(model);
	}
	
	@RequestMapping("/inner/api/schedule/layout2Schedule.xhtml")
	public String addScheduleLayout(Long layoutId, Long scheduleId, ModelMap model, HttpServletRequest request) {
		if(layoutId==null){
			return getErrorXmlView(model, "缺少参数layoutId");
		}
		Layout layout = daoService.getObject(Layout.class, layoutId);
		if(layout==null){
			return getErrorXmlView(model, "票版不存在");
		}
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		schedule.setLayoutId(layoutId);
		daoService.saveObject(schedule);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/schedule/getScheduleLayout.xhtml")
	public String addScheduleLayout(Long scheduleId, ModelMap model, HttpServletRequest request) {
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在");
		}
		if(schedule.getLayoutId()==null){
			return getErrorXmlView(model, "该场次未设置票版");
		}
		Layout layout = daoService.getObject(Layout.class, schedule.getLayoutId());
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("layoutId", schedule.getLayoutId());
		resMap.put("scheduleId", scheduleId);
		resMap.put("programId", schedule.getProgramId());
		resMap.put("layoutData", layout.getLayoutData());
		return getOpenApiXmlDetail(resMap, "scheduleLayout", model, request);
	}
	@RequestMapping("/inner/api/schedule/scheduleVenueAreaList.xhtml")
	public String scheduleVenueAreaList(Long scheduleId, Long venueId, ModelMap model, HttpServletRequest request) {
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		List<ScheduleVenueArea> areaList = daoService.getObjectListByField(ScheduleVenueArea.class, "scheduleId", scheduleId);
		for(ScheduleVenueArea area : areaList){
			if(venueId==null || (venueId!=null && area.getVenueId().equals(venueId))){
				Map<String,Object> resMap = ApiScheduleBeanHelp.getScheduleVenueArea(area);
				resMapList.add(resMap);
			}
		}
		return getOpenApiXmlList(resMapList, "scheduleVenueAreaList,scheduleVenueArea", model, request);
	}
	@RequestMapping("/inner/api/schedule/setScheduleVenueAreaUnshow.xhtml")
	public String setScheduleVenueAreaUnshow(Long scheduleVenueAreaId, ModelMap model, HttpServletRequest request) {
		ScheduleVenueArea area = daoService.getObject(ScheduleVenueArea.class, scheduleVenueAreaId);
		if(area==null){
			return getErrorXmlView(model, "场次区域不存在");
		}
		Schedule schedule = daoService.getObject(Schedule.class, area.getScheduleId());
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		if(StringUtils.equals(area.getStatus(), ScheduleVenueArea.STATUS_SHOW)){
			area.setStatus(ScheduleVenueArea.STATUS_UNSHOW);
		}else {
			area.setStatus(ScheduleVenueArea.STATUS_SHOW);
		}
		daoService.saveObject(area);
		return getSuccessXmlView(model);
	}
}
