package com.iticket.support;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;

import com.iticket.web.support.TimestampEditor;
public class RequestMapDataBinder extends DataBinder {
	public RequestMapDataBinder(Object target) {
		super(target);
		regEditor();
	}

	public RequestMapDataBinder(Object target, String objectName) {
		super(target, objectName);
		regEditor();
	}
	private void regEditor(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
		TimestampEditor timestampEditor = new TimestampEditor();
		dateFormat.setLenient(false);
		//TODO:timestampFormat.setLenient(false);
		this.registerCustomEditor(Date.class, null, dateEditor);
		this.registerCustomEditor(Timestamp.class, null, timestampEditor);
	}

	public void bind(Map dwrRequestMap) {
		MutablePropertyValues mpvs = new RequestMapPropertyValues(dwrRequestMap);
		doBind(mpvs);
	}
}
