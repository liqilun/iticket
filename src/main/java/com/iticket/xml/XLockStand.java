package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("lockStand")
public class XLockStand {
	private Long priceId;
	private Integer quantity;
	public XLockStand(){
		
	}
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
