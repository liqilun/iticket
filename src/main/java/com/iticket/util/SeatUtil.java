package com.iticket.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iticket.model.schedule.BaseSeatOrStand;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.vo.TicketVo;
import com.iticket.vo.report.SeatDetail;

/**
 * 排座座
 */
public class SeatUtil {
	public static final long LOCKTIMEOUT = 1000*60*15;
	
	private static final String[] SCHEDULESEAT_OUTPUT = {"id","uuid","lineno","rankno","specialLineno","x","y","scheduleId","ticketPriceId","ticketPoolId","venueAreaId","programId","status","sales"};
	
	/**
	 * 本地接口
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ScheduleSeat> filterSeatList(List<ScheduleSeat> list){
		for(ScheduleSeat ss : list){
			ss.setStatus(ss.getRealStatus());
		}
		List<ScheduleSeat> newList = BeanUtil.copyListByField(list, SCHEDULESEAT_OUTPUT);
		return newList;
	}
	

	public static String getSeatApiIds(List<ScheduleSeat> sSeatList){
		if(sSeatList==null){
			return "";
		}
		if(sSeatList.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder(sSeatList.size()*15);
		for(ScheduleSeat sSeat : sSeatList){
			sb.append(sSeat.getScheduleVenueAreaId());
			sb.append(':');
			sb.append(sSeat.getLineno());
			if(sSeat.getRankno()!=null){
				sb.append(':');
				sb.append(sSeat.getRankno());	
			}
			sb.append(',');
		}
		if(sb.length()==0){
			return "";
		}
		return sb.substring(0,sb.length()-1);
	}
	
	public static void filterCanSell(List<ScheduleSeat> ssList){
		Iterator<ScheduleSeat> it = ssList.iterator();
		while(it.hasNext()){
			ScheduleSeat ss = it.next();
			if(ss.getRealStatus().equals(ScheduleSeat.STATUS_FREE)){
				it.remove();
			}
		}
	}
	
	
	public static void filterSold(List<ScheduleSeat> ssList){
		Iterator<ScheduleSeat> it = ssList.iterator();
		while(it.hasNext()){
			ScheduleSeat ss = it.next();
			if(ss.getRealStatus().equals(ScheduleSeat.STATUS_FREE)){
				it.remove();
			}
		}
	}
	
	public static Map<String,String> getSeatStatusMap(List<ScheduleSeat> sSeatList){
		if(sSeatList==null){
			return null;
		}
		Map<String,String> map = new HashMap<String,String>();
		for(ScheduleSeat sSeat : sSeatList){
			String key = sSeat.getScheduleVenueAreaId()+":"+sSeat.getLineno();
			if(sSeat.getRankno()!=null){
				key += ":"+sSeat.getRankno();
			}
			
			String value = "N";
			if(sSeat.getRealStatus().equals(ScheduleSeat.STATUS_FREE)){
				value = "Y";
			}
			
			map.put(key, value);
		}
		return map;
	}
	
	
	public static String getSeatStatusList(List<ScheduleSeat> sSeatList){
		if(sSeatList==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(ScheduleSeat sSeat : sSeatList){
			String value = "N";
			if(sSeat.getRealStatus().equals(ScheduleSeat.STATUS_FREE)){
				value = "Y";
			}
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * 过滤不可售座位
	 * @param sSeatList
	 * @return
	 */
	public static void filterXX1(List<ScheduleSeat> sSeatList){
		if(sSeatList==null){
			return ;
		}
		Iterator<ScheduleSeat> it = sSeatList.iterator();
		while(it.hasNext()){
			ScheduleSeat ss = it.next();
			if(ss.getRealStatus()!=ScheduleSeat.STATUS_FREE){
				it.remove();
			}
		}
	}
	
	public static Integer getRealStatus(BaseSeatOrStand sSeat) {
		if(sSeat.getStatus()!=null && sSeat.getStatus().equals(ScheduleSeat.STATUS_LOCKED)){
			if(sSeat.getLockTime()==null){
				//warn! lockTime不能为空,不应该发生
				return ScheduleSeat.STATUS_FREE;
			}
			long lockTime = sSeat.getLockTime().getTime();
			long curTime = System.currentTimeMillis();
			if((curTime - lockTime) > LOCKTIMEOUT){
				return ScheduleSeat.STATUS_FREE;
			}
		}
		return sSeat.getStatus();
	}

	public static Integer getRealStatus(SeatDetail sSeat) {
		if(sSeat.getStatus()!=null && sSeat.getStatus().equals(ScheduleSeat.STATUS_LOCKED)){
			if(sSeat.getLockTime()==null){//!
				return ScheduleSeat.STATUS_FREE;
			}
			long lockTime = sSeat.getLockTime().getTime();
			long curTime = System.currentTimeMillis();
			if((curTime - lockTime) > LOCKTIMEOUT){
				return ScheduleSeat.STATUS_FREE;
			}
		}
		return sSeat.getStatus();
	}
	
	public static boolean setStandingSeat(List<TicketVo> voList){
		boolean hasStandingSeat = false;
		if(voList!=null){
			for(TicketVo tv : voList){
				if(tv.getRankno()==null||tv.getRankno().isEmpty()){
					tv.setLineno(null);
					hasStandingSeat = true;
				}
			}
		}
		return hasStandingSeat;
	}
	
	public static List<TicketVo> sort(List<TicketVo> voList, boolean standing){
		if(voList==null){
			return voList;
		}
		Comparator<TicketVo> comparator = null;
		if(standing){
			comparator = new Comparator<TicketVo>(){
				@Override
				public int compare(TicketVo o1, TicketVo o2) {
					int vflag = o1.getScheduleVenueAreaId().compareTo(o2.getScheduleVenueAreaId());
					if(vflag!=0){
						return vflag;
					}
					int flag = o1.getSerialNum().compareTo(o2.getSerialNum());
					return flag;
				}
			};
		}else{
			comparator = new Comparator<TicketVo>(){
				@Override
				public int compare(TicketVo o1, TicketVo o2) {
					int vflag = o1.getScheduleVenueAreaId().compareTo(o2.getScheduleVenueAreaId());
					if(vflag!=0){
						return vflag;
					}
					int flag = o1.getY().compareTo(o2.getY());
					if(flag==0){
						flag = o1.getX().compareTo(o2.getX());
					}
					return flag;
				}
			};
		}
		Collections.sort(voList, comparator);
		return voList;
	}
}
