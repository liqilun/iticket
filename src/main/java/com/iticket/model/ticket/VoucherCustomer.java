package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
/**
 * 凭证-客户信息
 */
public class VoucherCustomer extends BaseObject {
	
	private static final long serialVersionUID = 2833743087022426991L;
	//ID
	private Long id; 			
	//凭证Id
	private Long voucherId;		
	//客户姓名
	private String customerName;
	//客户手机
	private String customerMobile;		
	//客户电话
	private String customerPhone;	
	//省
	private String provinceName;	
	//市
	private String cityName;	
	//客户地址
	private String customerAddress;	
	//物流方式
	private String transport;
	//短信手机号
	private String msgMobile;	
	//物流地址
	private String transportAddress;	
	//关联信息
	private String relatedInfo;	
	//身份证号
	private String idnumber;			
	//记录添加时间
	private Timestamp addTime;
	public VoucherCustomer() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getMsgMobile() {
		return msgMobile;
	}

	public void setMsgMobile(String msgMobile) {
		this.msgMobile = msgMobile;
	}

	public String getTransportAddress() {
		return transportAddress;
	}

	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}

	public String getRelatedInfo() {
		return relatedInfo;
	}

	public void setRelatedInfo(String relatedInfo) {
		this.relatedInfo = relatedInfo;
	}

	@Override
	public Serializable realId() {
		return id;
	}

	public Long getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
}
