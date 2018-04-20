package com.ilib.utils;

import java.util.List ;

/**
 * @author KYOXUE
 * @date   2017年11月24日
 * @param <E> 集合存放的实体类型
 */
public class PagenationUtil<E> {
	/**
	 * 分页的集合
	 */
    private List<E>    list = null;  
    /**
     * 当前页
     */
    private int        page =0;  
    /**
     * 每页大小
     */
    private int        pageSize =0;
    /**
     * 集合总条数
     */
    private int        totalCount =0;
    /**
     * 总页数
     */
    private int 		totalPage =0;
    /**
     * 循环每页，处理每页的数据：<br>
     * PagenationUtil<集合存放的bean类> p = new PagenationUtil<>(集合数据, 每页大小);<br>
     * int total_page = p.getTotalPage();//先取出总共的页数，来循环页数，依次获取每页数据<br>
	   for (int i = 1; i < totalpage+1; i++) {//注意从1开始，因为是页数，不是下标<br>
		   <blockquote>p.setPage(i);//设置当前页来查找数据<br>
		   List<集合存放的bean类> eachlist = p.getPageList();//得到当前也数据集合<br>
		   //接下来对当前也集合做处理，比如报表输出。<br></blockquote>
	   }<br>
     * @param list 数据集合
     * @param pageSize 分页大小
     */
    public PagenationUtil(List<E> list,int pageSize){
    	this.list = list ;
    	this.pageSize = pageSize ;
    	this.totalCount = ((null == list || list.size() == 0)?0:list.size());
    	this.totalPage = totalCount>0?((totalCount + pageSize - 1) / pageSize):0;
    }
    public List<E> getPageList(){
    	if (totalCount == 0) {
			return null;
		}
    	if (pageSize == 0) {
			return null;
		}
    	int total = getTotalPage();
    	int current = getPage();
    	if (current > total) {
			return null;
		}
    	if (current <= 0) {
			return null;
		}
    	int start = pageSize*(current-1);
    	if (current == total) {
			int leftLen = totalCount%pageSize;
			if (leftLen>0) {
				return list.subList(start, start+leftLen);
			}
		}
    	return list.subList(start, start+pageSize);
    }
    public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotalPage(){
    	return  this.totalPage;
    }
}
