package com.ilib.service.rest.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ilib.common.Constant;
import com.ilib.model.RsReponse;
import com.ilib.model.RsRequest;
import com.ilib.other.utils.RestAutoMethodUtil;
import com.ilib.service.redis.RedisLinesCacheService;
import com.ilib.service.rest.RsCenterService;
import com.ilib.utils.DateTimeUtils;
import com.ilib.utils.MD5;
import com.ilib.utils.SecurityUtil;
import com.ilib.utils.StringUtil;
@Service
public class RsCenterServiceImpl implements RsCenterService, Serializable {
	private static final long serialVersionUID = 8214628017449625385L;
	public static final Logger log = LoggerFactory.getLogger(RsCenterServiceImpl.class);
	@Autowired
	private RedisLinesCacheService<Long> redis;//存放随机验证码，验证重放
	@Autowired
	private RestAutoMethodUtil restAutoMethodUtil;//自动执行单元处理工具
	@Override
	public Response requestCenter(RsRequest request,HttpServletRequest req) {
		log.info(LOG_PRE);
		String error = ResponseCode.ERROR.toString(); 
		if (null == request) {
			log.info(LOG_PRE.concat("未正常获取到请求的JSON信息，接口异常！"));
			return new RsReponse("","", error, "接口异常！").build(true);
		}
		String agentId = StringUtil.get().getVal(request.getAgentId());//接口方提供平台ID
		String requestType =StringUtil.get().getVal(request.getRequestType());//请求类型
		String sign = StringUtil.get().getVal(request.getSign());//签名
		Long timestamp = request.getTimestamp();//请求时间
		Long nonce = request.getNonce();//请求随机码
		String data = StringUtil.get().getVal(request.getData());//请求加密后的数据
		if (agentId.equals("" )|| !agentId.equals(AGENT_ID)) {
			log.info(LOG_PRE.concat("agentId提供错误"));
			return new RsReponse("","", ResponseCode.AGENT_ID_ERROR.toString(), "agentId提供错误！").build(true);
		}
		if (requestType.equals("") || sign.equals("") || timestamp == null || nonce == null || data.equals("")) {
			log.info(LOG_PRE.concat("请求参数提供不完整！"));
			return new RsReponse("","", ResponseCode.PARAMS_ERROR.toString(), "请求参数提供不完整！").build(true);
		}
		//验签：MD5(requestType+agentId+data+timestamp+nonce+key)
		//请求的时候data要经过URL编码再DES加密
		//agentId和Key在接口中有定义，后期维护到数据库
		StringBuilder builder = new StringBuilder();
		builder.append(requestType).append(agentId).append(data).append(timestamp.toString()).append(nonce.toString()).append(KEY);
		String validateSign = "";
		try {
			validateSign = MD5.encode(builder.toString(), Constant.ENCODING_UTF_8);
		} catch (Exception e) {
			log.info(LOG_PRE.concat("MD5加密验签时发生异常！"));
			return new RsReponse(requestType,agentId, error, "接口异常！").build(true);
		}
		if (!sign.equals(validateSign)) {
			log.info(LOG_PRE.concat("签名不通过！请求签名："+sign+"，当前计算签名："+validateSign));
			return new RsReponse(requestType,agentId, ResponseCode.SIGN_ERROR.toString(), "签名错误！").build(true);
		}
		String requestTime = DateTimeUtils.getDateTimeFromMill(timestamp);
		String nowTime = DateTimeUtils.getDateTime(new Date());
		int minute = DateTimeUtils.diffCurrentDate(requestTime);
		if (minute > NONCE_TIMEOUT.intValue()) {
			log.info(LOG_PRE.concat("短时间内重复请求，请求的时间："+requestTime+"，当前时间："+nowTime+"，超过了"+minute+"分钟！"));
			return new RsReponse(requestType,agentId, ResponseCode.MANY_TIME_REQUEST_ERROR.toString(), "您的请求过时，请不要短时间内多次请求！").build(true);
		}
		log.info(LOG_PRE+"开始验证是否重放请求..");
		try {
			String requestip = req.getLocalAddr();
			String checkKey = NONCE_KEY+requestip.replaceAll("\\.", "");
			log.info(LOG_PRE+"重放验证KEY："+checkKey);
			Object nonceObj = redis.getCacheObject(checkKey);
			if (null == nonceObj) {
				log.info(LOG_PRE+"KEY："+checkKey+"，暂无重放请求历史..");
				redis.setCacheObject(checkKey, nonce, NONCE_TIMEOUT);//1分钟内不能带着相同的随机码重复请求
			}else{
				Long longval  = Long.valueOf(nonceObj.toString());
				if (longval==nonce.longValue()) {
					log.info(LOG_PRE.concat("发现重放请求！用户IP："+requestip+"，重放的随机码："+nonce));
					return new RsReponse(requestType,agentId, ResponseCode.SAME_REQUEST_ERROR.toString(), "请求过于频繁！").build(true);
				}
			}
		} catch (Exception e) {
			log.error("验证重放攻击异常！",e);
		}
		String decodeData = "";
		try {
			decodeData = SecurityUtil.urlDecodeECB(KEY, data);
		} catch (Exception e) {
			log.info(LOG_PRE.concat("解密业务数据时发生异常！"));
			return new RsReponse(requestType,agentId, error, "接口异常！").build(true);
		}
		if ("".equals(decodeData)) {
			log.info(LOG_PRE.concat("未获取到解密后的业务数据！"));
			return new RsReponse(requestType,agentId, error, "未获取到业务数据！").build(true);
		}
		Map<Integer, String> map = restAutoMethodUtil.autoMethod(UNIT_PACKAGE, requestType, decodeData);//根据请求类型，将数据传到到单元处理
		Entry<Integer, String> entry = map.entrySet().iterator().next();
		Integer key = entry.getKey();
		String value = entry.getValue();
		if (key.intValue() == -1) {//执行系统异常
			log.error(value);
			return new RsReponse(requestType,agentId, error, "接口异常！").build(true);
		}else if(key.intValue() == 0){//请求类型没找到
			return new RsReponse(requestType,agentId,ResponseCode.PARAMS_ERROR.toString(), value).build(true);
		}else{//执行成功
			JSONObject valueobj = JSON.parseObject(value);
			String code = valueobj.getString("code");
			if (Constant.KEY_ERROR.equals(code)) {
				log.info(LOG_PRE.concat(valueobj.getString("errorInfo")));
				return new RsReponse(requestType,agentId, error, "接口异常！").build(true);
			}else{
				String successValue = valueobj.getString("successInfo");
				try {
					successValue =SecurityUtil.urlEncodeECB(KEY, successValue);//加密返回信息
				} catch (Exception e) {
					log.info(LOG_PRE.concat("加密返回信息异常！"));
					return new RsReponse(requestType,agentId, error, "接口异常！").build(true);
				}
				return new RsReponse(requestType,agentId, ResponseCode.SUCCESS.toString(), successValue).build(true);
			}
			
		}
	}

}
