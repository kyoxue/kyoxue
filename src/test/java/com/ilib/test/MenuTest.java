package com.ilib.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ilib.dao.MenuDao;
import com.ilib.model.Menu;
import com.ilib.testcommon.BaseTest;
import com.ilib.utils.JsonUtil;

public class MenuTest extends BaseTest{
	Logger log = LoggerFactory.getLogger(MenuTest.class);
	@Autowired
	private MenuDao menudao;
	/**
	 * 根据父ID获取子ID集合 调用sql函数
	 * @throws Exception
	 */
	@Test
	public void testGetChildIds()throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", 1);
		menudao.getChildIdArr(map);
		String ids = map.get("childIdArr").toString();
		log.info(ids+"=======================================");
	}
	@Test
	public void testGetChildMenu()throws Exception{
		Long id = new Long(1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", id);
		menudao.getChildIdArr(map);
		String ids = map.get("childIdArr").toString();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("childIdArr", ids);
		map1.put("parentId", id);
		List<Menu> list = menudao.getChildMenu(map1);
		for (Menu menu : list) {
			log.info(menu.getMenu());
		}
	}
	@Test
	public void testGetTopMenu()throws Exception{
		List<Menu> list = menudao.getTopMenu(new Long(1));
		for (Menu menu : list) {
			log.info(menu.getMenu());
		}
	}
	@Test
	public void testGetMenuAll()throws Exception{
		List<Menu> menus = menudao.getTopMenu(new Long(1));
		List<Menu> menuAll = getAllMenu(menus, menudao);
		String json = JsonUtil.fmtObj2JsonStr(menuAll);
		log.info(json);
	}
	public List<Menu> getAllMenu(List<Menu> menus,MenuDao dao)throws Exception{
		if (menus!=null && menus.size()>0) {
			for (Menu menu : menus) {
				Long id = menu.getId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parentId", id);
				dao.getChildIdArr(map);
				String ids = map.get("childIdArr").toString();
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("childIdArr", ids);
				map1.put("parentId", id);
				List<Menu> list = menudao.getChildMenu(map1);
				if (null == list || list.size() == 0) {
					continue;
				}
				menu.setChilds(list);
				getAllMenu(menu.getChilds(), dao);
			}
		}
		return menus;
	}
}
