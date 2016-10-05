package com.iticket.model.layout;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;
//票版
public class Layout extends BaseObject{
	private static final long serialVersionUID = 4756777841954299777L;
	//Id
	private Long id;
	//场馆Id
	private Long stadiumId;
	//标题
	private String title;
	//票版内容
	private String layoutData;
	//用户Id
	private Long memberId;
	//状态
	private String status;
	//时间
	private Timestamp addTime;
	public Layout(){
		
	}
	public Layout(String title, String layoutData, ClientMember member){
		this.stadiumId = member.getStadiumId();
		this.title = title;
		this.layoutData = layoutData;
		this.memberId = member.getId();
		this.addTime = DateUtil.getCurFullTimestamp();
		this.status = "Y";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLayoutData() {
		return layoutData;
	}
	public void setLayoutData(String layoutData) {
		this.layoutData = layoutData;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
