package com.ilib.service.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ilib.dao.ConfigDao;
import com.ilib.model.Config;

@Service
public class ConfigService implements IConfigService {
	@Autowired
	private ConfigDao configDao;
	
	public void addConfiguration(Config config)throws Exception{
		String ckey = config.getCkey();
		Long count = configDao.checkConfigExists(ckey);
		if (count.intValue()>0) {
			throw new RuntimeException("参数："+ckey+"，已经配置，不能重复！");
		}
		configDao.insertConfiguration(config);
	}

	@Override
	public String getConfigValueByKey(String key) throws Exception {
		// TODO Auto-generated method stub
		return configDao.getConfigValueByKey(key);
	}

	@Override
	public List<Config> getConfigurations(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return configDao.getConfigurations(map);
	}

	@Override
	public Long getConfigurationsCount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return configDao.getConfigurationsCount(map);
	}

	@Override
	public void deleteConfigById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		configDao.deleteConfigById(id);
	}

	@Override
	public Config getConfigByPrimaryKey(Integer id) throws Exception {
		return configDao.getConfigByPrimaryKey(id);
	}

	@Override
	public int updateConfigByPrimaryKey(Config config) throws Exception {
		// TODO Auto-generated method stub
		return configDao.updateConfigByPrimaryKey(config);
	}
}
