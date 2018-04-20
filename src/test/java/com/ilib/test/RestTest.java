package com.ilib.test;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.ilib.common.Constant;
import com.ilib.model.RsReponse;
import com.ilib.testcommon.BaseTest;
import com.ilib.utils.DateTimeUtils;
import com.ilib.utils.HttpUtil;
import com.ilib.utils.Ilib;
import com.ilib.utils.MD5;
import com.ilib.utils.SecurityUtil;

public class RestTest extends BaseTest {
	
	@Test
	public void testRestCenter()throws Exception{
		String centerurl = "http://localhost:8080/ilib/service/plat/center";
		String requestType = "query";
		String agentId = "ilib0001";
		String key = "6cab3e901c1e5f0d6f0b528e69776413";
		String data = "{\"question\":\"我是一只小小鸟？\",\"really\":1}";
		data = SecurityUtil.urlEncodeECB(key, data);
		Long timestamp = DateTimeUtils.getMillis(new Date());
		Long nonce = timestamp+RandomUtils.nextLong(1, 1000);
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
		System.out.println(reponse);
	}

}
