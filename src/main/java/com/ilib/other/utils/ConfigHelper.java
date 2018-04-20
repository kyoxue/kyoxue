package com.ilib.other.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilib.common.ConstantConfigKey;
import com.ilib.service.config.IConfigService;
import com.ilib.utils.StringUtil;

@Service
public class ConfigHelper {
	private final Logger log = LoggerFactory.getLogger(ConfigHelper.class);
	@Autowired
	private IConfigService configService;
	
	/**
	 * 读取配置信息
	 * @param key 参考 -- > {@link ConstantConfigKey}
	 * @return
	 */
	public String loadParam(String key){
		String value = "";
		if (StringUtil.get().isNull(key)) {
			return value;
		}
		try {
			value = configService.getConfigValueByKey(key);
		} catch (Exception e) {
			log.error("读取配置异常！",e);
		}
		return value;
	}

}
