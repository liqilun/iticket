package com.iticket.model.program;

import java.io.Serializable;
import java.sql.Timestamp;

import com.iticket.model.BaseObject;
import com.iticket.util.DateUtil;

/**
 * 项目
 */
public class Program extends BaseObject {
	private static final long serialVersionUID = 1970207140098123404L;
	//Id
	private Long id;
	//项目编号
	private String code;
	//项目中文名
	private String cnName;
	//英文名称
	private String enName;
	//场馆
	private Long stadiumId;
	//场地
	private Long venueId;
	//项目开始时间
	private Timestamp startTime;
	//项目结束时间
	private Timestamp endTime;
	//演唱会、话剧
	private Integer typeId;		
	//是否有效
	private String status;
	//项目图片地址
	private String imgurl;
	//用户id
	private Long memberId;
	//删除状态
	private String delStatus;
	//时间
	private Timestamp addTime;
	public Program() {
	}
	public Program(Long stadiumId, Long venueId, Long memberId) {
		this.stadiumId = stadiumId;
		this.venueId = venueId;
		this.memberId = memberId;
		this.delStatus = "N";
		this.addTime = DateUtil.getCurFullTimestamp();
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getCnName() {
		return cnName;
	}


	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public Long getStadiumId() {
		return stadiumId;
	}


	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}


	public Long getVenueId() {
		return venueId;
	}


	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}


	public Timestamp getStartTime() {
		return startTime;
	}


	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public Integer getTypeId() {
		return typeId;
	}


	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getImgurl() {
		return imgurl;
	}


	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
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