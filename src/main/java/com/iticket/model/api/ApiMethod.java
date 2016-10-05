package com.iticket.model.api;

import java.io.Serializable;

import com.iticket.model.BaseObject;

public class ApiMethod extends BaseObject{
	private static final long serialVersionUID = -5522661124019203983L;
	private Long id;
	private String method;
	private String methodType;
	private String description;
	private String resInfo;
	private Integer orderNo;
	private String module;
	private String reqUrl;
	public ApiMethod(){
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResInfo() {
		return resInfo;
	}
	public void setResInfo(String resInfo) {
		this.resInfo = resInfo;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	@Override
	public Serializable realId() {
		return id;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
}
