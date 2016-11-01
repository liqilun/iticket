package com.iticket.web.action.report;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
		query.setProjection(Projections.groupProperty("memberId"));
		String hql = "select new map(memberId as memberId, max(memberName) as memberName,count(*) as count, sum(seatNum) as seatNum, sum(seatAmount) as seatAmount, sum(discount) as discount) "
				+ "from Voucher where stadiumId=? and payStatus=? and refundStatus=? and addTime>=? and addTime<=? ";
		if(programId!=null){
			hql = hql + " and programId='" + programId + "' ";
		}
		hql= hql + " group by memberId";
		List<Map> resMapList = daoService.findByHql(hql, getStadiumId(request), "Y", "N", startTime, endTime);
		model.put("resMapList", resMapList);
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
