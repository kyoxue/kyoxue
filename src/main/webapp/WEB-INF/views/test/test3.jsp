<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"  isELIgnored="false"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/WEB-INF/views/common/common.jsp"%>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
    <meta content="telephone=no" name="format-detection">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <script type="text/javascript" src="${jsBasePath}/websocket/WebSocketUtil.js"></script>
    <script type="text/javascript">
  	  var ws;
  	  $(function(){
  		 var o = document.getElementById("message");
  		 ws = new WebSocketUtil(o,"ws://www.kyoxue.com:8080/ilib/jettyTest.ws");
  	  });
  	  function sendMsg(msg){
  		 ws.sendMessage(msg,backFun); 
	  }
	  function backFun(suc){
		  console.info(suc);
	  }
  	</script>
</head>
<body >
 Welcome<br/>
 <!-- 
 	$("#iframe_ws")[0].contentWindow 用jquery访问iframe必须带[0]
 	$("#iframe_ws")[0].contentWindow.$("#元素ID").val() 访问iframe页面的元素值 
 -->
    <input id="text" type="text" /><button onclick="sendMsg(document.getElementById('text').value);">Send</button>    <button onclick="ws.close()">Close</button>
    <div id="message">
    </div>
  </body>
</html>