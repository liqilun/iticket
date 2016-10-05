package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("schedulePrice")
public class XSchedulePrice {
	private Long priceId;
	// 票价ID
	private Long color;
	// 价格
	private Double price;
	// 项目id
	private Long programId;
	// 场次id
	private Long scheduleId;
	// 描述
	private String description;
	//类型 C
	private String type;
	// 场次区域Id
	private Long scheduleAreaId;

	public XSchedulePrice(){
		
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public Long getColor() {
		return color;
	}

	public void setColor(Long color) {
		this.color = color;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getScheduleAreaId() {
		return scheduleAreaId;
	}

	public void setScheduleAreaId(Long scheduleAreaId) {
		this.scheduleAreaId = scheduleAreaId;
	}

}
