package com.iticket.vo;

import org.apache.commons.lang.StringUtils;

import com.iticket.Config;
import com.iticket.constant.PaymentType;
import com.iticket.constant.VoucherType;
import com.iticket.model.program.Program;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.SchedulePrice;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.model.schedule.ScheduleVenueArea;
import com.iticket.model.stadium.Stadium;
import com.iticket.model.stadium.Venue;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherCustomer;
import com.iticket.util.DateUtil;

/**
 * 打票信息
 *
 */
public class TicketVo {

	private Long seatid;				//座位ID
	private String stand;				//是否为站票
	private String lineno;				//排
	private String rankno;				//座
	private Integer x;					// 座位坐标X
	private Integer y;					// 座位坐标Y
	private String oneCode;				//一维码（10位）
	private String twoCode;				//二维码（10位）
	private String tempPrientInfo;		//临时打印信息
	private String priceDescription;	//价格描述
	private String priceRemark;			//价格备注
	private String voucherNo;			//凭证编号
	private Long voucherId;				//凭证ID
	private String ticketType;			//出票类型
	private String areaCnName;			//区域中文名
	private String areaEnName;			//区域中文名
	private String areaDescription;		//区域描述
	private String areaIcon;			//区域图标
	private String playDate;			//场次日期
	private String playTime;			//场次时间
	private String scheduleCnName;		//场次中文名
	private String scheduleEnName;		//场次中文名
	private String venueCnName;	 		//场地名称
	private String venueEnName;	 		//场地名称
	private Long stadiumId;				//场馆ID
	private String stadiumCnName; 		//场馆名称
	private String stadiumEnName; 		//场馆名称
	private Long programId;				//项目ID
	private String programCnName;		//项目中文名
	private String programEnName;		//项目中文名
	private String programStartTime;	//项目开始日期
	private String programEndTime;	//项目开始日期
	private Double ticketPrice;			//票价
	private String showPrice;			//票价是否显示
	private String serialNum;			//序号
	private String cnAddress;			//中文地址
	private String memberName;
	private Long scheduleVenueAreaId;	//场次区域ID
	private Long scheduleId;			//场次ID
	private String scheduleCode;		//场次编号
	
	private String packInfo;			//套票信息
	private String packIntro;			//套票说明
	private String packInfo2;			//套票信息2

	private String greetings;			//个性化祝福语
	
	private String customerName;		//客户姓名
	private String userAbbrev;			//用户简称
	private String payMethod;			//支付方式
	private String numUuid;				//数字二维码
	private String idnumber;			//身份证号
	private Long ticketPriceId;			//价格id
	
	private String programImgURL;
	private String fixed;
 	
	public TicketVo(String areaCnName, ScheduleSeat seat){
		this.areaCnName = areaCnName;
		this.lineno = seat.getLineno();	
		this.rankno = seat.getRankno();
	}
     
