package com.iticket.web.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iticket.Config;
import com.iticket.model.api.ApiMethod;
import com.iticket.model.api.ApiUser;
import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;
import com.iticket.service.AuthService;
import com.iticket.service.DaoService;
import com.iticket.util.IpConfig;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;
import com.iticket.util.StringUtil;
import com.iticket.web.util.WebUtils;

public class InnerApiAuthenticationFilter extends OncePerRequestFilter  {
	protected final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	@Autowired@Qualifier("daoService")
	private DaoService daoService;
	@Autowired@Qualifier("authService")
	private AuthService authService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String innerIp = WebUtils.getRemoteIp(request);
		if(!IpConfig.isInnerIp(innerIp) && !IpConfig.isLocalIp(innerIp)) {
			response.sendError(403, "只能内部调用");
			return;
		}
		String appkey = request.getParameter("appkey");
		ApiUser apiUser = daoService.getObjectByUkey(ApiUser.class, "appkey", appkey);
		if (apiUser == null) {
			ApiFilterHelper.writeErrorResponse(response, "账户不存在");
			return;
		}
		String method = request.getParameter("method");
		ClientMember member = null;
		if(!StringUtils.equals(method, "com.iticket.client.login")){
			String memberKey = request.getParameter("memberKey");
			if(StringUtils.isBlank(memberKey)){
				ApiFilterHelper.writeErrorResponse(response, "缺少memberKey");
				return;
			}
			if(!StringUtils.contains(memberKey, "@")){
				ApiFilterHelper.writeErrorResponse(response, "memberKey格式错误");
				return;
			}
			String[] mkey = memberKey.split("@");
			Long memberId = Long.valueOf(mkey[0]);
			String mkeySign = mkey[1];
			member = daoService.getObject(ClientMember.class, memberId);
			if(member==null){
				ApiFilterHelper.writeErrorResponse(response, "登录用户信息错误！");
				return;
			}
			if(!StringUtils.equalsIgnoreCase(mkeySign, StringUtil.md5(member.getMemberName() + member.getPassword()))){
				ApiFilterHelper.writeErrorResponse(response, "登录用户签名错误！");
				return;
			}
			if(!apiUser.getStadiumId().equals(member.getStadiumId())){
				ApiFilterHelper.writeErrorResponse(response, "账户信息不匹配");
				return;
			}
			Map<String, ApiMethod> methodMap = authService.getApiMethodMap();
			if(!methodMap.containsKey(method)){
				dbLogger.warn(method+"未注册服务！");
				ApiFilterHelper.writeErrorResponse(response, method+"未注册服务！");
				return;
			}
			ApiMethod apiMethod = methodMap.get(method);
			if(!StringUtils.equalsIgnoreCase(apiMethod.getModule(), "common")){
				Set<String> moduleList = authService.getMemberModule(member);
				if(!moduleList.contains(apiMethod.getModule())){
					dbLogger.error(member.getMemberName() + "," + method + "无权访问！");
					ApiFilterHelper.writeErrorResponse(response, method+"无权访问！");
					return;
				}
			}
		}
		String remoteIp = request.getParameter("remoteIp");
		Stadium stadium = daoService.getObject(Stadium.class, apiUser.getStadiumId());
		request.setAttribute("__OPENAPI_AUTH_KEY__", new OpenApiAuth(stadium, apiUser, member, remoteIp));
		filterChain.doFilter(request, response);
	}

	@Override
	public void initFilterBean() throws ServletException {
	}

}
