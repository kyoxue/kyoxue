package com.ilib.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 基于commons-configuration读写properties配置<br>
 * 依赖库：commons-configuration commons-lang commons-collections commons-logging<br>
 * @author kyoxue
 */
public class PropertiesUtil {

	private static PropertiesConfiguration propConfig;
    private static final PropertiesUtil CONFIG = new PropertiesUtil();
   //自动保存
    private static boolean autoSave = true;
    private PropertiesUtil() {
    }
    /**
     * XX/XX/../XX.properties
     */
    public static PropertiesUtil getInstance(String propertiesFile)throws ConfigurationException {
        //执行初始化 
        init(propertiesFile);
        return CONFIG;
    }
    private static void init(String classpathFile)throws ConfigurationException {
    	String path = PropertiesUtil.class.getClassLoader().getResource(classpathFile).getPath();
        propConfig = new PropertiesConfiguration(path);
        //自动重新加载 
        propConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
        //自动保存 
        propConfig.setAutoSave(autoSave);
    }
    public Object getValue(String key) {
        return propConfig.getProperty(key);
    }
    public void setProperty(String key, String value) {
		propConfig.setProperty(key, value);
    }
    public void removeProperty(String key){
		propConfig.clearProperty(key);
    }
    public void addProperty(String key, String value){
    	propConfig.addProperty(key, value);
    }
    public void addOrSetProperty(String key, String value){
    	if (propConfig.containsKey(key)) {
    		setProperty(key, value);
    	}else{
    		addProperty(key, value);
    	}
    }
public static void main(String[] args)throws Exception {
	PropertiesUtil util = PropertiesUtil.getInstance("properties/JDConfiguration.properties");
	String witch = (String)util.getValue("JD_SWITCH");
	System.err.println(witch);
}

}
