package com.iticket.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.AliCon;
import com.iticket.model.Manager;
import com.iticket.service.ManagerService;
import com.iticket.util.StringUtil;
import com.iticket.web.util.WebUtils;
@Controller
public class AdminFrameController extends BaseController{
	@Autowired@Qualifier("managerService")
	protected ManagerService managerService;
	@RequestMapping("/admin/main.xhtml")
	public String main(ModelMap model){
		return "admin/main.vm";
	}
	@RequestMapping("/admin/logout.xhtml")
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		request.getSession().invalidate();
		WebUtils.clearCookie(response, "/", AliCon.USER_COOKIE);
		return "redirect:/admin/login.xhtml";
	}
	
	@RequestMapping("/admin/login.xhtml")
	public String login(HttpServletRequest req, ModelMap model){
		String referer = req.getHeader("REFERER");
		model.put("referer", referer);
		return "admin/login.vm";
	}
	protected Boolean isRightCK(HttpServletRequest req) {
		String checkcode = req.getParameter("textcode");
		String randck = (String) req.getSession().getAttribute("randck");
		if (StringUtils.equals(checkcode, randck)) return true;
		return false;
	}
	@RequestMapping("/admin/loginck.xhtml")
	public String index(String username, String password, String referer, String reme, HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception{
		/*if(!isRightCK(req)) {
			return alertMessage("验证码错误", "login.xhtml", model);
		}*/
		String md5password = StringUtil.md5(password);
		Manager user = managerService.getManager(username, md5password);
		if(user==null) {
			return alertMessage("用户名或密码错误！", "login.xhtml", model);
		}
		req.getSession().setAttribute(AliCon.USER_SESSION, user);
		if(StringUtils.isNotBlank(reme)) {
			int maxSecond = 60*60*12;
			WebUtils.addCookie(res, AliCon.USER_COOKIE, username+"="+md5password, "/", maxSecond);
		}
		return "redirect:/admin/main.xhtml"; 
	}
}
