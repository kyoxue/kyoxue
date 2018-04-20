<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<title>KYO-ILIB</title>
<script>
//实时时间显示
$(function(){
	setInterval('getNow($("#datetime"))',1000);
})
</script>
</head>
<body>
	<div class="header">
		<h1 class="h-title">
			<span class="h-logo"></span><span>KYO-ILIB</span>
		</h1>

		<div class="h-des" id="updatePass">
			<span class="h-r" id="datetime"></span> <b><span
				class="user-logo"></span>
			<shiro:principal />，您好！<shiro:hasPermission name="000001">下载</shiro:hasPermission></b> <span class="h-line">|</span>
			<a class="btn-updatepass" id="nav_order000" txt="修改密码" href="${contextPath }/updatePass.do">修改密码</a>
			<span class="h-line">|</span> <a class="btn-exit2" href='loginout'><span></span>退出</a>
		</div>
	</div>
	<div class="page">
		<div class="wrap">
			<!--左侧导航-->
			<div id="slidebar" class="slidebar">
				<div id="unexpress" class="unexpress"><a onclick="UnExpress()" href="javascript:void(0)">展开 >></a></div>
				<div id="slidenav">
					<ul>
						<li><a id="nav_orderall" style="float:left;width:110px"><i></i><strong>&nbsp;&nbsp;</strong></a><a style="float:left;width:40px;font-size:12px;color:#0069CA" href="javascript:void(0)" onclick="Express()" ><< 收缩</a></li>
							<ul class="sub-menu clear">
								<li><a id="nav_order_contact" txt="ILIB" href="contact.jsp" ><strong style="color:blue;">首页</strong></a></li>
								<!-- 一级菜单 -->
								<c:forEach var="first" items="${MENU_DATA_KEY}" varStatus="a">
									<li>
										<a id="nav_order_${first.id}"><i class="icon-list"></i><strong>${first.menu}</strong></a>
										<ul class="sub-menu" style="display: none;">
											<!-- 二级菜单 -->
												<c:forEach var="second" items="${first.childs}" varStatus="b">
													<li>
														<c:if test="${fn:length(second.childs) > 0}">
															<a id="nav_order_${second.id }"><i class="icon-list"></i><strong>${second.menu }</strong></a><!-- 带展开和缩小的只能一个属性ID，不能带链接 -->
															<ul class="sub-menu" style="display: none;">
																<!-- 三级菜单 以此类推 目前三级-->
																<c:forEach var="third" items="${second.childs}" varStatus="c">
																	<li>
																		<a id="nav_order_${third.id }" txt="${third.menu }" href="${third.url }">${third.menu }</a>
																	</li>
																</c:forEach>
															</ul>
														</c:if>
														<c:if test="${fn:length(second.childs) == 0}">
															<a id="nav_order_${second.id }" txt="${second.menu }" href="${second.url }">${second.menu }</a>
														</c:if>
													</li>
												</c:forEach>
										</ul>
									</li>
								</c:forEach>
							</ul>
						</li>
					</ul>
				</div>
			</div>
			<!--右侧导航-->
			<div id="content" class="content">
				<div id="tabs" class="tabs">
					<div class="slide_domain" id="domainSelect">
						平台跳转： <select>
							<option test="">TT国际订单系统</option>
							<option test="" selected="selected">TT国内订单系统</option>
							<option test="">政策中心系统</option>
							<option test="">网赢OA系统</option>
						</select>
					</div>
				</div>
				<div class="tabpanel" id="tabpanel">
				</div>
			</div>
		</div>
	</div>
	<!--右键菜单-->
	<ul id="menu">
		<li><a id="menu_refresh" href="javascript:void(0)">刷新</a></li>
		<li><a id="menu_copy" href="javascript:void(0)" style="color:red">复制页面</a></li>
		<li><a id="menu_closecurrent" href="javascript:void(0)">关闭其它页面</a></li>
		<li><a id="menu_closeall" href="javascript:void(0)">关闭所有</a></li>
		<li><a id="menu_cancel" href="javascript:void(0)">取消</a></li>
	</ul>
<%@ include file="/WEB-INF/views/common/info.jsp"%>
</body>
</html>
