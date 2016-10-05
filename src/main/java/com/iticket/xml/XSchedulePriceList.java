package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class XSchedulePriceList {
	@XStreamImplicit(itemFieldName="schedulePrice")
	private List<XSchedulePrice> schedulePriceList = new ArrayList<XSchedulePrice>();

	public List<XSchedulePrice> getSchedulePriceList() {
		return schedulePriceList;
	}

	public void setSchedulePriceList(List<XSchedulePrice> schedulePriceList) {
		this.schedulePriceList = schedulePriceList;
	}
	
}
