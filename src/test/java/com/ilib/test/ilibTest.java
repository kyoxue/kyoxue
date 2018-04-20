package com.ilib.test;


import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.dao.TestDao;
import com.ilib.model.TestBeanB;
import com.ilib.other.utils.MailHelper;
import com.ilib.testcommon.BaseTest;

public class ilibTest extends BaseTest {
	Logger log = LoggerFactory.getLogger(ilibTest.class);
	
	@Autowired
	private MailHelper mailHelper;
	@Test
	public void testMail()throws Exception{
		VelocityContext v= new VelocityContext();
		v.put("to", "小明2");
		v.put("assignee", "张老师2");
		String vm = "testMail.vm";
		String toMail = "838170000@qq.com";
		String title = "通知测试1222";
		mailHelper.sendTaskMail(v, vm, toMail, title);
	}
	@Autowired
	private TestDao testDao;
	@Test
	public void testDao()throws Exception{
		TestBeanB user = testDao.selectUserById(new Integer(1));
		log.info(user.getAccount()+"===============");
	}
}
