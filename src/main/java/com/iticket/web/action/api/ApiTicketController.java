package com.iticket.web.action.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.PaymentType;
import com.iticket.constant.SaleType;
import com.iticket.constant.VoucherType;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.model.ticket.VoucherDetail;
import com.iticket.service.IException;
import com.iticket.service.ProgramService;
import com.iticket.service.ScheduleService;
import com.iticket.service.Ticket4SeatService;
import com.iticket.service.TicketService;
import com.iticket.util.BeanUtil;
import com.iticket.util.DateUtil;
import com.iticket.vo.ApiVoucherBeanHelper;
import com.iticket.vo.ReserveInfoVo;
import com.iticket.vo.ReservePriceVo;
import com.iticket.vo.TicketVo;

@Controller
public class ApiTicketController extends BaseApiController{
	@Autowired@Qualifier("programService")
	private ProgramService programService;
	@Autowired@Qualifier("scheduleService")
	private ScheduleService scheduleService;
	@Autowired@Qualifier("ticketService")
	private TicketService ticketService;
	@Autowired@Qualifier("ticket4SeatService")
	private Ticket4SeatService ticket4SeatService;
	@RequestMapping("/inner/api/ticket/toSoldOrReserve.xhtml")
	public String toSoldOrReserve(Long scheduleId, String seatIds, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在！");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		if(!schedule.supportSeat()){
			return getErrorXmlView(model, "该场次不支持在线选座！");
		}
		List<Long> idList = BeanUtil.getIdList(seatIds, ",");
		List<ScheduleSeat> seatList = daoService.getObjectList(ScheduleSeat.class, idList);
		if(seatList.size()==0){
			return getErrorXmlView(model, "座位不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(seatList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			return getErrorXmlView(model, "你选择的座位在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			return getErrorXmlView(model, "你选择的座位说在的场次，和输入的场次不匹配");
		}
		for(ScheduleSeat seat : seatList){
			if(!seat.getStatus().equals(ScheduleSeat.STATUS_FREE)){
				return getErrorXmlView(model, seat.getSeatLabel()+"非空闲状态不能操作，请刷新重试");
			}
			seat.setStatus(ScheduleSeat.STATUS_LOCKED);
		}
		daoService.saveObjectList(seatList);
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("ticketType", getMap2Str(VoucherType.TICKETTYPEMAP));
		resMap.put("payType", getMap2Str(PaymentType.PAYMETHODMAP));
		resMap.put("logisticsType", getMap2Str(VoucherType.WULIUMAP));
		resMap.put("saleType", getMap2Str(SaleType.saleTypeMap));
		resMap.put("disType", getMap2Str(VoucherType.DISTYPE_MAP));
		return getOpenApiXmlDetail(resMap, "IType", model, request);
	}
	
