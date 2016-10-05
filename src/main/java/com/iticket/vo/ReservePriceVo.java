package com.iticket.vo;

import java.sql.Timestamp;
import java.util.List;

import com.iticket.constant.VoucherType;
import com.iticket.model.ticket.Voucher;
import com.iticket.model.ticket.VoucherDetail;

public class ReservePriceVo {
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
	//原价
	private Double totalPrice;	
	//总折扣
	private Double totalDiscount;	
	//折扣率
	private String discountRate;	
	//座位
	private String seatText;
	//数量
	private Integer quantity;
	public ReservePriceVo(){
		
	}
	public ReservePriceVo(Voucher voucher, String seatText, List<VoucherDetail> detailList){
		this.playTime = voucher.getPlayTime();
		this.programCnName = voucher.getProgramCnName();
		this.programEnName = voucher.getProgramEnName();
		this.scheduleCnName = voucher.getScheduleCnName();
		this.scheduleEnName = voucher.getScheduleEnName();
		this.venueAreaCnName = voucher.getVenueAreaCnName();
		this.venueAreaEnName = voucher.getVenueAreaEnName();
		this.venueCnName = voucher.getVenueCnName();
		this.venueEnName = voucher.getVenueEnName();
		Double sumPrice = 0D;
		for(VoucherDetail detail : detailList){
			sumPrice = sumPrice + detail.getTicketPrice();
		}
		this.totalPrice = sumPrice;
		this.totalDiscount = voucher.getDiscount();
		this.discountRate = VoucherType.DISTYPE_MAP.get(voucher.getDisType());
		this.seatText = seatText;
		this.quantity = detailList.size();
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
	public Timestamp getPlayTime() {
		return playTime;
	}
	public void setPlayTime(Timestamp playTime) {
		this.playTime = playTime;
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
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Double getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	public String getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
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
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
