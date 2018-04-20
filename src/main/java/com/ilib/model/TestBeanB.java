package com.ilib.model;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestBeanB {
	private Integer uid;
	private String account;
	private String password;
	private Date createTime;
	private Integer createBy;
	private String enable;
	private String mainRole;//是否权限负责人
	private String type;//权限所属类别
	@Override
	public String toString() {
		String pattern = "账户：{0}，权限负责人：{1}，创建时间：{2}；";
		String createDateTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
		String string = MessageFormat.format(pattern, account,mainRole,createDateTime);
		return string;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getMainRole() {
		return mainRole;
	}
	public void setMainRole(String mainRole) {
		this.mainRole = mainRole;
	}
}
