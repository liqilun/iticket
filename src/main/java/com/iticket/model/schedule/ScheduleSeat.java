package com.iticket.model.schedule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;

import com.iticket.model.stadium.Seat;
import com.iticket.util.StringUtil;

/**
 * 场次座位
 */
public class ScheduleSeat extends BaseSeatOrStand {
	private static final long serialVersionUID = -3536781289153837377L;
	public static final String[] STATUS_LABELS = {"释放","锁定","预留","售出","预购","作废"};
	//排
	private String lineno;
	//座(列)
	private String rankno;
	//物理坐标X
	private Integer x;
	//屋里坐标Y
	private Integer y;
	//锁定号
	private String lockNo;
	public ScheduleSeat() {
	}
	public ScheduleSeat(Schedule schedule, Seat seat, ScheduleVenueArea sarea){
		this.version = 0;
		this.scheduleId = schedule.getId();
		this.lineno = seat.getLineno();
		this.rankno = seat.getRankno();
		this.x = seat.getX();
		this.y = seat.getY();
		this.status = STATUS_FREE;
		this.scheduleVenueAreaId = sarea.getId();
		this.serialNum = RandomUtils.nextInt(1000000)+999999;
		this.barcode = StringUtil.getRandomString(12);
		this.uuid = StringUtil.getRandomString(10);
		this.numUuid = StringUtil.getDigitalRandomString(12);
	}
	public ScheduleSeat(Long id, Integer status, Date lockTime) {
		this.id = id;
		this.status = status;
		if(lockTime!=null){
			this.lockTime=new Timestamp(lockTime.getTime());
		}
	}
	public String getSeatLabel(){
		return lineno + "排"+rankno+"座";
	}
	@Override
	public Serializable realId() {
		return id;
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
	public String getLockNo() {
		return lockNo;
	}
	public void setLockNo(String lockNo) {
		this.lockNo = lockNo;
	}
	
}