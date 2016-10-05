package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("price2Stand")
public class XPrice2Stand {
	//场次价格Id
	private Long priceId;
	private Double price;
	//站票总量
	private Integer total;
	//限制数
	private Integer vlimit;
	public XPrice2Stand(){
		
	}
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getVlimit() {
		return vlimit;
	}
	public void setVlimit(Integer vlimit) {
		this.vlimit = vlimit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
