package com.iticket.web.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.ModuleCon;
import com.iticket.model.api.ApiMethod;
import com.iticket.model.api.ApiParam;
import com.iticket.service.AuthService;
import com.iticket.util.BeanUtil;
import com.iticket.util.BindUtils;
import com.iticket.web.util.WebUtils;
@Controller
public class AdminApiUserController extends BaseController{
	@Autowired@Qualifier("authService")
	protected AuthService authService;
	@RequestMapping("/admin/api/info.xhtml")
	public String info() {
		return "admin/api/apiInfo.vm";
	}
	@RequestMapping("/admin/api/getApiMethod.xhtml")
	public String getApiMethod(Long id, ModelMap model) {
		if(id!=null){
			ApiMethod apiMethod = daoService.getObject(ApiMethod.class, id);
			model.put("method", apiMethod);
		}
		model.put("adminModuleMap", ModuleCon.adminModuleMap);
		return "admin/api/apiMethodForm.vm";
	}
	@RequestMapping("/admin/api/saveApiMethod.xhtml")
	public String saveApiMethod(Long id, ModelMap model, HttpServletRequest request) {
		ApiMethod apiMethod = null;
		if(id!=null){
			apiMethod = daoService.getObject(ApiMethod.class, id);
		}else {
			apiMethod = new ApiMethod();
		}
		BindUtils.bindData(apiMethod, WebUtils.getRequestMap(request));
		daoService.saveObject(apiMethod);
		authService.initApiMethod();
		return writeJsonSuccess(model);
	}
	@RequestMapping("/admin/api/saveApiParam.xhtml")
	public String saveApiParam(Long id, ModelMap model, HttpServletRequest request) {
		ApiParam apiParam = null;
		if(id!=null){
			apiParam = daoService.getObject(ApiParam.class, id);
		}else {
			apiParam = new ApiParam();
		}
		BindUtils.bindData(apiParam, WebUtils.getRequestMap(request));
		daoService.saveObject(apiParam);
		return writeJsonSuccess(model);
	}
	@RequestMapping("/admin/api/methodList.xhtml")
	public String methodList(ModelMap model) {
		List<ApiMethod> methodList = daoService.getObjectList(ApiMethod.class, "orderNo", true, 0, 500);
		Map<String, List<ApiMethod>> methodTypeMapList = BeanUtil.groupBeanList(methodList, "methodType");
		model.put("methodTypeMapList", methodTypeMapList);
		model.put("adminModuleMap", ModuleCon.adminModuleMap);
		return "admin/api/methodList.vm";
	}
	@RequestMapping("/admin/api/paramList.xhtml")
	public String methodList(Long methodId, ModelMap model) {
		List<ApiParam> paramList = daoService.getObjectListByField(ApiParam.class, "methodId", methodId);
		Collections.sort(paramList, new PropertyComparator<ApiParam>("type", false, true));
		model.put("paramList", paramList);
		return "admin/api/paramList.vm";
	}
}
