package com.iticket.vo;

import java.util.List;

import com.iticket.model.ticket.VoucherCustomer;

public class ReserveInfoVo {
	private Long voucherId;
	private String ticketType;
	private String payType;
	private String logisticsType;
	private String saleType;
	private String disType;
	private List<ReservePriceVo> priceList;
	private VoucherCustomer customer;
	public ReserveInfoVo(){
		
	}
	public ReserveInfoVo(Long voucherId, String ticketType, String payType, String logisticsType, String saleType, String disType, 
			VoucherCustomer customer, List<ReservePriceVo> priceList){
		this.voucherId = voucherId;
		this.ticketType = ticketType;
		this.payType = payType;
		this.logisticsType = logisticsType;
		this.saleType = saleType;
		this.disType = disType;
		this.customer = customer;
		this.priceList = priceList;
	}
	public List<ReservePriceVo> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ReservePriceVo> priceList) {
		this.priceList = priceList;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getLogisticsType() {
		return logisticsType;
	}

	public void setLogisticsType(String logisticsType) {
		this.logisticsType = logisticsType;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getDisType() {
		return disType;
	}

	public void setDisType(String disType) {
		this.disType = disType;
	}
	public VoucherCustomer getCustomer() {
		return customer;
	}
	public void setCustomer(VoucherCustomer customer) {
		this.customer = customer;
	}
	public Long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}
}
