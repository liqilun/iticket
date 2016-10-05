package com.iticket.service;

import javax.servlet.http.HttpServletRequest;

import com.iticket.model.api.ClientMember;
import com.iticket.model.schedule.Schedule;

public interface ScheduleService {
	Schedule addSchedule(ClientMember member, Long stadiumId, Long scheduleId, Long venueId, String seat, String venueBackground, String fileType, HttpServletRequest request) throws IException;

	void addSchedulePrice(ClientMember member, Long scheduleId, String priceBody) throws IException;
	void addScheduleStandPrice(ClientMember member, Long scheduleId, String priceBody) throws IException;

	void addPrice2Stand(ClientMember member, Long scheduleId,
			Long scheduleVenueAreaId, String p2sBody) throws IException;
	
	void updScheudlePriceSnum(Long scheduleId);
}
