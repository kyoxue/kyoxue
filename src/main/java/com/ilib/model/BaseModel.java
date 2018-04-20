package com.ilib.model;

import java.io.Serializable;
import java.util.Date;

import com.ilib.utils.DateTimeUtils;

@SuppressWarnings("serial")
public class BaseModel implements Serializable {

	private Long creater;
	private Long modifer;
	private Date createtime;
	private Date updatetime;
	private String createtimestr;
	private String updatetimestr;
	private String enable;
	private String createrName;
	private String modiferName;
	public String getCreatetimestr() {
		if (null!=createtime) {
			return DateTimeUtils.getDateTime(createtime);
		}
		return createtimestr;
	}
	public void setCreatetimestr(String createtimestr) {
		this.createtimestr = createtimestr;
	}
	public String getUpdatetimestr() {
		if (null!=updatetime) {
			return DateTimeUtils.getDateTime(updatetime);
		}
		return updatetimestr;
	}
	public void setUpdatetimestr(String updatetimestr) {
		this.updatetimestr = updatetimestr;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getModiferName() {
		return modiferName;
	}
	public void setModiferName(String modiferName) {
		this.modiferName = modiferName;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Long getModifer() {
		return modifer;
	}
	public void setModifer(Long modifer) {
		this.modifer = modifer;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}
