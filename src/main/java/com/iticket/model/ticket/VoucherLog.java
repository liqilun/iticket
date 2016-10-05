package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;
//凭证日志
public class VoucherLog extends BaseObject{
	private static final long serialVersionUID = 8722438033886582126L;
	private Long id;
	//凭证Id
	private Long voucherId;
	//场馆Id
	private Long stadiumId;
	//场次Id
	private Long scheduleId;
	//项目Id
	private Long programId; 	
	//用户Id
	private Long memberId;
	//日志类型
	private String logType;
	//用户名
	private String memberName;
	//添加时间
	private Timestamp addTime;
	public VoucherLog(){
		
	}
	public VoucherLog(Voucher voucher, ClientMember member, String logType){
		this.voucherId = voucher.getId();
		this.stadiumId = voucher.getStadiumId();
		this.scheduleId = voucher.getScheduleId();
		this.programId = voucher.getProgramId();
		this.addTime = DateUtil.getCurFullTimestamp();
		this.memberId = member.getId();
		this.memberName = member.getMemberName();
		this.logType = logType;
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
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
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
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String gainLogTypeText(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("sell", "售出");
		map.put("refund", "退款");
		map.put("reserve", "预留");
		map.put("reserveSell", "预留售出");
		map.put("repeatPrint", "重新打印");
		map.put("reserveCancel", "预留取消");
		return map.get(logType);
	}
}
