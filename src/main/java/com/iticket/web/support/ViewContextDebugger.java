package com.iticket.web.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;

import com.iticket.Config;

public class ViewContextDebugger  {
	public static boolean DEBUG_ENABLED = false;
	private Set unusedList;
	
	public static List<String> ignoreField;
	
	public void addProperty(Map model) {
		if(DEBUG_ENABLED){
			if(unusedList==null) unusedList = new HashSet();
			unusedList.addAll(model.keySet());
		}
	}
	public void remove(String property){
		if(DEBUG_ENABLED){
			try{
				if(unusedList!=null) {
					unusedList.remove(property);
				}
			}catch(Exception e){
			}
		}
	}
	public String getUnUsedProperty(){
		try{
			if(unusedList ==null || unusedList.isEmpty()){
				return null;
			}
			Iterator pi = unusedList.iterator();
			while(pi.hasNext()){
				String key = "" + pi.next();
				if(key.startsWith(BindingResult.MODEL_KEY_PREFIX)){
					pi.remove();
				}
			}
			if(Config.getPageTools()!=null){
				unusedList.removeAll(Config.getPageTools().keySet());
			}
			unusedList.remove("springMacroRequestContext");
			unusedList.remove(GewaVelocityView.RENDER_XML);
			unusedList.remove(GewaVelocityView.RENDER_JSON);
			unusedList.remove(GewaVelocityView.KEY_HTTP_STATUS);
			unusedList.remove(GewaVelocityView.USE_OTHER_CHARSET);
			unusedList.remove(GewaVelocityView.KEY_IGNORE_TOOLS);
			if(ignoreField!=null){
				unusedList.removeAll(ignoreField);
			}
			return StringUtils.join(new TreeSet(unusedList), ",");
		}catch(Exception e){
			return "UnUsedException:" + e.getMessage();
		}
	}
}
