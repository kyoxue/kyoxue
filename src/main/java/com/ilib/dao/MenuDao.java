package com.ilib.dao;

import java.util.List;
import java.util.Map;

import com.ilib.model.Menu;

public interface MenuDao {
	String getChildIdArr(Map<String, Object> map)throws Exception;
	List<Menu> getChildMenu(Map<String, Object> map)throws Exception;
	List<Menu> getTopMenu(Long uid)throws Exception;
	Long getTopMenuManagerCount(Menu menu)throws Exception;
	List<Menu> getTopMenuManager(Menu menu)throws Exception;
	List<Menu> getChildMenuManager(Long parentId)throws Exception;
}
