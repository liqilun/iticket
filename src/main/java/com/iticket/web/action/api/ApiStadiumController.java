package com.iticket.web.action.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.Config;
import com.iticket.model.stadium.Seat;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.stadium.VenueArea;
import com.iticket.service.IException;
import com.iticket.service.StadiumService;
import com.iticket.util.BindUtils;
import com.iticket.util.StringUtil;
import com.iticket.vo.ApiStadiumBeanHelp;
import com.iticket.web.util.WebUtils;

@Controller
public class ApiStadiumController extends BaseApiController{
	@Autowired@Qualifier("stadiumService")
	protected StadiumService stadiumService;
	@RequestMapping("/inner/api/stadium/stadiumDetail.xhtml")
	public String staduimDetail(ModelMap model, HttpServletRequest request) {
		Stadium stadium = daoService.getObject(Stadium.class, getStadiumId(request));
		return getOpenApiXmlDetail(ApiStadiumBeanHelp.getStatium(stadium), "stadium", model, request);
	}
	@RequestMapping("/inner/api/stadium/venueList.xhtml")
	public String venueList(ModelMap model, HttpServletRequest request) {
		DetachedCriteria query = DetachedCriteria.forClass(Venue.class);
		query.add(Restrictions.eq("stadiumId", getStadiumId(request)));
		query.add(Restrictions.ne("status", "D"));
		query.addOrder(Order.asc("orderNo"));
		List<Venue> venueList = daoService.findByCriteria(query);
		Collections.sort(venueList, new PropertyComparator("orderNo", false, true));
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		for(Venue venue : venueList){
			resMapList.add(ApiStadiumBeanHelp.getVenue(venue));
		}
		return getOpenApiXmlList(resMapList, "venueList,venue", model, request);
	}
	@RequestMapping("/inner/api/stadium/venueDetail.xhtml")
	public String venueDetail(Long venueId, ModelMap model, HttpServletRequest request) {
		Venue venue = daoService.getObject(Venue.class, venueId);
		if(illegalOper(venue, request)){
			return getIllegalXmlView(model);
		}
		return getOpenApiXmlDetail(ApiStadiumBeanHelp.getVenue(venue), "venue", model, request);
	}
	@RequestMapping("/inner/api/stadium/venueAreaList.xhtml")
	public String venueAreaList(Long venueId, ModelMap model, HttpServletRequest request) {
		DetachedCriteria query = DetachedCriteria.forClass(VenueArea.class);
		query.add(Restrictions.eq("venueId", venueId));
		query.add(Restrictions.ne("status", "D"));
		List<VenueArea> venueAreaList = daoService.findByCriteria(query);
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		for(VenueArea venueArea : venueAreaList){
			resMapList.add(ApiStadiumBeanHelp.getVenueArea(venueArea));
		}
		return getOpenApiXmlList(resMapList, "venueAreaList,venueArea", model, request);
	}
	@RequestMapping("/inner/api/stadium/venueAreaDetail.xhtml")
	public String venueAreaDetail(Long venueAreaId, ModelMap model, HttpServletRequest request) {
		VenueArea venueArea = daoService.getObject(VenueArea.class, venueAreaId);
		return getOpenApiXmlDetail(ApiStadiumBeanHelp.getVenueArea(venueArea), "venueArea", model, request);
	}
	@RequestMapping("/inner/api/stadium/venueAreaSeatList.xhtml")
	public String venueAreaSeatList(Long venueAreaId, ModelMap model, HttpServletRequest request) {
		List<Seat> venueAreaSeatList = daoService.getObjectListByField(Seat.class, "venueAreaId", venueAreaId);
		List<Map<String,Object>> resMapList = new ArrayList<Map<String,Object>>();
		for(Seat seat : venueAreaSeatList){
			resMapList.add(ApiStadiumBeanHelp.getVenueAreaSeat(seat));
		}
		return getOpenApiXmlList(resMapList, "seatList,seat", model, request);
	}
	
