package com.ilib.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
	public static Object keyJsonValue(String json, String key) {
		JSONObject jsonObject = JSON.parseObject(json);
		return jsonObject.get(key);
	}

	public static void main(String[] args)throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("num", 1);
		obj.put("name", "kyoxue");
		
		JSONObject obj1 = new JSONObject();
		obj1.put("num", 4);
		obj1.put("name", "kyoxue4");
		
		JSONObject obj2 = new JSONObject();
		obj2.put("num", 2);
		obj2.put("name", "kyoxue2");
		
		JSONObject obj3 = new JSONObject();
		obj3.put("num", 9);
		obj3.put("name", "kyoxue9");
		JSONArray arr = new JSONArray();
		arr.add(obj);
		arr.add(obj1);
		arr.add(obj2);
		arr.add(obj3);
		JSONArray result = sortedJSONArray(arr, "num",Long.class,false);
		System.out.println(result.toJSONString());
	}

	public static String mapToJson(Map map) {
		return JSONObject.toJSONString(map);
	}

	/**
	 * @describe 将json集合转换成java对应对象集合
	 * @auto duanzaoxing
	 * @date
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> jsonToJavaObject(JSONArray jsonArray, Class cs)
			throws InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<T>();
		for (Object jsonObject : jsonArray) {
			list.add((T) JSON.toJavaObject((JSONObject) jsonObject, cs));
		}
		return list;
	}

	/**
	 * @describe 将json对象转换成java对应对象
	 * @auto duanzaoxing
	 * @date
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T jsonToJavaObject(JSONObject jsonObject, Class cs) {
		return (T) JSONObject.toJavaObject(jsonObject, cs);
	}

	/**
	 * 格式化显示数据信息,方便日志显示
	 * 
	 * @Description:
	 * @author wsd
	 * @date 2014年7月28日 上午9:57:10
	 * @param obj
	 * @return
	 */
	public static String fmtObj2JsonStr(Object obj) {
		String res = "";
		try {
			res = JSON.toJSONString(obj);// 扩张
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * JSON转list
	 * 
	 * @author：rex
	 * @param json
	 * @return
	 */
	public static <T> List<T> json2List(final String json) {
		if (json != null) {
			return JSON.parseObject(json, List.class);
		}
		return null;
	}

	/**
	 * @Description:
	 * @author wsd
	 * @date 2014年8月14日 下午1:53:46
	 * @param obj
	 * @param excludesAttr 需要排除的属性
	 * @return
	 */
	public static String fmtObj2JsonStrIgnoreAttr(Object obj, final String excludesAttr) {
		String res = "";
		try {
			PropertyFilter proFilter = new PropertyFilter() {
				public boolean apply(Object source, String name, Object value) {
					if (!StringUtil.get().isNull(excludesAttr)) {
						if (excludesAttr.contains("," + name + ",")) {
							return false;
						}
					}
					return true;
				}
			};
			res = JSON.toJSONString(obj, proFilter);
		} catch (Exception e) {
			//
		}
		return res;
	}
	/**
	 * add by kyoxue 20170825
	 * 对象转JSON，带日期格式化
	 * @param obj
	 * @param format 如：yyyy-MM-dd
	 * @return
	 */
	public static String fmtObj2JsonStrWithDateFmt(Object obj,String format) {
		String res = "";
		try {
			res = JSON.toJSONStringWithDateFormat(obj,format,new SerializerFeature[0]);
		} catch (Exception e) {
		}
		return res;
	}
	
	/**
	 * json数组按字段排序
	 */
	public static JSONArray sortedJSONArray(JSONArray arr,final String orderBy,final Class orderByClass,final boolean asc){
		if (null ==arr || arr.size() == 0) {
			return null;
		}
		JSONArray result = new JSONArray();
		List<JSONObject> sortedList = new LinkedList<JSONObject>();
		for (int i = 0; i < arr.size(); i++) {
			sortedList.add(arr.getJSONObject(i));
		}
		Collections.sort(sortedList, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject a, JSONObject b) {
				try {
					if (orderByClass.equals(Integer.class)) {
						Integer val_a = a.getInteger(orderBy);
						Integer val_b = b.getInteger(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else if(orderByClass.equals(Long.class)){
						Long val_a = a.getLong(orderBy);
						Long val_b = b.getLong(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else if(orderByClass.equals(Double.class)){
						Double val_a = a.getDouble(orderBy);
						Double val_b = b.getDouble(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else if(orderByClass.equals(BigDecimal.class)){
						BigDecimal val_a = a.getBigDecimal(orderBy);
						BigDecimal val_b = b.getBigDecimal(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else if(orderByClass.equals(Float.class)){
						Float val_a = a.getFloat(orderBy);
						Float val_b = b.getFloat(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else if(orderByClass.equals(String.class)){
						String val_a = a.getString(orderBy);
						String val_b = b.getString(orderBy);
						if (asc) {
							return val_a.compareTo(val_b);
						}else{
							return -val_a.compareTo(val_b);
						}
					}else{
						return 0;
					}
				} catch (JSONException e) {
					return 0;
				}
			}
		});
		for (int i = 0; i < arr.size(); i++) {
			result.add(sortedList.get(i));
		}
		return result;
	}

}
