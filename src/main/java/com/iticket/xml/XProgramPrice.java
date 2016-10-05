package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("programPrice")
public class XProgramPrice {
	private Long programPriceId;
	private Long color;
	private Double price;
	private Double deposit;
	private Long programId;
	private String description;
	private String remark;
	private String type;
	public XProgramPrice(){
		
	}
	
	public Long getProgramPriceId() {
		return programPriceId;
	}

	public void setProgramPriceId(Long programPriceId) {
		this.programPriceId = programPriceId;
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
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
