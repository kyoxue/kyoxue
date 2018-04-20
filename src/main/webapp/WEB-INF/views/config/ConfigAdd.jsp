<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"  isELIgnored="false"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<%@ taglib uri="/tags/ConfigTypeOption" prefix="sys"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/WEB-INF/views/common/common_workspace.jsp"%>
</head>
<body>
	<div id="msg" class="msg">${SUCCESS}</div>
	<div id="err" class="err">${ERROR}</div>
	<div class="container pbg2">
		<div class="content">
			<div class="cPanel">
				<form action="${contextPath }/config/configurations_add" method="post" rel="submit" target="_self">
					<table cellpadding="0" cellspacing="0" class="oderAdmin" align="center">
						<tbody>
							<tr>
								<th align="right">参数：</th>
								<td><input type="text" value="${config.ckey }"
									name="ckey" id="ckey" class="textInputN" isempty="no" maxlength="64" /></td>
							</tr>
							<tr>
								<th align="right">配置：</th>
								<td><input type="text" value="${config.cvalue }"
									name="cvalue" id="cvalue" class="textInputN" isempty="no" /></td>
							</tr>
							<tr>
								<th align="right">描述：</th>
								<td><input type="text" value="${config.desc }"
									name="desc" id="desc" class="textInputN" isempty="no" maxlength="128" /></td>
							</tr>
							<tr>
								<th align="right">类别：</th>
								<td>
									<sys:type name="ctype" blankShow="true" selectValue="${config.ctype}" style="width: 159px;" id="ctype"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<button type="submit" class="button button-primary">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
									<button type="reset" rel="add" class="button button-primary">重填</button>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
		<div class="clr"></div>
	</div>
</body>
</html>