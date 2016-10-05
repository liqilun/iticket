package com.iticket.service;

import java.util.List;

import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.ticket.Voucher;
import com.iticket.vo.TicketVo;

public interface TicketService {
	/**
	 * 预售卖出
	 * @param member
	 * @param voucherId
	 * @param ticketType
	 * @param payMethod
	 * @param showPrice
	 * @param saleType
	 * @param disType
	 * @return
	 * @throws IException
	 */
	List<TicketVo> reserveSell(ClientMember member, Long voucherId,
			String ticketType, String payMethod, String showPrice, Integer saleType, String disType)
			throws IException;
	/**
	 * 预售取消
	 * @param member
	 * @param voucherId
	 * @throws IException
	 */
	void reserveCancel(ClientMember member, Long voucherId) throws IException;
	/**
	 * 重复打印
	 * @param member
	 * @param voucherId
	 * @return
	 * @throws IException
	 */
	List<TicketVo> repeatPrint(Stadium stadium, ClientMember member, Long voucherId, String reprintPass)throws IException;
	/**
	 * 退款
	 * @param member
	 * @param voucherId
	 * @throws IException
	 */
	void refund(Stadium stadium, ClientMember member, Long voucherId, String refundPass) throws IException;
	/**
	 * 根据凭证获取座位名称
	 * @param voucher 
	 * @return
	 */
	String getSeatLabelsByVoucherId(Voucher voucher);
}
