package com.ilib.model;

import java.io.Serializable;
import java.util.Date;

import javax.ws.rs.core.Response;

import com.alibaba.fastjson.JSON;
import com.ilib.utils.DateTimeUtils;
/**
 *	接口对接响应样例
 */
public class RsReponse implements Serializable {

	private String responseType;//与请求类型一致
	private String agentId;//与请求类型一致
	private String responseCode;//响应码
	private String responseMessage;//响应内容
	private final String responseTime = DateTimeUtils.getDateTime(new Date());//响应时间
	public RsReponse(String responseType,String agentId,String responseCode,String responseMessage){
		this.responseType = responseType;
		this.agentId = agentId;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	/**
	 * 响应JSON给请求方
	 */
	public Response build(String dateFormat){
		return Response.ok(JSON.toJSONStringWithDateFormat(this,dateFormat)).build();
	}
	public Response build(){
		return Response.ok(JSON.toJSONString(this)).build();
	}
	public Response build(boolean fullDateFormat){
		if (fullDateFormat) {
			return Response.ok(JSON.toJSONStringWithDateFormat(this,"yyyy-MM-dd HH:mm:ss")).build();
		}else{
			return Response.ok(JSON.toJSONString(this)).build();
		}
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseTime() {
		return responseTime;
	}

}
