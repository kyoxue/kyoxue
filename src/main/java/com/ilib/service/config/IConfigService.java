package com.ilib.service.config;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ilib.model.Config;

public interface IConfigService {
	/**
	 * Transactional 使用时：<br> 
	 * 如果是cglib代理，只能标注在类和类的方法上，而且在autoware的时候必须是类加变量来引用private XxService xxService，不能private IXxService xxService<br> 
	 * 如果是JDK拦截，只能标注在接口和接口方法上，而且在autoware的时候必须是接口加变量来引用private IXxService xxService,不能private XxService xxService<br>
	 * 如果让cglib或jdk事务，见dao配置文件的<tx:annotation-driven  transaction-manager="transactionManager" /><br> 
	 *  proxy-target-class属性值决定是基于接口的还是基于类的代理被创建。
		如果proxy-target-class 属性值被设置为true，那么基于类的代理将起作用（这时需要cglib库）注解只能在具体类的方法上。
		如果proxy-target-class属值被设置为false或者这个属性被省略，那么标准的JDK 基于接口的代理将起作用。注解只能在接口方法上
		注意：SPRING 3.2以后自带CGLIB，不用单独再引入cglib的jar，可以不用写proxy-target-class属性，会自动根据你事务配置在哪来调用事务的代理方式。
	 * @param config
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly=false,value="transactionManager")
	public void addConfiguration(Config config)throws Exception;
	String getConfigValueByKey(String key)throws Exception;
	List<Config> getConfigurations(Map<String, Object> map)throws Exception;
	Long getConfigurationsCount(Map<String, Object> map)throws Exception;
	void deleteConfigById(Integer id)throws Exception;
	Config getConfigByPrimaryKey(Integer id)throws Exception;
	int updateConfigByPrimaryKey(Config config)throws Exception;
}
