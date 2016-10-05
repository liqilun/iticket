package com.iticket.web.action.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.iticket.model.BaseObject;
import com.iticket.model.api.ApiUser;
import com.iticket.model.api.ClientMember;
import com.iticket.util.BeanUtil;
import com.iticket.web.action.BaseController;

public class BaseApiController extends BaseController{
	//ByteArrayInputStream is = new ByteArrayInputStream(Hex.decodeHex(pic.toCharArray()));
	protected ClientMember getClientMember(HttpServletRequest request){
		return getOpenApiAuth(request).getClientMember();
	}
	protected ApiUser getApiUser(HttpServletRequest request){
		return getOpenApiAuth(request).getApiUser();
	}
	protected <T extends BaseObject> boolean illegalOper(T entity, HttpServletRequest request){
		Long sId = (Long) BeanUtil.get(entity, "stadiumId");
		if(sId==null){
			return false;
		}
		Long stadiumId = getStadiumId(request);
		if(stadiumId.equals(sId)){
			return false;
		}
		return true;
	}
	protected String getMap2Str(Map map){
		List<String> strList = new ArrayList<String>();
		for(Object key : map.keySet()){
			strList.add(key + "@" + map.get(key));
		}
		return StringUtils.join(strList, ",");
	}
}
