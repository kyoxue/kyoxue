<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"  isELIgnored="false"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
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
					<form action="${contextPath }/menu/get" method="get">
						<table cellpadding="0" cellspacing="0" class="oderAdmin">
							<tbody>
								<tr>
									<th>菜单名称：</th>
									<td>
										<input type="text" value="${_query.menu }" name="menu" class="textInputN" />
									</td>
									<th>是否有效：</th>
									<td>
										<drop:down name="enable" selectCode="STATE"  blankShow="true" selectValue="${_query.enable }"/>
									</td>
								</tr>
								<tr>
									<td colspan="3" align="right">
										<button type="submit" class="button button-primary">查询</button>
										&nbsp;
										<button type="button" onclick="AddForm('${contextPath }/menu/add','一级菜单添加',400,320)" class="button button-primary">新增一级菜单</button>
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
								<th>名称</th>
								<th>地址</th>
								<th>备注</th>
								<th>排序</th>
								<th>状态</th>
								<th>创建</th>
								<th>修改</th>
								<th>操作</th>
							</tr>
							<!-- forEach开始 -->
							<c:if test="${SUCCESS ne null}">
								<c:forEach var="menu" items="${SUCCESS}" varStatus="status">
									<tr class="highLight"><!--<c:if test="${status.index % 2==0}">class="highLight"</c:if>-->
										<td>${menu.menu}</td>
										<td>${menu.url}</td>
										<td>${menu.remark}</td>
										<td>${menu.sort}</td>
										<td>${so:optionDesc(menu.enable,'enable')}</td>
										<td>${dt:formatDatetime(menu.createtime,'yyyy-MM-dd HH:mm:ss',true)}/${user:formatUserid(menu.creater)}</td>
										<td>${dt:formatDatetime(menu.updatetime,'yyyy-MM-dd HH:mm:ss',true)}/${user:formatUserid(menu.modifier)}</td>
										<td>
											<a onclick="AddForm('${contextPath }/menu/edit?edit_flag=1&edit_id=${menu.id}','一级菜单编辑',400,320)"	href="javascript:void(0)">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a onclick="redirect('${contextPath }/menu/delete?id=${menu.id}')" href="javascript:void(0)">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
		pagenation(${totalPage},${totalRecords},'${contextPath }/menu/get');
	});
	</script>
</body>
</html>