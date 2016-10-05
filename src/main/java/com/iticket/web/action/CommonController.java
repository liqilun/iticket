package com.iticket.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.model.api.ApiUser;
import com.iticket.model.layout.Layout;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Seat;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.stadium.VenueArea;
import com.iticket.model.ticket.Customer;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.model.ticket.VoucherDetail;

@Controller
public class CommonController extends BaseController{
	@RequestMapping("/login.xhtml")
	public String login(ModelMap model, HttpServletRequest request) {
		return "login.vm";
	}
	@RequestMapping("/testSchedule.xhtml")
	public String testSchedule(ModelMap model, HttpServletRequest request) {
		List<String> msgList = new ArrayList<String>();
		daoService.getObject(ApiUser.class, 1L);
		daoService.getObject(Layout.class, 1L);
		daoService.getObject(Program.class, 1L);
		daoService.getObject(Schedule.class, 1L);
		daoService.getObject(ScheduleSeat.class, 1L);
		daoService.getObject(ScheduleVenueArea.class, 1L);
		daoService.getObject(Seat.class, 1L);
		daoService.getObject(Stadium.class, 1L);
		daoService.getObject(Venue.class, 1L);
		daoService.getObject(VenueArea.class, 1L);
		daoService.getObject(Customer.class, 1L);
		daoService.getObject(Voucher.class, 1L);
		daoService.getObject(VoucherCustomer.class, 1L);
		daoService.getObject(VoucherDetail.class, 1L);
		return formardMessage(msgList, model);
	}
}
