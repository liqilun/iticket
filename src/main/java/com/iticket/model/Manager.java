package com.iticket.model;

import java.io.Serializable;
//后台用户
public class Manager extends BaseObject{
	private static final long serialVersionUID = -2005823516490851505L;
	private Long id;
	private String username;
	private String password;
	public Manager(){
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public Serializable realId() {
		return id;
	}
}
