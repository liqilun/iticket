package com.iticket.service;


public class IException extends Exception {
	private static final long serialVersionUID = -7147976717398138894L;
	public static IException ILLEGAL = new IException("9998", "非法操作！", null);
	private String msg;
	private String code;
	private String detailMsg;
	public IException(String code, String msg){
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	public IException(String msg){
		super(msg);
		this.code = "9999";
		this.msg = msg;
	}
	public IException(String code, String msg, String detailMsg){
		this(code, msg);
		this.detailMsg = detailMsg;
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
	public String getDetailMsg() {
		return detailMsg;
	}
	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}
}
