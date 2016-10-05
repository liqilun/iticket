package com.iticket.model.api;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;

public class ClientMember extends BaseObject{
	private static final long serialVersionUID = 3473275747685505230L;
	//Id
	private Long id;
	//用户名
	private String memberName;
	//密码
	private String password;
	//场馆Id
	private Long stadiumId;
	//状态
	public String status;	
	//是否是管理员
	public String manager;	
	public ClientMember(){
		
	}
	public ClientMember(Long stadiumId, String memberName, String password){
		this.stadiumId = stadiumId;
		this.memberName = memberName;
		this.password = password;
		this.status = "Y";
		this.manager = "N";
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public boolean hasManage(){
		return StringUtils.equalsIgnoreCase(manager, "Y");
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public boolean isSysMember(){
		return id.equals(0L);
	}
}
