package com.iticket.service;

import java.sql.Timestamp;
import java.util.List;

import com.iticket.model.api.ClientMember;
import com.iticket.vo.TicketVo;

public interface Ticket4SeatService {
	/**
	 * 直接销售
	 * @param member
	 * @param scheduleId
	 * @param seatIds
	 * @param pzType
	 * @param ticketType
	 * @param disType
	 * @param showPrice
	 * @param customerId
	 * @param customerName
	 * @param customerMobile
	 * @param customerAddress
	 * @param payMethod
	 * @param saleType
	 * @return
	 * @throws IException
	 */
	List<TicketVo> sell(ClientMember member, Long scheduleId, String seatIds, String pzType, String ticketType, String disType, 
			String showPrice, Long customerId, String customerName, String customerMobile, String customerAddress, String payMethod, Integer saleType) throws IException;
	/**
	 * 预留
	 * @param scheduleId
	 * @param seatIds
	 * @param vcustomer
	 * @param customerId
	 * @return
	 * @throws IException
	 */
	Long reserve(ClientMember member, Long scheduleId, String seatIds,
			Long customerId, String customerName, String customerMobile, String customerAddress, Timestamp releaseTime, Timestamp tipTime, String disType) throws IException;
}