	@RequestMapping("/inner/api/ticket/unLock.xhtml")
	public String unLockSeat(Long scheduleId, String seatIds, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		Schedule schedule = daoService.getObject(Schedule.class, scheduleId);
		if(schedule==null){
			return getErrorXmlView(model, "场次不存在！");
		}
		if(illegalOper(schedule, request)){
			return getIllegalXmlView(model);
		}
		List<Long> idList = BeanUtil.getIdList(seatIds, ",");
		List<ScheduleSeat> seatList = daoService.getObjectList(ScheduleSeat.class, idList);
		if(seatList.size()==0){
			return getErrorXmlView(model, "座位不存在！");
		}
		List<Long> scheduleIdList = BeanUtil.getBeanPropertyList(seatList, Long.class, "scheduleId", true);
		if(scheduleIdList.size()>1){
			return getErrorXmlView(model, "你选择的座位在不同的场次，无法完成操作");
		}
		if(!scheduleIdList.contains(scheduleId)){
			return getErrorXmlView(model, "你选择的座位说在的场次，和输入的场次不匹配");
		}
		List<ScheduleSeat> updList = new ArrayList<ScheduleSeat>();
		for(ScheduleSeat seat : seatList){
			if(seat.getStatus().equals(ScheduleSeat.STATUS_LOCKED)){
				seat.setStatus(ScheduleSeat.STATUS_FREE);
				updList.add(seat);
			}
		}
		daoService.saveObjectList(updList);
		return getSuccessXmlView(model);
	}
	//正常出票
	@RequestMapping("/inner/api/ticket/sell.xhtml")
	public String sell(Long scheduleId, String seatIds, String ticketType/*出票类型*/, String showPrice, String disType,
			Long customerId, String customerName, String customerMobile, String customerAddress, 
			String payMethod, Integer saleType, ModelMap model, HttpServletRequest request) {
		if(scheduleId==null){
			return getErrorXmlView(model, "缺少scheduleId");
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		try {
			String pzType = VoucherType.PZTYPE_CP;
			List<TicketVo> voList = ticket4SeatService.sell(getClientMember(request), scheduleId, seatIds, pzType, ticketType, disType, showPrice, 
					customerId, customerName, customerMobile, customerAddress, payMethod, saleType);
			resMapList = BeanUtil.copyToListMap(voList);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getOpenApiXmlList(resMapList, "ticketVoList,ticketVo", model, request);
	}
	//预留
	@RequestMapping("/inner/api/ticket/reserve.xhtml")
	public String reserve(Long scheduleId, String seatIds, Long customerId, String customerName, String customerMobile, String customerAddress, String disType, Timestamp releaseTime, Timestamp tipTime, ModelMap model, HttpServletRequest request){
		try {
			Long vId = ticket4SeatService.reserve(getClientMember(request), scheduleId, seatIds, customerId, customerName, customerMobile, customerAddress, releaseTime, tipTime, disType);
			return getSingleResultXmlView(model, vId);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
	}
	//预留信息
	@RequestMapping("/inner/api/ticket/reserveInfo.xhtml")
	public String reserveInfo(Long voucherId, ModelMap model) {
		Voucher voucher = daoService.getObject(Voucher.class, voucherId);
		if(!voucher.getPzType().equals(VoucherType.PZTYPE_YL)){
			return getErrorXmlView(model, "非预留凭证");
		}
		if(StringUtils.equals(voucher.getPayStatus(), "Y")){
			return getErrorXmlView(model, "预留凭证已支付，不要重复操作");
		}
		List<ReservePriceVo> priceVoList = new ArrayList<ReservePriceVo>();
		List<VoucherDetail> detailList = daoService.getObjectListByField(VoucherDetail.class, "voucherId", voucherId);
		Map<Double, List<VoucherDetail>> groupMap = BeanUtil.groupBeanList(detailList, "ticketPrice");
		for(Map.Entry<Double, List<VoucherDetail>> entry : groupMap.entrySet()){
			List<VoucherDetail> pdList = entry.getValue();
			String seatText =  "";
			if(voucher.hasSeat()){
				List<Long> seatidList = BeanUtil.getBeanPropertyList(pdList, Long.class, "seatid", false);
				List<ScheduleSeat> seatList = daoService.getObjectList(ScheduleSeat.class, seatidList);
				List<String> seatLabelList = BeanUtil.getBeanPropertyList(seatList, String.class, "seatLabel", false);
				seatText =  StringUtils.join(seatLabelList, ",");
			}
			ReservePriceVo priceVo = new ReservePriceVo(voucher, seatText, pdList);
			priceVoList.add(priceVo);
		}
		VoucherCustomer customer = null;
		List<VoucherCustomer> vcustomerList = daoService.getObjectListByField(VoucherCustomer.class, "voucherId", voucherId);
		if(vcustomerList.size()>0){
			customer = vcustomerList.get(0);
		}
		ReserveInfoVo reserveInfo = new ReserveInfoVo(voucherId, getMap2Str(VoucherType.TICKETTYPEMAP), getMap2Str(PaymentType.PAYMETHODMAP), 
				getMap2Str(VoucherType.WULIUMAP), getMap2Str(SaleType.saleTypeMap), getMap2Str(VoucherType.DISTYPE_MAP), customer, priceVoList);
		model.put("reserveInfo", reserveInfo);
		return getXmlView(model, "api/reserveInfo.vm");
	}
	//预留出票
	@RequestMapping("/inner/api/ticket/reserveSell.xhtml")
	public String reserveSell(Long voucherId, String ticketType, String payMethod, String showPrice, 
			Integer saleType, String disType, ModelMap model, HttpServletRequest request) {
		if(voucherId==null){
			return getErrorXmlView(model, "缺少voucherId");
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		try {
			List<TicketVo> voList = ticketService.reserveSell(getClientMember(request), voucherId, ticketType, payMethod, showPrice, saleType, disType);
			resMapList = BeanUtil.copyToListMap(voList);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getOpenApiXmlList(resMapList, "ticketVoList,ticketVo", model, request);
	}
	
	//出票通知
	@RequestMapping("/inner/api/ticket/ticketNotification.xhtml")
	public String notifyPrint(Long voucherId, String seatIds, ModelMap model) {
		if(voucherId==null){
			return getErrorXmlView(model, "缺少voucherId");
		}
		List<Long> seatIdList = BeanUtil.getIdList(seatIds, ",");
		if(seatIdList.size()==0){
			return getErrorXmlView(model, "未查询到座位");
		}
		Voucher voucher = daoService.getObject(Voucher.class, voucherId);
		if(voucher==null){
			return getErrorXmlView(model, "凭证不存在！");
		}
		List<VoucherDetail> updList = new ArrayList<VoucherDetail>();
		List<VoucherDetail> detailList = daoService.getObjectListByField(VoucherDetail.class, "voucherId", voucherId);
		for(VoucherDetail detail : detailList){
			if(seatIdList.contains(detail.getSeatid())){
				detail.setStatus(VoucherType.VD_STATUS_Y);
				detail.setTimes(detail.getTimes() + 1);
				updList.add(detail);
			}
		}
		daoService.saveObjectList(updList);
		return getSuccessXmlView(model);
	}
	
	//预留查询
	@RequestMapping("/inner/api/ticket/qryReserveByMobile.xhtml")
	public String notifyPrint(String mobile, ModelMap model) {
		if(StringUtils.isBlank(mobile)){
			return getErrorXmlView(model, "缺少mobile");
		}
		List<Voucher> voucherList = new ArrayList<Voucher>();
		List<VoucherCustomer> vcList = daoService.getObjectListByField(VoucherCustomer.class, "customerMobile", mobile);
		Map<Long, VoucherCustomer> customerMap = new HashMap<Long, VoucherCustomer>();
		for(VoucherCustomer vc : vcList){
			Voucher voucher = daoService.getObject(Voucher.class, vc.getVoucherId());
			if(voucher.canReserve2Sell()){
				if(StringUtils.isBlank(voucher.getSeatText())){
					String seatText = ticketService.getSeatLabelsByVoucherId(voucher);
					voucher.setSeatText(seatText);
					daoService.saveObject(voucher);
				}
				customerMap.put(voucher.getId(), vc);
				voucherList.add(voucher);
			}
		}
		model.put("voucherList", voucherList);
		model.put("customerMap", customerMap);
		return getXmlView(model, "api/voucherQryByMobile.vm");
	}
	//
	@RequestMapping("/inner/api/ticket/getVoucherByVoucherNo.xhtml")
	public String getVoucherByVoucherNo(String voucherNo, ModelMap model) {
		if(StringUtils.isBlank(voucherNo)){
			return getErrorXmlView(model, "缺少voucherNo");
		}
		Voucher voucher = daoService.getObjectByUkey(Voucher.class, "voucherNo", voucherNo);
		if(voucher==null){
			return getErrorXmlView(model, "凭证不存在！");
		}
		List<VoucherCustomer> customerList = daoService.getObjectListByField(VoucherCustomer.class, "voucherId", voucher.getId());
		if(StringUtils.isBlank(voucher.getSeatText())){
			String seatText = ticketService.getSeatLabelsByVoucherId(voucher);
			voucher.setSeatText(seatText);
			daoService.saveObject(voucher);
		}
		model.put("voucher", voucher);
		model.put("customer", customerList.size()>0?customerList.get(0):null);
		return getXmlView(model, "api/voucher.vm");
	}
	@RequestMapping("/inner/api/ticket/reserveCancel.xhtml")
	public String reserveCancel(Long voucherId, ModelMap model, HttpServletRequest request) {
		try {
			ticketService.reserveCancel(getClientMember(request), voucherId);
		} catch (IException e) {
			return getErrorXmlView(model, e.getMsg());
		}
		return getSuccessXmlView(model);
	}
	//查询重复打印
	@RequestMapping("/inner/api/ticket/repeatPrintStep1.xhtml")
	public String repeatPrintStep2(String seatIds, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(seatIds)){
			return getErrorXmlView(model, "缺少seatIds");
		}
		List<Long> seatIdList = BeanUtil.getIdList(seatIds, ",");
		List<Voucher> voucherList = new ArrayList<Voucher>();
		for(Long seatId : seatIdList){
			List<VoucherDetail> detailList = daoService.getObjectListByField(VoucherDetail.class, "seatid", seatId);
			List<Long> vIdList = BeanUtil.getBeanPropertyList(detailList, Long.class, "voucherId", true);
			List<Voucher> qryVoucherList = daoService.getObjectList(Voucher.class, vIdList);
			for(Voucher voucher : qryVoucherList){
				if(!voucherList.contains(voucher) && voucher.canReprint()){
					voucherList.add(voucher);
				}
			}
		}
		if(voucherList.size()==0){
			return getErrorXmlView(model, "没有查询可打印订单记录");
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		for(Voucher voucher : voucherList){
			String seatText = ticketService.getSeatLabelsByVoucherId(voucher);
			Map<String, Object> resMap = ApiVoucherBeanHelper.getVoucher(voucher, seatText);
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "voucherList,voucher", model, request);
	}
	//查询重复打印
	@RequestMapping("/inner/api/ticket/repeatPrintStandStep1.xhtml")
	public String repeatPrintStandStep1(String voucherNo, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(voucherNo)){
			return getErrorXmlView(model, "缺少voucherNo");
		}
		Voucher voucher = daoService.getObjectByUkey(Voucher.class, "voucherNo", voucherNo);
		if(voucher==null){
			return getErrorXmlView(model, "订单不存在！");
		}
		if(!voucher.canReprint()){
			return getErrorXmlView(model, "该订单不可以重新打印！");
		}
		Map<String, Object> resMap = ApiVoucherBeanHelper.getVoucher(voucher, null);
		return getOpenApiXmlDetail(resMap, "voucher", model, request);
	}
	//重复打印
	@RequestMapping("/inner/api/ticket/repeatPrintStep2.xhtml")
	public String repeatPrint(Long voucherId, String reprintPass, ModelMap model, HttpServletRequest request) {
		if(voucherId==null){
			return getErrorXmlView(model, "缺少voucherId");
		}
		List<TicketVo> voList = new ArrayList<TicketVo>();
		try {
			voList = ticketService.repeatPrint(getStadium(request), getClientMember(request), voucherId, reprintPass);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		List<Map<String, Object>> resMapList = BeanUtil.copyToListMap(voList);
		return getOpenApiXmlList(resMapList, "ticketVoList,ticketVo", model, request);
	}
	//退款查询
	@RequestMapping("/inner/api/ticket/refundStep1.xhtml")
	public String refund(String seatIds, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(seatIds)){
			return getErrorXmlView(model, "缺少seatIds");
		}
		List<Long> seatIdList = BeanUtil.getIdList(seatIds, ",");
		List<Voucher> voucherList = new ArrayList<Voucher>();
		for(Long seatId : seatIdList){
			List<VoucherDetail> detailList = daoService.getObjectListByField(VoucherDetail.class, "seatid", seatId);
			List<Long> voucherIdList = BeanUtil.getBeanPropertyList(detailList, Long.class, "voucherId", true);
			List<Voucher> qryVoucherList = daoService.getObjectList(Voucher.class, voucherIdList);
			for(Voucher voucher : qryVoucherList){
				if(!voucherList.contains(voucher) && voucher.canRefund()){
					voucherList.add(voucher);
				}
			}
		}
		if(voucherList.size()==0){
			return getErrorXmlView(model, "没有查询可退票订单记录");
		}
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		for(Voucher voucher : voucherList){
			String seatText = null;
			if(StringUtils.isBlank(voucher.getSeatText())){
				seatText = ticketService.getSeatLabelsByVoucherId(voucher);
				voucher.setSeatText(seatText);
				daoService.saveObject(voucher);
			}
			ticketService.getSeatLabelsByVoucherId(voucher);
			Map<String, Object> resMap = ApiVoucherBeanHelper.getVoucher(voucher, seatText);
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "voucherList,voucher", model, request);
	}
	//做退款操作
	@RequestMapping("/inner/api/ticket/refundStep2.xhtml")
	public String refund(Long voucherId, String refundPass, ModelMap model, HttpServletRequest request) {
		if(voucherId==null){
			return getErrorXmlView(model, "缺少voucherId");
		}
		try {
			ticketService.refund(getStadium(request),getClientMember(request), voucherId, refundPass);
		} catch (IException e) {
			e.printStackTrace();
			return getErrorXmlView(model, e.getMsg());
		}
		return getSuccessXmlView(model);
	}
	
	//预留提醒
	@RequestMapping("/inner/api/ticket/reserveMessageList.xhtml")
	public String reserveMessageList(ModelMap model, HttpServletRequest request) {
		List<Long> voucherIdList = daoService.findByHql("select voucherId from ReserveMessage where stadiumId=? and tipTime<=?", getStadiumId(request), DateUtil.getCurFullTimestamp());
		List<Voucher> qryList = daoService.getObjectList(Voucher.class, voucherIdList);
		List<Voucher> voucherList = new ArrayList<Voucher>();
		Map<Long, VoucherCustomer> customerMap = new HashMap<Long, VoucherCustomer>();
		for(Voucher voucher : qryList){
			if(voucher.canReserve2Sell()){
				if(StringUtils.isBlank(voucher.getSeatText())){
					String seatText = ticketService.getSeatLabelsByVoucherId(voucher);
					voucher.setSeatText(seatText);
					daoService.saveObject(voucher);
				}
				VoucherCustomer customer = null;
				List<VoucherCustomer> vcustomerList = daoService.getObjectListByField(VoucherCustomer.class, "voucherId", voucher.getId());
				if(vcustomerList.size()>0){
					customer = vcustomerList.get(0);
				}
				customerMap.put(voucher.getId(), customer);
				voucherList.add(voucher);
			}
		}
		model.put("voucherList", voucherList);
		model.put("customerMap", customerMap);
		return getXmlView(model, "api/voucherQryByMobile.vm");
	}
}
