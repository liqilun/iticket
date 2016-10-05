package com.iticket.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 业务相关的entity基类，项目中entity共有的代码可以放在这里
 */
public abstract class BasicBizEntity extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	public static final String TRUE = "Y"; //是
	public static final String FALSE = "N"; //否
	//记录添加时间
	protected Timestamp addTime;
	//记录更新时间
	protected Timestamp updateTime;
	
	public abstract Long getId();

	public abstract void setId(Long id);
	
	@Override
	public Serializable realId() {
		return this.getId();
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
