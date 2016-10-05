package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("price2StandList")
public class XPrice2StandList {
	@XStreamImplicit(itemFieldName="price2Stand")
	private List<XPrice2Stand> price2StandList = new ArrayList<XPrice2Stand>();

	public List<XPrice2Stand> getPrice2StandList() {
		return price2StandList;
	}

	public void setPrice2StandList(List<XPrice2Stand> price2StandList) {
		this.price2StandList = price2StandList;
	}
	
}
