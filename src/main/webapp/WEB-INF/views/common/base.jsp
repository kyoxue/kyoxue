<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib uri="/tags/ConfigValue" prefix="cfg"%>
<%@ taglib uri="/tags/SelectOption" prefix="drop"%>
<%@ taglib prefix="dt" uri="/datetime.format" %>
<%@ taglib prefix="user" uri="/userid.format" %>
<%@ taglib prefix="so" uri="/option.desc" %>
<%-- <c:set var="contextPath" value="<%=request.getContextPath() %>" /> --%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="jsBasePath" value="${contextPath}/js" />
<c:set var="cssBasePath" value="${contextPath}/css" />
<c:set var="imagesBasePath" value="${contextPath}/images" />
<%
	response.setHeader("Cache-Control", "no-cache,no-strore");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
%>
