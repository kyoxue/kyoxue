package com.ilib.taglib;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ilib.common.SpringContext;
import com.ilib.dao.ConfigTypeDao;
import com.ilib.utils.DataUtil;
import com.ilib.utils.StringUtil;

public class ConfigTypeOptionTag extends TagSupport {
	private static final long serialVersionUID = 7112362299421079638L;
	private static final Logger logger = LoggerFactory.getLogger(ConfigTypeOptionTag.class);
	private ConfigTypeDao configTypeDao;
	private String className;
	private String id;
	private String style;
	private String name;
	private String selectValue;
	private String blankShow;
	private void init() {
		configTypeDao = (ConfigTypeDao)(SpringContext.getBean("configTypeDao"));
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			init();
		} catch (Exception e) {
			throw new JspException("com.ilib.dao.configTypeDao cant be init!");
		}
		List<Map<String, Object>> configTypes = null;
		try {
			configTypes = configTypeDao.getConfigurationTypes();
		} catch (Exception e) {
			logger.error("生成配置类型下拉自定义标签，数据库链接异常！",e);
			return SKIP_BODY;
		}
		if (DataUtil.get().isNull(configTypes)) {
			return SKIP_BODY;
		}
		StringBuffer buffer = new StringBuffer();
		className = StringUtil.get().getVal(className);
		id = StringUtil.get().getVal(id);
		style = StringUtil.get().getVal(style);
		name = StringUtil.get().getVal(name);
		selectValue = StringUtil.get().getVal(selectValue);
		buffer.append("<select ");
		if (!"".equals(className)) {
			buffer.append(" class='"+className+"' ");
		}
		if (!"".equals(style)) {
			buffer.append(" style='"+style+"' ");
		}
		if (!"".equals(id)) {
			buffer.append(" id='"+id+"' ");
		}
		if (!"".equals(name)) {
			buffer.append(" name='"+name+"' ");
		}
		buffer.append(">");
		if (!StringUtil.get().isNull(blankShow)) {
			buffer.append("<option value=''> ------------------ </option>");
		}
		for (Map<String, Object> map : configTypes) {
			Object text = map.get("ctypename");
			Object value = map.get("id");
			if (text == null || value == null) {
				continue;
			}
			buffer.append("<option");
			buffer.append(" value='"+value+"'");
			if (!"".equals(selectValue)&&selectValue.equals(value.toString())) {
				buffer.append(" selected='selected'");
			}
			buffer.append(">");
			buffer.append(text.toString());
			buffer.append("</option>");
		}
		buffer.append("</select>");
		try {
			pageContext.getOut().println(buffer);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public String getBlankShow() {
		return blankShow;
	}

	public void setBlankShow(String blankShow) {
		this.blankShow = blankShow;
	}

	public ConfigTypeDao getConfigTypeDao() {
		return configTypeDao;
	}

	public void setConfigTypeDao(ConfigTypeDao configTypeDao) {
		this.configTypeDao = configTypeDao;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}

}
