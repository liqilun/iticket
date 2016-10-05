package com.iticket.model.schedule;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;
import com.iticket.model.stadium.VenueArea;
import com.iticket.util.DateUtil;

/**
 * 场次场地区域
 */
public class ScheduleVenueArea extends BaseObject {
	private static final long serialVersionUID = -7388597240751274680L;
	public static final String STATUS_SHOW = "show";
	public static final String STATUS_UNSHOW = "unshow";
	//Id
	private Long id;
	//场次ID
	private Long scheduleId;
	//区域中文名
	private String cnName;
	//英文名称
	private String enName;
	//区域描述
	private String description;
	//是否站票,取值：Y,N
	private String standing;
	//站票总量
	private Integer total;
	//限制数
	private Integer vlimit;
	//区域图标
	private String icon;
	//场地id
	private Long venueId;
	//场馆场地区域ID
	private Long venueAreaId;
	//热区
	private String hotZone;
	//表格宽度
	private Integer gridWidth;
	//表格高度
	private Integer gridHeight;
	//状态
	private String status;
	//时间
	private Timestamp addTime;
	public ScheduleVenueArea() {
	}
	public ScheduleVenueArea(Schedule schedule, VenueArea area) {
		this.scheduleId = schedule.getId();
		this.cnName = area.getCnName();
		this.description = area.getDescription();
		this.standing = area.getStanding();
		this.total = area.getTotal();
		this.vlimit = area.getVlimit();
		this.icon = area.getIcon();
		this.venueId = area.getVenueId();
		this.hotZone = area.getHotZone();
		this.gridHeight = area.getGridHeight();
		this.gridWidth = area.getGridWidth();
		this.venueAreaId = area.getId();
		this.addTime = DateUtil.getCurFullTimestamp();
		this.status = STATUS_SHOW;
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

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getVenueAreaId() {
		return venueAreaId;
	}

	public void setVenueAreaId(Long venueAreaId) {
		this.venueAreaId = venueAreaId;
	}

	@Override
	public String toString() {
		return "ScheduleVenueArea [id=" + id + ", cnName=" + cnName
				+ ", standing=" + standing + ", scheduleId=" + scheduleId + "]";
	}

	public Integer getVlimit() {
		return vlimit;
	}

	public void setVlimit(Integer vlimit) {
		this.vlimit = vlimit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
