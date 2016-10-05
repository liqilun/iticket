package com.iticket.model.schedule;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;
import com.iticket.support.ResultCode;
import com.iticket.util.DateUtil;

/**
 * 场次
 */
public class Schedule extends BaseObject {
	private static final long serialVersionUID = -4373469495299618083L;
	private Long id;
	private Long stadiumId;
	//场次编号
	private String code;
	//场次中文名
	private String cnName;
	//场次英文名
	private String enName;
	//是否有效
	private String status;
	//是否支持在线选座[同一个场次可能有选座，也有可能有站票]
	private String seat;
	//场次时间(1.固定时间的为：放映时间，2.无固定时间为：开始时间)
	private Timestamp playTime;
	//场次结束时间
	private Timestamp playEndTime;
	//内部出票时间
	private Timestamp integerernalTime;
	//内部结束时间
	private Timestamp integerernalEndTime;
	//外部出票时间
	private Timestamp externalTime;
	//外部结束时间
	private Timestamp externalEndTime;
	//是否激活
	private String active;
	//演出项目编号
	private Long programId;
	//场地ID
	private Long venueId;
	//是否固定时间场次
	private String fixed;
	//场地底图
	private String venueBackground;
	//票版id
	private Long layoutId;
	//用户id
	private Long memberId;
	//删除状态
	private String delStatus;
	//时间
	private Timestamp addTime;
	public Schedule() {
	}
	public Schedule(Long memberId) {
		this.memberId = memberId;
		this.active = "Y";
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public Timestamp getPlayTime() {
		return playTime;
	}

	public void setPlayTime(Timestamp playTime) {
		this.playTime = playTime;
	}

	public Timestamp getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(Timestamp playEndTime) {
		this.playEndTime = playEndTime;
	}

	public Timestamp getExternalTime() {
		return externalTime;
	}

	public void setExternalTime(Timestamp externalTime) {
		this.externalTime = externalTime;
	}

	public Timestamp getExternalEndTime() {
		return externalEndTime;
	}

	public void setExternalEndTime(Timestamp externalEndTime) {
		this.externalEndTime = externalEndTime;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Timestamp getIntegerernalTime() {
		return integerernalTime;
	}

	public void setIntegerernalTime(Timestamp integerernalTime) {
		this.integerernalTime = integerernalTime;
	}

	public Timestamp getIntegerernalEndTime() {
		return integerernalEndTime;
	}

	public void setIntegerernalEndTime(Timestamp integerernalEndTime) {
		this.integerernalEndTime = integerernalEndTime;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	@Override
	public String toString() {
		return "Schedule [id=" + id + ", cnName=" + cnName + "]";
	}

	public String getVenueBackground() {
		return venueBackground;
	}

	public void setVenueBackground(String venueBackground) {
		this.venueBackground = venueBackground;
	}
	public Long getStadiumId() {
		return stadiumId;
	}

	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}

	public Long getLayoutId() {
		return layoutId;
	}
	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	public boolean supportSeat(){
		return StringUtils.equalsIgnoreCase(seat, "Y");
	}
	public ResultCode booking(){
		if(!StringUtils.equals(status, "Y")){
			return ResultCode.getFailure("场次状态为无效，不能操作");
		}
		if(!StringUtils.equals(active, "Y")){
			return ResultCode.getFailure("场次处于未激活状态，不能操作");
		}
		if(StringUtils.equals(delStatus, "Y")){
			return ResultCode.getFailure("场次已被删除，不能操作");
		}
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		if(integerernalTime!=null){
			if(curtime.before(integerernalTime)){
				return ResultCode.getFailure("还未到出票时间！");
			}
		}
		if(integerernalEndTime!=null){
			if(curtime.after(integerernalEndTime)){
				return ResultCode.getFailure("出票时间已结束！");
			}
		}
		if(StringUtils.equals(fixed, "Y")){
			
			if(curtime.after(playEndTime)){
				return ResultCode.getFailure("场次已过期，不能操作");
			}
		}
		return ResultCode.SUCCESS;
	}
}