	public TicketVo(Stadium stadium, Venue venue, ScheduleVenueArea area, Program program, Schedule schedule, SchedulePrice schedulePrice,
			ScheduleSeat seat, Voucher voucher, VoucherCustomer vCustomer){
		this.seatid = seat.getId();
		this.x = seat.getX();
		this.y = seat.getY();
		this.serialNum = seat.getSerialNum()+"";
		this.lineno = seat.getLineno();	
		this.rankno = seat.getRankno();
		this.oneCode = seat.getBarcode();
		this.twoCode = seat.getUuid();
		
		this.stadiumId = stadium.getId();
		this.stadiumCnName = stadium.getCnName();
		this.stadiumEnName = stadium.getEnName();
		this.venueCnName = venue.getCnName();
		this.venueEnName = venue.getEnName();
		this.voucherNo = voucher.getVoucherNo();
		
		this.voucherId = voucher.getId();
		this.stand = voucher.getStand();
		this.ticketType = VoucherType.TICKETTYPEMAP.get(voucher.getTicketType());
		this.areaCnName = area.getCnName();
		this.areaEnName = area.getEnName();
		this.areaDescription = area.getDescription();
		if(StringUtils.isNotBlank(area.getIcon())){
			this.areaIcon = Config.IMAGE_PATH + area.getIcon();
		}
		this.playDate = DateUtil.formatDate(schedule.getPlayTime());
		this.playTime = DateUtil.format(schedule.getPlayTime(),"HH:mm");
		this.scheduleCnName = schedule.getCnName();
		this.scheduleEnName = schedule.getEnName();
		this.programId = program.getId();
		this.programCnName = program.getCnName();
		this.programEnName = program.getEnName();
		this.programStartTime = DateUtil.format(program.getStartTime(),"yyyy-MM-dd HH:mm");
		this.priceDescription = schedulePrice.getDescription();
		this.ticketPrice = schedulePrice.getPrice();
		this.ticketPriceId = schedulePrice.getId();
		this.cnAddress = stadium.getCnAddress();
		this.payMethod = PaymentType.PAYMETHODMAP.get(voucher.getPayMethod());
		
		this.scheduleId = schedule.getId();
		this.scheduleCode = schedule.getCode();
		this.scheduleVenueAreaId = area.getId();
		this.userAbbrev = voucher.getMemberName();
		this.numUuid = seat.getNumUuid();
		if(vCustomer!=null){
			this.customerName = vCustomer.getCustomerName();
			if(vCustomer.getIdnumber()!=null && vCustomer.getIdnumber().length()>=6){
				this.idnumber = vCustomer.getIdnumber().substring(vCustomer.getIdnumber().length()-6);	
			}
		}
		if(StringUtils.isNotBlank(program.getImgurl())){
			this.programImgURL = Config.IMAGE_PATH + program.getImgurl();
		}
		this.programEndTime=DateUtil.format(program.getEndTime(),"yyyy-MM-dd HH:mm");
		
		this.fixed=schedule.getFixed();
	}
	public TicketVo(Stadium stadium, Venue venue, ScheduleVenueArea area, Program program, Schedule schedule, SchedulePrice schedulePrice,
			ScheduleStand stand, Voucher voucher, VoucherCustomer vCustomer){
		
		this.seatid = stand.getId();
		this.serialNum = stand.getSerialNum()+"";
		this.oneCode = stand.getBarcode();
		this.twoCode = stand.getUuid();
		
		this.stadiumId = stadium.getId();
		this.stadiumCnName = stadium.getCnName();
		this.stadiumEnName = stadium.getEnName();
		this.venueCnName = venue.getCnName();
		this.venueEnName = venue.getEnName();
		this.voucherNo = voucher.getVoucherNo();
		this.voucherId = voucher.getId();
		this.ticketType = VoucherType.TICKETTYPEMAP.get(voucher.getTicketType());
		this.areaCnName = area.getCnName();
		this.areaEnName = area.getEnName();
		this.areaDescription = area.getDescription();
		if(StringUtils.isNotBlank(area.getIcon())){
			this.areaIcon = Config.IMAGE_PATH + area.getIcon();
		}
		this.playDate = DateUtil.formatDate(schedule.getPlayTime());
		this.playTime = DateUtil.format(schedule.getPlayTime(),"HH:mm");
		this.scheduleCnName = schedule.getCnName();
		this.scheduleEnName = schedule.getEnName();
		this.programId = program.getId();
		this.programCnName = program.getCnName();
		this.programEnName = program.getEnName();
		this.programStartTime = DateUtil.format(program.getStartTime(),"yyyy-MM-dd HH:mm");
		this.priceDescription = schedulePrice.getDescription();
		this.ticketPrice = schedulePrice.getPrice();
		this.ticketPriceId = schedulePrice.getId();
		this.cnAddress = stadium.getCnAddress();
		this.payMethod = PaymentType.PAYMETHODMAP.get(voucher.getPayMethod());
		
		this.scheduleId = schedule.getId();
		this.scheduleCode = schedule.getCode();
		this.scheduleVenueAreaId = area.getId();
		this.userAbbrev = voucher.getMemberName();
		this.numUuid = stand.getNumUuid();
		if(vCustomer!=null){
			this.customerName = vCustomer.getCustomerName();
			if(vCustomer.getIdnumber()!=null && vCustomer.getIdnumber().length()>=6){
				this.idnumber = vCustomer.getIdnumber().substring(vCustomer.getIdnumber().length()-6);	
			}
		}
		if(StringUtils.isNotBlank(program.getImgurl())){
			this.programImgURL = Config.IMAGE_PATH + program.getImgurl();
		}
		this.programEndTime=DateUtil.format(program.getEndTime(),"yyyy-MM-dd HH:mm");
		
		this.fixed=schedule.getFixed();
	}
	public String getVenueCnName() {
		return venueCnName;
	}

	public void setVenueCnName(String venueCnName) {
		this.venueCnName = venueCnName;
	}

