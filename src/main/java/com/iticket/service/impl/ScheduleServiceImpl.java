package com.iticket.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.iticket.model.api.ClientMember;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Price2Stand;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Seat;
import com.iticket.model.stadium.Venue;
import com.iticket.model.stadium.VenueArea;
import com.iticket.service.IException;
import com.iticket.service.ScheduleService;
import com.iticket.support.ResultCode;
import com.iticket.util.BeanUtil;
import com.iticket.util.BindUtils;
import com.iticket.util.StringUtil;
import com.iticket.web.util.WebUtils;
import com.iticket.xml.XHelp;
import com.iticket.xml.XPrice2Stand;
import com.iticket.xml.XPrice2StandList;
import com.iticket.xml.XSchedulePrice;
import com.iticket.xml.XSchedulePriceList;

@Service("scheduleService")
public class ScheduleServiceImpl extends BaseServiceImpl implements ScheduleService{
	private String getUploadPath(){
		return config.getString("uploadPath");
	}
	@Override
	public Schedule addSchedule(ClientMember member, Long stadiumId, Long scheduleId, Long venueId, String seat, String venueBackground, String fileType, HttpServletRequest request)
			throws IException {
		if(venueId==null){
			throw new IException("场地id不能为空");
		}
		Venue venue = baseDao.getObject(Venue.class, venueId);
		if(venue==null){
			throw new IException("场地不存在");
		}
		if(!venue.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		Schedule s = null;
		String fileName = null;
		Long layoutId = null;
		List<VenueArea> venueAreaList = new ArrayList<VenueArea>();
		if(scheduleId!=null){
			s = baseDao.getObject(Schedule.class, scheduleId);
			if(!s.getStadiumId().equals(stadiumId)){
				throw IException.ILLEGAL;
			}
			if(!StringUtils.equals(s.getSeat(), seat)){
				throw new IException("不能由选座改为普通场次，或有普通场次转化为选座");
			}
			fileName = s.getVenueBackground();
			layoutId = s.getLayoutId();
		}else {
			s = new Schedule(member.getId());
			venueAreaList = baseDao.getObjectListByField(VenueArea.class, "venueId", venueId);
			if(venueAreaList.size()==0){
				throw new IException("还未设置基础区域，请先设置区域才开场次");
			}
		}
		BindUtils.bindData(s, WebUtils.getRequestMap(request));
		if(StringUtils.isBlank(s.getCode())){
			throw new IException("场次编号不能为空");
		}
		if(StringUtils.isBlank(s.getCnName())){
			throw new IException("场次名称不能为空");
		}
		if(StringUtils.length(s.getCnName())<5){
			throw new IException("场次名称至少5个字符");
		}
		if(StringUtils.isBlank(s.getSeat())){
			throw new IException("是否为支持选座不能为空");
		}
		if(StringUtils.isBlank(s.getFixed())){
			throw new IException("是否为固定时间不能为空");
		}
		if(StringUtils.equals(s.getFixed(), "Y")) {
			if(s.getPlayTime()==null){
				throw new IException("场次时间不能为空");
			}
		}else {
			if(s.getPlayEndTime()==null){
				throw new IException("无固定时间的场次，结束时间不能为空");
			}
		}
		if(StringUtils.isBlank(s.getActive())){
			throw new IException("是否激活不能为空");
		}
		if(s.getProgramId()==null){
			throw new IException("项目不能为空");
		}else {
			Program program = baseDao.getObject(Program.class, s.getProgramId());
			if(program==null){
				throw new IException("项目不存在");
			}
		}
		if(StringUtils.isNotBlank(venueBackground)){
			try {
				byte[] x = Hex.decodeHex(venueBackground.toCharArray());
				String ext = StringUtils.contains(fileType, ".") ? fileType : "."+fileType;
				fileName = "schedule/" + StringUtil.getRandomString(12).toLowerCase() + ext;
				File file = new File(getUploadPath() + fileName);
				FileUtils.writeByteArrayToFile(file, x);
			}  catch (Exception e) {
				e.printStackTrace();
				throw new IException("处理图片异常");
			}
		}else {
			fileName = venue.getBackground();
		}
		s.setStadiumId(stadiumId);
		s.setLayoutId(layoutId);
		s.setVenueBackground(fileName);
		s.setDelStatus("N");
		baseDao.saveObject(s);
		if(scheduleId==null){
			boolean hasSeat = StringUtils.equals(seat, "Y");
			if(hasSeat){
				for(VenueArea area : venueAreaList){
					if(area.hasSeat()){
						ScheduleVenueArea sarea = new ScheduleVenueArea(s, area);
						baseDao.saveObject(sarea);
						List<Seat> bSeatList = baseDao.getObjectListByField(Seat.class, "venueAreaId", area.getId());
						for(Seat bSeat : bSeatList){
							ScheduleSeat sseat = new ScheduleSeat(s, bSeat, sarea);
							baseDao.saveObject(sseat);
						}
					}else {
						ScheduleVenueArea sarea = new ScheduleVenueArea(s, area);
						baseDao.saveObject(sarea);
					}
				}
			}else {
				for(VenueArea area : venueAreaList){
					if(!area.hasSeat()){
						ScheduleVenueArea sarea = new ScheduleVenueArea(s, area);
						baseDao.saveObject(sarea);
					}
				}
			}
		}
		return s;
	}
	@Override
	public void addSchedulePrice(ClientMember member, Long scheduleId, String priceBody)
			throws IException {
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在");
		}
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ResultCode<XSchedulePriceList> code = XHelp.getBean(priceBody, "schedulePriceList");
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		List<SchedulePrice> nxpList = new ArrayList<SchedulePrice>();
		List<XSchedulePrice> xpList = code.getRetval().getSchedulePriceList();
		for(XSchedulePrice xp : xpList){
			SchedulePrice nxp = null;
			if(xp.getPriceId()!=null){
				nxp = baseDao.getObject(SchedulePrice.class, xp.getPriceId());
			}else {
				nxp = new SchedulePrice(member);
			}
			BeanUtil.copyProperties(nxp, xp);
			schedule.setProgramId(schedule.getProgramId());
			nxpList.add(nxp);
		}
		baseDao.saveObjectList(nxpList);
	}
	@Override
	public void addScheduleStandPrice(ClientMember member, Long scheduleId, String priceBody)
			throws IException {
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在");
		}
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ResultCode<XSchedulePriceList> code = XHelp.getBean(priceBody, "schedulePriceList");
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		List<SchedulePrice> nxpList = new ArrayList<SchedulePrice>();
		List<XSchedulePrice> xpList = code.getRetval().getSchedulePriceList();
		for(XSchedulePrice xp : xpList){
			SchedulePrice nxp = null;
			if(xp.getPriceId()!=null){
				nxp = baseDao.getObject(SchedulePrice.class, xp.getPriceId());
			}else {
				nxp = new SchedulePrice(member);
			}
			BeanUtil.copyProperties(nxp, xp);
			schedule.setProgramId(schedule.getProgramId());
			nxpList.add(nxp);
		}
		baseDao.saveObjectList(nxpList);
	}
	
	@Override
	public void addPrice2Stand(ClientMember member, Long scheduleId, Long scheduleVenueAreaId, String p2sBody)
			throws IException {
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			throw new IException("场次不存在");
		}
		if(!schedule.getStadiumId().equals(member.getStadiumId())){
			throw IException.ILLEGAL;
		}
		ScheduleVenueArea sva = baseDao.getObject(ScheduleVenueArea.class, scheduleVenueAreaId);
		if(sva==null){
			throw new IException("场次区域不存在");
		}
		if(sva.hasSeat()){
			throw new IException("该区域是座位场次，不能设置站票价格");
		}
		dbLogger.warn(p2sBody);
		ResultCode<XPrice2StandList> code = XHelp.getBean(p2sBody, "price2StandList");
		if(!code.isSuccess()){
			throw new IException(code.getMsg());
		}
		List<Price2Stand> resList = new ArrayList<Price2Stand>();
		List<XPrice2Stand> xpList = code.getRetval().getPrice2StandList();
		Map<String, Price2Stand> oldMap = new HashMap<String, Price2Stand>();
		int vlimit = 0, total = 0;
		for(XPrice2Stand xp2s : xpList){
			String pskey = xp2s.getPriceId() + "_" + scheduleVenueAreaId;
			Price2Stand p2s = baseDao.getObject(Price2Stand.class, pskey);
			if(p2s==null){
				p2s = new Price2Stand(member, schedule, xp2s.getPriceId(), scheduleVenueAreaId);
			}else {
				oldMap.put(pskey, p2s);
			}
			p2s.setTotal(xp2s.getTotal());
			p2s.setVlimit(xp2s.getVlimit());
			resList.add(p2s);
			vlimit = vlimit + xp2s.getVlimit();
			total = total + xp2s.getTotal();
		}
		if(total>sva.getTotal()){
			throw new IException("总数不能大于场地区域总数");
		}
		if(vlimit>sva.getVlimit()){
			throw new IException("限制数不能大于场地区域限制总数");
		}
		if(total<=vlimit){
			throw new IException("站票总量不能小于限制总数");
		}
		baseDao.saveObjectList(resList);
		List<ScheduleStand> removeList = new ArrayList<ScheduleStand>();
		List<ScheduleStand> standList = new ArrayList<ScheduleStand>();
		for(Price2Stand res : resList){
			int count = res.getTotal()-res.getVlimit();
			if(oldMap.containsKey(res.getPskey())){
				Price2Stand old = oldMap.get(res.getPskey());
				int oldCount = old.getTotal() - old.getVlimit();
				int diff = oldCount - count;
				if(diff>0){
					List<ScheduleStand> tmpList = getScheduleStand(old, diff);
					removeList.addAll(tmpList);
				}else if(diff<0){
					diff = Math.abs(diff);
					for(int i=1;i<=diff;i++){
						ScheduleStand stand = new ScheduleStand(schedule, res.getPriceId(), scheduleVenueAreaId);
						standList.add(stand);
					}
				}
			}else {
				for(int i=1;i<=count;i++){
					ScheduleStand stand = new ScheduleStand(schedule, res.getPriceId(), scheduleVenueAreaId);
					standList.add(stand);
				}
			}
		}
		baseDao.saveObjectList(standList);
		updScheudlePriceSnum(scheduleId);
	}
	private List<ScheduleStand> getScheduleStand(Price2Stand p2s, int count){
		DetachedCriteria query = DetachedCriteria.forClass(ScheduleStand.class);
		query.add(Restrictions.eq("priceId", p2s.getPriceId()));
		query.add(Restrictions.eq("scheduleVenueAreaId", p2s.getScheduleVenueAreaId()));
		query.add(Restrictions.eq("status", ScheduleStand.STATUS_FREE));
		List<ScheduleStand> standList = baseDao.findByCriteria(query, 0, count);
		return standList;
	}
	@Override
	public void updScheudlePriceSnum(Long scheduleId){
		List<SchedulePrice> spriceList = baseDao.getObjectListByField(SchedulePrice.class, "scheduleId", scheduleId);
		Map<Long, Integer> quantityMap = new HashMap<Long, Integer>();
		Schedule schedule = baseDao.getObject(Schedule.class, scheduleId);
		if(schedule.supportSeat()){
			String hql = "select count(*) from ScheduleSeat s where s.priceId=?";
			for(SchedulePrice sprice : spriceList){
				int q = Integer.valueOf(baseDao.findByHql(hql, sprice.getId()).get(0)+"");
				Integer tmp = quantityMap.get(sprice.getId());
				if(tmp==null){
					tmp = 0;
				}
				quantityMap.put(sprice.getId(), tmp + q);
			}
		}else {
			String hql = "select count(*) from ScheduleStand s where s.priceId=?";
			for(SchedulePrice sprice : spriceList){
				int q = Integer.valueOf(baseDao.findByHql(hql, sprice.getId()).get(0)+"");
				Integer tmp = quantityMap.get(sprice.getId());
				if(tmp==null){
					tmp = 0;
				}
				quantityMap.put(sprice.getId(), tmp + q);
			}
		}
		for(SchedulePrice sprice : spriceList){
			sprice.setSnum(quantityMap.get(sprice.getId()));
		}
		baseDao.saveObjectList(spriceList);
	}
}
