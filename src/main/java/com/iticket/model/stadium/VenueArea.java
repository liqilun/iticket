package com.iticket.model.stadium;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.util.DateUtil;

/**
 * 场地区域
 */
public class VenueArea extends BaseObject {
	private static final long serialVersionUID = 2444430752202843246L;
	//Id
	private Long id;
	//场馆id
	private Long stadiumId;
	//区域中文名
	private String cnName;
	//区域英文名
	private String enName;
	//区域描述
	private String description;
	//座位数
	private Integer seatNum;
	//是否站票
	private String standing;
	//站票总量:站票最多能卖多少票
	private Integer total;
	//限制数(特指站票)最后要剩几张票，以防人爆满
	private Integer vlimit;	
	//区域图标
	private String icon;
	//所属场地id
	private Long venueId;
	//热区
	private String hotZone;
	//表格宽度
	private Integer gridWidth;
	//表格高度
	private Integer gridHeight;
	//用户id
	private Long memberId;
	//状态
	private String status;
	//时间
	private Timestamp addTime;
	public VenueArea(){
		
	}
	public VenueArea(ClientMember member, Venue venue){
		this.stadiumId = venue.getStadiumId();
		this.venueId = venue.getId();
		this.memberId = member.getId();
		this.status = "Y";
		this.seatNum = 0;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStanding() {
		return standing;
	}

	public void setStanding(String standing) {
		this.standing = standing;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getVlimit() {
		return vlimit;
	}

	public void setVlimit(Integer vlimit) {
		this.vlimit = vlimit;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getHotZone() {
		return hotZone;
	}

	public void setHotZone(String hotZone) {
		this.hotZone = hotZone;
	}

	public Integer getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(Integer gridWidth) {
		this.gridWidth = gridWidth;
	}

	public Integer getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(Integer gridHeight) {
		this.gridHeight = gridHeight;
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
	public boolean hasSeat(){
		return StringUtils.equalsIgnoreCase(standing, "N");
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public Integer getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}
}
