package com.iticket.web.action.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.constant.VoucherType;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.model.ticket.VoucherDetail;
import com.iticket.service.ProgramService;
import com.iticket.web.action.BaseController;

@Controller
public class ReportController extends BaseController{
	@Autowired@Qualifier("programService")
	protected ProgramService programService;
	@RequestMapping("/client/report/index.xhtml")
	public String index(Timestamp startTime, Timestamp endTime, Long programId, ModelMap model, HttpServletRequest request) {
		Long stadiumId = getStadiumId(request);
		model.put("programList", programService.getProgramList(stadiumId));
		if(startTime==null || endTime==null){
			return "report/index.vm";
		}
		DetachedCriteria query = DetachedCriteria.forClass(Voucher.class);
		query.add(Restrictions.eq("stadiumId", stadiumId));
		query.add(Restrictions.eq("payStatus", "Y"));
		query.add(Restrictions.eq("refundStatus", "N"));
		query.add(Restrictions.ge("addTime", startTime));
		query.add(Restrictions.le("addTime", endTime));
		if(programId!=null){
			query.add(Restrictions.eq("programId", programId));
		}
		List<Voucher> voucherList = daoService.findByCriteria(query);
		Map<Long, String> memberMap = new HashMap<Long, String>();
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		Map<Long, Integer> seatNumMap = new HashMap<Long, Integer>();
		Map<Long, Double> seatAmountMap = new HashMap<Long, Double>();
		Map<Long, Double> discountMap = new HashMap<Long, Double>();
		Map<Double, Integer> priceQuantityMap = new HashMap<Double, Integer>();
		for(Voucher voucher : voucherList){
			Long memberId = voucher.getMemberId();
			String memberName = memberMap.get(memberId);
			int count = 1;
			int seatNum = voucher.getSeatNum();
			double seatAmount = voucher.getSeatAmount();
			double discount = voucher.getDiscount();
			if(StringUtils.isBlank(memberName)){
				memberMap.put(voucher.getMemberId(), voucher.getMemberName());
			}else {
				count = countMap.get(memberId) + count;
				seatNum = seatNumMap.get(memberId) + seatNum;
				seatAmount = seatAmountMap.get(memberId) + seatAmount;
				discount = discountMap.get(memberId) + discount;
			}
			countMap.put(memberId, count);
			seatNumMap.put(memberId, seatNum);
			seatAmountMap.put(memberId, seatAmount);
			discountMap.put(memberId, discount);
			
			List<VoucherDetail> detailList = daoService.getObjectListByField(VoucherDetail.class, "voucherId", voucher.getId());
			for(VoucherDetail detail : detailList){
				Integer quantity = priceQuantityMap.get(detail.getTicketPrice());
				if(quantity == null){
					quantity = 0;
				}
				priceQuantityMap.put(detail.getTicketPrice(), quantity+1);
			}
		}
		model.put("memberMap", memberMap);
		model.put("countMap", countMap);
		model.put("seatNumMap", seatNumMap);
		model.put("seatAmountMap", seatAmountMap);
		model.put("discountMap", discountMap);
		model.put("priceQuantityMap", priceQuantityMap);
		return "report/index.vm";
	}
	@RequestMapping("/client/report/qry.xhtml")
	public String program(String voucherNo, String customerMobile, String customerName, ModelMap model, HttpServletRequest request) {
		if(StringUtils.isBlank(customerMobile) && StringUtils.isBlank(voucherNo) && StringUtils.isBlank(customerName)){
			return "report/qry.vm";
		}
		List<Voucher> voucherList = new ArrayList<Voucher>();
		if(StringUtils.isNotBlank(voucherNo)){
			Voucher voucher = daoService.getObjectByUkey(Voucher.class, "voucherNo", voucherNo);
			if(voucher!=null && voucher.getStadiumId().equals(getStadiumId(request))){
				voucherList.add(voucher);
			}
		}else {
			DetachedCriteria query = DetachedCriteria.forClass(VoucherCustomer.class);
			if(StringUtils.isNotBlank(customerMobile)){
				query.add(Restrictions.eq("customerMobile", customerMobile));
			}
			if(StringUtils.isNotBlank(customerName)){
				query.add(Restrictions.eq("customerName", customerName));
			}
			query.setProjection(Projections.property("voucherId"));
			List<Long> voucherIdList = daoService.findByCriteria(query);
			if(voucherIdList.size()>0){
				Long stadiumId = getStadiumId(request);
				List<Voucher> qryList = daoService.getObjectList(Voucher.class, voucherIdList);
				for(Voucher voucher : qryList){
					if(stadiumId.equals(voucher.getStadiumId())){
						voucherList.add(voucher);
					}
				}
				Collections.sort(voucherList, new PropertyComparator("addTime", false, false));
			}
		}
		model.put("pztypeMap", VoucherType.PZTYPENAMEMAP);
		model.put("voucherList", voucherList);
		return "report/qry.vm";
	}
	@RequestMapping("/client/report/voucherLogList.xhtml")
	public String voucherLogList(Long voucherId, ModelMap model, HttpServletRequest request) {
		String hql = "from VoucherLog ";
		return null;
	}
}
