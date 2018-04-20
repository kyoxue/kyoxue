package com.ilib.utils;

import com.ilib.common.Constant;
import com.ilib.model.ResponseMessage;

public class InterfaceResponseUtil {
	/**
	 * 构建接口响应JSON
	 * @param code
	 * @param message
	 * @return
	 */
	public static String response(String code,String message){
		ResponseMessage rm = new ResponseMessage();
		rm.setCode(code);
		if (Constant.KEY_ERROR.equals(code)) {
			rm.setErrorInfo(message);
		}
		if (Constant.KEY_SUCCESS.equals(code)) {
			rm.setSuccessInfo(message);
		}
		return JsonUtil.fmtObj2JsonStr(rm);
	}

}