	@RequestMapping("/inner/api/stadium/addVenue.xhtml")
	public String addVenue(Long venueId, Integer orderNo, String background, String fileType, ModelMap model, HttpServletRequest request) {
		Venue venue = null;
		String fileName = null;
		if(venueId!=null){
			venue = daoService.getObject(Venue.class, venueId);
			if(illegalOper(venue, request)){
				return getIllegalXmlView(model);
			}
			fileName = venue.getBackground();
		}else {
			venue = new Venue(getClientMember(request));
		}
		BindUtils.bindData(venue, WebUtils.getRequestMap(request));
		if(StringUtils.isBlank(venue.getCnName())){
			return getErrorXmlView(model, "场地名称不能为空");
		}
		if(StringUtils.length(venue.getCnName())<2){
			return getErrorXmlView(model, "场地名称至少2个字符");
		}
		if(orderNo==null){
			venue.setOrderNo(1);
		}
		if(StringUtils.isNotBlank(background)){
			try {
				byte[] x = Hex.decodeHex(background.toCharArray());
				String ext = StringUtils.contains(fileType, ".") ? fileType : "."+fileType;
				fileName = "venue/" + StringUtil.getRandomString(12).toLowerCase() + ext;
				File file = new File(Config.UPLOAD_PATH + fileName);
				FileUtils.writeByteArrayToFile(file, x);
			}  catch (Exception e) {
				e.printStackTrace();
				return getErrorXmlView(model, "处理图片异常");
			}
		}
		venue.setBackground(fileName);
		daoService.saveObject(venue);
		return getOpenApiXmlDetail(ApiStadiumBeanHelp.getVenue(venue), "venue", model, request);
	}
	@RequestMapping("/inner/api/stadium/delVenue.xhtml")
	public String delVenue(Long venueId, ModelMap model, HttpServletRequest request) {
		Venue venue = daoService.getObject(Venue.class, venueId);
		if(venue==null){
			return getErrorXmlView(model, "场地不存在");
		}
		if(illegalOper(venue, request)){
			return getIllegalXmlView(model);
		}
		venue.setStatus("D");
		daoService.saveObject(venue);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/stadium/addVenueArea.xhtml")
	public String addVenueArea(Long venueAreaId, Long venueId, String icon, String fileType,  ModelMap model, HttpServletRequest request) {
		VenueArea venueArea = null;
		String fileName = null;
		Venue venue = daoService.getObject(Venue.class, venueId);
		if(venueAreaId!=null){
			venueArea = daoService.getObject(VenueArea.class, venueAreaId);
			if(illegalOper(venue, request)){
				return getIllegalXmlView(model);
			}
			fileName = venueArea.getIcon();
		}else {
			venueArea = new VenueArea(getClientMember(request), venue);
		}
		BindUtils.bindData(venueArea, WebUtils.getRequestMap(request));
		if(StringUtils.isBlank(venueArea.getCnName())){
			return getErrorXmlView(model, "区域名称不能为空");
		}
		if(StringUtils.length(venueArea.getCnName())<2){
			return getErrorXmlView(model, "区域场地名称至少2个字符");
		}
		if(StringUtils.isBlank(venueArea.getStanding())){
			return getErrorXmlView(model, "是否站票不能为空");
		}
		if(!venueArea.hasSeat()){
			if(venueArea.getTotal()==null || venueArea.getVlimit()==null){
				return getErrorXmlView(model, "站票总量或限制数不能为空！");
			}
			if(venueArea.getTotal()<=venueArea.getVlimit()){
				return getErrorXmlView(model, "站票总量不能小于总限制数！");
			}
		}
		if(StringUtils.isNotBlank(icon)){
			try {
				byte[] x = Hex.decodeHex(icon.toCharArray());
				String ext = StringUtils.contains(fileType, ".") ? fileType : "."+fileType;
				fileName = "area/" + StringUtil.getRandomString(12).toLowerCase() + ext;
				File file = new File(Config.UPLOAD_PATH + fileName);
				FileUtils.writeByteArrayToFile(file, x);
			}  catch (Exception e) {
				e.printStackTrace();
				return getErrorXmlView(model, "处理图片异常");
			}
		}
		venueArea.setIcon(fileName);
		daoService.saveObject(venueArea);
		return getOpenApiXmlDetail(ApiStadiumBeanHelp.getVenueArea(venueArea), "venueArea", model, request);
	}
	@RequestMapping("/inner/api/stadium/delVenueArea.xhtml")
	public String delVenueArea(Long venueAreaId, ModelMap model, HttpServletRequest request) {
		VenueArea venueArea = daoService.getObject(VenueArea.class, venueAreaId);
		if(venueArea==null){
			return getErrorXmlView(model, "场地区域不存在！");
		}
		if(illegalOper(venueArea, request)){
			return getIllegalXmlView(model);
		}
		venueArea.setStatus("D");
		daoService.saveObject(venueArea);
		return getSuccessXmlView(model);
	}
	@RequestMapping("/inner/api/stadium/addVenueAreaSeat.xhtml")
	public String addVenueAreaSeat(Long venueAreaId, String seatBody, Integer gridWidth,Integer gridHeight, ModelMap model, HttpServletRequest request) {
		try {
			stadiumService.addVenueAreaSeatList(getClientMember(request), venueAreaId, seatBody, gridWidth, gridHeight);
		} catch (IException e) {
			return getErrorXmlView(model, e.getCode(), e.getMessage());
		}
		return getSuccessXmlView(model);
	}
}
