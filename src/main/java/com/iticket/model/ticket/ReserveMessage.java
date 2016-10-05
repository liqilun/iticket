package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;

public class ReserveMessage extends BaseObject{
	private static final long serialVersionUID = 4674185697674444648L;
	private Long id;
	//场馆Id
	private Long stadiumId;
	//凭证Id
	private Long voucherId;
	//释放时间
	private Timestamp releaseTime;
	//提醒时间
	private Timestamp tipTime;
	public ReserveMessage(){
		
	}
	public ReserveMessage(Voucher voucher){
		this.voucherId = voucher.getId();
		this.stadiumId = voucher.getStadiumId();
		this.releaseTime = voucher.getReleaseTime();
		this.tipTime = voucher.getTipTime();
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
	public Timestamp getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	public Timestamp getTipTime() {
		return tipTime;
	}
	public void setTipTime(Timestamp tipTime) {
		this.tipTime = tipTime;
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}	
}
