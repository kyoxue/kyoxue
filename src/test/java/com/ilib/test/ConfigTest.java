package com.ilib.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.common.ConstantConfigKey;
import com.ilib.service.config.IConfigService;
import com.ilib.testcommon.BaseTest;

public class ConfigTest extends BaseTest {
	private static Logger log = Logger.getLogger(ConfigTest.class);
	@Autowired
	private IConfigService configService;
	@Test
	public void testGetConfigByKey() throws Exception{
		String value = configService.getConfigValueByKey(ConstantConfigKey.CONFIG_KEY_NOTICE_MIN_DELAY);
		log.info("------------>"+value);
	}

}
