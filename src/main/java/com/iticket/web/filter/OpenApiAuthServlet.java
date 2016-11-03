package com.iticket.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iticket.Config;
import com.iticket.model.api.ApiMethod;
import com.iticket.model.api.ApiUser;
import com.iticket.service.AuthService;
import com.iticket.service.DaoService;
import com.iticket.util.ApiUtil;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;
import com.iticket.web.util.HttpResult;
import com.iticket.web.util.HttpUtils;
import com.iticket.web.util.WebUtils;

@WebServlet(urlPatterns = "/showapi/router/rest",asyncSupported=true)
public class OpenApiAuthServlet extends HttpServlet {
	private final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	private static final long serialVersionUID = 5865824964720064458L;
	private DaoService daoService;
	private AuthService authService;
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		ServletContext servletContext = servletConfig.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		daoService = ctx.getBean("daoService", DaoService.class);
		authService = ctx.getBean("authService", AuthService.class);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException  {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, String> params = WebUtils.getRequestMap(request);
		if (ServletFileUpload.isMultipartContent(request)) {// 文件FileData不做签名
			params.remove("FileData");
		}
		TreeMap<String, String> nameValueMap = new TreeMap<String, String>(params);
		String appkey = nameValueMap.get("appkey");
		if (StringUtils.isBlank(appkey)) {
			ApiFilterHelper.writeErrorResponse(response, "1001", "缺少参数appkey");
			return;
		}
		String method = nameValueMap.get("method");
		if (StringUtils.isBlank(method)) {
			ApiFilterHelper.writeErrorResponse(response, "1002", "缺少参数method");
			return;
		}
		String sign = nameValueMap.get("sign");
		if(StringUtils.isBlank(sign) || StringUtils.length(sign)!=32){
			ApiFilterHelper.writeErrorResponse(response, "1003", "参数sign错误");
			return;
		}
		ApiUser apiUser = daoService.getObjectByUkey(ApiUser.class, "appkey", appkey);
		if(apiUser==null){
			ApiFilterHelper.writeErrorResponse(response, "1004", "商户不存在");
			return;
		}
		String validSign = ApiUtil.signMD5(params, apiUser.getPrivatekey());
		if(!StringUtils.equalsIgnoreCase(sign, validSign)){
			ApiFilterHelper.writeErrorResponse(response, "1004", "校验签名错误");
			return;
		}
		String remoteIp = WebUtils.getRemoteIp(request);
		params.put("remoteIp", remoteIp);
		ApiMethod apiCfg = authService.getApiMethodMap().get(method);
		if(apiCfg==null){
			ApiFilterHelper.writeErrorResponse(response, "1004", "请求方法不存在");
			return;
		}
		String url = apiCfg.getReqUrl();
		HttpResult result = HttpUtils.postUrlAsString("http://localhost:8080/iticket/" + url, params, null, "utf-8");
		if(!result.isSuccess()){
			dbLogger.error(url+":" + result.getStatus()+":" + params.toString());
			ApiFilterHelper.writeErrorResponse(response, "1004", "服务器异常：" + result.getStatus());
			return;
		}else {
			dbLogger.warn(ApiUtil.getFullReq("http://114.215.107.90:8080/iticket/showapi/router/rest", nameValueMap));
		}
		response.setContentType("text/xml; charset=UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(result.getResponse());
			writer.close();
		} catch (IOException e) {
		}
	}
}
