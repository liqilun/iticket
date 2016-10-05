package com.iticket.model.stadium;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Transient;

import com.iticket.model.BaseObject;
import com.iticket.util.DateUtil;

/**
 * 座位
 * 
 */
public class Seat extends BaseObject implements Cloneable{
	private static final long serialVersionUID = 2L;
	
	private Long id;
	//场馆区域编号
	private Long venueAreaId;
	//排
	private String lineno;
	//座(列)
	private String rankno;
	//物理坐标X
	private Integer x;
	//屋里坐标Y
	private Integer y;
	//记录添加时间
	private Timestamp addTime;
	public Seat(){
		
	}
	public Seat(Long venueAreaId, String lineno, String rankno, Integer x, Integer y){
		this.venueAreaId = venueAreaId;
		this.lineno = lineno;
		this.rankno = rankno;
		this.x = x;
		this.y = y;
		this.addTime = DateUtil.getCurFullTimestamp();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Long getVenueAreaId() {
		return venueAreaId;
	}

	public void setVenueAreaId(Long venueAreaId) {
		this.venueAreaId = venueAreaId;
	}
	
	@Transient
	public String getLocation(){
		return x+","+y;
	}
	@Transient
	public String getLabel(){
		return lineno +"," + rankno;
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