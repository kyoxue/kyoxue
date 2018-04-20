package com.ilib.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ilib.common.Constant;
import com.ilib.common.ConstantPage;
import com.ilib.dao.MenuDao;
import com.ilib.model.Menu;
import com.ilib.utils.DataUtil;
import com.ilib.utils.PageUtil;


@Controller
@RequestMapping("/menu")
public class MenuController {
	Logger log = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private MenuDao menuDao;
	@RequestMapping(value = "/get", method = {RequestMethod.GET})
	public String get(Menu menu,HttpServletRequest request) {
		request.setAttribute(Constant.EL_QUERY_PARAMS, menu);
		Long total = null;
		List<Menu> menus = null;
		try {
			total = menuDao.getTopMenuManagerCount(menu);
			PageUtil.prepare(Constant.COMMON_PAGE_SIZE, total.intValue(), request, menu);
			menus = menuDao.getTopMenuManager(menu);
			if (DataUtil.get().isNull(menus)) {
				request.setAttribute(Constant.KEY_ERROR, "暂无数据...");
			}else
			{
				request.setAttribute(Constant.KEY_SUCCESS, menus);
			}
		} catch (Exception e) {
			log.error("查询菜单异常！",e);
			request.setAttribute(Constant.KEY_ERROR, "系统繁忙，稍后再试...");
		}
		return ConstantPage.PAGE_MENU_GET;
	}
}
