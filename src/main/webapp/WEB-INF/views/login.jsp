<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<script type="text/javascript" src="${jsBasePath}/jquery.base64.js"></script>
<script type="text/javascript" src="${jsBasePath}/jquery.cookie.js"></script>
<title>KYO-ILIB</title>
<link rel="stylesheet" type="text/css" href="${cssBasePath}/login.css"/>
<!-- 验证码刷新 -->
<script type="text/javascript">
	function refreshCaptcha(){
    	document.getElementById("img_captcha").src="kaptcha.jpg?t=" + Math.random();
 	}
	$(function(){
		var loginCode = $.cookie("login_code"); //获取cookie中的用户名    
		var pwd = $.cookie("mcpwd"); //获取cookie中的登陆密码    
		if(loginCode) { //用户名存在的话把用户名填充到用户名文本框    
			$("#username").val(loginCode);
		}
		if(pwd) { //密码存在的话把密码填充到密码文本框  
			$("#password").val($.base64.decode(pwd));
			$("#autologin").prop("checked", "true");
		}
	});
	function setmcCookie() { //设置cookie
		var loginCode = $("#username").val(); //获取用户名信息    
		var pwd = $("#password").val(); //获取登陆密码信息    
		var checked = $("[type='checkbox']"); //获取“是否记住密码”复选框  
		if($("#autologin").prop("checked")) {
			//判断是否选中了“记住密码”复选框    
			$.cookie("login_code", loginCode, {
				path: '/',
				expires: 7
			}); //调用jquery.cookie.js中的方法设置cookie中的用户名    
			$.cookie("mcpwd", $.base64.encode(pwd), {
				path: '/',
				expires: 7
			}); //调用jquery.cookie.js中的方法设置cookie中的登陆密码，并使用base64（jquery.base64.js）进行加密    
		} else {
			$.cookie("mcpwd", null, {
				path: '/',
				expires: -1
			});
		}
		return true;
	}
</script>

<!-- 重要！iframe内嵌页面，在注销时会显示不完整，这里强制刷新，恢复登录页面！！ -->
<script language="JavaScript"> 
	if (window != top) 
		top.location.href = location.href; 
</script>

</head>
<body>
<div class="container">
	<div class="loginTop">
		<h1>ILib信息管理系统</h1>
	</div>
	<div class="bigBox mar_b_10">
		<div class="bigBox_t"></div>
		<div class="bigBox_m clearfix">
			<div class="loginAd"></div>
			<form id="index" name="index" action="${contextPath}/login" method="post" onsubmit="return setmcCookie()">
				<div class="loginBox">
					<div class="loginBox_t"></div>
					<div class="loginBox_m">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<lable style="color:red;font-weight:bold;">${LOGIN_ERROR_KEY}</lable>
							<tr>
								<td align="right"><strong>账号：</strong><br /></td>
								<td id="user_name"><label for="textfield"></label> <input
									name="username" type="text" class="loginTextInput"
									id="username" size="30" /></td>
							</tr>
							<tr>
								<td align="right"><strong>密码：</strong></td>
								<td><input name="password" type="password"
									class="loginTextInput" id="password" size="30" />
								</td>
							</tr>
							<tr>
								<td align="right"><strong>验证码：</strong></td>
								<td>
								<input name="captcha" type="text" class="loginTextInput" id="captcha" size="16" />
								<img alt="验证码" src="kaptcha.jpg" title="点击更换"
		                   			 id="img_captcha" onclick="javascript:refreshCaptcha();"/></td>
								</td>
							</tr>
							<tr>
								<td align="right">&nbsp;</td>
								<td>
									<input type="checkbox" id="autologin" title="记住密码"  /><label>记住密码？</label>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><input type="submit" name="button" id="btn_login" value="马上登录" class="loginBtn mar_r_10" /></td>
							</tr>
						</table>
					</div>
					<div class="loginBox_b"></div>
				</div>
			</form>
		</div>
		<div class="bigBox_b"></div>
	</div>
	<div class="loginFooter">
		<div class="copyright">
			kyo ilib - infomation libary.<br /> 粤ICP备16078807 © 2012 CopyRight
			All rights reserved.
		</div>
	</div>
</div>
</body>
</html>
