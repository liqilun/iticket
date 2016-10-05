package com.iticket.web.action.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.ticket.Customer;
import com.iticket.service.ProgramService;
import com.iticket.service.ScheduleService;
import com.iticket.util.BindUtils;
import com.iticket.web.util.WebUtils;

@Controller
public class ApiCustomerController extends BaseApiController{
	@Autowired@Qualifier("programService")
	private ProgramService programService;
	@Autowired@Qualifier("scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping("/inner/api/ticket/addCustomer.xhtml")
	public String addCustomer(Long customerId, ModelMap model, HttpServletRequest request) {
		Customer customer = null;
		if(customerId!=null){
			customer = daoService.getObject(Customer.class, customerId);
			if(illegalOper(customer, request)){
				return getIllegalXmlView(model);
			}
		}else {
			customer = new Customer(getClientMember(request));
		}
		BindUtils.bindData(customer, WebUtils.getRequestMap(request));
		if(StringUtils.isBlank(customer.getName())){
			return getErrorXmlView(model, "客户名称不能为空");
		}
		if(StringUtils.length(customer.getName())<2){
			return getErrorXmlView(model, "客户名称至少两给字符");
		}
		daoService.saveObject(customer);
		return getSingleResultXmlView(model, customer.getId());
	}
	
	@RequestMapping("/inner/api/ticket/customerList.xhtml")
	public String customerList(String skey, ModelMap model, HttpServletRequest request) {
		DetachedCriteria query = DetachedCriteria.forClass(Customer.class);
		query.add(Restrictions.eq("memberId", getClientMember(request).getId()));
		if(StringUtils.isNotBlank(skey)){
			query.add(Restrictions.or(Restrictions.like("name", skey, MatchMode.ANYWHERE), Restrictions.like("mobile", skey, MatchMode.ANYWHERE)));
		}
		List<Map<String, Object>> qryMapList = new ArrayList<Map<String, Object>>();
		List<Customer> customerList = daoService.findByCriteria(query);
		for(Customer customer : customerList){
			Map<String, Object> qryMap = new HashMap<String, Object>();
			qryMap.put("customerid", customer.getId());
			qryMap.put("customerId", customer.getId());
			qryMap.put("name", customer.getName());
			qryMap.put("mobile", customer.getMobile());
			qryMap.put("telephone", customer.getTelephone());
			qryMap.put("address", customer.getAddress());
			qryMapList.add(qryMap);
		}
		return getOpenApiXmlList(qryMapList, "customerList,customer", model, request);
	}
}
