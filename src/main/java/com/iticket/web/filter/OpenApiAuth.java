package com.iticket.web.filter;

import com.iticket.model.api.ApiUser;
import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;

public class OpenApiAuth {
	private ApiUser apiUser;
	private ClientMember clientMember;
	private Stadium stadium;
	private String msg;
	private String code;
	private String remoteIp;
	
	public OpenApiAuth(Stadium stadium, ApiUser apiUser, ClientMember clientMember, String remoteIp){
		this.stadium = stadium;
		this.apiUser = apiUser;
		this.clientMember = clientMember;
		this.remoteIp = remoteIp;
	}
	public ClientMember getClientMember() {
		return clientMember;
	}
	public void setClientMember(ClientMember clientMember) {
		this.clientMember = clientMember;
	}
	public ApiUser getApiUser() {
		return apiUser;
	}
	public void setApiUser(ApiUser apiUser) {
		this.apiUser = apiUser;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public OpenApiAuth(ApiUser apiUser){
		this.apiUser = apiUser;
	}
	public Stadium getStadium() {
		return stadium;
	}
	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}
	
}
