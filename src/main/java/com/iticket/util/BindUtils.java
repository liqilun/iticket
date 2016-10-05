package com.iticket.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindException;

import com.iticket.support.RequestMapDataBinder;

/**
 * 
 */
public class BindUtils {
	public static BindException bindData(Object bean, Map requestParam){
		RequestMapDataBinder binder = new RequestMapDataBinder(bean);
		binder.bind(requestParam);
		BindException errors = new BindException(binder.getBindingResult());
		return errors;
	}
	public static BindException bindData(Object bean , Map map, String[] allowedFields ,String[] disallowedFields){
		RequestMapDataBinder binder = new RequestMapDataBinder(bean);
		if(allowedFields!=null && allowedFields.length>0){
			binder.setAllowedFields(allowedFields);
		}
		if(disallowedFields!=null && disallowedFields.length>0){
			binder.setDisallowedFields(disallowedFields);
		}
		binder.bind(map);
		BindException errors = new BindException(binder.getBindingResult());
		return errors;
	}
	public static BindException bind(Object bean ,Map requestParam, boolean allow, String[] fields){
		Map bindData = null;
		if(allow) bindData = new HashMap();
		else bindData = new HashMap(requestParam);
		for(String field: fields){
			Object value = requestParam.get(field); 
			if(allow) {
				if(requestParam.containsKey(field)) bindData.put(field, value);
			}else{
				bindData.remove(field);
			}
		}
		return bindData(bean, bindData);
	}

}
