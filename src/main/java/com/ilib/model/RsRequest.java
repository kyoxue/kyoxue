package com.ilib.model;

import java.io.Serializable;
/**
 *	接口对接请求样例
 */
public class RsRequest implements Serializable{

	private String requestType;//请求类型
	private String agentId;//提供给平台的ID
	private String data;//加密的业务数据
	private String sign;//签名
	private Long timestamp;//请求时间
	private Long nonce;//加密随机串
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getNonce() {
		return nonce;
	}
	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

}
