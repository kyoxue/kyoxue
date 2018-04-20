package com.ilib.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ilib.common.Constant;
import com.ilib.common.Pagenation;
import com.ilib.common.annotation.TimeCost;
import com.ilib.common.annotation.TimeCost.MethodType;
import com.ilib.dao.ConfigTypeDao;
import com.ilib.model.Config;
import com.ilib.model.User;
import com.ilib.service.config.IConfigService;
import com.ilib.utils.DataUtil;
import com.ilib.utils.JsonUtil;
import com.ilib.utils.PageUtil;
import com.ilib.utils.StringUtil;


@Controller
@RequestMapping("/config")
public class ConfigController {
	Logger log = LoggerFactory.getLogger(ConfigController.class);
	@Autowired
	private IConfigService configService;
	@RequestMapping(value = "/configurations", method = {RequestMethod.GET})
	@TimeCost(type=MethodType.SEARCH)
	public String getconfigurations(HttpServletRequest request) {
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constant.LOGIN_SESSION_USER_KEY);
		String username = StringUtil.get().getVal(user.getUsername());
		String logpre = "开始查询系统配置（用户："+username+"） ---- >";
		log.info(logpre);
		String ckey = request.getParameter("ckey");
		String ctype = request.getParameter("ctype");
		Map<String, Object> querymap = new HashMap<>();
		if (!StringUtil.get().isNull(ckey)) {
			querymap.put("ckey", ckey);
			log.info(logpre.concat("当前查询配置参数："+ckey));
		}
		if (!StringUtil.get().isNull(ctype)) {
			querymap.put("ctype", Integer.valueOf(ctype));
			log.info(logpre.concat("当前查询配置类别："+ctype));
		}
		request.setAttribute("querymap", querymap);
		Long total = null;
		try {
			total  = configService.getConfigurationsCount(querymap);
			log.info(logpre.concat("当前查询到配置总数："+total.longValue()));
		} catch (Exception e) {
			log.error("查询配置总数数据库异常！{}",e);
			request.setAttribute(Constant.KEY_ERROR,"系统繁忙！");
			return "config/ConfigView";
		}
		PageUtil.prepare(2, total.intValue(), request, querymap);
		List<Config> configurations = null;
		try {
			configurations = configService.getConfigurations(querymap);
			log.info(logpre.concat("获取配置信息成功.."));
		} catch (Exception e) {
			log.error("查询配置数据库异常！{}",e);
			request.setAttribute(Constant.KEY_ERROR,"系统繁忙！");
			return "config/ConfigView";
		}
		if (DataUtil.get().isNull(configurations)) {
			request.setAttribute(Constant.KEY_ERROR,"暂无数据！");
			return "config/ConfigView";
		}
		request.setAttribute("r_configurations", configurations);
		return "config/ConfigView";
	}
	@RequestMapping(value="/configurations_delete",method={RequestMethod.GET})
	public String configurations_delete(HttpServletRequest request)throws Exception{
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constant.LOGIN_SESSION_USER_KEY);
		String username = StringUtil.get().getVal(user.getUsername());
		String id = request.getParameter("id");
		if (StringUtil.get().isNull(id)) {
			request.setAttribute(Constant.KEY_ERROR, "缺失删除的依据编号！请联系系统管理员!");
			return "forward:/config/configurations";
		}
		String logpre = "开始删除系统配置（用户："+username+"） ---- >";
		log.info(logpre.concat("当前删除的编号：").concat(id));
		try {
			configService.deleteConfigById(Integer.valueOf(id));
			request.setAttribute(Constant.KEY_SUCCESS, "删除成功！");
			return "forward:/config/configurations";
		} catch (Exception e) {
			log.error("删除失败！数据库服务器链接异常！当前删除的编号："+id,e);
			request.setAttribute(Constant.KEY_ERROR, "删除失败！数据库服务器链接异常！");
			return "forward:/config/configurations";
		}
	}
	@RequestMapping(value="/configurations_add",method={RequestMethod.POST,RequestMethod.GET})
	public String configurations_add(Config config,HttpServletRequest request){
		request.setAttribute("config", config);
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constant.LOGIN_SESSION_USER_KEY);
		String username = StringUtil.get().getVal(user.getUsername());
		Long userId = user.getId();
		String logpre = "开始添加系统配置（用户："+username+"） ---- >";
		log.info(logpre);
		String ckey = config.getCkey();
		String cvalue = config.getCvalue();
		Integer ctype = config.getCtype();
		if (StringUtil.get().isNull(ckey)) {
			request.setAttribute(Constant.KEY_ERROR, "参数，配置，参数类型必须配置，其余选择添加。");
			return "config/ConfigAdd";
		}
		if (StringUtil.get().isBlankExist(ckey,cvalue)||ctype == null) {
			request.setAttribute(Constant.KEY_ERROR, "参数与配置必须填写，还有参数的类型必须选择！");
			return "config/ConfigAdd";
		}
		try {
			config.setCreater(userId);
			configService.addConfiguration(config);
			log.info(logpre.concat("添加成功！参数："+ckey));
			request.setAttribute(Constant.KEY_SUCCESS, "添加成功！");
			return "config/ConfigAdd";
		}catch (RuntimeException ex){
			log.error(logpre.concat(ex.getMessage()),ex);
			request.setAttribute(Constant.KEY_ERROR, ex.getMessage());
			return "config/ConfigAdd";
		}
		catch (Exception e) {
			log.error(logpre.concat("添加系统配置数据库异常！"),e);
			request.setAttribute(Constant.KEY_ERROR, "系统繁忙！");
			return "config/ConfigAdd";
		}
	}
	@RequestMapping(value="/configurations_edit",method={RequestMethod.POST,RequestMethod.GET})
	public String configurations_edit(Config config,HttpServletRequest request){
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constant.LOGIN_SESSION_USER_KEY);
		String username = StringUtil.get().getVal(user.getUsername());
		Long userId = user.getId();
		String edit_flag = request.getParameter("edit_flag");
		if (!StringUtil.get().isNull(edit_flag)) {
			String logpre = "点击修改系统配置（用户："+username+"） ---- >";
			log.info(logpre);
			String edit_id = request.getParameter("edit_id");
			Integer id = Integer.valueOf(edit_id);
			try {
				Config oginalConfig = configService.getConfigByPrimaryKey(id);
				if (null == oginalConfig) {
					request.setAttribute(Constant.KEY_ERROR, "原始配置已经被删除..");
					return "config/ConfigView";
				}
				request.setAttribute("edit_config", oginalConfig);
			} catch (Exception e) {
				log.error("查询配置数据库链接异常！id："+edit_id,e);
				request.setAttribute(Constant.KEY_ERROR, "系统繁忙！稍后再试..");
				return "config/ConfigView";
			}
		}else{
			String logpre = "开始修改系统配置（用户："+username+"） ---- >";
			log.info(logpre);
			if (config == null) {
				request.setAttribute(Constant.KEY_ERROR, "系统繁忙！稍后再试..");
				return "config/ConfigEdit";
			}
			String ckey = config.getCkey();
			String cvalue = config.getCvalue();
			Integer ctype = config.getCtype();
			if (null == ctype) {
				request.setAttribute(Constant.KEY_ERROR, "必须选择配置的类型！");
				return "config/ConfigEdit";
			}
			if (StringUtil.get().isBlankExist(ckey,cvalue)) {
				request.setAttribute(Constant.KEY_ERROR, "系统配置参数和值不能为空！");
				return "config/ConfigEdit";
			}
			config.setUpdatetime(new Date());
			config.setModifer(userId);
			try {
				int update = configService.updateConfigByPrimaryKey(config);
				if (update > 0) {
					request.setAttribute(Constant.KEY_SUCCESS,"更新配置成功！");
					log.info(logpre+"更新配置成功！更新信息："+JsonUtil.fmtObj2JsonStr(config));
				}else{
					request.setAttribute(Constant.KEY_ERROR, "未做任何修改..");
					log.info(logpre+"未做任何修改！更新信息："+JsonUtil.fmtObj2JsonStr(config));
				}
				request.setAttribute("edit_config", config);
			} catch (Exception e) {
				request.setAttribute(Constant.KEY_ERROR, "系统繁忙！稍后再试..");
				return "config/ConfigEdit";
			}
		}
		return "config/ConfigEdit";
	}
}
