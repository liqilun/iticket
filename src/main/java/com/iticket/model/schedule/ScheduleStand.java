package com.iticket.model.schedule;

import java.io.Serializable;

import org.apache.commons.lang.math.RandomUtils;

import com.iticket.util.StringUtil;

/**
 * 场次座位
 */
public class ScheduleStand extends BaseSeatOrStand {
	private static final long serialVersionUID = -3536781289153837377L;
		
	public ScheduleStand() {
	}
	public ScheduleStand(Schedule schedule, Long priceId, Long scheduleVenueAreaId) {
		this.version = 0;
		this.scheduleId = schedule.getId();
		this.priceId = priceId;
		this.scheduleVenueAreaId = scheduleVenueAreaId;
		this.serialNum = RandomUtils.nextInt(1000000)+999999;
		this.barcode = StringUtil.getRandomString(12);
		this.uuid = StringUtil.getRandomString(10);
		this.numUuid = StringUtil.getDigitalRandomString(12);
		this.status = STATUS_FREE;
	}
	@Override
	public Serializable realId() {
		return id;
	}
}