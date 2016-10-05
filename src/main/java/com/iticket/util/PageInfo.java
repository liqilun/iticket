package com.iticket.util;

public class PageInfo {
	private String url;
	private String pageNo;
	private boolean prePage;
	private boolean currentPage;
	private boolean nextPage;
	private int realPageNo;

	public boolean isCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(boolean currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isPrePage() {
		return prePage;
	}

	public void setPrePage(boolean prePage) {
		this.prePage = prePage;
	}

	public boolean isNextPage() {
		return nextPage;
	}

	public void setNextPage(boolean nextPage) {
		this.nextPage = nextPage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public int getRealPageNo() {
		return realPageNo;
	}

	public void setRealPageNo(int realPageNo) {
		this.realPageNo = realPageNo;
	}

}
