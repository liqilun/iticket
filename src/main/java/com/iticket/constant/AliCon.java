package com.iticket.constant;

import com.iticket.util.StringUtil;

public class AliCon {
	public static final String USER_COOKIE = "ck_user";
	public static final String USER_SESSION = "session_user";
	public static void main(String[] args) {
		System.out.println(3+"@" + StringUtil.md5("123456" + "123456"));
	}
}
