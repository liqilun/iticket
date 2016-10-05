package com.iticket.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.iticket.model.api.ApiUser;
import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Seat;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.VenueArea;
import com.iticket.service.IException;
import com.iticket.service.StadiumService;
import com.iticket.support.ResultCode;
import com.iticket.util.StringUtil;
import com.iticket.xml.XHelp;
import com.iticket.xml.XSeat;
import com.iticket.xml.XSeatList;
@Service("stadiumService")
public class StadiumServiceImpl extends BaseServiceImpl implements StadiumService{
	//添加管理员
	@Override
	public ResultCode<Stadium> addStadiumApi(String cnName, String cnAddress,
			String telephone, String contact, String appkey) {
		if(StringUtils.isBlank(cnName) || StringUtils.isBlank(cnAddress) || StringUtils.isBlank(telephone) ||
				StringUtils.isBlank(contact)){
			return ResultCode.getFailure("信息不完整");
		}
		if(StringUtils.isBlank(appkey)){
			return ResultCode.getFailure("请输入appkey");
		}
		Stadium stadium = new Stadium();
		stadium.setCnAddress(cnAddress);
		stadium.setCnName(cnName);
		stadium.setTelephone(telephone);
		stadium.setContact(contact);
		baseDao.saveObject(stadium);
		
		ApiUser apiUser = new ApiUser(stadium.getId(), appkey);
		String text = StringUtil.getRandomString(32).toLowerCase();
		apiUser.setPrivatekey(text);
		baseDao.saveObject(apiUser);
		
		ClientMember member = new ClientMember();
		member.setManager("Y");
		member.setStatus("Y");
		member.setStadiumId(stadium.getId());
		member.setMemberName(appkey);
		member.setPassword(StringUtil.getDigitalRandomString(8).toLowerCase());
		baseDao.saveObject(member);
		return ResultCode.getSuccessReturn(stadium);
	}
	@Override
	public void addVenueAreaSeatList(ClientMember member, Long venueAreaId, String seatBody, Integer gridWidth,Integer gridHeight)
			throws IException {
		VenueArea venueArea = baseDao.getObject(VenueArea.class, venueAreaId);
		if(venueArea==null){
			throw new IException("场次区域不存在");
		}
		if(!member.getStadiumId().equals(venueArea.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ResultCode<XSeatList> code = XHelp.getBean(seatBody, "seatList");
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		if(gridWidth!=null || gridHeight!=null){
			if(gridWidth!=null){
				venueArea.setGridWidth(gridWidth);
			}
			if(gridHeight!=null){
				venueArea.setGridHeight(gridHeight);
			}
			baseDao.saveObject(venueArea);
		}
		List<Seat> seatList = baseDao.getObjectListByField(Seat.class, "venueAreaId", venueAreaId);
		baseDao.removeObjectList(seatList);
		List<XSeat> xseatList = code.getRetval().getSeatList();
		List<Seat> nseatList = new ArrayList<Seat>();
		for(XSeat xseat : xseatList){
			Seat seat = new Seat(venueAreaId, xseat.getLineno(), xseat.getRankno(), xseat.getX(), xseat.getY());
			nseatList.add(seat);
		}
		baseDao.saveObjectList(nseatList);
	}

}
