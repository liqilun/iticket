package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("seatList") 
public class XSeatList {
	@XStreamImplicit(itemFieldName="seat")
	private List<XSeat> seatList = new ArrayList<XSeat>();

	public List<XSeat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<XSeat> seatList) {
		this.seatList = seatList;
	}
	
}
