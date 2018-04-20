package com.ilib.taglib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ilib.common.Constant;
import com.ilib.common.SpringContext;
import com.ilib.common.enums.EnumEnable;
import com.ilib.dao.UserDao;
import com.ilib.other.utils.ConfigHelper;
import com.ilib.utils.StringUtil;

/**
 * 前台数据处理展示，优化EL的显示<br>
 * 这种自定义写法与传统的不同，不用实现任何自定义标签的接口，这种类似前台的值直接通过后台java方法来处理。<br>
 * 通过${自定义标签的prefix:自定义标签的name(后台传来的vo变量名.字段)}来使用<br>
 * 示例：${kyoxue:formatDatetime(config.createtime)}
 * @author kyoxue
 */
public class KyoxueTag{
	private static final Logger logger = LoggerFactory.getLogger(KyoxueTag.class);
	/**
	 * 日期时间字段格式化
	 */
	public static String formatDatetime(String datetime,String format,Boolean defaulDate){
		if (StringUtil.get().isNull(datetime)) {
			return "";
		}
		//如果没提供格式，默认按日期格式化yyyy-MM-dd
		if (StringUtil.get().isNull(format)) {
			datetime = Constant.DATE_FORMAT;
		}
		//检测提供的格式是否支持，详细见Constant.DATE_TIME_FORMAT_ARRAY
		boolean support = false;
		for (String dtfmt : Constant.DATE_TIME_FORMAT_ARRAY) {
			if (format.equals(dtfmt)) {
				support=true;
				break;
			}
		}
		//不支持，原样返回照旧的日期时间
		if (!support) {
			return datetime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			if (defaulDate.booleanValue()) {
				SimpleDateFormat sdfin = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
				return sdf.format(sdfin.parse(datetime));
			}else{
				return sdf.format(sdf.parse(datetime));
			}
		} catch (ParseException e) {
			logger.error("前台日期时间格式展示异常！",e);
			return datetime;
		}
	}
	public static String formatUserID(Long id){
		if (null == id) {
			return "";
		}
		UserDao userDao = null;
		try {
			userDao = (UserDao)SpringContext.getBean("userDao");
		} catch (Exception e) {
			logger.error("前台用户id转名字展示异常！",e);
			return id.toString();
		}
		if (null!=userDao) {
			try {
				return userDao.getNameByID(id);
			} catch (Exception e) {
				logger.error("前台用户id转名字展示异常！",e);
				return id.toString();
			}
		}
		return id.toString();
	}
	public static String fomartOption(Object optionValue,String formatType){
		if (null == optionValue) {
			return "";
		}
		if (StringUtil.get().isNull(formatType)) {
			return String.valueOf(optionValue);
		}
		if (Constant.FORMAT_TYPE_ENABLE.equalsIgnoreCase(formatType)) {//是否禁用选项 Y/N
			String val = String.valueOf(optionValue);
			for(EnumEnable each:EnumEnable.values()){
				if (each.getVal().equals(val)) {
					return each.getDesc();
				}
			}
		}
		/**
		 * 后续有别的选项描述，在这里参照Enable添加。<br>
		 * 首先你要添加一个EnumXx<br>
		 * 其次在Constant里添加类别FORMAT_TYPE_XX，类别的值最好与数据库字段名一致。<br>
		 * 最后参照Enable迭代枚举返回描述
		 */
		return String.valueOf(optionValue);
	}
}
