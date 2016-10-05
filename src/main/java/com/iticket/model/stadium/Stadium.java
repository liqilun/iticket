package com.iticket.model.stadium;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.validator.constraints.NotBlank;

import com.iticket.model.BaseObject;
import com.iticket.util.DateUtil;
import com.iticket.util.StringUtil;

/**
 * 大场馆
 */
public class Stadium extends BaseObject {
	private static final long serialVersionUID = 1672750972173108586L;
	//Id
	private Long id;
	//中文名称
	@NotBlank(message="{entity.name.null}")
	private String cnName;
	//英文名称
	private String enName;
	//中文地址
	@NotBlank(message="请输入场馆地址")
	private String cnAddress;
	//电话
	private String telephone;
	//联系人
	@NotBlank(message="请输入联系人")
	private String contact;
	//是否有效
	private String status;
	//排序
	private Integer orderNo;
	//退票密码
	private String refundPass;
	//重新打印密码
	private String reprintPass;
	//时间
	private Timestamp addTime;
	public Stadium() {
		this.orderNo = 1;
		this.status = "Y";
		this.refundPass = StringUtil.getDigitalRandomString(6);
		this.reprintPass = StringUtil.getDigitalRandomString(6);
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getCnAddress() {
		return cnAddress;
	}

	public void setCnAddress(String cnAddress) {
		this.cnAddress = cnAddress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getRefundPass() {
		return refundPass;
	}
	public void setRefundPass(String refundPass) {
		this.refundPass = refundPass;
	}
	public String getReprintPass() {
		return reprintPass;
	}
	public void setReprintPass(String reprintPass) {
		this.reprintPass = reprintPass;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
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
