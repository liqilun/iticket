package com.iticket.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.iticket.Config;
import com.iticket.constant.AliCon;
import com.iticket.model.Manager;
import com.iticket.service.ManagerService;
import com.iticket.web.util.WebUtils;

public class AdminFilter implements HandlerInterceptor{
	@Autowired@Qualifier("managerService")
	private ManagerService managerService;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Manager user = (Manager) session.getAttribute(AliCon.USER_SESSION);
		if (user!=null) { 
			return true;
		}else {
			String ht = WebUtils.getCookieValue(request, AliCon.USER_COOKIE);
			if(StringUtils.isNotBlank(ht)){
				String[] hts = ht.split("=");
				String username = hts[0], password = hts[1];
				user = managerService.getManager(username, password);
				if(user!=null) {
					request.getSession().setAttribute(AliCon.USER_SESSION, user);
					return true;
				}
			}
			response.sendRedirect(Config.getPageTools().get("basePath") + "login.xhtml"); 
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
