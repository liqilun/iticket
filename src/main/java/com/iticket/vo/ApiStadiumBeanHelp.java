package com.iticket.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.iticket.Config;
import com.iticket.model.stadium.Seat;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.stadium.VenueArea;

public class ApiStadiumBeanHelp {
	public static Map<String, Object> getStatium(Stadium stadium){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("stadiumId", stadium.getId());
		resMap.put("cnName", stadium.getCnName());
		resMap.put("enName", stadium.getEnName());
		resMap.put("cnAddress", stadium.getCnAddress());
		resMap.put("telephone", stadium.getTelephone());
		resMap.put("contact", stadium.getContact());
		resMap.put("status", stadium.getStatus());
		resMap.put("orderNo", stadium.getOrderNo());
		resMap.put("refundPass", stadium.getRefundPass());
		resMap.put("reprintPass", stadium.getReprintPass());
		return resMap;
	}
	public static Map<String, Object> getVenue(Venue venue){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("venueId", venue.getId());
		resMap.put("cnName", venue.getCnName());
		resMap.put("enName", venue.getEnName());
		if(StringUtils.isNotBlank(venue.getBackground())){
			resMap.put("background", Config.IMAGE_PATH + venue.getBackground());
		}
		resMap.put("stadiumId", venue.getStadiumId());
		resMap.put("orderNo", venue.getOrderNo());
		resMap.put("status", venue.getStatus());
		return resMap;
	}
	public static Map<String, Object> getVenueArea(VenueArea area){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("venueAreaId", area.getId());
		resMap.put("cnName", area.getCnName());
		resMap.put("enName", area.getEnName());
		resMap.put("description", area.getDescription());
		resMap.put("standing", area.getStanding());
		resMap.put("total", area.getTotal());
		resMap.put("vlimit", area.getVlimit());
		if(StringUtils.isNotBlank(area.getIcon())){
			resMap.put("icon", Config.IMAGE_PATH + area.getIcon());
		}
		resMap.put("venueId", area.getVenueId());
		resMap.put("hotZone", area.getHotZone());
		resMap.put("gridWidth", area.getGridWidth());
		resMap.put("gridHeight", area.getGridHeight());
		return resMap;
	}
	
	public static Map<String, Object> getVenueAreaSeat(Seat seat){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("seatId", seat.getId());
		resMap.put("venueAreaId", seat.getVenueAreaId());
		resMap.put("lineno", seat.getLineno());
		resMap.put("rankno", seat.getRankno());
		resMap.put("x", seat.getX());
		resMap.put("y", seat.getY());
		return resMap;
	}
}
