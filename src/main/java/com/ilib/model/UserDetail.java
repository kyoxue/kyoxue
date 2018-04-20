package com.ilib.model;

import java.util.Date;

public class UserDetail {
	private Long id;//这里和r_user主表的id区分，如果主表和从表联合查询详细信息的时候id用别名，比如AS tetailId赋值给result的column
	private String name;
	private String email;
	private String qq;
	private String telephone;
	private String mobile;
	private Integer age;
	private String sex;//1男 0女
	private String birthd;
	private String icon;//头像路径
	private String remark;
	private Date updatetime;//这里和r_user主表的updatetime区分，如果主表和从表联合查询详细信息的时候updatetime用别名，比如AS tetailUpdatetime赋值给result的column
	private Long modifier;//修改人ID 这里和r_user主表的modifier区分，如果主表和从表联合查询详细信息的时候modifier用别名，比如AS tetailModifier赋值给result的column
	private Long uid;//外键
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthd() {
		return birthd;
	}
	public void setBirthd(String birthd) {
		this.birthd = birthd;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
}
