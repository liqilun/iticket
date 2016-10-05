package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class XProgramPriceList {
	@XStreamImplicit(itemFieldName="programPrice")
	private List<XProgramPrice> programPriceList = new ArrayList<XProgramPrice>();

	public List<XProgramPrice> getProgramPriceList() {
		return programPriceList;
	}

	public void setProgramPriceList(List<XProgramPrice> programPriceList) {
		this.programPriceList = programPriceList;
	}
	
}
