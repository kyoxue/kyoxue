package com.ilib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	/**
	 * @describe 根据传进来的正则表达式和字符串找出是否符合
	 * @auto ouyuexing
	 * @date 2014-10-10下午15:50:47
	 * @param String 需要查找的字符串
	 * @param p 正则表达式
	 * @return boolean
	 */
	public static Boolean ishere(String star, Pattern p) {
		Matcher m = p.matcher(star);
		return m.find();
	}

	/**
	 * @describe 根据传进来的正则表达式和字符串找出是否符合并返回对应的字符串
	 * @auto ouyuexing
	 * @date 2014-10-10下午15:50:47
	 * @param String 需要查找的字符串
	 * @param p 正则表达式
	 * @return string
	 */
	public static String getstring(String star, Pattern p) {
		Matcher m = p.matcher(star);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

}
