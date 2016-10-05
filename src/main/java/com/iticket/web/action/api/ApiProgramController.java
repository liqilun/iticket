package com.iticket.web.action.api;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.Config;
import com.iticket.constant.ProgramType;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.stadium.Venue;
import com.iticket.service.ProgramService;
import com.iticket.util.BindUtils;
import com.iticket.util.DateUtil;
import com.iticket.util.StringUtil;
import com.iticket.vo.ApiProgramBeanHelp;
import com.iticket.web.util.WebUtils;

@Controller
public class ApiProgramController extends BaseApiController{
	@Autowired@Qualifier("programService")
	protected ProgramService programService;
	@RequestMapping("/inner/api/program/programTypeList.xhtml")
	public String programTypeList(ModelMap model, HttpServletRequest request) {
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		Map<Integer, String> typeMap = ProgramType.typeMap;
		for(Integer id : typeMap.keySet()){
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("typeId", id);
			resMap.put("typeName", typeMap.get(id));
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "programTypeList,programType", model, request);
	}
	@RequestMapping("/inner/api/program/programDetail.xhtml")
	public String programDetail(Long programId, ModelMap model, HttpServletRequest request) {
		if(programId==null){
			return getErrorXmlView(model, "缺少参数programId");
		}
		Program program = daoService.getObject(Program.class, programId);
		if(illegalOper(program, request)){
			return getIllegalXmlView(model);
		}
		return getOpenApiXmlDetail(ApiProgramBeanHelp.getProgram(program), "program", model, request);
	}
	@RequestMapping("/inner/api/program/programList.xhtml")
	public String programList(Integer typeId, Timestamp startTime, Long venueId, String cnName, ModelMap model, HttpServletRequest request) {
		DetachedCriteria query = DetachedCriteria.forClass(Program.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		if(typeId!=null){
			query.add(Restrictions.eq("typeId", typeId));
		}
		if(venueId!=null){
			query.add(Restrictions.eq("venueId", venueId));
		}
		if(startTime!=null){
			query.add(Restrictions.ge("startTime", startTime));
		}
		if(StringUtils.isNotBlank(cnName)){
			query.add(Restrictions.ilike("cnName", cnName, MatchMode.ANYWHERE));
		}
		query.add(Restrictions.eq("delStatus", "N"));
		query.addOrder(Order.desc("addTime"));
		List<Program> programList = daoService.findByCriteria(query);
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		for(Program program : programList){
			resMapList.add(ApiProgramBeanHelp.getProgram(program));
		}
		return getOpenApiXmlList(resMapList, "programList,program", model, request);
	}
	@RequestMapping("/inner/api/program/addProgram.xhtml")
	public String addProgram(Long venueId, Long programId, String imgurl, String fileType, ModelMap model, HttpServletRequest request) {
		if(venueId==null){
			return getErrorXmlView(model, "缺少参数venueId");
		}
		Venue venue = daoService.getObject(Venue.class, venueId);
		if(!venue.getStadiumId().equals(getStadiumId(request))){
			return getErrorXmlView(model, "不能操作其它场馆的场地信息");
		}
		Program program = null;
		String fileName = null;
		if(programId!=null){
			program = daoService.getObject(Program.class, programId);
			if(illegalOper(program, request)){
				return getIllegalXmlView(model);
			}
			fileName = program.getImgurl();
		}else {
			program = new Program(getStadiumId(request), venueId, getClientMember(request).getId());
		}
		BindUtils.bindData(program, WebUtils.getRequestMap(request));
		if(StringUtils.isBlank(program.getCnName())){
			return getErrorXmlView(model, "项目名称不能为空");
		}
		if(StringUtils.length(program.getCnName())<5){
			return getErrorXmlView(model, "项目名称太多，至少5个字符");
		}
		if(StringUtils.isBlank(program.getCode())){
			return getErrorXmlView(model, "项目编号为空");
		}
		if(program.getTypeId()==null){
			return getErrorXmlView(model, "项目类型不能为空");
		}
		if(!ProgramType.typeMap.containsKey(program.getTypeId())){
			return getErrorXmlView(model, "项目类型不存在");
		}
		if(program.getStartTime()==null || program.getEndTime()==null){
			return getErrorXmlView(model, "项目时间不能为空");
		}
		if(StringUtils.isBlank(program.getStatus())){
			return getErrorXmlView(model, "项目状态不能为空");
		}
		if(StringUtils.isNotBlank(imgurl)){
			try {
				byte[] x = Hex.decodeHex(imgurl.toCharArray());
				String ext = StringUtils.contains(fileType, ".") ? fileType : "."+fileType;
				fileName = "program/" + StringUtil.getRandomString(12).toLowerCase() + ext;
				File file = new File(Config.UPLOAD_PATH + imgurl);
				FileUtils.writeByteArrayToFile(file, x);
			}  catch (Exception e) {
				e.printStackTrace();
				return getErrorXmlView(model, "处理图片异常");
			}
		}
		program.setImgurl(fileName);
		daoService.saveObject(program);
		return getOpenApiXmlDetail(ApiProgramBeanHelp.getProgram(program), "program", model, request);
	}
	@RequestMapping("/inner/api/program/delProgram.xhtml")
	public String delProgram(Long programId, ModelMap model, HttpServletRequest request) {
		Program program = daoService.getObject(Program.class, programId);
		if(program==null){
			return getErrorXmlView(model, "项目不存在！");
		}
		if(illegalOper(program, request)){
			return getIllegalXmlView(model);
		}
		List<Schedule> scheduleList = daoService.getObjectListByField(Schedule.class, "programId", programId);
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		for(Schedule schedule: scheduleList){
			if(StringUtils.equals(schedule.getFixed(), "Y")){
				if(schedule.getPlayTime().after(curtime)){
					return getErrorXmlView(model, "场次'"+ schedule.getCnName() +"'还未结束，不能删除！");
				}
			}else {
				if(schedule.getPlayEndTime().after(curtime)){
					return getErrorXmlView(model, "场次'"+ schedule.getCnName() +"'还未结束，不能删除！");
				}
			}
		}
		program.setDelStatus("Y");
		daoService.saveObject(program);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/program/setStatus.xhtml")
	public String setStatus(Long programId, String status, ModelMap model, HttpServletRequest request) {
		Program program = daoService.getObject(Program.class, programId);
		if(program==null){
			return getErrorXmlView(model, "项目不存在！");
		}
		if(illegalOper(program, request)){
			return getIllegalXmlView(model);
		}
		program.setStatus(status);
		daoService.saveObject(program);
		return getSuccessXmlView(model);
	}
}
