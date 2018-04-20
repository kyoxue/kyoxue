package com.ilib.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DataUtil {

  private DataUtil() {
  }
  private static class PrivateClass {
      static final DataUtil datautil = new DataUtil();
  }
  //DataUtil.get获取工具类，而真正的实例是不可变的只有一个。
  public static DataUtil get() {
      return PrivateClass.datautil;
  }

  public boolean isNull(Collection<?> datas){
	  return (datas==null||datas.size()==0)?true:false;
  }
  public boolean isNull(Set<?> datas){
	  return (datas==null||datas.size()==0)?true:false;
  }
  public boolean isNull(Map<?,?> datas){
	  return (datas==null||datas.size()==0)?true:false;
  }
  /**
   * 去重，排序ASC
   * @param list
   * @return
   */
  public <E> List<E> duplicateRmWithSort( List<E> list){
	  if (isNull(list)) {
		return list;
	  }
	  Set<E> temp = new HashSet<>(list);
	  List<E> result =  new ArrayList<>(temp);
	  temp.clear();
	  temp = null;
	  list.clear();
	  list = null;
	  return result;
  }
  /**
   * 去重，原序
   * @param list
   * @return
   */
  public <E> List<E> duplicateRmNoSort(List<E> list){
	  if (isNull(list)) {
			return list;
	  }
	  Set<E> set = new  HashSet<>(); 
      List<E> newList = new  ArrayList<>(); 
      for (E e:list) {
         if(set.add(e)){
             newList.add(e);
         }
     }
      set.clear();
      set=null;
      list.clear();
	  list = null;
      return newList;
  }
  /**
   * 去重，原序
   * @param list
   * @return
   */
  public <E> List<E> duplicateRmByContain(List<E> list){
	  if (isNull(list)) {
			return list;
	  }
	  List<E> newList = new  ArrayList<E>(); 
      for (E e:list) {
         if(!newList.contains(e)){
             newList.add(e);
         }
     }
      list.clear();
	  list = null;
      return newList;
  }
  /**
   * 去重，排序ASC
   * @param list
   * @return
   */
  public <E> List<E> duplicateRmBySet(List<E> list){
	  if (isNull(list)) {
			return list;
	  }
	  Set<E> set = new  HashSet<>(); 
      List<E> newList = new  ArrayList<>(); 
      set.addAll(list);
      newList.addAll(set);
      set.clear();
      set = null;
      list.clear();
	  list = null;
      return newList;
  }
  /**
   * 去重，排序ASC
   * @param list
   * @return
   */
  public <E> List<E> duplicateRmByTreeSet(List<E> list){
	  if (isNull(list)) {
			return list;
	  }
	 Set<E> set =  new TreeSet<>(list);
	List<E> result =  new ArrayList<>(set);
	set.clear();
	set = null;
	list.clear();
	list = null;
	return result;
  }
  /**
	 * 分页取集合数据（不在页数范围内数据返回为空，正常的页数范围：一到总页数）
	 * @param page 起始页
	 * @param pageSize 每页的数据条数
	 * @param collection 待分页取的集合数据
	 * @return 当前页的数据
	 */
	public static <T> List<T> pagingCollection(int page,int pageSize,List<T> collection){
		if (null == collection || collection.size() ==0) {
			return null;
		}
		if (page<1) {
			return null;
		}
		int totalNum = collection.size();
		int tempPage  = totalNum/pageSize;
		int totalPage = ((totalNum % pageSize == 0)?(tempPage):(tempPage+1));
		if (page>totalPage) {
			return null;
		}
		List<T> currentCollection = new ArrayList<T>();
      int currentPage = (page > 1 ? (page -1) * pageSize : 0);
      for (int i = 0; i < pageSize && i < collection.size() - currentPage; i++) {
          currentCollection.add(collection.get(currentPage + i));
      }
      return currentCollection;
	}
	
	public <T> boolean isNull(T[] arrs){
		return (arrs == null || arrs.length == 0)?true:false;
	}
}
