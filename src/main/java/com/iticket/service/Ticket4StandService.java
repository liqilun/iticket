package com.iticket.service;

import java.sql.Timestamp;
import java.util.List;

import com.iticket.model.api.ClientMember;
import com.iticket.vo.TicketVo;

public interface Ticket4StandService {
	List<TicketVo> sell(ClientMember member, Long scheduleId, String standIds, String pzType, String ticketType, String disType, 
			String showPrice, Long customerId, String customerName, String customerMobile, String customerAddress, String payMethod, Integer saleType) throws IException;
	Long reserve(ClientMember member, Long scheduleId, String standIds,  
			Long customerId, String customerName, String customerMobile, String customerAddress, Timestamp releaseTime, Timestamp tipTime, String disType) throws IException;
	List<Long> lockStand(ClientMember member, Long scheduleId, Long scheduleVenueAreaId, String lockStandBody) throws IException;
}
