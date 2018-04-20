package com.ilib.utils;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class ActionUtil {
	/**
	 * 缓存页面条件，直到在当前页面改变条件
	 * @param bean bean实例
	 * @param request HttpServletRequest
	 * @param pageFlag 是否页面请求 null菜单请求 不空页面请求 通过在页面放置hidden传送
	 * @param properties 需要缓存的bean属性值数组，页面通过对应数组中的属性名session.getAttribute获取。
	 */
     public  static  void  sessionSearch(String prefix,Object bean,HttpServletRequest request,String pageFlag,String...properties)
     {
    	HttpSession session = request.getSession(false);
    	String thispage = request.getParameter(pageFlag);
  		boolean pageSend = (null==thispage||thispage.trim().equals(""))?false:true;
    	if (null!=bean) {
    		 if (null!=properties&&properties.length>0) {
    				for (String propertyName : properties) {
						Object val = RefUtil.invoke(propertyName, bean, true, new Class[0], new Object[0]);
						String sessionName =(null==prefix||prefix.trim().equals(""))?propertyName:prefix.concat(propertyName);
						if (pageSend) {
			 					session.setAttribute(sessionName, val);
				 		}else
				 		{
				 				Object obj = session.getAttribute(sessionName);
				 				String sessionVal = ((obj==null)?"":String.valueOf(obj));
				 				RefUtil.invoke(propertyName, bean, false, new Class[]{String.class}, new Object[]{sessionVal});
						}
				 	}
    			}
    		}
     }
}
