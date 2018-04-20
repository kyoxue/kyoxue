package com.ilib.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 
 * 反射
 * @ClassName: RefUtil 
 * @Description: TODO 
 * @author KYOXUE 
 * @date 2014-5-30 下午5:28:55 
 *  
 */
public  class RefUtil {
	public static Object getObject(Class cls)
	{
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object getObject(String cls)
	{
		try {
			return Class.forName(cls).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Class<?> getClass(Object obj)
	{
		if (null!=obj) {
			return obj.getClass();
		}
		else
		{
			return null;
		}
	}
	public static Class<?> getClass(String class_cn)
	{
		if (null!=class_cn&&class_cn.length()>0&&class_cn.contains(".")) {
			try {
				return Class.forName(class_cn);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}else
		{
			return null;
		}
	}
	public static Field getField(Class<?> cls,String propertyName)
	{
		Field beanField = null;
		if (null==cls||null==propertyName||propertyName.trim().length()==0) {
			return beanField;
		}
		try {
			beanField = cls.getDeclaredField(propertyName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		beanField.setAccessible(true);
		return beanField;
	}
	public static String StrOfGetName(Field beanField)
	{
		if (null==beanField) {
			return "";
		}
		String fieldName = beanField.getName();
	   if (null!=fieldName&&fieldName.trim().length()>0) {
		  String firstLetter = fieldName.substring(0, 1).toUpperCase();
		  return  "get" + firstLetter + fieldName.substring(1);
	   }
	   else
	   {
		   return "";
	   }
	}
	public static String StrOfSetName(Field beanField)
	{
		if (null==beanField) {
			return "";
		}
		String fieldName = beanField.getName();
	   if (null!=fieldName&&fieldName.trim().length()>0) {
		  String firstLetter = fieldName.substring(0, 1).toUpperCase();
		  return  "set" + firstLetter + fieldName.substring(1);
	   }
	   else
	   {
		   return "";
	   }
	}
	public static String StrOfGetName(String fieldName)
	{
	   if (null!=fieldName&&fieldName.trim().length()>0) {
		  String firstLetter = fieldName.substring(0, 1).toUpperCase();
		  return  "get" + firstLetter + fieldName.substring(1);
	   }
	   else
	   {
		   return "";
	   }
	}
	public static String StrOfSetName(String fieldName)
	{
	   if (null!=fieldName&&fieldName.trim().length()>0) {
		  String firstLetter = fieldName.substring(0, 1).toUpperCase();
		  return  "set" + firstLetter + fieldName.substring(1);
	   }
	   else
	   {
		   return "";
	   }
	}
	public static Method methodOfGet(Class<?> cls,String getMethodName)
	{
		if (null==cls||null==getMethodName||getMethodName.trim().length()==0) {
			return null;
		}
		if (!getMethodName.contains("get")) {
			return null;
		}
		try {
			return cls.getMethod(getMethodName, new Class[]{});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Method methodOfSet(Class<?> cls,String setMethodName,Field beanField)
	{
		if (null==cls||null==setMethodName||setMethodName.trim().length()==0) {
			return null;
		}
		if (!setMethodName.contains("set")) {
			return null;
		}
		try {
			return cls.getMethod(setMethodName, new Class[]{beanField.getType()});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Method methodOfSet(Class<?> cls,String setMethodName,Class<?> argumentTypes[])
	{
		if (null==cls||null==setMethodName||setMethodName.trim().length()==0) {
			return null;
		}
		if (!setMethodName.contains("set")) {
			return null;
		}
		try {
			return cls.getMethod(setMethodName, argumentTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object invokeMethod(Method method,Object obj,Object[] arguments)
	{
		if (null!=method&&null!=obj) {
			try {
				return  method.invoke(obj, arguments);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static Object invokeMethod(Method method,Object obj)
	{
		if (null!=method&&null!=obj) {
			try {
				return method.invoke(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 唤醒方法
	 * @param property bean属性
	 * @param bean 	   bean实例	
	 * @param get      get or set 
	 * @param arg_cls  set方法的参数类型 数组 
	 * @param args     set方法的参数值     数组 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object invoke(String property,Object bean,boolean get,Class[] arg_cls,Object...args)
	{
		if (null!=property&&!property.trim().equals("")) {
			if (get) {
				String getMethodName = StrOfGetName(property);
				if (null!=getMethodName&&!getMethodName.trim().equals("")) {
					Method method =  methodOfGet(bean.getClass(), getMethodName);
					if (null!=method) {
						return invokeMethod(method, bean);
					}
				}
			}else
			{
				if (null!=arg_cls&&null!=args&&arg_cls.length==args.length) {
					String setMehodName = StrOfSetName(property);
					if (null!=setMehodName&&!setMehodName.trim().equals("")) 
					{
						Method setMethod = RefUtil.methodOfSet(bean.getClass(), setMehodName, arg_cls);
						if (null!=setMethod) {
							return RefUtil.invokeMethod(setMethod, bean,args);
						}
					}
				}
			}
		}
		return null;
	}
	public  static void main(String[] args) {
	}
}
