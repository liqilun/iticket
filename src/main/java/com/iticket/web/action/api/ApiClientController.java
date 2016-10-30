package com.iticket.web.action.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.api.ClientMember;
import com.iticket.service.AuthService;
import com.iticket.util.StringUtil;
import com.iticket.util.ValidateUtil;

@Controller
public class ApiClientController extends BaseApiController {
	@Autowired@Qualifier("authService")
	private AuthService authService;
	@RequestMapping("/inner/api/member/login.xhtml")
	public String login(String memberName, String password, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(memberName) || StringUtils.isBlank(password)){
			return getErrorXmlView(model, "用户名或密码不能为空");
		}
		String hql = "from ClientMember where memberName=? and password=?";
		List<ClientMember> list = daoService.findByHql(hql, memberName, password);
		if(list.isEmpty()){
			return getErrorXmlView(model, "用户名或密码不正确");
		}
		ClientMember member = list.get(0);
		if(!StringUtils.equalsIgnoreCase(member.getStatus(), "Y")){
			return getErrorXmlView(model, "用户已被禁用或删除");
		}
		if(!member.getStadiumId().equals(getApiUser(request).getStadiumId())){
			return getErrorXmlView(model, "账户信息不匹配");
		}
		Map<String, Object> resMap = getResMap(member);
		return getOpenApiXmlDetail(resMap, "member", model, request);
	}
	@RequestMapping("/inner/api/member/modpass.xhtml")
	public String modpass(String password, String newpassword, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(password) || StringUtils.isBlank(newpassword)){
			return getErrorXmlView(model, "密码不能为空");
		}
		ClientMember member = getClientMember(request);
		if(!member.getPassword().equalsIgnoreCase(password)){
			return getErrorXmlView(model, "原始密码不正确");
		}
		if(!ValidateUtil.isPassword(newpassword)){
			return getErrorXmlView(model, "密码不符合规则(长度6-14位，由数字和字母组成)");
		}
		if(StringUtils.equalsIgnoreCase(password, newpassword)){
			return getErrorXmlView(model, "新密码和老密码不能一样");
		}
		member.setPassword(newpassword);
		daoService.saveObject(member);
		Map<String, Object> resMap = getResMap(member);
		return getOpenApiXmlDetail(resMap, "member", model, request);
	}
	private Map<String, Object> getResMap(ClientMember member){
		Map<String, Object> resMap = new HashMap<String, Object>();
		Set<String> moduleList = authService.getMemberModule(member);
		resMap.put("memberKey", member.getId() + "@" + StringUtil.md5(member.getMemberName() + member.getPassword()));
		resMap.put("memberName", member.getMemberName());
		resMap.put("module", StringUtils.join(moduleList, ","));
		resMap.put("reportUrl", config.getString("reportUrl"));
		resMap.put("layOutSize", "330*200,300*180,280*160,560*220");
		return resMap;
	}
}
