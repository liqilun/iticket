package com.iticket.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.api.ApiUser;
import com.iticket.model.api.ClientMember;
import com.iticket.model.stadium.Stadium;
import com.iticket.service.StadiumService;
import com.iticket.support.ResultCode;
@Controller
public class AdminStadiumController extends BaseController{
	@Autowired@Qualifier("stadiumService")
	protected StadiumService stadiumService;
	@RequestMapping("/admin/stadium/saveStadium.xhtml")
	public String place(Stadium stadium, String appkey, ModelMap model) {
		if(stadium.getId()==null){
			ResultCode code = stadiumService.addStadiumApi(stadium.getCnName(), stadium.getCnAddress(), stadium.getTelephone(), stadium.getContact(), appkey);
			if(!code.isSuccess()){
				return writeJsonError(model, code.getMsg());
			}
			return writeJsonSuccess(model);
		}
		daoService.saveObject(stadium);
		return writeJsonSuccess(model);
	}
	@RequestMapping("/admin/stadium/stadiumDetail.xhtml")
	public String stadiumDetail(Long id, ModelMap model) {
		if(id!=null){
			Stadium stadium = daoService.getObject(Stadium.class, id);
			model.put("stadium", stadium);
		}
		return "admin/stadium/stadiumForm.vm";
	}
	@RequestMapping("/admin/stadium/list.xhtml")
	public String list(ModelMap model) {
		List<Stadium> stadiumList = daoService.getObjectList(Stadium.class, "addTime", false, 0, 200);
		model.put("stadiumList", stadiumList);
		return "admin/stadium/stadiumList.vm";
	}
	@RequestMapping("/admin/stadium/modStatus.xhtml")
	public String modStatus(Long id, String status, ModelMap model) {
		Stadium stadium = daoService.getObject(Stadium.class, id);
		stadium.setStatus(status);
		daoService.saveObject(stadium);
		return writeJsonSuccess(model);
	}
	@RequestMapping("/admin/stadium/getApiAccount.xhtml")
	public String getApiAccount(Long id, ModelMap model) {
		ApiUser apiUser = daoService.getObjectByUkey(ApiUser.class, "stadiumId", id);
		model.put("apiUser", apiUser);
		ClientMember member = daoService.getObjectByUkey(ClientMember.class, "memberName", apiUser.getAppkey());
		model.put("member", member);
		model.put("stadium", daoService.getObject(Stadium.class, id));
		return "admin/stadium/apiAccount.vm";
	}
}
