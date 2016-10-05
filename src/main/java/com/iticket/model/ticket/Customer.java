package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

/**
 * 客户
 */
public class Customer extends BaseObject{
	
	private static final long serialVersionUID = 8398267083181142905L;
	//Id
	private Long id;
	//客户姓名
	private String name;
	//电话
	private String telephone;
	//手机
	private String mobile;
	//地址
	private String address;
	//场馆Id
	private Long stadiumId;
	//用户Id
	private Long memberId;
	//添加时间
	private Timestamp addTime;
	public Customer() {
		super();
	}
	public Customer(ClientMember member) {
		super();
		this.memberId = member.getId();
		this.stadiumId = member.getStadiumId();
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
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
}
