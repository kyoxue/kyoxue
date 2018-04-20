package com.ilib.testcommon;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-base.xml"})
//@ContextConfiguration({"classpath:spring/spring-*.xml"})
//@ContextConfiguration({"classpath:spring/spring-base.xml","classpath:spring/spring-dao.xml","classpath:spring/spring-activiti.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {
	
}
