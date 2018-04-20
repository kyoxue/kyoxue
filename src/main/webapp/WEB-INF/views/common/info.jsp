<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.float_layer{padding:0;margin:0;}


/* float_layer */
.float_layer{width:600px;border:1px solid #aaaaaa;display:none;background:#fff;}
.float_layer h2{height:25px;line-height:25px;padding-left:10px;font-size:14px;color:#333;background:url(${imagesBasePath}/title_bg.gif) repeat-x;border-bottom:1px solid #aaaaaa;position:relative;}

.float_layer .min{width:21px;height:20px;background:url(${imagesBasePath}/min.gif) no-repeat 0 bottom;position:absolute;top:2px;right:25px;}
.float_layer .min:hover{background:url(${imagesBasePath}/min.gif) no-repeat 0 0;}

.float_layer .max{width:21px;height:20px;background:url(${imagesBasePath}/max.gif) no-repeat 0 bottom;position:absolute;top:2px;right:25px;}
.float_layer .max:hover{background:url(${imagesBasePath}/max.gif) no-repeat 0 0;}
.float_layer .close{width:21px;height:20px;background:url(${imagesBasePath}/close.gif) no-repeat 0 bottom;position:absolute;top:2px;right:3px;}
.float_layer .close:hover{background:url(${imagesBasePath}/close.gif) no-repeat 0 0;}

.float_layer .xfcontent{height:250px;overflow-y:auto;overflow-x:auto;font-size:14px;line-height:18px;color:#666;text-indent:28px;}
.float_layer .wrap{border:none;padding:10px;overflow-y:auto;overflow-x:auto;height:auto;}
.float_layer hr{height:1px;border:none;border-top:1px dashed #0066CC;} 
</style>

</head>
<body>

  <div class="float_layer" id="float_layer" >
   <h2>
        <strong id="xfTitle">提示信息</strong>
        <a id="btn_min" href="javascript:;" class="min"></a>
        <a id="btn_close" href="javascript:;" class="close"></a>
    </h2>
    <div class="xfcontent">
    	<div id="xfcontent" class="wrap">
        	消息内容区域
        </div>
     </div>
</div>
<input type="hidden" id="preNoticeTime" value=""><!-- 上次通知的时间 -->
<script type="text/javascript" src="${jsBasePath}/xianfu.js"></script>

<shiro:hasPermission name="000001">
	<script>
	$(function(){
		var min_delay,reflush_delay,time_before,immediately,notice_show;
		var next = false;
		$.ajax({
			   type: "POST",
			   url: "${contextPath }/system/notice/notice_config",
			   data: JSON.stringify({ keys:['notice_min_delay','notice_reflush_delay','notice_time_before','notice_immediately','notice_show']}),
			   async:false,
			   contentType:"application/json; charset=UTF-8",
			   dataType:'json',
			   success: function(data){
				   console.log(JSON.stringify(data));
				   if(data!=null && typeof(data)!="undefined"){
					   var code = data.code;
					   if(code == 'SUCCESS'){
						   var jsonresult = data.successInfo;
						   var successObj = JSON.parse(jsonresult);
						   console.info(successObj.notice_min_delay +'  '+successObj.notice_reflush_delay +'  '+successObj.notice_time_before +'  '+successObj.notice_immediately);
						   var mindelay = parseInt(successObj.notice_min_delay);
						   var reflushdelay = parseInt(successObj.notice_reflush_delay);
						   min_delay = mindelay * 1000;
						   reflush_delay = reflushdelay * 1000;
						   time_before = parseInt(successObj.notice_time_before);
						   immediately = (successObj.notice_immediately == 'Y'?true:false);
						   notice_show = (successObj.notice_show == 'Y'?true:false);
						   next = true;
					   }else{
						   //console.info(data.errorInfo); 
						   next = false;
					   }
				   }else{
				  	  //console.info("ajax response data exception."); 
					   next = false;
				   }
		   		},
			    error:function(result){
					//console.info("ajax response error.");
			    	 next = false;
			    }
		});
		if(next){
			//000001是权限代码 代码和权限对应 有相关权限的代码块执行;
			var preNoticeTime = null;
			function getNotice(hour){
				preNoticeTime = $("#preNoticeTime").val();
				//console.info("请求前上次通知时间"+preNoticeTime);
				$.ajax({
						   type: "GET",
						   url: "${contextPath }/system/notice/notice_information?r="+Math.random(),
						   data: { hour: hour,preNoticeTime:preNoticeTime},
						   async:false,
						   contentType:"application/x-www-form-urlencoded; charset=UTF-8",
						   dataType:'json',
						   success: function(data){
							   //console.log(JSON.stringify(data));
							   if(data!=null && typeof(data)!="undefined"){
								   var code = data.code;
								   if(code == 'SUCCESS'){
									   var msg = data.successInfo;
									   var successData = JSON.parse(msg);
									   var title = successData.title;
									   var noticeTime = successData.createtime;
									   var content = successData.content;
									   $("#preNoticeTime").val(noticeTime);
									   //console.info("请求后本轮通知时间"+noticeTime);
							    	   content = "<p>发布时间："+noticeTime+"</p><hr><p>"+content+"</p>";
									   showXf(title,content);
									   maxNotice();//如果被缩小状态，则恢复视窗显示
									   minNotice();//延时一段时间后，自动缩小状态
								   }else{
									   //console.info(data.errorInfo); 
									   hideXf();
								   }
							   }else{
							  	  //console.info("ajax response data exception."); 
							  	  //window.parent.window.
							  	  hideXf();
							   }
					   		},
						    error:function(result){
								//console.info("ajax response error.");
								hideXf();
						    }
					});
			}
			function maxNotice(){
				var oBtnMin=document.getElementById('btn_min');
				if(oBtnMin && typeof(oBtnMin)!='undefined'){//通知窗体消失的话访问不到
					var maxState = oBtnMin.isMax;
					if(maxState == false){
						oBtnMin.click();
					}
				}
			}
			function minNotice(){
				clearTimeout(t);
				var t = setTimeout(function(){
					var oBtnMin=document.getElementById('btn_min');
					if(oBtnMin && typeof(oBtnMin)!='undefined'){//通知窗体消失的话访问不到
						var maxState = oBtnMin.isMax;
						if(maxState == true){
							oBtnMin.click();
						}
					}
				},min_delay);//1分钟后自动缩小状态
			}
			if(notice_show){
				if(immediately){
					getNotice(time_before);//刚进页面直接显示
				}
				window.setInterval(getNotice,reflush_delay,time_before);//轮训1小时内发布的最新通知，2分钟刷新一次，通知存在2分钟。
			}
		}
	});
	</script>
</shiro:hasPermission>
</body>
</html>