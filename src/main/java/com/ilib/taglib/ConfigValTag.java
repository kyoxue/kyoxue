package com.ilib.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ilib.common.SpringContext;
import com.ilib.other.utils.ConfigHelper;
import com.ilib.utils.StringUtil;

public class ConfigValTag extends TagSupport {
	private static final long serialVersionUID = 7112362299421079638L;
	private static final Logger logger = LoggerFactory.getLogger(ConfigValTag.class);
	private ConfigHelper configHelper;
	private String key; // 根据key查config value
	private String className;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	private void init() {
		configHelper = (ConfigHelper) (SpringContext.getBean("configHelper"));
	}

	@Override
	public int doStartTag() throws JspException {
		init();
		try {
			StringBuffer buffer = new StringBuffer();
			String value = configHelper.loadParam(key);
			className = StringUtil.get().getVal(className);
			value = StringUtil.get().getVal(value);
			buffer.append("<label class='"+className+"'>");
			buffer.append(value);
			buffer.append("</label>");
			pageContext.getOut().println(buffer);
		} catch (Exception e) {
			logger.error("配置参数标签异常：{}", e);
		}
		return SKIP_BODY;
	}

}
