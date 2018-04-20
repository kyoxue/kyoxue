package com.ilib.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ilib.model.RsRequest;
/**
 * REST接口对接中心<BR>
 * 所有的请求，会请求到这里进行校验和过滤，然后会根据请求类型requestType将请求内容自动转发处理单元执行。
 * 接口文档定义：接口提供给各个平台<BR>
 * 请求地址：/plat/xx plat：平台 xx：对应的请求方平台<BR>
 * 请求方式：POST<BR>
 * 所有的请求和响应都用json格式数据传递<BR>
 */
@Path("/plat")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RsCenterService {
	 static final String AGENT_ID="ilib0001";//样例ID提供给平台，区分客户，请求的时候传过来验证，是否是接口方。
	 final static String KEY = "6cab3e901c1e5f0d6f0b528e69776413";//加密解密用的key
	 final static String LOG_PRE = "接口请求开始 ---- >";
	 final static String NONCE_KEY = "nonce";
	 final static Long NONCE_TIMEOUT = 1L;//建议重放攻击的失效范围 与 请求时间检验一致
	 final static String UNIT_PACKAGE ="com.ilib.service.rest.unit";//自动执行单元所在的包
	public enum ResponseCode{SUCCESS,ERROR,NONE,AGENT_ID_ERROR,UNVALID_IP,PARAMS_ERROR,SIGN_ERROR,MANY_TIME_REQUEST_ERROR,SAME_REQUEST_ERROR}; 
	/**
	 * 所有请求都从这里进来<BR>
	 * 验证签名和重放请求<BR>
	 * 然后根据请求类型进行转向处理<BR>
	 * 后续对这个接口方法进行拦截统计时间和日志。
	 */
	@POST
	@Path("/center")
	public Response requestCenter(final RsRequest request,@Context HttpServletRequest req); 
}
