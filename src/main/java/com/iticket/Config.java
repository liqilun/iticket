package com.iticket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.map.UnmodifiableMap;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.InitializingBean;

import com.iticket.support.ResultCode;
import com.iticket.util.DateUtil;
import com.iticket.util.IpConfig;

public class Config implements InitializingBean {
	public static final String SYSTEMID;
	public static final String DEPLOYID;
	public static String IMAGE_PATH;
	public static String UPLOAD_PATH;
	private static final Properties props = new Properties();
	static{
		try {
			props.load(Config.class.getClassLoader().getResourceAsStream("global.properties"));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		SYSTEMID = props.getProperty("systemid");
		
		
		DEPLOYID = SYSTEMID + "-" + IpConfig.getHostname();
	}
	private Map<String, String> configMap = new HashMap<String, String>();
	private Map<String, Object> pageMap = new HashMap<String, Object>();
	private static Map pageTools;
	public static Map getPageTools() {
		return pageTools;
	}
	private boolean initedConfig = false;
	private boolean initedPage = false;
	private static boolean debugEnabled = false;
	public String getGlobalProp(String key){
		return props.getProperty(key);
	}
	public String getString(String key){
		String result = configMap.get(key);
		if(StringUtils.isBlank(result)){
			result = pageTools.get(key)==null?null:""+pageTools.get(key);
		}
		return result;
	}
	public Long getLong(String key){
		String result = getString(key);
		if(StringUtils.isBlank(result)) return null;
		return Long.parseLong(result);
	}
	public void setConfigMap(Map<String, String> configMap) {
		if (!initedConfig) {
			this.configMap = configMap;
			this.initedConfig = true;
		} else{
			throw new IllegalStateException("不能再次调用");
		}
	}
	public void setPageMap(Map<String, Object> pageMap) {
		if (!initedPage) {
			this.pageMap = pageMap;
			this.initedPage = true;
		} else{
			throw new IllegalStateException("不能再次调用");
		}
	}
	public Map<String, Object> getPageMap(){
		return new HashMap<String, Object>(pageMap);
	}
	public static boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean enabled) {
		debugEnabled = enabled;
	}
	public static String getServerIp() {
		return IpConfig.getServerip();
	}
	public static String getHostname() {
		return IpConfig.getHostname();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.configMap = UnmodifiableMap.decorate(this.configMap);
		initPageTools();
	}
	public void initPageTools(){
		Map tmp = new HashMap();
		tmp.put("math", new MathTool());
		tmp.put("DateUtil", new DateUtil());
		tmp.putAll(pageMap);
		pageTools = UnmodifiableMap.decorate(tmp);
	}
	public ResultCode replacePageTool(String property, Object value){
		Object old = pageTools.get(property);
		if(value == null || old == null) return ResultCode.getFailure("参数错误:old 或 new 为空");
		if(!value.getClass().equals(old.getClass())) return ResultCode.getFailure("参数类型不兼容");
		Map tmp = new HashMap(pageTools);
		tmp.put(property, value);
		pageTools = UnmodifiableMap.decorate(tmp);
		if(pageMap.containsKey(property)){
			pageMap.put(property, value);
		}
		return ResultCode.SUCCESS;
	}	

	public String getBasePath() {
		return (String) pageMap.get("basePath");
	}

	public String getAbsPath() {
		return (String) pageMap.get("absPath");
	}
	public String getCacheVersionKey(){
		return getString("cacheVersionKey");
	}
	public static boolean isServerIp(String ip){
		return IpConfig.isServerIp(ip);
	}
	public String getImagePath(){
		return (String) pageMap.get("imagePath");
	}
	public static void setImagePath(String imagePath) {
		IMAGE_PATH = imagePath;
	}
	public static void setUploadPath(String uploadPath) {
		UPLOAD_PATH = uploadPath;
	}
}
