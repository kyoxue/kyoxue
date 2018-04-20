package com.ilib.other.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.ilib.common.Constant;
import com.ilib.common.SpringContext;
import com.ilib.common.annotation.UnitMethod;
import com.ilib.utils.DataUtil;
import com.ilib.utils.ScanPackgeUtil;
import com.ilib.utils.StringUtil;
/**
 * 自动执行处理单元
 * @author Kyoxue
 */
@Service
public class RestAutoMethodUtil {
	Logger log = LoggerFactory.getLogger(RestAutoMethodUtil.class); 
	/**
	 * 自动根据rest中心的请求类型，寻找方法执行
	 * @param pack 执行方法类所在的包位置 如：com.ilib.service.rest.unit
	 * @param requestType 请求接口的类型定义，也是通过UnitMethod注解定义的名称
	 * @param param 请求的json信息
	 * @return 处理完成返回的json信息
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, String> autoMethod(String pack,String requestType,String param){
		String logpre = "请求中心得到请求，类型："+requestType+"，开始匹配处理单元 --- >";
		log.info(logpre);
		Map<Integer, String> result = new HashMap<>();
		Set<Class> clazzs = ScanPackgeUtil.getClasses(pack, false, Constant.ENCODING_UTF_8);
		if (DataUtil.get().isNull(clazzs)) {
			result.put(-1, "没有找到任何单元处理类..");
			return result;
		}
		log.info(logpre+"扫描处理单元所在的所有类完成，总共："+clazzs.size());
		List<Map<Class, Method>> methodList = new ArrayList<>();
		for (Class cls : clazzs) {//迭代所有找到的类
			Method[] methods = cls.getDeclaredMethods();
			if (!DataUtil.get().isNull(methods)) {
				for (Method method : methods) {//迭代每个类下面的所有方法
					method.setAccessible(true);//设置可访问
					boolean yes  = method.isAnnotationPresent(UnitMethod.class);//寻找方法上面配置了UnitMethod注解的方法
					if (yes) {
						UnitMethod unitMethod = method.getAnnotation(UnitMethod.class);//获取方法上的这个注解配置
						String value = unitMethod.value();//请求方式
						if (!StringUtil.get().isNull(value)&&value.trim().equals(requestType)) {//注解配置请求方式和传递的一致，就是要执行的方法
							Map<Class, Method> _temp = new HashMap<>();
							_temp.put(cls, method);
							methodList.add(_temp);
						}
					}
				}
			}
		}
		if (DataUtil.get().isNull(methodList)) {
			result.put(0, "不存在的请求类型："+requestType);
			return result;
		}
		if (methodList.size()>1) {
			StringBuffer stringBuffer = new StringBuffer("请检查注解UnitMethod是否存在相同的value定义，");
			for (Map<Class, Method> map : methodList) {
				Entry<Class, Method> entry = map.entrySet().iterator().next();
				String classcn = entry.getKey().getName();
				String methodcn = entry.getValue().getName();
				stringBuffer.append("类："+classcn+"，方法："+methodcn+"；");
			}
			result.put(-1, stringBuffer.toString());
			return result;
		}
		Map<Class, Method> target = methodList.get(0);
		Entry<Class, Method> entry = target.entrySet().iterator().next();
		Class clazz = entry.getKey();
      //------------------------------------取到bean类头上配置的spring id----------------------------------------		
		Class[] annotations = new Class[]{Component.class,Repository.class,Service.class,Controller.class};
		String annotationValue = null;
		for (Class classAnnotation : annotations) {
			if (clazz.isAnnotationPresent(classAnnotation)) {//类上面标注了,spring自动反射实例的注解
				if (classAnnotation.equals(Component.class)) {
					Component component = (Component)clazz.getAnnotation(classAnnotation);
					annotationValue = component.value();//获取到注解value的值
				}
				if (classAnnotation.equals(Repository.class)) {
					Repository repository = (Repository)clazz.getAnnotation(classAnnotation);
					annotationValue = repository.value();//获取到注解value的值
				}
				if (classAnnotation.equals(Service.class)) {
					Service service = (Service)clazz.getAnnotation(classAnnotation);
					annotationValue = service.value();//获取到注解value的值
				}
				if (classAnnotation.equals(Controller.class)) {
					Controller controller = (Controller)clazz.getAnnotation(classAnnotation);
					annotationValue = controller.value();//获取到注解value的值
				}
				break;
			}
		}
		if (StringUtil.get().isNull(annotationValue)) {//每取到注解定义的bean id，则取类的名字，第一位小写
			String className = clazz.getSimpleName();
			if (StringUtil.get().isNull(className)) {
				result.put(-1, "获取注解方法所在对象异常，没取到spring bean id..");
				return result;
			}
			String first = className.substring(0,1).toLowerCase();
			String after = className.substring(1);
			annotationValue = first+after;
		}
      //--------------------------------------------------------------------------------		
		Object instance = SpringContext.getBean(annotationValue);
		if (null == instance) {
			result.put(-1, "未获取到单元处理方法所在的类的实例..");
			return result;
		}
		Method method = entry.getValue();
		log.info(logpre+"扫描处理单元，匹配到方法："+method.getName()+"，所在的类："+clazz.getName());
		Object returnObject = null;
		try {
			log.info(logpre+"开始执行...");
			returnObject = method.invoke(instance, param);
		} catch (Exception e) {
			log.error("执行处理单元方法异常！",e);
			result.put(-1, "执行单元处理方法异常..");
			return result;
		} 
		if (null == returnObject) {
			result.put(-1, "执行单元处理方法未获取到返回信息..");
			return result;
		}
		String returnstr = String.valueOf(returnObject);
		log.info(logpre+"执行完成.返回："+returnstr);
		result.put(1, returnstr);
		return result;
	}

}
