package com.ilib.common.enums;

import com.ilib.common.Constant;

public enum EnumEnable {
	ENABLE_Y(Constant.Y,"有效"),ENABLE_N(Constant.N,"禁用");
	private String val;
	private String desc;
	private EnumEnable(String val,String desc){
		this.val = val;
		this.desc = desc;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
