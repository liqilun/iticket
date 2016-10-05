package com.iticket.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("lockStandList")
public class XLockStandList {
	@XStreamImplicit(itemFieldName="lockStand")
	private List<XLockStand> lockStandList = new ArrayList<XLockStand>();

	public List<XLockStand> getLockStandList() {
		return lockStandList;
	}

	public void setLockStandList(List<XLockStand> lockStandList) {
		this.lockStandList = lockStandList;
	}
	
}
