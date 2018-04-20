package com.ilib.other.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ilib.common.annotation.TimeCost;
import com.ilib.common.annotation.TimeCost.MethodType;
import com.ilib.utils.DateTimeUtils;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
/**
 * 统计耗时
 */
@Aspect  
@Service  
public class TimeCostAspect {
		Logger log = LoggerFactory.getLogger(TimeCostAspect.class); 
		private long start = 0l;  
		//定义一个关切点，这里是注解TimeCost，所有被这个注解使用的方法
	    @Pointcut("@annotation(com.ilib.common.annotation.TimeCost)")  
	    public void timeCostAspect() {  
	    }  
	    //被注解的方法执行前，进行doSomthing，timeCostAspect()为上面定义的方法名
	    @Before("timeCostAspect()")  
	    public void doBefore(JoinPoint joinPoint) {
	    	String desc = getClassAndMethodDesc(joinPoint);
	    	start = System.currentTimeMillis();
	    	log.info(desc+"开始执行..当前开始时间"+DateTimeUtils.getDateTimeFromMill(start));
	    }  
	    //被注解的方法执行返回后，进行doSomthing，timeCostAspect()为上面定义的方法名  
	    @AfterReturning(pointcut="timeCostAspect()")  
	    public  void doAfter(JoinPoint joinPoint) {  
	    	String desc = getClassAndMethodDesc(joinPoint);
	    	long end = System.currentTimeMillis();
	    	log.info(desc+"结束执行..当前结束时间"+DateTimeUtils.getDateTimeFromMill(end)); 
	    	String diff =  DateTimeUtils.getMillDiff(start, end);
	    	log.info("总共耗时："+diff);
	    }  
	    //被注解的方法抛出异常后，进行doSomthing，timeCostAspect()为上面定义的方法名 
	    @AfterThrowing(value="timeCostAspect()",throwing="e")  
	    public void doAfter(JoinPoint joinPoint, Exception e) {  
	    	String desc = getClassAndMethodDesc(joinPoint);
	        log.error(desc+"发生异常！",e);  
	    }  
	  
