package com.ilib.common;

public interface Constant {
	final static String LGOIND_SUCCESS_PAGE="index";
	final static int COMMON_PAGE_SIZE = 2;//全局分页大小
	final static String LOGIN_ERROR_KEY="LOGIN_ERROR_KEY";
	final static String LOGIN_ERROR_UNKNOW_USER="您输入的账户不存在，请先注册！";
	final static String LOGIN_ERROR_DISABLED="您的账户已经失效，请联系管理员！";
	final static String LOGIN_ERROR_BLACKLIST="您已经被列入黑名单，无法登录！";
	final static String LOGIN_ERROR_CAPTCHA_UNRIGHT="验证码不正确！";
	final static String LOGIN_ERROR_PASSWORD_UNRIGHT="密码不正确！";
	final static String LOGIN_ERROR_TIME_OVER="登录失败次数过多，无法登录！";
	final static String LOGIN_ERROR_LOCKED="SORRY!您的帐号已被锁定！";
	final static String LOGIN_ERROR_USER_DISABLED="帐号已被禁用！";
	final static String LOGIN_ERROR_INVALID="帐号已过期！";
	final static String LOGIN_ERROR_UNEXISTS="帐号不存在！";
	final static String LOGIN_ERROR_UNRIGHT="没有权限，请联系管理员！";
	final static String LOGIN_ERROR_CONFIG_EXCEPTION="登录失败！";
	
	final static String LOGIN_SESSION_USER_KEY="LOGIN_SESSION_USER_KEY";
	
	final static String MENU_DATA_KEY = "MENU_DATA_KEY";
	
	final static String Y="Y";
	final static String N="N";
	
	final static int NUM_OME=1;
	final static int NUM_THERO=0;
	
	final static String DATE_FORMAT="yyyy-MM-dd";
	final static String DATE_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	final static String DATE_TIME_FORMAT_SIMPLIFY ="yyyy-MM-dd HH:mm";
	final static String DATE_TIME_FORMAT_12="yyyy-MM-dd hh:mm:ss";
	final static String DATE_TIME_FORMAT_SIMPLIFY_12 ="yyyy-MM-dd hh:mm";
	final static String DATE_FORMAT_SLASH="yyyy/MM/dd";
	final static String DATE_TIME_FORMAT_SLASH="yyyy/MM/dd HH:mm:ss";
	final static String DATE_TIME_FORMAT_SLASH_12="yyyy/MM/dd hh:mm:ss";
	final static String[] DATE_TIME_FORMAT_ARRAY ={DATE_FORMAT,DATE_TIME_FORMAT,DATE_TIME_FORMAT_SIMPLIFY,DATE_TIME_FORMAT_12,DATE_TIME_FORMAT_SIMPLIFY_12,DATE_FORMAT_SLASH,DATE_TIME_FORMAT_SLASH,DATE_TIME_FORMAT_SLASH_12};
	final static String KEY_ERROR = "ERROR";
	final static String KEY_SUCCESS = "SUCCESS";
	
	final static String ENCODING_UTF_8 ="UTF-8";
	final static String ENCODING_GBK ="GBK";
	
	final static String EL_QUERY_PARAMS = "_query";//查询记录EL KEY
	
	//前台展示选项值替换成描述，选项类型的指定
	final static String FORMAT_TYPE_ENABLE = "enable";
}
