package com.ilib.controller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.ilib.common.Constant;
import com.ilib.model.Menu;
import com.ilib.model.User;
import com.ilib.other.utils.MenuHelper;
import com.ilib.utils.DataUtil;
import com.ilib.utils.DateTimeUtils;

/**
 * 首页进入
 * @author Kyoxue
 */
@Controller
public class IndexController {
	Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private MenuHelper menuHelper;
	
	@RequestMapping(value = "/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String index(HttpServletRequest request) {
		User currentUser = (User) SecurityUtils.getSubject().getSession().getAttribute(Constant.LOGIN_SESSION_USER_KEY);
		if (currentUser != null && currentUser.getUsername() != null && !currentUser.getUsername().trim().equals("")) {
			List<Menu> menus = menuHelper.getMenus(currentUser.getId());
			if (!DataUtil.get().isNull(menus)) {
				request.setAttribute("MENU_DATA_KEY", menus);
			}
		}
		return "index";
	}
	@RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
	public String login(){
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return "redirect:/index";
		}
		return "login";
	}
	@RequestMapping(value = "/unauthorized", method = {RequestMethod.GET,RequestMethod.POST})
	public String unauthorized(){
		return "login";
	}
}
