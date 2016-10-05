package com.iticket.service;

import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;
import com.iticket.support.ResultCode;

public interface StadiumService {
	ResultCode<Stadium> addStadiumApi(String cnName, String cnAddress, String telephone, String contact, String appkey);
	void addVenueAreaSeatList(ClientMember member, Long venueAreaId, String seatBody, Integer gridWidth,Integer gridHeight) throws IException;
}
