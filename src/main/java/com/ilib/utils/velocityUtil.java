package com.ilib.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class velocityUtil 
{
	/**
	 * 根据velocity的VM模板生成文件，存放到classpath目录下，如果传入xx/xx.xml则存放到classpath/xx/下。<br>
	 * 目前只允许在src下建立一层目录，也可以不建。
	 * @param context 数据内容，类似map.put(key,value)，vm模板根据KEY来取值$!{xx.xx}
	 * @param vmIn    vm模板文件 如：/xx/../xx.vm
	 * @param output  文件输出位置 路径+文件  如：xx/xx.xml
	 * @param loadVM  key:加载的方式 value:vm文件路径 注意：是vm模板文件的前缀路径，不带VM文件<br>
	 * 				  key=CLASS_PATH value=	    		src目录路径，这里为空字符串<br>
	 * 				  key=PYSICAL_PATH value=x:/xx/..			绝对路径<br>
	 *                key=RESOURCE_PATH value=/xx/../xx.properties		资源文件配置加载方式，加载方式在资源文件配置
	 * @param encIn	  vm编码 
	 * @param encOut  输出文件编码	
	 */
	public static void outputByTemplate(VelocityContext context,String vmIn,String output,Map<PathType, String> loadVM,String encIn,String encOut)throws Exception{
		//校验
		if (null == context) {
			throw new Exception("请提供模板数据。");
		}
		if (null == vmIn || vmIn.trim().equals("")) {
			throw new Exception("请提供模板。");
		}
		if (output == null || output.trim().equals("")) {
			throw new Exception("请提供输出文件目录信息。");
		}
		if (null ==loadVM ||loadVM.size() ==0) {
			throw new Exception("请提供加载VM的方式。");
		}
		if (encIn == null || encIn.trim().equals("")) {
			encIn = "UTF-8";
		}
		if (encOut == null || encOut.trim().equals("")) {
			encOut = "UTF-8";
		}
		//classpath目录
		String targetBaseDir = velocityUtil.class.getResource("/").getFile().toString();//classpath根目录
		//输出文件路径
		String outPath = targetBaseDir.concat(output);
		File file = new File(outPath);
		//输出目录不存在，创建文件夹outputs
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		//文件不存在，新建文件
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new Exception("新建文件异常！"+e.toString());
			}
		}
		//配置模板生成的参数
		Properties prop = new Properties();
		prop.setProperty(Velocity.INPUT_ENCODING, encIn);
		prop.setProperty(Velocity.OUTPUT_ENCODING, encOut);
		Entry<PathType, String> vm = loadVM.entrySet().iterator().next();
		//模板加载方式
		PathType type = vm.getKey();
		//模板文件所在路径
		String vmInPrePath = vm.getValue();
		if (PathType.CLASS_PATH.equals(type)) {//classpath下加载
			prop.put("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		}else if(PathType.PYSICAL_PATH.equals(type)){//绝对路径加载
			prop.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, vmInPrePath);
		}else if(PathType.RESOURCE_PATH.equals(type)){//资源文件配置加载方式加载
			try {
				prop.load(velocityUtil.class.getResourceAsStream(vmInPrePath));
			} catch (IOException e) {
				throw new Exception("读取模板加载方式配置异常！"+e.toString());
			}
		}else{
			throw new Exception("请提供正确的加载VM方式！");
		}
		//加载配置
		VelocityEngine velocity = new VelocityEngine();
		velocity.init(prop);
		//获取模板内容
		Template template = velocity.getTemplate(vmIn);
		//开始生成文件
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			template.merge(context, writer);
		} catch (IOException e) {
			throw new Exception("生成文件异常！"+e.toString());
		}finally{
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (Exception ee) {
				}
			}
		}
	}
	/**
	 * 
	 * @param model 设置模板的变量信息 像map一样put key和value
	 * @param templateFile src下模板VM的位置 注意前面不带/，不然变成绝对路径 比如：mailTemplate/testMail.vm
	 * @param encoding 资源文件和模板加载的编码 统一
	 * @return 生成的内容
	 * @throws Exception
	 */
	public static String getTemplateVal(VelocityContext model,String templateFile,String encoding)throws Exception{
		Properties prop = new Properties();
		prop.setProperty(Velocity.INPUT_ENCODING, encoding);
		prop.setProperty(Velocity.OUTPUT_ENCODING, encoding);
		//通过资源文件说明模板加载方式，配置详见velocity.properties
		try {
			prop.load(velocityUtil.class.getClassLoader().getResourceAsStream("velocity.properties"));
		} catch (Exception e) {
			throw new Exception("找不到VM加载方式配置文件velocity.properties！"+e.toString());
		}
		Velocity.init(prop);
		Template template = null;
       try {
    	   template = Velocity.getTemplate(templateFile, encoding);
		} catch (Exception e) {
			throw new Exception("找不到VM文件！"+e.toString());
		}
        StringWriter writer = null;
		try {
			writer= new StringWriter();
			template.merge(model, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new Exception("生成模板内容异常！"+e.toString());
		}finally{
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (Exception ee) {
				}
			}
		}
        
	}
	public static void main(String[] args)throws Exception {
		String templateFile = "mailTemplate/testMail.vm";
		VelocityContext model = new VelocityContext();
		model.put("title", "审批通知信息");
		model.put("to", "张三");
		model.put("content", "你的审批已经通过！");
		System.out.println(getTemplateVal(model, templateFile, "utf-8"));
//		Customer cus = new Customer();
//		cus.setName("kyoxue");
//		cus.setAge("30");
//		cus.setSex("M");
//		cus.setTel("13411552132");
//		List<Product> products = new ArrayList<Product>();
//		for (int i = 0; i < 3; i++) {
//			Product product = new Product();
//			product.setName("商品"+(i+1));
//			product.setPrice("20"+i);
//			product.setRemark("商品"+(i+1)+"备注");
//			products.add(product);
//		}
//		cus.setProducts(products);
//		//数据
//		VelocityContext context = new VelocityContext();
//		context.put("info", cus);
//		//模板
//		String vmIn = "/template/template_customer.vm";
//		//生成文件
//		String output = "outputs/target.xml";
//		//加载VM方式classpath加载
//		Map<PathType, String> loadVM = new HashMap<PathType, String>();
//		/**
//		 * template.properties中配置加载方式：
//		 * file.resource.loader.class=org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
//		 * file.resource.loader.path=E:/tmp
//		 */
//		loadVM.put(PathType.RESOURCE_PATH, "/template.properties");
//		//默认都为utf-8编码输入输出
//		String encIn = "";
//		String encOut = "";
//		outputByTemplate(context, vmIn, output, loadVM, encIn, encOut);
	}
}

	enum PathType {
		/**
		 * p.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		 */
		CLASS_PATH,
		/**
		 * p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "x:/xx/../xx.vm")
		 */
		PYSICAL_PATH,
		/**
		 * p.load(this.getClass().getResourceAsStream("/xx/../xxx.properties"));
		 */
		RESOURCE_PATH;
	}