	    private String getClassAndMethodDesc(JoinPoint joinPoint){
	    	 //获得注解  
        	TimeCost timeCostAnnotation = null;
        	try {
        		timeCostAnnotation =getTimeCost(joinPoint); 
			} catch (Exception e) {
				log.error("获取耗时注解对象异常！",e);
				return "";
			}
            if(timeCostAnnotation == null)  
            {  
                return "";  
            }  
	    	// 获取目标方法签名  
	    	String signature = joinPoint.getSignature().toString(); 
            //方法名
            String methodName = signature.substring(signature.lastIndexOf(".") + 1,signature.indexOf("("));  
            //完整的类，方法，参数描述
            //比如：execution(public java.lang.String com.ilib.controller.ConfigController.getconfigurations(javax.servlet.http.HttpServletRequest))
            //String longTemp = joinPoint.getStaticPart().toLongString();  
            //类名
            String classType = joinPoint.getTarget().getClass().getName();  
            //反射获取注解所在的类
            Class<?> clazz = null;
            try {
				clazz = Class.forName(classType);
			} catch (Exception e) {
				log.error("耗时注解拦截获取注解所在的类说明异常！",e);
				return "";
			}
            //类下所有公开的方法
            Method[] methods = clazz.getDeclaredMethods();  
            for (Method method : methods) {
            	//当前方法上有耗时注解配置，并且方法名一致
                if (method.isAnnotationPresent(TimeCost.class)&& method.getName().equals(methodName)) {  
                    MethodType type = timeCostAnnotation.type();  
                    String clazzName = clazz.getName(); 
                    try {
                    	String[] paramNames = getFieldsName(clazz, classType, methodName,joinPoint);
    					for (String string : paramNames) {
    						System.err.println(string);
    					}
					} catch (Exception e) {
						// TODO: handle exception
					}
                    return clazzName+"."+methodName+"();操作类型："+type.toString()+"\t";
                }  
            }  
            return "";
	    }
	    /**
	     * 获取方法上的统计耗时注解对象
	     */
	    private static TimeCost getTimeCost(JoinPoint joinPoint) throws Exception {  
	        Signature signature = joinPoint.getSignature();  
	        MethodSignature methodSignature = (MethodSignature) signature;  
	        Method method = methodSignature.getMethod();  
	        if (method != null) {  
	            return method.getAnnotation(TimeCost.class);  
	        }  
	        return null;  
	    } 
	    /**
	     * 参数名
	     * @param cls
	     * @param clazzName
	     * @param methodName
	     * @param joinPoint
	     * @return
	     * @throws Exception
	     */
	    private static String[] getFieldsName(Class cls, String clazzName, String methodName,JoinPoint joinPoint)throws Exception{
			ClassPool pool = ClassPool.getDefault();
			//ClassClassPath classPath = new ClassClassPath(this.getClass());
			ClassClassPath classPath = new ClassClassPath(cls);
			pool.insertClassPath(classPath);
			CtClass cc = pool.get(clazzName);
			CtMethod cm = cc.getDeclaredMethod(methodName);
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++){
				CtClass ptype = cm.getParameterTypes()[i];
				paramNames[i] = attr.variableName(i + pos);	//paramNames即参数名
				System.err.println("---------------------");
				System.err.println(ptype.getName()+"  "+ptype.getSimpleName());
				//System.err.println(getFieldsValue(paramNames, joinPoint));
			}
			Map<String, Map<String,Object>> paramMap = mapParams(paramNames, joinPoint);
			Iterator<Entry<String, Map<String, Object>>> paramIter = paramMap.entrySet().iterator();
			while (paramIter.hasNext()) {
				Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> entry = (Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>) paramIter
						.next();
				String name = entry.getKey();
				Map<String, Object> value = entry.getValue();
				Entry<String, Object> innerentry = value.entrySet().iterator().next();
				String type = innerentry.getKey();
				Object val = innerentry.getValue();
				System.err.println("类型："+type+"参数名："+name+"参数值："+val);
				if (isObject(types, type)) {
					List<Map<String, Map<String, Object>>>  list = listObjectMapParams(val, type);
					System.err.println("-------列举值："+name+"-----------");
					for (Map<String, Map<String, Object>> map : list) {
						Iterator<Entry<String, Map<String, Object>>> objparamIter = map.entrySet().iterator();
						while (objparamIter.hasNext()) {
							Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> entry2 = (Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>) objparamIter
									.next();
							String objname = entry2.getKey();
							Map<String, Object> objvalue = entry2.getValue();
							Entry<String, Object> objinnerentry = objvalue.entrySet().iterator().next();
							String objtype = objinnerentry.getKey();
							Object objval = objinnerentry.getValue();
							System.err.println("-------列举值："+name+"--类型："+objtype+"参数名："+objname+"参数值："+objval);
						}
					}
				}
			}
			return paramNames;
		}
		 /**
		  * 获取所有参数
		  * @param paramNames 参数名称
		  * @param joinPoint 拦截点
		  * @return <参数名,<参数类型,参数值>>
		  */
		 private static Map<String, Map<String,Object>> mapParams(String[] paramNames, JoinPoint joinPoint){
			 Map<String, Map<String,Object>> result = new HashMap<String, Map<String,Object>>();
			 Object[] args = joinPoint.getArgs();  
			 for(int k=0; k<args.length; k++){
				 Object arg = args[k]; 
				 String param = paramNames[k];
				 String typeName = arg.getClass().getName(); 
				 Map<String, Object> each = new HashMap<String, Object>();
				 each.put(typeName, arg);
				 result.put(param, each);
			 }
			 return result;
		 }
		 /**
		  * 判断参数类型是对象还是数据类型
		  * @param types
		  * @param typeName
		  * @return
		  */
		 private static boolean isObject(String[] types,String typeName){
			 for (String type : types) {  
	            if(type.equals(typeName))  
	            {
	            	return false;
	            }
			 }
			 return true;
		 }
		 /**
		  * 获取参数值为对象类型的  参数对象下的所有属性的值 不支持集合，集合可以通过json转成字符串作为参数获取
		  * @param arg
		  * @param typeName
		  * @return <参数名,<参数类型,参数值>>[]
		  */
		 private static List<Map<String, Map<String,Object>>> listObjectMapParams(Object arg,String typeName){
			 List<Map<String, Map<String,Object>>> list = new ArrayList<>();
			try {
				 Field[] fields = arg.getClass().getDeclaredFields(); 
				 if (null!=fields && fields.length>0) {
					Map<String, Map<String,Object>> map = new HashMap<>();
					for (Field field : fields) {
						field.setAccessible(true); 
						Map<String,Object> each = new HashMap<>();
						each.put(field.getType().getName(), field.get(arg));
						map.put(field.getName(), each);
					}
					list.add(map);
				}
			} catch (IllegalArgumentException e) {  
            } catch (IllegalAccessException e) {  
            } catch (Exception e) {
			}
			return list;
		 }
	    private static String[] types = {"java.util.HashMap","java.util.ArrayList","java.math.Bigdecimal","java.util.Date","java.lang.Integer", "java.lang.Double",  
	            "java.lang.Float", "java.lang.Long", "java.lang.Short",  
	            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",  
	            "java.lang.String", "int", "double", "long", "short", "byte",  
	            "boolean", "char", "float" }; 
}
