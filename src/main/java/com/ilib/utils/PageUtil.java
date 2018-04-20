package com.ilib.utils;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.ilib.common.Pagenation;
/**
 * 分页预处理
 */
public class PageUtil {
	/**
	 * 通过Map传递查询
	 * 前台调用分页，获取总页数和总记录数，必须使用${totalPage},${totalRecords}
	 * @param pageSize 每页大小
	 * @param total 总共条数
	 * @param request HttpServletRequest
	 * @param querymap 查询总记录数使用的map，再查询分页记录，继续传递这个map添加起始和每页大小查询。
	 */
	public static void prepare(int pageSize,int total,HttpServletRequest request,Map<String, Object> querymap){
		String pageParam = request.getParameter("pno");
		Pagenation pagenation = new Pagenation(pageParam, pageSize, total); 
		querymap.put("page", pagenation.getPage());
		querymap.put("pagesize", pagenation.getPageSize());
		request.setAttribute("totalPage", pagenation.getTotalPage());
		request.setAttribute("totalRecords",pagenation.getTotal());
	}
	/**
	 * 通过实体bean传递查询
	 * @param pageSize
	 * @param total
	 * @param request
	 * @param queryParam
	 */
	public static void prepare(int pageSize,int total,HttpServletRequest request,Object queryParam){
		String pageParam = request.getParameter("pno");
		Pagenation pagenation = new Pagenation(pageParam, pageSize, total); 
		try {
			Method pageMethod = queryParam.getClass().getMethod("setPage", Integer.class);
			pageMethod.invoke(queryParam, pagenation.getPage());
			Method pagesizeMethod = queryParam.getClass().getMethod("setPagesize", Integer.class);
			pagesizeMethod.invoke(queryParam, pagenation.getPageSize());
		} catch (Exception e) {
		}
		request.setAttribute("totalPage", pagenation.getTotalPage());
		request.setAttribute("totalRecords",pagenation.getTotal());
	}
}
