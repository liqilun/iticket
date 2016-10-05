package com.iticket.web.action;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.iticket.Config;
import com.iticket.model.stadium.Stadium;
import com.iticket.service.DaoService;
import com.iticket.support.VelocityTemplate;
import com.iticket.util.BeanUtil;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;
import com.iticket.util.XmlUtils;
import com.iticket.web.filter.OpenApiAuth;
import com.iticket.web.support.GewaVelocityView;
import com.iticket.web.util.JsonDirectOut;

public class BaseController {
	protected final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	@Autowired@Qualifier("velocityTemplate")
	private VelocityTemplate velocityTemplate;
	@Autowired@Qualifier("daoService")
	protected DaoService daoService;
	@Autowired@Qualifier("config")
	protected Config config;
	@Autowired@Qualifier("hibernateTemplate")
	protected HibernateTemplate hibernateTemplate;
	protected String getXmlView(ModelMap model, String view){
		String result = velocityTemplate.parseTemplate(view, model);
		model.put(GewaVelocityView.RENDER_XML, "true");
		result = XmlUtils.formatXml(result, "utf-8");
		model.put("result", result);
		return "result.vm";
	}
	protected String getErrorXmlView(ModelMap model, String errorcode, String msg){
		model.put("errmsg", msg);
		model.put("errcode", errorcode);
		model.put(GewaVelocityView.RENDER_XML, "true");
		model.put(GewaVelocityView.KEY_IGNORE_TOOLS, "true");
		return "api/error.vm";
	}
	protected String getErrorXmlView(ModelMap model, String msg){
		model.put("errmsg", msg);
		model.put("errcode", "9999");
		model.put(GewaVelocityView.RENDER_XML, "true");
		model.put(GewaVelocityView.KEY_IGNORE_TOOLS, "true");
		return "api/error.vm";
	}
	protected String getIllegalXmlView(ModelMap model){
		model.put("errmsg", "非法操作");
		model.put("errcode", "9998");
		model.put(GewaVelocityView.RENDER_XML, "true");
		model.put(GewaVelocityView.KEY_IGNORE_TOOLS, "true");
		return "api/error.vm";
	}
	public String alertMessage(String msg, String returnUrl, ModelMap model) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.putAll(model);
		jsonMap.put("msg", msg);
		jsonMap.put("returnUrl", returnUrl);
		model.put("jsonMap", jsonMap);
		return "common/alertMessage.vm";
	}
	protected String formardMessage(List<String> msgList, ModelMap model) {
		model.put("msgList", msgList);
		return "common/showResult.vm";
	}
	protected String showReturn(ModelMap model, String msg, String url){
		model.put("url", url);
		model.put("msg", msg);
		return "wap/wapTip.vm";
	}
	protected String showWapMsg(ModelMap model, String msg){
		model.put("msg", msg);
		return "wap/wapTip.vm";
	}
	protected String showTouchMsg(ModelMap model, String msg){
		model.put("msg", msg);
		return "common/404.vm";
	}
	protected String showErrorMsg(ModelMap model, String msg){
		model.put("msg", msg);
		return "wap/error.vm";
	}
	protected String writeJsonError(ModelMap model, String msg) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		result.put("msg", msg);
		model.put(GewaVelocityView.RENDER_JSON, "true");
		model.put(GewaVelocityView.DIRECT_OUTPUT, new JsonDirectOut(result));
		return "common/directOut.vm";
	}
	protected String writeJsonSuccess(ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		model.put(GewaVelocityView.RENDER_JSON, "true");
		model.put(GewaVelocityView.DIRECT_OUTPUT, new JsonDirectOut(result));
		return "common/directOut.vm";
	}
	protected String writeJsonSuccess(ModelMap model, Object retval) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("retval", retval);
		model.put(GewaVelocityView.RENDER_JSON, "true");
		model.put(GewaVelocityView.DIRECT_OUTPUT, new JsonDirectOut(result));
		return "common/directOut.vm";
	}
	protected String showJsonTemplate(String template, ModelMap model){
		return showJsonSuccess(model, velocityTemplate.parseTemplate(template, model));
	}
	protected final String showRedirect(String path, ModelMap model){
		if(StringUtils.startsWith(path, "/")) path = path.substring(1);
		StringBuilder targetUrl = new StringBuilder(path);
		appendQueryProperties(targetUrl, model, "utf-8");
		model.put("redirectUrl", targetUrl.toString());
		return "tempRedirect.vm";
	}
	protected HttpServletRequest getRequest(){
		ServletRequestAttributes holder = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = holder.getRequest();
		return request;
	}
	private void appendQueryProperties(StringBuilder targetUrl, ModelMap model, String encoding) {
		boolean first = (targetUrl.indexOf("?") < 0);
		for (Map.Entry<String, Object> entry : queryProperties(model).entrySet()) {
			Object rawValue = entry.getValue();
			Iterator valueIter = null;
			if (rawValue != null && rawValue.getClass().isArray()) {
				valueIter = Arrays.asList(ObjectUtils.toObjectArray(rawValue)).iterator();
			}
			else if (rawValue instanceof Collection) {
				valueIter = ((Collection) rawValue).iterator();
			}
			else {
				valueIter = Collections.singleton(rawValue).iterator();
			}
			while (valueIter.hasNext()) {
				Object value = valueIter.next();
				if (first) {
					targetUrl.append('?');
					first = false;
				}
				else {
					targetUrl.append('&');
				}
				String encodedKey = urlEncode(entry.getKey(), encoding);
				String encodedValue = (value != null ? urlEncode(value.toString(), encoding) : "");
				targetUrl.append(encodedKey).append('=').append(encodedValue);
			}
		}
	}
	private Map<String, Object> queryProperties(Map<String, Object> model) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (isEligibleProperty(entry.getValue())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	private boolean isEligibleProperty(Object value) {
		if (value == null) return false;
		if (isEligibleValue(value)) return true;

		if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			if (length == 0) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				Object element = Array.get(value, i);
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			if (coll.isEmpty()) {
				return false;
			}
			for (Object element : coll) {
				if (!isEligibleValue(element)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}
	private String urlEncode(String input, String encodingScheme) {
		try {
			return (input != null ? URLEncoder.encode(input, encodingScheme) : null);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	private boolean isEligibleValue(Object value) {
		return (value != null && org.springframework.beans.BeanUtils.isSimpleValueType(value.getClass()));
	}
	
	protected final String showJsonSuccess(ModelMap model){
		return showJsonSuccess(model, "");
	}
	protected final String showJsonSuccess(ModelMap model, String retval){
		return showJsonSuccess(model, retval, "data");
	}
	protected final String showJsonSuccess(ModelMap model, Map<String, Object> jsonMap){
		return showJsonSuccess(model, jsonMap, "data");
	}
	protected final String showJsonSuccess(ModelMap model, Object bean){
		return showJsonSuccess(model, BeanUtil.getBeanMap(bean), "data");
	}
	protected final String showJsonSuccess(ModelMap model, Map<String, Object> jsonMap, String jsname){
		jsonMap.put("success", true);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "touch/json.vm";
	}
	protected final String showJsonSuccess(ModelMap model, String retval, String jsname){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", true);
		jsonMap.put("html", retval);
		jsonMap.put("retval", retval);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "touch/json.vm";
	}
	
	protected final String showJsonError(ModelMap model, String msg){
		return showJsonError(model, msg, "data");
	}
	protected final String showJsonError(ModelMap model, Map<String, Object> jsonMap){
		return showJsonError(model, jsonMap, "data");
	}
	protected final String showJsonError(ModelMap model, Map<String, Object> jsonMap, String jsname){
		if(jsonMap == null) jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", false);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "touch/json.vm";
	}
	protected final String showJsonError(ModelMap model, String msg, String jsname){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", false);
		jsonMap.put("html", msg);
		jsonMap.put("msg", msg);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "touch/json.vm";
	}
	public void initField(Map model, HttpServletRequest request) {
		String fields = request.getParameter("fields");
		List<String> fieldList = new ArrayList();
		boolean hasField = false;
		if (StringUtils.isNotBlank(fields)) {
			fieldList.addAll(Arrays.asList(fields.split(",")));
			hasField = true;
		}
		model.put("hasField", hasField);
		model.put("fieldList", fieldList);
	}
	protected String getOpenApiXmlList(List<Map<String, Object>> resMapList, String nodes, ModelMap model, HttpServletRequest request){
		initField(model, request);
		if(StringUtils.isNotBlank(nodes)) {
			String[] node = StringUtils.split(nodes, ",");
			model.put("root", node[0]);
			model.put("nextroot", node[1]);
		}
		model.put("resMapList", resMapList);
		return directWriteXml(model, "api/list.vm");
	}
	protected String getOpenApiXmlDetail(Map<String, Object> resMap, String root, ModelMap model,
			HttpServletRequest request) {
		initField(model, request);
		model.put("root", root);
		model.put("resMap", resMap);
		return directWriteXml(model, "api/detail.vm");
	}
	protected String directWriteXml(ModelMap model, String view){
		String result = velocityTemplate.parseTemplate(view, model);
		model.put(GewaVelocityView.RENDER_XML, "true");
		model.put(GewaVelocityView.KEY_IGNORE_TOOLS, "true");
		result = XmlUtils.filterInvalid(result);	//TODO:writer时直接过滤掉
		model.put("result", result);
		return "api/result.vm";
	}
	protected String getSingleResultXmlView(ModelMap model, String result){
		model.put(GewaVelocityView.RENDER_XML, "true");
		model.put(GewaVelocityView.KEY_IGNORE_TOOLS, "true");
		model.put("result", result);
		return "api/singleResult.vm";
	}
	protected String getSingleResultXmlView(ModelMap model, boolean result){
		return getSingleResultXmlView(model, String.valueOf(result));
	}
	
	protected String getSingleResultXmlView(ModelMap model, long result){
		return getSingleResultXmlView(model, String.valueOf(result));
	}
	
	protected String getSingleResultXmlView(ModelMap model, int result){
		return getSingleResultXmlView(model, String.valueOf(result));
	}
	protected String getSuccessXmlView(ModelMap model){
		return getSingleResultXmlView(model, "success");
	}
	protected Integer getPageNo(Integer pageNo){
		return (pageNo==null || pageNo<0)?0:pageNo;
	}
	protected OpenApiAuth getOpenApiAuth(HttpServletRequest request){
		return (OpenApiAuth)request.getAttribute("__OPENAPI_AUTH_KEY__");
	}
	protected Stadium getStadium(HttpServletRequest request){
		return getOpenApiAuth(request).getStadium();
	}
	protected Long getStadiumId(HttpServletRequest request){
		return getStadium(request).getId();
	}
}