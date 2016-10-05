package com.iticket.model.stadium;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

/**
 * 小场地（属于场馆）
 */
public class Venue extends BaseObject {
	private static final long serialVersionUID = -3521331818907898749L;
	//Id
	private Long id;
	//中文名
	private String cnName;
	//区域英文名
	private String enName;
	//场馆底图
	private String background;
	//所属场地
	private Long stadiumId;
	//排序
	private Integer orderNo;
	//用户id
	private Long memberId;
	//是否有效[Y,N,D]
	private String status;
	//时间
	private Timestamp addTime;	
	public Venue(){
		
	}
	public Venue(ClientMember member){
		this.stadiumId = member.getStadiumId();
		this.status = "Y";
		this.memberId = member.getId();
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
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	@Override
	public String toString() {
		return "Venue [id=" + id + ", cnName=" + cnName
				+ ", background=" + background + ", stadiumId=" + stadiumId
				+ ", orderNo=" + orderNo + ", status=" + status + "]";
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
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
