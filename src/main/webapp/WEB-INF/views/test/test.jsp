<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"  isELIgnored="false"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<style type="text/css">
	.tagclassname{
		color:red;
	}
</style>
<script type="text/javascript" >
 //刷新某个区域，动态显示数据
 //window.setInterval(refreshData,60000,param1,param2);//后面有多少参数跟多少参数
 /**
 function refreshData(param1,param2){
	 $.get("${contextPath }/refresh", { param1: 'val1', param2: val2},
		  function(data){
			  $("#refreshAreaId").html(data);
		  }
	 ); 
 }
 **/
 var downeach = function(aobj){
	 var arr ={};
	arr.path=aobj.attr("href");
	arr.name =aobj.text();
	var data = JSON.stringify(arr);
	$.ajax({
      type:"POST",
      async:false,
      url:"${contextPath }/downloadWithMVC",
      data:data,
      contentType:"application/json;charset=UTF-8"
   });
 }
 $(function(){
	 $("#filedown").on("click",function(){
		 var arr ={};
			arr.path=$(this).attr("href");
			arr.name =$(this).text();
			var data = JSON.stringify(arr);
			$.ajax({
	         type:"POST",
	         async:false,
	         url:"${contextPath }/download",
	         data:data,
	         contentType:"application/json;charset=UTF-8"
	      });
	 });
	 $("#testshowid").text((new Date()).format("yyyy-MM-dd hh:mm:ss.S"));
	 
	//查找父页面元素 $('#id', window.parent.document)
	//parent.value调父页面的变量
	//因为菜单页面都嵌在iframe里面，而消息提示页面在index父页面，所以需要用parent.method来调
	//parent.messageComming("消息提示","测试页面1有新消息啦！！！");
 });
 function jsoup(){
	 $.jsonp({
			url: "http://localhost:8080/ilib/getData",
			data: { yourdata: "data" },//将会以拼接的方式拼在地址后面，如：?yourdata=data
			callbackParameter:"callback",//后台拿来获取回调函数名的参数名，后台拿到后通过：回调函数名(json字符串) 返回
			timeout:3000,  
			dataFilter:function(json){ //成功拿到后台JSON后，在SUCCESS函数调用之前进行处理拦截 
				console.log("jsonp.filter:"+json.name);  
				json.name = "测试123435";  
				return json;  
			},  
			success:function(json,textStatus,xOptions){  
				console.log("jsonp.success:"+json.name);  
			},  
			error:function(xOptions,textStatus){  
				console.log("jsonp.error:"+textStatus+", rel="+xOptions.data.rel);  
			}  
		});
 }
 function convert(){
	 var arr ={};
		arr.name='薛正辉';
		arr.age=30;
		var subs=[];
		for(var i=0;i<2;i++){
			var sub={};
			sub.id = i;
			subs.push(sub);
		}
		arr.subs=subs;
		var data = JSON.stringify(arr);
		$.ajax({
         //提交数据的类型 POST GET
         type:"POST",
         async:false,
         //提交的网址
         url:"${contextPath }/convert",
         //提交的数据{qnrOrderNo:"33333333"}
         data:data,
         //返回数据的格式
         datatype: "json",//"xml", "html", "script", "json", "jsonp", "text".
         contentType:"application/json;charset=UTF-8",
         //在请求之前调用的函数
         //beforeSend:function(){$("#msg").html("logining");},
         //成功返回之后调用的函数             
         success:function(data){
       		//$("#msg").html(decodeURI(data));  
       		console.log(JSON.stringify(data));
         },
         //调用执行后调用的函数
         //complete: function(XMLHttpRequest, textStatus){
         //   alert(XMLHttpRequest.responseText);
         //   alert(textStatus);
             //HideLoading();
         //},
         //调用出错执行的函数
         //error: function(){
             //请求出错处理
         //}         
      });
 }
 function testRedis(){
		$.ajax({
         type:"GET",
         url:"${contextPath }/testRedis",
         datatype: "json",//"xml", "html", "script", "json", "jsonp", "text".
         success:function(data){
       		console.log(JSON.stringify(data));
         }
      });
 }
</script>
</head>
<body>
	<div style="text-align:left;font-size: 24px;">
         <table border="0" cellpadding="3" cellspacing="0" style="width: 60%;margin:auto">
			<tr>
				<th style="text-align:left;">性别：<drop:down selectCode="SEX" blankShow="true" name="sex" selectValue="${2 }"/></th>
			</tr>
			<tr>
				<th style="text-align:left;" id="testshowid"></th>
			</tr>
			<tr>
				<th style="color: blue;"><input  type="button"  value="convert" onclick="convert();"/></th>
			</tr>
			<tr>
				<th style="color: blue;"><input  type="button"  value="跨域测试" onclick="jsoup();"/></th>
			</tr>
			<tr>
				<th style="color: blue;"><input  type="button"  value="redis测试" onclick="testRedis();"/></th>
			</tr>
			<tr>
				<th style="color: blue;">
					<form action="${contextPath}/uploadtest" method="post" enctype="multipart/form-data" >
						<table cellpadding="0" cellspacing="0"  border="0">
							<tr>
							 	<th colspan="3" style="font-size: 14px;">上传文件：</th>
								<td colspan="3">
									<input type="file" id="file" name="file"/>
									<a href="${filePath }" id="filedown">${fileName }</a>
									<button type="submit" class="button button-primary" >上传</button>
								</td>
							</tr>
						</table>
					</form>
				</th>
			</tr>
			<tr>
				<th style="text-align:left;"><cfg:value key="notice_min_delay" className="tagclassname"/></th>
			</tr>
			<tr>
				<th>
					<form id='fForm' action="${contextPath}/uploadMultipartTest"  encType="multipart/form-data"  method="post">  
		                 <table cellpadding="0" cellspacing="0"  border="0">
							<tr>
							 	<th colspan="3" style="font-size: 14px;">上传一个或多个文件：</th>
								<td colspan="3">
									<input type="file"  name="file"/><br/>
									<input type="file"  name="file"/><br/>
									<input type="file"  name="file"/><br/>
									<c:if test="${not empty resultMap}">
										<c:forEach items="${resultMap}" var="item" varStatus="i">  
											<a href="${item.value}" id="filedown${i.index}" onclick="downeach($('#filedown${i.index}'));">${item.key}</a>
										</c:forEach> 
									</c:if>
				                    <button type="submit" class="button button-primary">submit</button>  
								</td>
							</tr>
						</table>
			        </form>
			       <!-- <iframe name="uploadf" style="display:none"></iframe>--><!-- 如果只是上传不下载，通过form的target="uploadf" submit提交不刷新页面，当然返回结果也隐藏啦。 -->   
				</th>
			</tr>
			
		</table>
		
	</div>
</body>
</html>