package com.iticket.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.iticket.constant.ModuleCon;
import com.iticket.model.api.ApiMethod;
import com.iticket.model.api.ClientMember;
import com.iticket.model.auth.Group;
import com.iticket.model.auth.Member2Group;
import com.iticket.service.AuthService;
import com.iticket.service.IException;
import com.iticket.util.BeanUtil;
import com.iticket.util.ValidateUtil;
@Service("authService")
public class AuthServiceImpl  extends BaseServiceImpl implements AuthService, InitializingBean{
	private Map<String, ApiMethod> methodMap = new HashMap<String, ApiMethod>();
	@Override
	public void addClientMember(ClientMember manager, String memberName,
			String password, String groupIds) throws IException {
		if(StringUtils.isBlank(memberName)){
			throw new IException("用户名不能为空！");
		}
		if(!ValidateUtil.isMemberName(memberName)){
			throw new IException( "用户名不符合规则(长度6-14位，由数字和字母组成)");
		}
		String hql = "from ClientMember where memberName=? and stadiumId=?";
		List<ClientMember> list = baseDao.findByHql(hql, memberName, manager.getStadiumId());
		if(list.isEmpty()){
			if(StringUtils.isBlank(password)){
				throw new IException("密码不能为空！");
			}
			if(!ValidateUtil.isPassword(password)){
				throw new IException( "密码不符合规则(长度6-14位，由数字和字母组成)");
			}
		}
		List<Long> groupIdList = BeanUtil.getIdList(groupIds, ",");
		for(Long groupId : groupIdList){
			Group group = baseDao.getObject(Group.class, groupId);
			if(group==null){
				throw new IException( groupId + "找不到记录！");
			}
		}
		ClientMember member = null;
		if(list.isEmpty()){
			member = new ClientMember(manager.getStadiumId(), memberName, password);
		}else {
			member = list.get(0);
			List<Member2Group> m2gList = baseDao.getObjectListByField(Member2Group.class, "memberId", member.getId());
			baseDao.removeObjectList(m2gList);
		}
		if(StringUtils.isNotBlank(password)){
			member.setPassword(password);
		}
		baseDao.saveObject(member);
		for(Long groupId : groupIdList){
			Member2Group m2g = new Member2Group(member, groupId);
			baseDao.saveObject(m2g);
		}
	}
	@Override
	public Set<String> getMemberModule(ClientMember member){
		Set<String> moduleList = new HashSet<String>();
		if(member.hasManage()){
			moduleList.addAll(ModuleCon.getAllModuleList());
		}else {
			List<Member2Group> m2gList = baseDao.getObjectListByField(Member2Group.class, "memberId", member.getId());
			if(m2gList.size()>0){
				List<Long> groupIdList = BeanUtil.getBeanPropertyList(m2gList, Long.class, "groupId", true);
				List<Group> groupList = baseDao.getObjectList(Group.class, groupIdList);
				for(Group group : groupList){
					List<String> mdList = Arrays.asList(group.getModule().split(","));
					moduleList.addAll(mdList);
				}
			}
		}
		return moduleList;
	}
	@Override
	public Map<String, ApiMethod> getApiMethodMap() {
		return methodMap;
	}

	@Override
	public void initApiMethod() {
		List<ApiMethod> mdList = baseDao.findByHql("from ApiMethod");
		methodMap = new HashMap<String, ApiMethod>();
		methodMap.putAll(BeanUtil.beanListToMap(mdList, "method"));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initApiMethod();
	}
	
}
