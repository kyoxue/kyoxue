package com.ilib.model;

import java.io.Serializable;
import java.util.Date;

import com.ilib.utils.DateTimeUtils;
/**
 * 接口响应基类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class ResponseMessage implements Serializable{
	private String code;
	private String errorInfo;
	private String successInfo;
	private String responseTime = DateTimeUtils.getDateTime(new Date());
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getSuccessInfo() {
		return successInfo;
	}
	public void setSuccessInfo(String successInfo) {
		this.successInfo = successInfo;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

}
