package com.ilib.utils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
/**
 * 资源文件管理
 * @author Kyoxue 2014-06-20
 *
 */
public class PropertiesConfig {
	private static final Logger log = Logger.getLogger(PropertiesConfig.class.getName());
	private static Properties p = null;
	private static Properties initProperties()
	{
		if (null==p) {
			p= new Properties();
		}
		return p;
	}
	public static Properties loadFile(InputStream is)
	{
		initProperties();
		if (null!=p) {
			try {
				p.load(is);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFile(InputStreamReader reader)
	{
		initProperties();
		if (null!=p) {
			try {
				p.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFile(String fileName)
	{
		initProperties();
		if (null!=p) {
			InputStream in = null;
			try {
				in = new FileInputStream(fileName);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return null;
			}        
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFileBuffered(String fileName)
	{
		initProperties();
		if (null!=p) {
			InputStream in = null;
			try {
				in  = new BufferedInputStream(new FileInputStream(fileName)); 
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				return null;
			}        
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFileAsStream(String fileName)
	{
		initProperties();
		if (null!=p) {
			InputStream in = null;
			in   = PropertiesConfig.class.getResourceAsStream(fileName);        
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	/**
	 * src下的资源文件名
	 */
	private static String srcFileName;
	/**
	 * 获取src下面的配置文件
	 * @param fileName  *.properties
	 * @return
	 */
	public static Properties loadFileOnSrc(String fileName)
	{
		initProperties();
		if (null!=p) {
			File file = new File(PropertiesConfig.class.getClassLoader().getResource(fileName).getPath());        
			InputStreamReader reader  = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file));
			} catch (FileNotFoundException e1) {
				log.error(fileName+" not found...",e1);
				return null;
			}
			try {
				p.load(reader);
				srcFileName = fileName;
			} catch (IOException e) {
				log.error("save "+fileName+" faild..",e);
				return null;
			}
		}
		return p;
	}
	public static Properties loadFileByClsLoader(String fileName)
	{
		initProperties();
		if (null!=p) {
			InputStream in =PropertiesConfig .class.getClassLoader().getResourceAsStream(fileName); 
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFileByClsLoaderStatic(String fileName)
	{
		initProperties();
		if (null!=p) {
			InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	public static Properties loadFileByActionContext(ServletContext ctx,String filePath)
	{
		initProperties();
		if (null!=p) {
			InputStream in = ctx.getResourceAsStream(filePath);  
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}
	/**
	 * 读取key对应的配置
	 * @param key
	 * @return
	 */
	public static String getConfigration(String key)
	{
		String str = "";
		if (null!=p) {
			str = p.getProperty(key);
		}
		return str;
	}
	/**
	 * 读取key对应的配置,读取不到返回defaultVal
	 * @param key
	 * @return
	 */
	public static String getConfigration(String key,String defaultVal)
	{
		String str = "";
		if (null!=p) {
			str = p.getProperty(key,defaultVal);
		}
		return str;
	}
	/**
	 * 自带加载配置文件，读取src下的资源配置信息
	 * @param key
	 * @param fileName
	 * @return
	 */
	public static String getConfigValue(String key,String fileName)
	{
		String str = "";
		Properties p = loadFileOnSrc(fileName);
		if (null!=p) {
			str = p.getProperty(key);
		}
		return str;
	}
	/**
	 * 删除key对应的配置
	 * @param key
	 * @return
	 */
	public static boolean delConfig(String key)
	{
		if (null!=p) {
			if (p.containsKey(key)) {
				p.remove(key);
				return true;
			}
		}
		return false;
	}
	/**
	 * 添加配置信息
	 * @param key
	 * @param value
	 */
	public static void addConfigration(String key,String value)
	{
		if (null!=p) {
			if (!p.containsKey(key)) {
				p.setProperty(key, value);
			}
		}
	}
	public static boolean containKey(Map<String, String> configs)
	{
		if (null!=p) {
			Iterator<Entry<String, String>> it = configs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) it.next();
				String key = entry.getKey();
				if (p.containsKey(key))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	/**
	 * 添加一批配置信息
	 * @param configs
	 * @return
	 */
	public static boolean addConfigrations(Map<String, String> configs)
	{
		if (containKey(configs)) {
			return false;
		}
		if (null!=p) {
			Iterator<Entry<String, String>> it = configs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) it.next();
				String key = entry.getKey();
				String value =entry.getValue();
				p.setProperty(key, value);
			}
			return true;
		}
		return false;
	}
	/**
	 * 修改key对应的配置
	 * @param key
	 * @param value
	 */
	public static void setConfigration(String key,String value)
	{
		if (null!=p) {
			if (p.containsKey(key)) {
				p.setProperty(key, value);
			}
		}
	}
	/**
	 * 修改一批配置信息
	 * @param configs
	 */
	public static void setConfigrations(Map<String, String> configs)
	{
		if (null!=p) {
			Iterator<Entry<String, String>> it = configs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) it.next();
				String key = entry.getKey();
				String value =entry.getValue();
				if (p.containsKey(key))
					p.setProperty(key, value);
			}
		}
	}
	/**
	 * 自带保存的src资源文件单个修改
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setConfigrationValue(String key,String value)
	{
		if (srcFileName==null || srcFileName.trim().equals("")) {
			return false;
		}
		File file = new File(PropertiesConfig.class.getClassLoader().getResource(srcFileName).getPath());      
		OutputStream fos=  null;
		try {
			 fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		if (null!=p) {
			if (p.containsKey(key))
				p.setProperty(key, value);
			try {
				p.store(fos,"");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/**
	 * 保存src资源文件
	 * @param fileName
	 * @return
	 */
	public static boolean saveConfigration()
	{
		if (null==srcFileName||srcFileName.trim().equals("")) {
			return false;
		}
		File file = new File(PropertiesConfig.class.getClassLoader().getResource(srcFileName).getPath());      
		OutputStream fos=  null;
		try {
			 fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		if (null!=p) {
			try {
				p.store(fos,"");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public static boolean saveConfigration(File file)
	{
		OutputStream fos=  null;
		try {
			 fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		if (null!=p) {
			try {
				p.store(fos,"");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public static ResourceBundle loadResourceBundle(String fileName)
	{
		return ResourceBundle.getBundle(fileName, Locale.getDefault());        
	}
	public static ResourceBundle loadResourceBundleByCst(String fileName)
	{
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}        
		try {
			return new PropertyResourceBundle(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}     
	}
	public static void main(String[] args) {
//		loadFileOnSrc("example.properties");
//		addConfigration("test", "1111111");
//		saveConfigration("example.properties");
//		System.out.println(getConfigration("test"));
//		setConfigration("test", "22222222");
//		saveConfigration("example.properties");
//		System.out.println(getConfigration("test"));
//		delConfig("test");
//		saveConfigration();
//		System.out.println(getConfigration("test"));
	}
}
