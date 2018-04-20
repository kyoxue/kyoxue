package com.ilib.service.rest.unit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ilib.common.Constant;
import com.ilib.common.annotation.UnitMethod;
import com.ilib.dao.ConfigDao;
import com.ilib.model.ResponseMessage;
/**
 *  rest接口样例。
 *  通过接口中心请求过来，到这里处理详细单元。
 *  只要写方法，用注解@UnitMethod定义请求类型即可成为一个接口暴露给第三方使用。
 *  注意所有的类和方法必须放在com.ilib.service.rest.unit包下。
 */
@Service
public class TestUnitService {
	@Autowired
	private ConfigDao configDao;
	@UnitMethod("query")
	public String query(String param){
		JSONObject paramObj = JSON.parseObject(param);
		String notice_min_delay = paramObj.getString("notice_min_delay");
		String value = "";
		try {
			value = configDao.getConfigValueByKey(notice_min_delay);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage rm = new ResponseMessage();
			rm.setCode(Constant.KEY_ERROR);
			rm.setErrorInfo("查询异常！");
			return JSON.toJSONStringWithDateFormat(rm, "yyyy-MM-dd HH:mm:ss");
		}
		ResponseMessage rm = new ResponseMessage();
		rm.setCode(Constant.KEY_SUCCESS);
		rm.setSuccessInfo(value);
		return JSON.toJSONStringWithDateFormat(rm, "yyyy-MM-dd HH:mm:ss");
	}
	
	@UnitMethod("apply")
	public String apply(String data){
		return data;
	}
}
