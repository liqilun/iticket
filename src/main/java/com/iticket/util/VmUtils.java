package com.iticket.util;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class VmUtils extends VmBaseUtil {
	private String defPath;
	public String getPicUrl(String url, String size){
		if(StringUtils.isBlank(url)) return null;
		if(StringUtils.startsWith(url, "http")) {
			return url.replaceFirst("/?w\\d+h\\d+", size);
		}
		return defPath + size + "/" + url;
	}
	
	public static String getUniqueString(String str, String splitter){
		if(StringUtils.isBlank(str)) return "";
		Set<String> set = new LinkedHashSet<String>();
		set.addAll(Arrays.asList(str.split(splitter)));
		return join(set.iterator(), splitter);
	}
	public static String getLight(String str, String qryStr){
		if(StringUtils.isBlank(str)) return "";
		if(StringUtils.isBlank(qryStr)) return str;
		String result = StringUtil.substitute(str, "([" + qryStr + "]+)", "<span class='fs14'>$1</span>", true);
		return result;
	}
	
	public static String replaceSeparator(String oldString, String oldSeparator, String newSeparator){
		String[] tmp = StringUtils.split(oldString, oldSeparator);
		return StringUtils.join(tmp, newSeparator);
	}
	public static Object defalutValue(Object o, Object defaultValue){
		if(o==null) return defaultValue;
		if(o instanceof String && StringUtils.isBlank(""+o)) return defaultValue;
		return o;
	}
	public static String getTotal(List<Map<String, Object>> qryList, String key){
		if(qryList==null || StringUtils.isBlank(key)) return "";
		Long result = 0L;
		for(Map m : qryList){
			if(m.get(key) instanceof Number) {
				result += new Long(m.get(key)+"");
			}
		}
		return result+"";
	}
	public static String getPercent(Object num, Object total){
		if(StringUtils.isBlank(num+"") || StringUtils.isBlank(total+"") || (total+"").equals("0") ) return 0+"";
		if(num instanceof Number && total instanceof Number) {
			Double d = (Long.valueOf(num+"")*100.00)/Long.valueOf(total+"");
			Double d2 = Math.round(d*100)/100.0;
			return d2+"%";
		}
		return "0";
	}
	public static String formatPer(Object obj){
		NumberFormat nf = NumberFormat.getPercentInstance();
		Double number = 0.0;
		if(obj != null)
			number = Double.valueOf(obj+"");
		return nf.format(number); 
	}
	
	public static String appendString(String str, String separatorChars, String newstr){
		List<String> list = new ArrayList<String>();
		if(StringUtils.isBlank(str)) return newstr;
		list.addAll(Arrays.asList(StringUtils.split(str, separatorChars)));
		list.add(newstr);
		return printList(list, separatorChars);
	}
	/*public static Object getProperty(BaseObject object, String property){
		try {
			return PropertyUtils.getProperty(object, property);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch(Exception e){
		}
		return null;
	}*/
	public static String abbresc(String propertyname, int length){
		String str = StringUtil.enabbr(propertyname, length);
		return HtmlUtils.htmlEscape(str);
	}
	public static boolean isValidCaptchaId(String captchaId) {
		if(StringUtils.length(captchaId) != 24) return false;
		return StringUtil.md5(StringUtils.substring(captchaId, 0, 16) + "sk#8Kr", 8).equals(StringUtils.substring(captchaId, 16));
	}
	public static String getRandomCaptchaId() {
		String s = StringUtil.getRandomString(16) ;
		s += StringUtil.md5(s + "sk#8Kr", 8);
		return s;
	}
	
	public static String enabbrTextFromOneToOne(String temp,String text){
		return text.substring(text.lastIndexOf(temp)+1);
	}
	public static int mult(String num1,String num2){
		int numL = Integer.parseInt(num1);
		int numR = Integer.parseInt(num2);
		return  numL * numR;
	}
	public static int sub(String max,String min){
		int numL = Integer.parseInt(max);
		int numR = Integer.parseInt(min);
		return  numL - numR;
	}
	public static int getItemTimeCount(String detail){
		String[] detailArray=detail.split("\\)");
		int count=0;
		for(int i=0;i<detailArray.length;i++){
			String detailArrays=detailArray[i];
			String[] detailArrayCount=(detailArrays.substring(detailArrays.indexOf(":")+1).split(","));
			count=count+detailArrayCount.length;
		}
		return count;
	}
	public static Integer getFeeByRate(Integer totalAmount, Integer discount, String strRate) {
		int rate = Integer.valueOf(strRate);
		int result = (totalAmount - discount)*rate/100;
		return result ==0?1:result;
	}
	
	public static int indexOf(String str,String searchChar){
		return StringUtils.indexOf(str, searchChar);
	}
	
	public static String[] split(String inputStr,String splitChar){
		if(StringUtils.isBlank(inputStr) || StringUtils.isBlank(splitChar)) return null;
		return inputStr.split(splitChar);
	}
	public static String decodeStr(String str){
		String res = "";
		try {
			res = new String(org.apache.commons.codec.binary.Base64.decodeBase64(str), "gbk");
		} catch (Exception e) {
			return "";
		}
		return res;
	}
	public static String writeMapToJson(Map<String, String> map){
		if(map==null) map = new HashMap<String, String>();
		return JsonUtils.writeMapToJson(map);
	}
	public static Map readJsonToMap(String json){
		if(StringUtils.isBlank(json)) return new HashMap<String, String>();
		return JsonUtils.readJsonToMap(json);
	}
	public static String subString(String str,int start,int end){
		return StringUtils.substring(str, start, end);
	}
	public static String subString(String str,int start){
		return StringUtils.substring(str, start);
	}
	
	private static String jsVersion = DateUtil.format(new Date(), "yyyyMMddHH");
	public static void setJsVersion(String jv) {
		jsVersion = jv;
	}
	public static String getJsVersion() {
		return jsVersion;
	}
	public String getDefPath() {
		return defPath;
	}
	public void setDefPath(String defPath) {
		this.defPath = defPath;
	}
	
	public long diffDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) return 0;
		/*Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date1);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(date2);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day1 - day2;*/
		return date1.getTime() / 86400000 - date2.getTime() / 86400000;
	}
	public String getScriptObject(Object modelObject){
		return JsonUtils.writeObjectToJson(modelObject);
	}
	public String getScriptString(String str){
		str = JsonUtils.writeObjectToJson(str);
		return str;
	}
	
	public static String xmlOutputList(String root, String nextroot, List<Map<String, Object>> resMapList, boolean hasField, List<String> fieldList){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(root)) sb.append("<" + root + ">");
		if(resMapList!=null){
			for(Map<String, Object> resMap : resMapList){
				sb.append(xmlOutput(nextroot, resMap, hasField, fieldList));
			}
		}
		if(StringUtils.isNotBlank(root)) sb.append("</" + root + ">");
		return sb.toString();
	}
	
	public static String xmlOutput(String root, Map<String, Object> resMap, boolean hasField, List<String> fieldList){
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(root)) sb.append("<" + root + ">");
		if(resMap!=null) {
			for(String key : resMap.keySet()){
				if(hasField) {
					if(fieldList!=null && fieldList.contains(key)) sb.append(getNodeOutput(key, resMap.get(key)));
				}else {
					sb.append(getNodeOutput(key, resMap.get(key)));
				}
			}
		}
		if(StringUtils.isNotBlank(root)) sb.append("</" + root + ">");
		return sb.toString();
	}
	private static String getNodeOutput(String key, Object value){
		String output ="";
		boolean isCdata = false;
		if(value==null){ 
			output = "";
		}else if(value instanceof Timestamp){ 
			output =  DateUtil.formatTimestamp((Timestamp)value);
		}else if(value instanceof Date){ 
			output = DateUtil.formatDate((Date)value);
		}else if(value instanceof String) {
			output = value+"";
			isCdata = true;
		}else {
			output = String.valueOf(value);
		}
		StringBuilder sb = new StringBuilder("<" + key + ">");
		if(isCdata){
			sb.append("<![CDATA[" + output + "]]>");
		}else {
			sb.append(output);
		}
		sb.append("</" + key + ">");
		return sb.toString();
	}
}