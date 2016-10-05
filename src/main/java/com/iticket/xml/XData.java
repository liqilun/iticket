package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("data")
public class XData {
	private XSeatList seatList;
	private XProgramPriceList programPriceList;
	private XSchedulePriceList schedulePriceList;
	private XPrice2SeatList price2SeatList;
	private XPrice2StandList price2StandList;
	private XLockStandList lockStandList;
	public XPrice2SeatList getPrice2SeatList() {
		return price2SeatList;
	}

	public void setPrice2SeatList(XPrice2SeatList price2SeatList) {
		this.price2SeatList = price2SeatList;
	}

	public XSchedulePriceList getSchedulePriceList() {
		return schedulePriceList;
	}

	public void setSchedulePriceList(XSchedulePriceList schedulePriceList) {
		this.schedulePriceList = schedulePriceList;
	}

	public XProgramPriceList getProgramPriceList() {
		return programPriceList;
	}

	public void setProgramPriceList(XProgramPriceList programPriceList) {
		this.programPriceList = programPriceList;
	}

	public XSeatList getSeatList() {
		return seatList;
	}

	public void setSeatList(XSeatList seatList) {
		this.seatList = seatList;
	}

	public XPrice2StandList getPrice2StandList() {
		return price2StandList;
	}

	public void setPrice2StandList(XPrice2StandList price2StandList) {
		this.price2StandList = price2StandList;
	}

	public XLockStandList getLockStandList() {
		return lockStandList;
	}

	public void setLockStandList(XLockStandList lockStandList) {
		this.lockStandList = lockStandList;
	}
	
}
