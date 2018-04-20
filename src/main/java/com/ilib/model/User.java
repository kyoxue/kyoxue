package com.ilib.model;

import java.util.Date;

public class User {
	private Long id;
	private String username;
	private String pwd;
	private Integer departmentid;//用户所在组的ID
	private String enable;//是否有效(Y/N)
	private Date endtime;//失效时间，与enable结合使用，N的时候设置时间
	private Date updatetime;
	private Date createtime;
	private String creater;
	private Long modifier;
	private String isblacklist;//是否黑名单，N是白名单
	private Date lastupdatepwdtime;//上一次修改密码时间
	private UserDetail userDetail;
	public UserDetail getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public String getIsblacklist() {
		return isblacklist;
	}
	public void setIsblacklist(String isblacklist) {
		this.isblacklist = isblacklist;
	}
	public Date getLastupdatepwdtime() {
		return lastupdatepwdtime;
	}
	public void setLastupdatepwdtime(Date lastupdatepwdtime) {
		this.lastupdatepwdtime = lastupdatepwdtime;
	}
}
