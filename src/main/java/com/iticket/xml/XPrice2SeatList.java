package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("price2SeatList") 
public class XPrice2SeatList {
	@XStreamImplicit(itemFieldName="price2Seat")
	private List<XPrice2Seat> price2SeatList = new ArrayList<XPrice2Seat>();

	public List<XPrice2Seat> getPrice2SeatList() {
		return price2SeatList;
	}

	public void setPrice2SeatList(List<XPrice2Seat> price2SeatList) {
		this.price2SeatList = price2SeatList;
	}
	
}
