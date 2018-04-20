package com.ilib.common;

import com.ilib.utils.StringUtil;

public class Pagenation {
	private String pageParam;
	private int page;
	private int pageSize;
	private int total;
	public Pagenation(String pageParam,int pageSize,int total){
		this.pageParam = pageParam;
		this.pageSize = pageSize;
		this.total = total;
	}
	public String getPageParam() {
		return pageParam;
	}
	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}
	public int getPage() {
		if (StringUtil.get().isNull(pageParam)) {
			setPage(0);
		}else{
			setPage(Integer.parseInt(pageParam)-1);
		}
		return page*pageSize;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalPage() {
		return (total%pageSize == 0 ? (total/pageSize):((total/pageSize)+1));
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

}
