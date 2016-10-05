package com.iticket.web.action.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.api.ClientMember;
import com.iticket.model.layout.Layout;
import com.iticket.vo.ApiLayoutBeanHelp;

@Controller
public class ApiLayoutController extends BaseApiController{
	@RequestMapping("/inner/api/layout/list.xhtml")
	public String scheduleLayoutList(ModelMap model, HttpServletRequest request) {
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		DetachedCriteria query = DetachedCriteria.forClass(Layout.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		query.add(Restrictions.eq("status", "Y"));
		query.addOrder(Order.desc("addTime"));
		List<Layout> layoutList = daoService.findByCriteria(query);
		for(Layout layout : layoutList){
			ClientMember member = daoService.getObject(ClientMember.class, layout.getMemberId());
			Map<String,Object> resMap = ApiLayoutBeanHelp.getLayout(layout, member);
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "layoutList,layout", model, request);
	}
	@RequestMapping("/inner/api/layout/detail.xhtml")
	public String scheduleLayout(Long layoutId, ModelMap model, HttpServletRequest request) {
		Layout layout = daoService.getObject(Layout.class, layoutId);
		if(illegalOper(layout, request)){
			return getIllegalXmlView(model);
		}
		ClientMember member = daoService.getObject(ClientMember.class, layout.getMemberId());
		Map<String,Object> resMap = ApiLayoutBeanHelp.getLayout(layout, member);
		return getOpenApiXmlDetail(resMap, "layout", model, request);
	}
	@RequestMapping("/inner/api/layout/delete.xhtml")
	public String delete(Long layoutId, ModelMap model, HttpServletRequest request) {
		Layout layout = daoService.getObject(Layout.class, layoutId);
		if(illegalOper(layout, request)){
			return getIllegalXmlView(model);
		}
		layout.setStatus("D");
		daoService.saveObject(layout);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/layout/add.xhtml")
	public String scheduleLayout(Long layoutId, String title, String layoutData, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(title)){
			return getErrorXmlView(model, "标题不能能为空");
		}
		if(StringUtils.length(title)<5){
			return getErrorXmlView(model, "标题至少5个字符");
		}
		if(StringUtils.isBlank(layoutData)){
			return getErrorXmlView(model, "票版内容不能为空");
		}
		Layout layout = null;
		ClientMember member = getClientMember(request);
		if(layoutId!=null){
			layout = daoService.getObject(Layout.class, layoutId);
			if(illegalOper(layout, request)){
				return getIllegalXmlView(model);
			}
		}else {
			layout = new Layout(title, layoutData, member);
		}
		layout.setTitle(title);
		layout.setLayoutData(layoutData);
		daoService.saveObject(layout);
		Map<String,Object> resMap = ApiLayoutBeanHelp.getLayout(layout, member);
		return getOpenApiXmlDetail(resMap, "layout", model, request);
	}
}