	public Long getStadiumId() {
		return stadiumId;
	}

	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}

	public String getStadiumCnName() {
		return stadiumCnName;
	}

	public void setStadiumCnName(String stadiumCnName) {
		this.stadiumCnName = stadiumCnName;
	}

	public String getShowPrice() {
		return showPrice;
	}

	public void setShowPrice(String showPrice) {
		this.showPrice = showPrice;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public TicketVo(){}
	
	public Long getSeatid() {
		return seatid;
	}

	public void setSeatid(Long seatid) {
		this.seatid = seatid;
	}

	public String getOneCode() {
		return oneCode;
	}
	public void setOneCode(String oneCode) {
		this.oneCode = oneCode;
	}
	public String getTwoCode() {
		return twoCode;
	}
	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}
	public String getTempPrientInfo() {
		return tempPrientInfo;
	}
	public void setTempPrientInfo(String tempPrientInfo) {
		this.tempPrientInfo = tempPrientInfo;
	}
	public String getPriceDescription() {
		return priceDescription;
	}
	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}
	public Long getVoucherId() {
		return voucherId;
	}

	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getAreaCnName() {
		return areaCnName;
	}
	public void setAreaCnName(String areaCnName) {
		this.areaCnName = areaCnName;
	}
	public String getAreaDescription() {
		return areaDescription;
	}
	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}
	public String getAreaIcon() {
		return areaIcon;
	}
	public void setAreaIcon(String areaIcon) {
		this.areaIcon = areaIcon;
	}
	public String getPlayDate() {
		return playDate;
	}
	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}
	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	public String getScheduleCnName() {
		return scheduleCnName;
	}
	public void setScheduleCnName(String scheduleCnName) {
		this.scheduleCnName = scheduleCnName;
	}
	public String getLineno() {
		return lineno;
	}
	public void setLineno(String lineno) {
		this.lineno = lineno;
	}
	public String getRankno() {
		return rankno;
	}
	public void setRankno(String rankno) {
		this.rankno = rankno;
	}
	
	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getProgramCnName() {
		return programCnName;
	}
	public void setProgramCnName(String programCnName) {
		this.programCnName = programCnName;
	}
	public Double getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public String getProgramStartTime() {
		return programStartTime;
	}
	public void setProgramStartTime(String programStartTime) {
		this.programStartTime = programStartTime;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getCnAddress() {
		return cnAddress;
	}

	public void setCnAddress(String cnAddress) {
		this.cnAddress = cnAddress;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "\tTicketVo [seatid=" + seatid + ", serialNum=" + serialNum
				+ ", y=" + y + ", x=" + x + ",lineno=" + lineno + ", rankno=" + rankno + "]\n";
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(String packInfo) {
		this.packInfo = packInfo;
	}

	public String getPriceRemark() {
		return priceRemark;
	}

	public void setPriceRemark(String priceRemark) {
		this.priceRemark = priceRemark;
	}

	public String getPackInfo2() {
		return packInfo2;
	}

	public void setPackInfo2(String packInfo2) {
		this.packInfo2 = packInfo2;
	}

	public String getPackIntro() {
		return packIntro;
	}

	public void setPackIntro(String packIntro) {
		this.packIntro = packIntro;
	}

	public String getGreetings() {
		return greetings;
	}

	public void setGreetings(String greetings) {
		this.greetings = greetings;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUserAbbrev() {
		return userAbbrev;
	}

	public void setUserAbbrev(String userAbbrev) {
		this.userAbbrev = userAbbrev;
	}

	public String getScheduleCode() {
		return scheduleCode;
	}

	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getNumUuid() {
		return numUuid;
	}

	public void setNumUuid(String numUuid) {
		this.numUuid = numUuid;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public Long getTicketPriceId() {
		return ticketPriceId;
	}

	public void setTicketPriceId(Long ticketPriceId) {
		this.ticketPriceId = ticketPriceId;
	}

	public String getProgramImgURL() {
		return programImgURL;
	}

	public void setProgramImgURL(String programImgURL) {
		this.programImgURL = programImgURL;
	}

	public String getProgramEndTime() {
		return programEndTime;
	}

	public void setProgramEndTime(String programEndTime) {
		this.programEndTime = programEndTime;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public String getTicketTypeText(){
		return VoucherType.TICKETTYPEMAP.get(ticketType);
	}

	public String getAreaEnName() {
		return areaEnName;
	}

	public void setAreaEnName(String areaEnName) {
		this.areaEnName = areaEnName;
	}

	public String getScheduleEnName() {
		return scheduleEnName;
	}

	public void setScheduleEnName(String scheduleEnName) {
		this.scheduleEnName = scheduleEnName;
	}

	public String getVenueEnName() {
		return venueEnName;
	}

	public void setVenueEnName(String venueEnName) {
		this.venueEnName = venueEnName;
	}

	public String getStadiumEnName() {
		return stadiumEnName;
	}

	public void setStadiumEnName(String stadiumEnName) {
		this.stadiumEnName = stadiumEnName;
	}

	public String getProgramEnName() {
		return programEnName;
	}

	public void setProgramEnName(String programEnName) {
		this.programEnName = programEnName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Long getScheduleVenueAreaId() {
		return scheduleVenueAreaId;
	}

	public void setScheduleVenueAreaId(Long scheduleVenueAreaId) {
		this.scheduleVenueAreaId = scheduleVenueAreaId;
	}

	public String getStand() {
		return stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}
}
