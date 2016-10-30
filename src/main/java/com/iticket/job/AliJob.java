package com.iticket.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.iticket.Config;
import com.iticket.constant.VoucherType;
import com.iticket.model.api.ClientMember;
import com.iticket.model.ticket.Voucher;
import com.iticket.service.DaoService;
import com.iticket.service.IException;
import com.iticket.service.TicketService;
import com.iticket.util.DateUtil;
import com.iticket.util.GewaLogger;
import com.iticket.util.LoggerUtils;

//石英
@Service
public class AliJob {
	private final transient GewaLogger dbLogger = LoggerUtils.getLogger(getClass(), Config.getServerIp(), Config.SYSTEMID);
	@Autowired@Qualifier("daoService")
	private DaoService daoService;
	@Autowired@Qualifier("ticketService")
	private TicketService ticketService;
	public void cancelReverse(){
		//dbLogger.warn("cancelReverse task start");
		String hql = "from Voucher where pzType=? and payStatus=? and reserveStatus=? and releaseTime>?";
		List<Voucher> voucherList = daoService.findByHql(hql, VoucherType.PZTYPE_YL, "N", "Y", DateUtil.getCurFullTimestamp());
		ClientMember member = daoService.getObject(ClientMember.class, 0L);
		for(Voucher voucher : voucherList){
			try {
				ticketService.reserveCancel(member, voucher.getId());
				dbLogger.warn(voucher.getId() + ":OK");
			} catch (IException e) {
				dbLogger.warn(voucher.getId() + ":" + e.getMsg());
			}
		}
		//dbLogger.warn("cancelReverse task end");
	}
	
}
