package com.ilib.service.rest.impl;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ilib.dao.NoticeDao;
import com.ilib.model.Notice;
import com.ilib.model.ResponseMessage;
import com.ilib.service.rest.RsTestService;
@Service
public class TestService implements RsTestService,Serializable {
	private static final long serialVersionUID = -5437240538041460776L;
	@Autowired
	private NoticeDao noticeDao;

	@Override
	public Response selectBy(String titleJson)throws Exception {
		Notice notice = JSON.toJavaObject(JSON.parseObject(titleJson), Notice.class);
		List<Notice> notices = noticeDao.getNoticeBy(notice.getTitle());
		String json = JSON.toJSONStringWithDateFormat(notices, "yyyy-MM-dd HH:mm:ss");
		return Response.ok(json).build();
	}

	@Override
	public String create(Notice notice)throws Exception {
		noticeDao.insertNotice(notice);
		ResponseMessage rm = new ResponseMessage();
		rm.setCode("0000");
		rm.setSuccessInfo("保存成功！");
		return JSON.toJSONStringWithDateFormat(rm, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public Notice getById(Long id) throws Exception {
		return noticeDao.getNoticeByPrimarykey(id);
	}

	@Override
	public Object getMap(String title) throws Exception {
		return noticeDao.getNoticeTitleAndContent(title);
	}

}
