package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("price2Seat")
public class XPrice2Seat {
	private Long priceId;
	private String seatIds;
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	public String getSeatIds() {
		return seatIds;
	}
	public void setSeatIds(String seatIds) {
		this.seatIds = seatIds;
	}
}
