package com.iticket.xml;

import org.apache.commons.lang.StringUtils;

import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XHelp<T> {
	public static <T> ResultCode<T> getBean(String xml, String property){
		if(StringUtils.isBlank(xml)){
			return ResultCode.getFailure("xml为空，不能解析");
		}
		try {
			XStream xstream = new XStream(new StaxDriver());
			xstream.processAnnotations(XData.class);
			XData xdata = (XData)xstream.fromXML(xml);
			T t = (T)BeanUtil.get(xdata, property);
			return ResultCode.getSuccessReturn(t);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultCode.getFailure("解析xml异常");
		}
	}
}
