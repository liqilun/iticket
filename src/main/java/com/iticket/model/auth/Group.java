package com.iticket.model.auth;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

public class Group extends BaseObject{
	private static final long serialVersionUID = 2645405669215456456L;
	private Long id;
	private Long stadiumId;
	private String groupName;
	private Long memberId;
	private String memberName;
	private String module;
	private Timestamp addTime;
	public Group(){
		
	}
	public Group(ClientMember member, String groupName, String module){
		this.stadiumId = member.getStadiumId();
		this.groupName = groupName;
		this.module = module;
		this.memberId = member.getId();
		this.memberName = member.getMemberName();
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
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
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
