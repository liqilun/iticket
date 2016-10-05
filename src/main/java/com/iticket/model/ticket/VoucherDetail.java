package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.util.DateUtil;
/**
 * 凭证详情（对应一个座位）
 */
public class VoucherDetail extends BaseObject {
	private static final long serialVersionUID = -1802101075577570520L;
	//版本
	private Integer version;
	//ID
	private Long id;				
	//凭证ID
	private Long voucherId; 	
	//座位ID
	private Long seatid;	
	//区域ID
	private Long scheduleVenueAreaId;	
	//区域名称
	private String areaCnName;
	//价格ID
	private Long priceid;
	//原价
	private Double ticketPrice;
	//打票次数
	private Integer times;	
	//状态
	private String status;	
	//凭证类型
	private String pzType;		
	//是否自助取票
	private String selfTake;	
	//座位
	private String seatLabel;
	//添加时间
	private Timestamp addTime;		
	public VoucherDetail() {
		super();
	}

	public VoucherDetail(ScheduleVenueArea scheduleVenueArea, ScheduleSeat seat, Long voucherId, String pzType,Double price) {
		this.version = 0;
		this.scheduleVenueAreaId = scheduleVenueArea.getId();
		this.areaCnName = scheduleVenueArea.getCnName();
		this.voucherId = voucherId;
		this.ticketPrice = price;
		this.scheduleVenueAreaId = seat.getScheduleVenueAreaId();
		this.seatid = seat.getId();
		this.priceid = seat.getPriceId();
		this.pzType = pzType;
		this.status = "N";
		this.addTime = DateUtil.getMillTimestamp();
		this.times = 0;
		this.selfTake = "N";
		this.seatLabel = seat.getSeatLabel();
	}
	public VoucherDetail(ScheduleStand stand, Long voucherId, String pzType, SchedulePrice sprice) {
		this.version = 0;
		this.voucherId = voucherId;
		this.ticketPrice = sprice.getPrice();
		this.scheduleVenueAreaId = stand.getScheduleVenueAreaId();
		this.seatid = stand.getId();
		this.priceid = stand.getPriceId();
		this.pzType = pzType;
		this.status = "N";
		this.addTime = DateUtil.getMillTimestamp();
		this.times = 0;
		this.selfTake = "N";
	}
	public VoucherDetail(VoucherDetail vd) {
		this.voucherId = vd.getVoucherId();
		this.ticketPrice = vd.getTicketPrice();
		this.scheduleVenueAreaId = vd.getScheduleVenueAreaId();
		this.seatid = vd.getSeatid();
		this.priceid = vd.getPriceid();
		this.pzType = vd.getPzType();
		this.status = "N";
		this.addTime = DateUtil.getMillTimestamp();
		this.times = 0;
		this.selfTake = "N";
		this.seatLabel = vd.getSeatLabel();
	}

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Long getPriceid() {
		return priceid;
	}

	public void setPriceid(Long priceid) {
		this.priceid = priceid;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public Long getSeatid() {
		return seatid;
	}

	public void setSeatid(Long seatid) {
		this.seatid = seatid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getAreaCnName() {
		return areaCnName;
	}

	public void setAreaCnName(String areaCnName) {
		this.areaCnName = areaCnName;
	}

	public String getSelfTake() {
		return selfTake;
	}

	public void setSelfTake(String selfTake) {
		this.selfTake = selfTake;
	}

	public String getSeatLabel() {
		return seatLabel;
	}

	public void setSeatLabel(String seatLabel) {
		this.seatLabel = seatLabel;
	}

	public String getPzType() {
		return pzType;
	}

	public void setPzType(String pzType) {
		this.pzType = pzType;
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

	public Long getScheduleVenueAreaId() {
		return scheduleVenueAreaId;
	}

	public void setScheduleVenueAreaId(Long scheduleVenueAreaId) {
		this.scheduleVenueAreaId = scheduleVenueAreaId;
	}
}
