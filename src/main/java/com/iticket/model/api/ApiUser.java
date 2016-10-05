package com.iticket.model.api;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;
import com.iticket.util.DateUtil;

public class ApiUser extends BaseObject{
	private static final long serialVersionUID = -2932132947179135973L;
	private Long id;
	//场馆Id
	private Long stadiumId;
	//API账户标识
	private String appkey;
	//API账户私钥
	private String privatekey;
	//状态
	private String status;
	//添加时间
	private Timestamp addTime;
	//角色和普通用户，manager用户拥有所有权限
	private String role;		
	public ApiUser(){
		
	}
	public ApiUser(Long stadiumId, String appkey){
		this.stadiumId = stadiumId;
		this.appkey = appkey;
		this.status = "Y";
		this.role = "manage";
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getPrivatekey() {
		return privatekey;
	}
	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean hasManager(){
		return StringUtils.equalsIgnoreCase(role, "manager");
	}
}
