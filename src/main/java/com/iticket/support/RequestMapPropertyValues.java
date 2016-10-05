package com.iticket.support;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.util.Assert;
public class RequestMapPropertyValues extends MutablePropertyValues {
	private static final long serialVersionUID = 5230454540179625738L;
	public static final String DEFAULT_PREFIX_SEPARATOR = "_";
	public RequestMapPropertyValues(Map requestParams) {
		this(requestParams, null, null);
	}

	/**
	 * Create new ServletRequestPropertyValues using the given prefix and
	 * the default prefix separator (the underscore character "_").
	 * @param request HTTP request
	 * @param prefix the prefix for parameters (the full prefix will
	 * consist of this plus the separator)
	 * @see #DEFAULT_PREFIX_SEPARATOR
	 */
	public RequestMapPropertyValues(Map<String, Object> requestParams, String prefix) {
		this(requestParams, prefix, DEFAULT_PREFIX_SEPARATOR);
	}

	/**
	 * Create new ServletRequestPropertyValues supplying both prefix and
	 * prefix separator.
	 * @param request HTTP request
	 * @param prefix the prefix for parameters (the full prefix will
	 * consist of this plus the separator)
	 * @param prefixSeparator separator delimiting prefix (e.g. "spring")
	 * and the rest of the parameter name ("param1", "param2")
	 */
	public RequestMapPropertyValues(Map requestParams, String prefix, String prefixSeparator) {
		super(getParametersStartingWith(requestParams, (prefix != null) ? prefix + prefixSeparator : null));
	}
	public static Map<String, Object> getParametersStartingWith(Map requestParams, String prefix) {
		Assert.notNull(requestParams, "requestParams must not be null");
		Iterator paramNames = requestParams.keySet().iterator();
		Map params = new TreeMap();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				Object values = requestParams.get(paramName);
				if (values == null) {
				}else{
					if(values instanceof String[]){
						if (((String[])values).length > 1) {
							params.put(unprefixed, values);
						}else {
							params.put(unprefixed, ((String[])values)[0]);
						}
					}else{
						params.put(unprefixed, values);
					}
				}
			}
		}
		return params;
	}

}
