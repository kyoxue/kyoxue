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
	<div class="container">
		<div class="content">
			<div class="cPanel pbg">
				<div class="detailPanel">
					<form action="${contextPath }/config/configurations" method="get">
						<table cellpadding="0" cellspacing="0" class="oderAdmin">
							<tbody>
								<tr>
									<th>参数类别：</th>
									<td>
										<sys:type name="ctype" blankShow="true" selectValue="${querymap.ctype }"/>
									</td>
								</tr>
								<tr>
									<th>参数名称：</th>
									<td>
										<input type="text" value="${querymap.ckey }" name="ckey" class="textInputN" />
									</td>
									<td>
										<button type="submit" class="button button-primary">查询</button>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<button type="button" onclick="AddForm('${contextPath }/config/configurations_add','配置参数',400,320)" class="button button-primary">新增配置</button>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<div class="cPanel">
				<div id="err" class="err">${ERROR}</div>
				<div class="detailPanel">
					<table cellpadding="0" cellspacing="0" class="oderListTbl" id="oderListTbl">
						<tbody>
							<tr>
								<th>编号</th>
								<th>参数</th>
								<th>配置</th>
								<th>描述</th>
								<th>类别</th>
								<th>状态</th>
								<th>创建人</th>
								<th>创建时间</th>
								<th>操作</th>
							</tr>
							<!-- forEach开始 -->
							<c:if test="${r_configurations ne null}">
								<c:forEach var="r_config" items="${r_configurations}" varStatus="status">
									<tr class="highLight"><!--<c:if test="${status.index % 2==0}">class="highLight"</c:if>-->
										<td>${r_config.id}</td>
										<td>${r_config.ckey}</td>
										<td>${r_config.cvalue}</td>
										<td>${r_config.desc}</td>
										<td>${r_config.type}</td>
										<td>${r_config.enable}</td>
										<td>${r_config.createrName}</td>
										<td>${r_config.createtimestr}</td>
										<td>
											<a onclick="AddForm('${contextPath }/config/configurations_edit?edit_flag=1&edit_id=${r_config.id}','配置编辑',400,320)"	href="javascript:void(0)">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a onclick="redirect('${contextPath }/config/configurations_delete?id=${r_config.id}')" href="javascript:void(0)">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
									</tr>
								</c:forEach>
							</c:if>
							<!-- forEach结束 -->
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="clr" id="kkpager"></div>
	</div>
	<script>
	//init
	$(function(){
		pagenation(${totalPage},${totalRecords},'${contextPath }/config/configurations');
	});
	</script>
</body>
</html>