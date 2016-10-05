package com.iticket.vo.report;

import java.sql.Timestamp;

/**
 * 座位明细
 */
public class SeatDetail {
	private Double price ;
	private String areaName;
	private String lineno;
	private String specialLineno;
	private String rankno;
	private String statusName;
	private String checkStatusName;
	private String playTime;
	
	private Integer status;
	private Integer checkStatus;
	private Timestamp lockTime;
	private Long ticketPoolId;
	
	public SeatDetail() {
	}
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getLineno() {
		return lineno;
	}
	public void setLineno(String lineno) {
		this.lineno = lineno;
	}
	public String getRankno() {
		return rankno;
	}
	public void setRankno(String rankno) {
		this.rankno = rankno;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer statusCode) {
		this.status = statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String status) {
		this.statusName = status;
	}
	public String getCheckStatusName() {
		return checkStatusName;
	}
	public void setCheckStatusName(String checkStatus) {
		this.checkStatusName = checkStatus;
	}
	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String scheduleName) {
		this.playTime = scheduleName;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatusCode) {
		this.checkStatus = checkStatusCode;
	}
	public Timestamp getLockTime() {
		return lockTime;
	}
	public void setLockTime(Timestamp lockTime) {
		this.lockTime = lockTime;
	}
	public Long getTicketPoolId() {
		return ticketPoolId;
	}
	public void setTicketPoolId(Long ticketPoolId) {
		this.ticketPoolId = ticketPoolId;
	}

	public String getSpecialLineno() {
		return specialLineno;
	}

	public void setSpecialLineno(String specialLineno) {
		this.specialLineno = specialLineno;
	}
	
}
