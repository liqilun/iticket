package com.iticket.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.iticket.Config;
import com.iticket.model.schedule.Price2Stand;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleVenueArea;

public class ApiScheduleBeanHelp {
	public static Map<String, Object> getSchedule(Schedule s){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("scheduleId", s.getId());
		resMap.put("code", s.getCode());
		resMap.put("cnName", s.getCnName());
		resMap.put("enName", s.getEnName());
		resMap.put("stadiumId", s.getStadiumId());
		resMap.put("venueId", s.getVenueId());
		resMap.put("playTime", s.getPlayTime());
		resMap.put("playEndTime", s.getPlayEndTime());
		
		resMap.put("integerernalTime", s.getIntegerernalTime());
		resMap.put("integerernalEndTime", s.getIntegerernalEndTime());
		
		resMap.put("active", s.getActive());
		resMap.put("seat", s.getSeat());
		resMap.put("programId", s.getProgramId());
		resMap.put("layoutId", s.getLayoutId());
		resMap.put("status", s.getStatus());
		resMap.put("fixed", s.getFixed());
		resMap.put("venueBackground", Config.IMAGE_PATH + s.getVenueBackground());
		
		return resMap;
	}
	public static Map<String, Object> getSchedulePrice(SchedulePrice price){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("priceId", price.getId());
		resMap.put("scheduleId", price.getScheduleId());
		resMap.put("color", price.getColor());
		resMap.put("price", price.getPrice());
		resMap.put("programId", price.getProgramId());
		resMap.put("description", price.getDescription());
		resMap.put("type", price.getType());
		resMap.put("addTime", price.getAddTime());
		resMap.put("snum", price.getSnum());
		resMap.put("sold", price.getSold());
		return resMap;
	}
	public static Map<String, Object> getSchedulePrice(SchedulePrice price, Price2Stand p2s){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("priceId", price.getId());
		resMap.put("scheduleId", price.getScheduleId());
		resMap.put("color", price.getColor());
		resMap.put("price", price.getPrice());
		resMap.put("programId", price.getProgramId());
		resMap.put("description", price.getDescription());
		resMap.put("type", price.getType());
		
		resMap.put("total", p2s.getTotal());
		resMap.put("vlimit", p2s.getVlimit());
		
		resMap.put("snum", price.getSnum());
		resMap.put("sold", price.getSold());
		
		return resMap;
	}
	public static Map<String, Object> getScheduleSeat(ScheduleSeat seat){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("seatId", seat.getId());
		resMap.put("scheduleId", seat.getScheduleId());
		resMap.put("priceId", seat.getPriceId());
		resMap.put("status", seat.getStatus());
		resMap.put("scheduleVenueAreaId", seat.getScheduleVenueAreaId());
		resMap.put("lineno", seat.getLineno());
		resMap.put("rankno", seat.getRankno());
		resMap.put("x", seat.getX());
		resMap.put("y", seat.getY());
		return resMap;
	}
	
	public static Map<String, Object> getScheduleVenueArea(ScheduleVenueArea area){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("scheduleVenueAreaId", area.getId());
		resMap.put("scheduleId", area.getScheduleId());
		resMap.put("cnName", area.getCnName());
		resMap.put("enName", area.getEnName());
		resMap.put("description", area.getDescription());
		resMap.put("gridHeight", area.getGridHeight());
		resMap.put("gridWidth", area.getGridWidth());
		resMap.put("hotZone", area.getHotZone());
		if(StringUtils.isNotBlank(area.getIcon())){
			resMap.put("icon", Config.IMAGE_PATH + area.getIcon());
		}
		resMap.put("standing", area.getStanding());
		resMap.put("total", area.getTotal());
		resMap.put("venueAreaId", area.getVenueAreaId());
		resMap.put("venueId", area.getVenueId());
		resMap.put("vlimit", area.getVlimit());
		resMap.put("status", area.getStatus());
		return resMap;
	}
}
