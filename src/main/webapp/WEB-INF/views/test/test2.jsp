<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"  isELIgnored="false"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<script type="text/javascript" >
 $(function(){
 });
</script>
</head>
<body>
	<div style="text-align:left;font-size: 24px;">
         <table border="0" cellpadding="3" cellspacing="0" style="width: 60%;margin:auto">
			<tr>
				<th style="text-align:left;">
					<a onclick="openPage('${contextPath }/test','test',1000,750)" href="javascript:void(0)">测试打开链接</a>
				</th>
			</tr>
		</table>
		
	</div>
</body>
</html>