package com.ilib.other.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ilib.dao.MenuDao;
import com.ilib.model.Menu;
@Service
public class MenuHelper {
	Logger log = LoggerFactory.getLogger(MenuHelper.class);
    @Autowired
    private MenuDao menuDao;
    public List<Menu> getMenus(Long uid){
    	List<Menu> result = null;
    	try {
    		result = getAllMenu(menuDao.getTopMenu(uid));
		} catch (Exception e) {
			log.error("获取系统菜单异常！",e);
		}
    	return result;
    }
	public List<Menu> getAllMenu(List<Menu> menus)throws Exception{
		if (menus!=null && menus.size()>0) {
			for (Menu menu : menus) {
				Long id = menu.getId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parentId", id);
				menuDao.getChildIdArr(map);
				String ids = map.get("childIdArr").toString();
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("childIdArr", ids);
				map1.put("parentId", id);
				List<Menu> list = menuDao.getChildMenu(map1);
				if (null == list || list.size() == 0) {
					continue;
				}
				menu.setChilds(list);
				getAllMenu(menu.getChilds());
			}
		}
		return menus;
	}

}
