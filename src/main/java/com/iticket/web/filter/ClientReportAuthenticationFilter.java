package com.iticket.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;
import com.iticket.service.DaoService;
import com.iticket.util.StringUtil;
import com.iticket.web.util.WebUtils;

public class ClientReportAuthenticationFilter extends OncePerRequestFilter  {
	@Autowired@Qualifier("daoService")
	private DaoService daoService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String memberKey = request.getParameter("memberKey");
		if(StringUtils.isBlank(memberKey)){
			memberKey = WebUtils.getCookieValue(request, "memberKey");
		}
		if(StringUtils.isBlank(memberKey)){
			response.sendError(403, "缺少参数memberKey");
			return;
		}
		if(!StringUtils.contains(memberKey, "@")){
			response.sendError(403,  "memberKey格式错误");
			return;
		}
		String[] mkey = memberKey.split("@");
		Long cmId = Long.valueOf(mkey[0]);
		String mkeySign = mkey[1];
		ClientMember cm = daoService.getObject(ClientMember.class, cmId);
		if(cm==null){
			response.sendError(403, "用户信息不存在");
			return;
		}
		if(!StringUtils.equalsIgnoreCase(mkeySign, StringUtil.md5(cm.getMemberName()+cm.getPassword()))){
			response.sendError(403, "memberKey校验错误");
			return;
		}
		WebUtils.addCookie(response, "memberKey", memberKey, "/", 60*60*24);
		String remoteIp = request.getParameter("remoteIp");
		Stadium stadium = daoService.getObject(Stadium.class, cm.getStadiumId());
		request.setAttribute("__OPENAPI_AUTH_KEY__", new OpenApiAuth(stadium, null, cm, remoteIp));
		filterChain.doFilter(request, response);
	}

	@Override
	public void initFilterBean() throws ServletException {
	}

}
