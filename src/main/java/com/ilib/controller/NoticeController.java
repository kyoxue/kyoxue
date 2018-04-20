package com.ilib.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilib.common.Constant;
import com.ilib.dao.NoticeDao;
import com.ilib.other.utils.ConfigHelper;
import com.ilib.utils.DataUtil;
import com.ilib.utils.DateTimeUtils;
import com.ilib.utils.InterfaceResponseUtil;
import com.ilib.utils.JsonUtil;
import com.ilib.utils.StringUtil;

@Controller
@RequestMapping("/system/notice")
public class NoticeController {
	Logger log = LoggerFactory.getLogger(NoticeController.class);
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private ConfigHelper configHelper;
	@RequestMapping(value = "/notice_information", method = {RequestMethod.GET},produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String getNotice(HttpServletRequest request) {
		String hourstr = request.getParameter("hour");
		String preNoticeTime = request.getParameter("preNoticeTime");
		if (StringUtil.get().isNull(hourstr)) {
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "hour is null");
		}
		Integer hour = Integer.valueOf(hourstr);
		Map<String, Object> notice = null;
		try {
			notice = noticeDao.getNotice(hour);
		} catch (Exception e) {
			log.error("查询系统通知异常！",e);
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "查询系统通知异常！");
		}
		if (DataUtil.get().isNull(notice)) {
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "暂无通知..");
		}
		//log.info("上次通知时间："+StringUtil.get().getVal(preNoticeTime));
		//log.info("本轮通知时间："+StringUtil.get().getVal(DateTimeUtils.getDateTime((Date)notice.get("createtime"))));
		if (StringUtil.get().isNull(preNoticeTime)) {//第一次页面请求
			return InterfaceResponseUtil.response(Constant.KEY_SUCCESS, JsonUtil.fmtObj2JsonStrWithDateFmt(notice, Constant.DATE_TIME_FORMAT));
		}else{//页面重复刷新请求
			//当前最后条通知的时间
			Date currentNoticeTime = DateTimeUtils.parseDate(DateTimeUtils.getDateTime((Date)notice.get("createtime")), Constant.DATE_TIME_FORMAT);
			//上一次通知的时间
			Date previewNoticeTime = DateTimeUtils.parseDate(preNoticeTime, Constant.DATE_TIME_FORMAT);
			//最后条通知时间在上次时间之后，则视为最新通知
			if (currentNoticeTime.compareTo(previewNoticeTime) > 0) {
				return InterfaceResponseUtil.response(Constant.KEY_SUCCESS, JsonUtil.fmtObj2JsonStrWithDateFmt(notice, Constant.DATE_TIME_FORMAT));
			}
		}
		return InterfaceResponseUtil.response(Constant.KEY_ERROR, "暂无最新通知..");
	}
	@RequestMapping(value = "/notice_config", method = {RequestMethod.GET,RequestMethod.POST},produces={"application/json;charset=UTF-8"},consumes={"application/json;charset=UTF-8"} )
	@ResponseBody
	public String getNoticeConfig(@RequestBody Map<String, String[]> map){
		String keys[] = new String[0];
		try {
			keys = map.get("keys");
		} catch (Exception e) {
			log.error("parse param error",e);
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "parse param error.");
		}
		if (StringUtil.get().isBlankExist(keys)) {
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "Config key exist null.");
		}
		Map<String, String> resultMap = new HashMap<>();
		for (String key : keys) {
			String value = configHelper.loadParam(key);
			resultMap.put(key, value);
		}
		if (DataUtil.get().isNull(resultMap)) {
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "Config is null.");
		}
		if (resultMap.size()!=keys.length) {
			return InterfaceResponseUtil.response(Constant.KEY_ERROR, "Config exist null.");
		}
		return InterfaceResponseUtil.response(Constant.KEY_SUCCESS, JsonUtil.fmtObj2JsonStr(resultMap));
	}
}
