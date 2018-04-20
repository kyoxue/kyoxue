package com.ilib.maintest;

import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ilib.common.Constant;
import com.ilib.utils.DateTimeUtils;
import com.ilib.utils.HttpUtil;
import com.ilib.utils.MD5;
import com.ilib.utils.SecurityUtil;


public class Test {
	static Logger log = LoggerFactory.getLogger(Test.class); 
	/**
	 * ------------------------------------------------------------------------------
	 * 测试请求REST平台中心，根据requestType自动转发到对应的单元方法进行处理返回信息。<br>
	 * @see com.ilib.service.rest.RsCenterService<br>
	 * <h4>使用方法</h4>
	 * requestType即单元处理类中方法上面的UnitMethod注解定义的名字<br>
	 * 通过请求与注解对应到方法自动执行<br>
	 * 请求和响应都是json格式数据，具体看这里的参考请求代码<br>
	 * 注意几个参数：<br>
	 * <ul>
	 * <li>key：请求方和接收方协议的key，md5加密</li>
	 * <li>agentId：REST平台发出的客户Id，用来请求方区分是哪个客户的接口。</li>
	 * <li>data：必须加密传输，先URL的UTF-8编码，然后DES的UTF-8加密</li>
	 * <li>timestamp：当前时间的毫秒millis，注意如果那之前请求过的时间去请求，如果时间超过了当前时间1分钟，则平台中心视为过期的请求。</li>
	 * <li>nonce：timestamp+RandomUtils.nextLong(1, 1000);其主要作用是，防止重放攻击。如果在1分钟内，平台中心存在一样的nonce，则会视为恶意攻击。</li>
	 * </ul>
	 * 最后，请求的时候注意：返回的数据是加密的，加密格式个请求是一致的，需要先des的UTF-8解密，然后URL的UTF-8解码。<br>
	 * 接口返回成功，code是SUCCESS，其余错误的另行解析。
	 * ------------------------------------------------------------------------------
	 * @throws Exception
	 */
	public static void testRequestRestCenter()throws Exception{
		String centerurl = "http://localhost:8080/ilib/service/plat/center";
		String requestType = "query";
		String agentId = "ilib0001";
		String key = "6cab3e901c1e5f0d6f0b528e69776413";
		JSONObject dataobj = new JSONObject();
		dataobj.put("notice_min_delay", "notice_min_delay");
		String data = SecurityUtil.urlEncodeECB(key, dataobj.toJSONString());
		Long timestamp = DateTimeUtils.getMillis(new Date());
		Long nonce = timestamp+RandomUtils.nextLong(1, 1000);//可以拿一串固定值 来测试频繁请求1512243727944l
		StringBuilder builder = new StringBuilder();
		builder.append(requestType).append(agentId).append(data).append(timestamp.toString()).append(nonce.toString()).append(key);
		String sign =  MD5.encode(builder.toString(), Constant.ENCODING_UTF_8);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("requestType", requestType);
		jsonObject.put("agentId", agentId);
		jsonObject.put("data", data);
		jsonObject.put("sign", sign);
		jsonObject.put("timestamp", timestamp);
		jsonObject.put("nonce", nonce);
		String reponse = HttpUtil.post_http_connect(centerurl, jsonObject.toJSONString(), 9000, 9000, Constant.ENCODING_UTF_8);
		JSONObject obj = JSON.parseObject(reponse);
		String code = obj.getString("responseCode");
		String responseMessage = obj.getString("responseMessage");
		if (!code.equals(Constant.KEY_SUCCESS)) {
			System.err.println(code+"   "+responseMessage);
		}else{
			System.out.println(responseMessage);
			responseMessage = SecurityUtil.urlDecodeECB(key, responseMessage);
			System.out.println("解密后："+responseMessage);
		}
	}
	public static void main(String[] args) throws Exception{
		testRequestRestCenter();
	}

}
