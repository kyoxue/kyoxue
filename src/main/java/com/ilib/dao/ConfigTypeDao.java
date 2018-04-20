package com.ilib.dao;

import java.util.List;
import java.util.Map;

public interface ConfigTypeDao {
	List<Map<String, Object>> getConfigurationTypes()throws Exception;
	String getConfigurationTypeById(Integer id)throws Exception;
}
