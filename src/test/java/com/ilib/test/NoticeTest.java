package com.ilib.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.dao.NoticeDao;
import com.ilib.testcommon.BaseTest;
import com.ilib.utils.DateTimeUtils;

public class NoticeTest extends BaseTest {
	Logger loge = Logger.getLogger(NoticeTest.class);
	@Autowired
	private NoticeDao noticeDao;
	
	@Test
	public void testGetNotice()throws Exception{
		Map<String, Object> map = noticeDao.getNotice(new Integer(10));
			String title = (String)map.get("title");
			String content = (String)map.get("content");
			Date createtime = (Date)map.get("createtime");
			String datetimestr = DateTimeUtils.getDateTime(createtime);
			loge.info(title);
			loge.info(content);
			loge.info(datetimestr);
	}

}
