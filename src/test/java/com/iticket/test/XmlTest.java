package com.iticket.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.iticket.support.ResultCode;
import com.iticket.xml.XHelp;
import com.iticket.xml.XSeatList;

public class XmlTest {
	public static void main(String[] args) throws Exception {
		String xml = FileUtils.readFileToString(new File("E:\\aliworkspace\\iticket\\src\\main\\test\\com\\iticket\\test\\1.xml"), "utf-8");
		ResultCode<XSeatList> xcode = XHelp.getBean(xml, "seatList");
		if(!xcode.isSuccess()){
			System.out.println(xcode.getMsg());
		}
		XSeatList seatList = xcode.getRetval();
		System.out.println(seatList);
	}
}
