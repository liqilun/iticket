package com.iticket.model.schedule;

import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.util.SeatUtil;

public abstract class BaseSeatOrStand extends BaseObject{
	private static final long serialVersionUID = -1602220888936602102L;
	public static final Integer STATUS_FREE = 1;//空闲
	public static final Integer STATUS_LOCKED = 2;//锁定
	public static final Integer STATUS_RESERVE = 3;//预留
	public static final Integer STATUS_SOLD = 4;//售出
	public static final Integer STATUS_ORDERED = 5;//预购
	public static final Integer STATUS_DEAD = 6;//作废
	//Id
	protected Long id;
	//版本
	protected Integer version;
	//场次ID
	protected Long scheduleId;
	//价格ID
	protected Long priceId;
	//场次区域ID
	protected Long scheduleVenueAreaId;
	//状态
	protected Integer status;
	//锁定的用户
	protected Long lockUserId;
	//锁定的时间
	protected Timestamp lockTime;
	//序号 (7位)
	protected Integer serialNum;
	//条形码(12位)
	protected String barcode;
	//唯一码(英文+数字)，验证用(10位)
	protected String uuid;
	//数字唯一码（随机,纯数字,12位）
	protected String numUuid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
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
	public Long getScheduleVenueAreaId() {
		return scheduleVenueAreaId;
	}
	public void setScheduleVenueAreaId(Long scheduleVenueAreaId) {
		this.scheduleVenueAreaId = scheduleVenueAreaId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Timestamp getLockTime() {
		return lockTime;
	}
	public void setLockTime(Timestamp lockTime) {
		this.lockTime = lockTime;
	}
	public Integer getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getNumUuid() {
		return numUuid;
	}
	public void setNumUuid(String numUuid) {
		this.numUuid = numUuid;
	}
	public Long getLockUserId() {
		return lockUserId;
	}
	public void setLockUserId(Long lockUserId) {
		this.lockUserId = lockUserId;
	}
	public Integer getRealStatus() {
		return SeatUtil.getRealStatus(this);
	}
	
}
