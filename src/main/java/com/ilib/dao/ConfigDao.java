package com.ilib.dao;

import java.util.List;
import java.util.Map;

import com.ilib.model.Config;

public interface ConfigDao {
	String getConfigValueByKey(String key)throws Exception;
	List<Config> getConfigurations(Map<String, Object> map)throws Exception;
	Long getConfigurationsCount(Map<String, Object> map)throws Exception;
	void insertConfiguration(Config config)throws Exception;
	Long checkConfigExists(String ckey)throws Exception;
	void deleteConfigById(Integer id)throws Exception;
	Config getConfigByPrimaryKey(Integer id)throws Exception;
	int updateConfigByPrimaryKey(Config config)throws Exception;
}
