package com.iticket.model.ticket;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.iticket.constant.VoucherType;
import com.iticket.model.BaseObject;
import com.iticket.model.api.ClientMember;
import com.iticket.model.schedule.Schedule;
import com.iticket.model.schedule.ScheduleSeat;
import com.iticket.model.schedule.ScheduleStand;
import com.iticket.util.BeanUtil;
import com.iticket.util.DateUtil;
import com.iticket.util.TicketUtil;
/**
 * 凭证（对应一次出票）
 */
public class Voucher extends BaseObject {
	private static final long serialVersionUID = 1871849609148156875L;
	//Id
	private Long id;
	//版本
	private Integer version;
	//是否是站票
	private String stand;		
	//凭证编码
	private String voucherNo; 		
	//场馆ID
	private Long stadiumId; 		
	//场地ID
	private Long venueId; 			
	//项目ID
	private Long programId; 		
	//场次ID
	private Long scheduleId; 		
	//项目中文名称
	private String programCnName;
	//项目英文名称
	private String programEnName; 	
	//场次中文名称
	private String scheduleCnName;	
	//场次英文名称
	private String scheduleEnName;	
	//放映时间
	private Timestamp playTime;		
	//场地中文名称
	private String venueCnName;		
	//场地英文名称
	private String venueEnName;		
	//区域中文名称
	private String venueAreaCnName;	
	//区域英文名称
	private String venueAreaEnName;
	//座位数量
	private Integer seatNum; 		
	//座位金额
	private Double seatAmount;		
	//优惠金额
	private Double discount;		
	//是否显示价格
	private String showPrice;	
	//出票类型
	private String ticketType;	
	//凭证类型
	private String pzType;			
	//优惠类型
	private String disType;			
	//当预留变为售出的时候，记录对应的预留凭证	
	private Long reserveId;	
	//预留状态	
	private String reserveStatus;	
	//支付状态
	private String payStatus;
	//退款状态
	private String refundStatus;
	//释放时间
	private Timestamp releaseTime;
	//提醒时间
	private Timestamp tipTime;	
	//取票密码
	private String passwd;			
	//支付方式
	private String payMethod;
	//销售方式
	private Integer saleType;			
	//用户id
	private String memberName;	
	//用户名
	private Long memberId;
	//座位
	private String seatText;
	//添加时间
	private Timestamp addTime;
	public Voucher() {
	}
	public Voucher(ClientMember member, Schedule schedule, List<ScheduleSeat> seatList) {
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		this.version = 0;
		this.memberId = member.getId();
		this.voucherNo = TicketUtil.getVoucherNo(curtime);
		this.stadiumId = schedule.getStadiumId();
		this.venueId = schedule.getVenueId();
		this.programId = schedule.getProgramId();
		this.scheduleId = schedule.getId();
		this.seatNum = seatList.size();
		this.ticketType = VoucherType.CHUPIAO_CHUPIAO;
		this.pzType = VoucherType.PZTYPE_CP;
		this.addTime = curtime;
		this.scheduleCnName = schedule.getCnName();
		this.scheduleEnName = schedule.getEnName();
		this.playTime = schedule.getPlayTime();
		List<String> seatLabelList = BeanUtil.getBeanPropertyList(seatList, "seatLabel", false);
		this.seatText = StringUtils.join(seatLabelList, ",");
		this.stand = "N";
	}
	public Voucher(ClientMember member, List<ScheduleStand> standList, Schedule schedule) {
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		this.version = 0;
		this.memberId = member.getId();
		this.voucherNo = TicketUtil.getVoucherNo(curtime);;
		this.stadiumId = schedule.getStadiumId();
		this.venueId = schedule.getVenueId();
		this.programId = schedule.getProgramId();
		this.scheduleId = schedule.getId();
		this.seatNum = standList.size();
		this.ticketType = VoucherType.CHUPIAO_CHUPIAO;
		this.pzType = VoucherType.PZTYPE_CP;
		this.addTime = curtime;
		this.scheduleCnName = schedule.getCnName();
		this.scheduleEnName = schedule.getEnName();
		this.playTime = schedule.getPlayTime();
		this.stand = "Y";
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSeatAmount() {
		return seatAmount;
	}

	public void setSeatAmount(Double seatAmount) {
		this.seatAmount = seatAmount;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Integer getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public String createVoucherId(){
		return DateUtil.format(new Date(), "yyMMddHHmmss") + StringUtils.leftPad("" + new Random().nextInt(999), 3, '0');
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	@Override
	public Serializable realId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String gainTicketTypeText(){
		return VoucherType.TICKETTYPEMAP.get(ticketType);
	}
	public String gainTypeText(){
		return VoucherType.PZTYPENAMEMAP.get(pzType);
	}
	public String gainDisTypeText(){
		return VoucherType.DISTYPE_MAP.get(disType);
	}
	public String gainPayStatusText(){
		if(StringUtils.equals(payStatus, "Y")){
			return "已支付";
		}
		return "未支付";
	}
	public Timestamp getPlayTime() {
		return playTime;
	}
	public void setPlayTime(Timestamp playTime) {
		this.playTime = playTime;
	}
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
	public Timestamp getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	public Timestamp getTipTime() {
		return tipTime;
	}
	public void setTipTime(Timestamp tipTime) {
		this.tipTime = tipTime;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getReserveStatus() {
		return reserveStatus;
	}
	public void setReserveStatus(String reserveStatus) {
		this.reserveStatus = reserveStatus;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Long getStadiumId() {
		return stadiumId;
	}
	public void setStadiumId(Long stadiumId) {
		this.stadiumId = stadiumId;
	}
	public Long getVenueId() {
		return venueId;
	}
	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getPzType() {
		return pzType;
	}
	public void setPzType(String pzType) {
		this.pzType = pzType;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Long getReserveId() {
		return reserveId;
	}
	public void setReserveId(Long reserveId) {
		this.reserveId = reserveId;
	}
	//能够退款
	public boolean canRefund(){
		return StringUtils.equalsIgnoreCase(payStatus, "Y") && StringUtils.equalsIgnoreCase(refundStatus, "N");
	}
	//能够预留去付款
	public boolean canReserve2Sell(){
		return StringUtils.equalsIgnoreCase(pzType,VoucherType.PZTYPE_YL) 
				&& StringUtils.equalsIgnoreCase(payStatus, "N") && StringUtils.equalsIgnoreCase(reserveStatus, "Y");
	}
	//能够重复打印
	public boolean canReprint(){
		return StringUtils.equalsIgnoreCase(payStatus, "Y") && StringUtils.equalsIgnoreCase(refundStatus, "N");
	}
	public String getSeatText() {
		return seatText;
	}
	public void setSeatText(String seatText) {
		this.seatText = seatText;
	}
	public String getProgramEnName() {
		return programEnName;
	}
	public void setProgramEnName(String programEnName) {
		this.programEnName = programEnName;
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
	public String getVenueAreaEnName() {
		return venueAreaEnName;
	}
	public void setVenueAreaEnName(String venueAreaEnName) {
		this.venueAreaEnName = venueAreaEnName;
	}
	public String getProgramCnName() {
		return programCnName;
	}
	public void setProgramCnName(String programCnName) {
		this.programCnName = programCnName;
	}
	public String getScheduleCnName() {
		return scheduleCnName;
	}
	public void setScheduleCnName(String scheduleCnName) {
		this.scheduleCnName = scheduleCnName;
	}
	public String getVenueCnName() {
		return venueCnName;
	}
	public void setVenueCnName(String venueCnName) {
		this.venueCnName = venueCnName;
	}
	public String getVenueAreaCnName() {
		return venueAreaCnName;
	}
	public void setVenueAreaCnName(String venueAreaCnName) {
		this.venueAreaCnName = venueAreaCnName;
	}
	public String getStand() {
		return stand;
	}
	public void setStand(String stand) {
		this.stand = stand;
	}
	public boolean hasSeat(){
		return StringUtils.equalsIgnoreCase(stand, "N");
	}
}
