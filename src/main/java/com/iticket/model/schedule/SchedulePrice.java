package com.iticket.model.schedule;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

/**
 * 场次票价设置
 */
public class SchedulePrice extends BaseObject {
	private static final long serialVersionUID = -7641265660670259325L;
	//唯一约束   ----（priceId*scheduleAreaId）
	private Long id;
	//票价ID
	private Long color;
	//价格
	private Double price;
	//项目id
	private Long programId;
	//场次id
	private Long scheduleId;
	//描述
	private String description;
	//类型 C
	private String type;
	//站票、坐票数量
	private Integer snum;
	//卖出数量
	private Integer	sold;
	//用户id
	private Long memberId;
	//添加时间
	private Timestamp addTime;
	public SchedulePrice(){
		
	}
	public SchedulePrice(ClientMember member){
		this.memberId = member.getId();
		this.addTime = DateUtil.getCurFullTimestamp();
		this.sold = 0;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public Integer getSnum() {
		return snum;
	}
	public void setSnum(Integer snum) {
		this.snum = snum;
	}
	public Integer getSold() {
		return sold;
	}
	public void setSold(Integer sold) {
		this.sold = sold;
	}
	public void addSold(Integer s) {
		this.sold = this.sold + s;
	}
}