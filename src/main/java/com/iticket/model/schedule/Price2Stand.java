package com.iticket.model.schedule;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

public class Price2Stand extends BaseObject{
	private static final long serialVersionUID = 6138236440987785934L;
	private String pskey;
	//场次Id
	private Long scheduleId;
	//场次价格Id
	private Long priceId;
	//场次区域Id
	private Long scheduleVenueAreaId;
	//站票总量
	private Integer total;
	//限制数
	private Integer vlimit;
	//用户id
	private Long memberId;
	//时间
	private Timestamp addTime;
	
	public Price2Stand(){
		
	}
	public Price2Stand(ClientMember member, Schedule schedule, Long priceId, Long scheduleVenueAreaId){
		this.pskey = priceId + "_" + scheduleVenueAreaId;
		this.scheduleId = schedule.getId();
		this.priceId = priceId;
		this.scheduleVenueAreaId = scheduleVenueAreaId;
		this.memberId = member.getId();
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public String getPskey() {
		return pskey;
	}
	public void setPskey(String pskey) {
		this.pskey = pskey;
	}
	public Long getScheduleVenueAreaId() {
		return scheduleVenueAreaId;
	}
	public void setScheduleVenueAreaId(Long scheduleVenueAreaId) {
		this.scheduleVenueAreaId = scheduleVenueAreaId;
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
		return pskey;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getPriceId() {
		return priceId;
	}
	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}
}
