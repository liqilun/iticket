package com.iticket.service;

import java.util.Map;
import java.util.Set;

import com.iticket.model.api.ApiMethod;
import com.iticket.model.api.ClientMember;

public interface AuthService {
	void addClientMember(ClientMember manager, String memberName, String password, String groupIds) throws IException;
	Map<String, ApiMethod> getApiMethodMap();
	void initApiMethod();
	Set<String> getMemberModule(ClientMember member);
}
