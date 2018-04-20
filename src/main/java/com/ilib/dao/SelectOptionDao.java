package com.ilib.dao;

import java.util.List;
import java.util.Map;

public interface SelectOptionDao {
	List<Map<String, String>> getSelectOptionByScode(String scode)throws Exception;
}
