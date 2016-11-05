package com.iticket.web.action.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.ModuleCon;
import com.iticket.model.api.ClientMember;
import com.iticket.model.auth.Group;
import com.iticket.model.auth.Member2Group;
import com.iticket.service.AuthService;
import com.iticket.service.IException;
import com.iticket.util.BeanUtil;
import com.iticket.util.ValidateUtil;
@Controller
public class ApiAuthController extends BaseApiController {
	@Autowired@Qualifier("authService")
	private AuthService authService;
	@RequestMapping("/inner/api/auth/addGroup.xhtml")
	public String addGroup(String groupName, String module, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(groupName)){
			return getErrorXmlView(model, "缺少参数groupName");
		}
		if(StringUtils.isBlank(module)){
			return getErrorXmlView(model, "缺少参数module");
		}
		List<String> moduleList = ModuleCon.getAllModuleList();
		List<String> mdList = Arrays.asList(module.split(","));
		for(String md : mdList){
			if(!moduleList.contains(md)){
				return getErrorXmlView(model, "系统不包含模块:" + md);
			}
		}
		DetachedCriteria query = DetachedCriteria.forClass(Group.class);
		query.add(Restrictions.eq("groupName", groupName));
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		List<Group> groupList = daoService.findByCriteria(query);
		Group group = null;
		if(groupList.size()>0){
			group = groupList.get(0);
			group.setModule(module);
		}else {
			group = new Group(getClientMember(request), groupName, module);
		}
		daoService.saveObject(group);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/groupList.xhtml")
	public String groupList(ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		DetachedCriteria query = DetachedCriteria.forClass(Group.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		List<Group> groupList = daoService.findByCriteria(query);
		for(Group group : groupList){
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("groupId", group.getId());
			resMap.put("groupName", group.getGroupName());
			resMap.put("module", group.getModule());
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "groupList,group", model, request);
	}
	@RequestMapping("/inner/api/auth/delGroup.xhtml")
	public String groupList(Long groupId, ModelMap model, HttpServletRequest request) {
		Group group = daoService.getObject(Group.class, groupId);
		if(!group.getStadiumId().equals(getStadiumId(request))){
			return getErrorXmlView(model, "非法操作");
		}
		List<Member2Group> m2gList = daoService.getObjectListByField(Member2Group.class, "groupId", groupId);
		daoService.removeObjectList(m2gList);
		daoService.removeObject(group);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/addClientMember.xhtml")
	public String addClientMember(String memberName, String password, String groupIds, ModelMap model, HttpServletRequest request) {
		try {
			authService.addClientMember(getClientMember(request), memberName, password, groupIds);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/group2MemberList.xhtml")
	public String group2MemberList(ModelMap model, HttpServletRequest request) {
		DetachedCriteria query = DetachedCriteria.forClass(Group.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		List<Group> groupList = daoService.findByCriteria(query);
		Map<Long, List<ClientMember>> memberMap = new HashMap<Long, List<ClientMember>>();
		for(Group group : groupList){
			List<Member2Group> m2gList = daoService.getObjectListByField(Member2Group.class, "groupId", group.getId());
			List<Long> memberIdList = BeanUtil.getBeanPropertyList(m2gList, Long.class, "memberId", true);
			List<ClientMember> memberList = daoService.getObjectList(ClientMember.class, memberIdList);
			memberMap.put(group.getId(), memberList);
		}
		model.put("groupList", groupList);
		model.put("memberMap", memberMap);
		return getXmlView(model, "api/group2MemberList.vm");
	}
	@RequestMapping("/inner/api/auth/delMember.xhtml")
	public String del(Long memberId, ModelMap model, HttpServletRequest request) {
		if(memberId==null){
			return getErrorXmlView(model, "缺少参数memberid");
		}
		ClientMember member = daoService.getObject(ClientMember.class, memberId);
		if(member==null){
			return getErrorXmlView(model, "用户不存在！");
		}
		if(member.hasManage()){
			return getErrorXmlView(model, "管理员不能删除！");
		}
		if(illegalOper(member, request)){
			return getIllegalXmlView(model);
		}
		member.setStatus("D");
		daoService.saveObject(member);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/recoveryMember.xhtml")
	public String recovery(Long memberId, ModelMap model, HttpServletRequest request) {
		if(memberId==null){
			return getErrorXmlView(model, "缺少参数memberid");
		}
		ClientMember member = daoService.getObject(ClientMember.class, memberId);
		if(member==null){
			return getErrorXmlView(model, "用户不存在！");
		}
		if(illegalOper(member, request)){
			return getIllegalXmlView(model);
		}
		member.setStatus("Y");
		daoService.saveObject(member);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/forbidMember.xhtml")
	public String forbid(Long memberId, ModelMap model, HttpServletRequest request) {
		if(memberId==null){
			return getErrorXmlView(model, "缺少参数memberid");
		}
		ClientMember member = daoService.getObject(ClientMember.class, memberId);
		if(member==null){
			return getErrorXmlView(model, "用户不存在！");
		}
		if(member.hasManage()){
			return getErrorXmlView(model, "管理员不能删除！");
		}
		if(illegalOper(member, request)){
			return getIllegalXmlView(model);
		}
		member.setStatus("F");
		daoService.saveObject(member);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/auth/modpass4Member.xhtml")
	public String modpass(Long memberId, String password, ModelMap model, HttpServletRequest request) {
		if(memberId==null){
			return getErrorXmlView(model, "缺少参数memberid");
		}
		if(!ValidateUtil.isPassword(password)){
			return getErrorXmlView(model, "新密码不符合规则");
		}
		ClientMember member = daoService.getObject(ClientMember.class, memberId);
		if(member==null){
			return getErrorXmlView(model, "用户不存在！");
		}
		if(illegalOper(member, request)){
			return getIllegalXmlView(model);
		}
		member.setPassword(password);
		daoService.saveObject(member);
		return getSuccessXmlView(model);
	}
}
