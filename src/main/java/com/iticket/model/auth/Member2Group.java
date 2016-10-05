package com.iticket.model.auth;

import java.io.Serializable;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;

public class Member2Group extends BaseObject{
	private static final long serialVersionUID = 7384051822698075252L;
	private Long id;
	private Long memberId;
	private Long groupId;
	public Member2Group(){
		
	}
	public Member2Group(ClientMember member, Long groupId){
		this.memberId = member.getId();
		this.groupId = groupId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
