package com.iticket.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("seat")
public class XSeat {
	@XStreamAlias("seatId")  
    @XStreamAsAttribute
	private Long id;
	private Long venueAreaId;
	private String lineno;
	private String rankno;
	private Integer x;
	private Integer y;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getVenueAreaId() {
		return venueAreaId;
	}

	public void setVenueAreaId(Long venueAreaId) {
		this.venueAreaId = venueAreaId;
	}
}