package com.iticket.web.util;

public class HttpResult {
	private boolean success;
	private String response;
	private String msg;
	private int status;

	public HttpResult(boolean success, String response, String msg) {
		this.success = success;
		this.response = response;
		this.msg = msg;
	}

	public HttpResult(boolean success, String response, String msg, int status) {
		this.success = success;
		this.response = response;
		this.msg = msg;
		this.status = status;
	}

	public static HttpResult getFailure(String response) {
		return new HttpResult(false, response, "");
	}

	public static HttpResult getFailure(String msg, int status) {
		return new HttpResult(false, null, msg, status);
	}

	public static HttpResult getFailure(String msg, int status, String content) {
		return new HttpResult(false, content, msg, status);
	}
	
	public static HttpResult getSuccessReturn(String result) {
		return new HttpResult(true, result, null, 200);
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
