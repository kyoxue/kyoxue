package com.ilib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
 	  private StringUtil() {
	  }
	  private static class PrivateClass {
	      static final StringUtil util = new StringUtil();
	  }
	  public static StringUtil get() {
	      return PrivateClass.util;
	  }
	  public boolean isNull(String string){
		  return (null == string || string.trim().equals(""));
	  }
	  public String getVal(String string){
		  return (isNull(string)?"":string.trim());
	  }
		/**
		 * 功能：判断字符串是否为数字
		 */
		public  boolean isNumeric(String str) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(str);
			if (isNum.matches()) {
				return true;
			} else {
				return false;
			}
		}
		/**
		 * 功能：判断字符串是否为日期格式
		 */
		public  boolean isDate(String strDate) {
			Pattern pattern = Pattern.compile(
					"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			Matcher m = pattern.matcher(strDate);
			if (m.matches()) {
				return true;
			} else {
				return false;
			}
		}
		/**
		 * 判断字符编码（仅常用编码）
		 * Kyoxue
		 */
		public  String getcode (String str) { 
		String[] encodelist ={"GB2312","ISO-8859-1","UTF-8","GBK","gb 18030","Big5","UTF-16LE","Shift_JIS","EUC-JP","ISO-2002-JP"};
		for(int i =0;i<encodelist.length;i++){
		try { 
			if (str.equals(new String(str.getBytes(encodelist[i]),encodelist[i]))) {return encodelist[i];} 
		} catch (Exception e) {} 
		finally{}
		}
		return ""; 
		}
		/**
		 * 存在空的字符串判断
		 * Kyoxue
		 */
		public  boolean isBlankExist(String ... strs){
			if (null == strs || strs.length==0) {
				return true;
			}
			for (String string : strs) {
				if (isNull(string)) {
					return true;
				}
			}
			return false;
		}
}
