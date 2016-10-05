package com.iticket.web.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.iticket.Config;
import com.iticket.util.BaseWebUtils;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;

public class PageExceptionResolver extends SimpleMappingExceptionResolver implements InitializingBean{
	private final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	private String sensitive;
	public void setSensitive(String sensitive) {
		this.sensitive = sensitive;
	}
	private String[] sensitiveList = new String[]{};

	protected Map saveExceptionMessage(Exception ex, HttpServletRequest request) {
		String uri = request.getRequestURI();
		String remoteIp = BaseWebUtils.getRemoteIp(request);
		
		String title = uri + "@" + Config.getServerIp() + ", RemoteIp:" + remoteIp;
		String reqMap = BaseWebUtils.getRequestMap(request)+"";
		Map<String, String> result = new HashMap<String, String>();
		result.put("remoteIp", remoteIp);
		result.put("reqParams", ""+reqMap);
		result.put("reqHeader", BaseWebUtils.getHeaderStr(request));
		result.put("reqUri", uri);
		
		return result;
	}
	
	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		saveExceptionMessage(ex, request);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isNotBlank(sensitive)){
			sensitiveList = StringUtils.split(sensitive, ",");
		}
	}
}
