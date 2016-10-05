package com.iticket.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ValidateUtil {
	public static final List<String> ydMobile = Arrays.asList(new String[]{"139","138","137","136","135","134",
			"147", "150", "151", "152","157","158","159", "182","183", "187", "188"});
	public static boolean isYdMobile(String mobile){
		return ydMobile.contains(mobile.substring(0, 3));
	}
	public static boolean isEmail(String email){
		if(StringUtils.isBlank(email)) return false;
		return StringUtil.regMatch(email, "^[A-Z0-9._-]+@[A-Z0-9_-]+(\\.[A-Z0-9_-]+)*(\\.[A-Z]{2,4})+$", true);
	}
	public static boolean isMobile(String mobile) {
		if(StringUtils.isBlank(mobile)) return false;
		return StringUtil.regMatch(mobile, "^1[34578]{1}\\d{9}$", true);
	}
	public static boolean isNumber(String number) {
		if(StringUtils.isBlank(number)) return false;
		return StringUtil.regMatch(number, "^\\d+$", true);
	}
	public static boolean isNumber(String number, int minLength, int maxLength) {
		if(StringUtils.isBlank(number)) return false;
		return StringUtil.regMatch(number, "^\\d{" + minLength + "," + maxLength + "}$", true);
	}
	public static boolean isIDCard(String number){
		return StringUtil.regMatch(number, "^(\\d{15}|\\d{17}[0-9xX]{1})$", false);
	}
	/**
	 * @param variable
	 * @param length1
	 * @param length2
	 * @return
	 */
	public static boolean isVariable(String variable, int length1, int length2) {
		if(StringUtils.isBlank(variable)) return false;
		return StringUtil.regMatch(variable, "^\\w{" + length1 + "," + length2 + "}$", true);
	}
	/**
	 * @param variable
	 * @param length1
	 * @param length2
	 * @return
	 */
	public static boolean isCNVariable(String variable, int length1, int length2) {
		if(StringUtils.isBlank(variable)) return false;
		return StringUtil.regMatch(variable, "^[\\w+$\\u4e00-\\u9fa5]{" + length1 + "," + length2 +"}$", true);
	}
	public static boolean isPhone(String phone){
		if(StringUtils.isBlank(phone)) return false;
		return StringUtil.regMatch(phone, "^[0-9,-]{6,24}$", true);
	}
	public static boolean isPostCode(String postcode) {
		return StringUtil.regMatch(postcode, "^[0-9]{6}$", true);
	}
	private static char[] cncharRange = new char[]{'\u4e00','\u9fa5'};
	
	public static boolean isPassword(String password) {
		int len = StringUtils.length(password);
		return StringUtils.isAsciiPrintable(password) && len >=6 && len <=14;
	}
	public static boolean isMemberName(String memberName) {
		int len = StringUtils.length(memberName);
		return StringUtils.isAsciiPrintable(memberName) && len >=6 && len <=14;
	}
}