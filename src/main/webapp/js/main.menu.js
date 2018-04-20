
function AddTab(href,id,title) {
	if($("#tabs-wraper").length==0){
		$("#tabs").append("<ul id=\"tabs-wraper\" class=\"tabs-wraper\"></ul><div class=\"tabs-bottom\"></div>");
	}
	RemoveClassName();
	if(href && typeof(href)!="undefined"){
		if(href.indexOf("?")>0){
			href=href+"&r="+Math.random();
		}
		else{
			href=href+"?r="+Math.random();
		}
	}
	if($("#tabitem_"+id).length>0){
		$("#tabitem_"+id).addClass("tabs-item-selected");
		$("#tabpanel_"+id).css("display","");
		var url=$("#iframe_"+id).attr("src");
		if(typeof(url)!="undefined"){
			if(url.indexOf("?")>0){
				url=url+"&r="+Math.random();
			}
			else{
				url=url+"?r="+Math.random();
			}
			$("#iframe_"+id).attr("src",url);
		}
	}
  	else
	{
		var html="<li id=\"tabitem_"+id+"\" class=\"tabs-item tabs-center tabs-item-selected\" role=\"tab\" title=\""+title+"\" style=\"width: 81px;\"><span class=\"tabs-item-text\" id=\"\">"+title+"</span><a id=\"tabclose_"+id+"\" class=\"tabs-item-close tabs-item-close2\" hidefocus=\"hidefocus\" title=\"点击关闭标签\"><span class=\"tabs-item-transarea\"></span><b class=\"tabs-item-ico\">x</b></a></li>";
		$("#tabs-wraper").append(html);

		html="<div id=\"tabpanel_"+id+"\" class=\"tabs-panel\" ><iframe name=\"indexArea\" id=\"iframe_"+id+"\" src=\""+href+"\" frameborder=\"0\" class=\"tabs-panel-iframe\"></iframe></div>";
		$("#tabpanel").append(html);

		$("#tabitem_"+id).click(function(){
			RemoveClassName();
			$(this).addClass("tabs-item-selected");
			$("#tabpanel_"+id).css("display","block");
		});
		$("#tabclose_"+id).click(function(){
			RemoveClassName();
			showPreTabpanel(id);
			$("#tabclose_"+id).parent().remove();
			$("#tabpanel_"+id).remove();
			//因为在点击选项卡的时候，会隐藏其它选项卡 $("#tabitem_"+id).click  ==> RemoveClassName();
			//所以在关闭选项卡的时候，其它的还是隐藏着，导致上一页是空的，这里手动打开关闭页面的前一页，其它隐藏的页面，如果要显示，点击选项卡就会显示。
			//考虑性能，不全部显示。
			//document.getElementById('iframe_'+id).contentWindow.document.getElementById('iframe_ws').ws.close();
			//console.log("closed ws");
		});
		BindMenu(id);
	}
}
function CopyTab(id){
	var navorderid="nav_order_"+id;
	var tabitemid="tabitem_"+navorderid;
	var obj=$("li[id^='"+tabitemid+"']");
	var cid=0;
	var tmp=0;
	var str="";
	for(var i=0;i<obj.length;i++){
		str=$(obj[i]).attr("id").replace("tabitem_","");
		if(str.indexOf("_")){
			var array=str.split('_');
			if(array.length == 3){//第一次复制类似nav_order_8 取目标的数字作为复制目标的数字
				tmp=parseInt(array[2]);
				if(cid<tmp){;
					cid=tmp;
				}
			}
			if(array.length == 4){//接下来复制类似nav_order_8_8 取上次复制目的数字+1作为本次复制的数字
				cid=parseInt(array[3]);;
			}
		}
	}
	if(obj.length>=4){
		alert("sorry!最多允许复制3页！");
		return;
	}
	cid=cid+1;
	AddTab($("#"+navorderid).attr("href"),"nav_order_"+id+"_"+cid,$("#"+navorderid).attr("txt"));
}
function RemoveClassName(){
	var o=$("li[id^='tabitem_']");
	for(var i=0;i<o.length;i++){
		if($(o[i]).attr("class").indexOf("tabs-item-selected")>0){
			var tmpid = $(o[i]).attr("id").replace("tabitem_","");
			$("#tabpanel_"+tmpid).css("display","none");
		}
		$(o[i]).removeClass("tabs-item-selected");
	}
}
/**
 * 关闭页面的时候展示前一页数据 add by kyoxue 2017.11.18
 * @param closeId 关闭页的id名字
 * @returns
 */
function showPreTabpanel(closeId){;
	var o=$("li[id^='tabitem_']");
	if(o.length > 1){//关到只剩下一页的时候，就没有显示上一页了，直接关闭。2页以上，则关闭当前页，显示上一页。
		for(var i=0;i<o.length;i++){;
			var tmpid = $(o[i]).attr("id").replace("tabitem_","");;
			if(closeId == tmpid){
				var preindex = i-1;
				if(preindex >=0){//上面判断页数只剩一页，那是从后往前按顺序关页面，如果直接关第一页，则第一页和后面的加起来也不是只剩一页，这个时候判断前一页的索引比第一页大，再显示。
					var preid = $(o[preindex]).attr("id").replace("tabitem_","");;
					$(o[preindex]).addClass("tabs-item-selected");
					$("#tabpanel_"+preid).css("display","block");
					break;
				}else{//关的第一页，则展示第二页
					var nextindex = i+1;
					var nextid = $(o[nextindex]).attr("id").replace("tabitem_","");;
					$(o[nextindex]).addClass("tabs-item-selected");
					$("#tabpanel_"+nextid).css("display","block");
					break;
				}
			}
		}
	}
}
function Express(){
	$("#slidenav").hide();
	$("#slidebar").css("width","45px");
	$("#content").css("left","46px");
	$("#unexpress").show();
}
function UnExpress(){
	$("#unexpress").hide();
	$("#slidebar").css("width","195px");
	$("#content").css("left","196px");
	$("#slidenav").show();
}



function BindMenu(id){
	$("#tabitem_"+id).bind("contextmenu",function(e){
		if(typeof e.preventDefault === "function"){
			e.preventDefault();
			e.stopPropagation();
		}else{
			e.returnValue = false;
			e.cancelBubble = true;
		}
		$(document).data("currentid", id);
		$("#menu").css({"left":e.clientX,"top":e.clientY-20}).fadeIn();
		$("#menu").show();
	})
}

function CloseIframe(id){
	var o = $("iframe");
	var idtmp;
	for(var i=0;i<o.length;i++){
		idtmp=$(o[i]).attr("id").replace("iframe_","");
		if(idtmp!=id){
			$("#tabclose_"+idtmp).parent().remove();
			$("#tabpanel_"+idtmp).remove();
		}
	}
}
function defaultMenuCN(){
	var nav_orderall = $("#nav_orderall");
	nav_orderall.find("strong").text("关闭");
}
$(function(){
	var contact_href = $("#nav_order_contact").attr("href");
	if(contact_href && contact_href != undefined)
		AddTab($("#nav_order_contact").attr("href"),$("#nav_order_contact").attr("id"),$("#nav_order_contact").attr("txt"));
	$(document).bind("contextmenu",function(){return false;});
	$("#nav_orderall").click(function(){
		var o = $(this).parent().next();
		if($(o).css("display")!="none"){
			$(this).find("strong").text("展开");
		}
		else
		{
			$(this).find("strong").text("关闭");
		}
	});
	$("#slidenav a[id^='nav_order']").click(function(e){
		if(typeof($(this).attr("href"))!="undefined" && $(this).attr("href").length>0){
			if ( e && e.preventDefault )
				e.preventDefault();
			else
				window.event.returnValue = false;
			AddTab($(this).attr("href"),$(this).attr("id"),$(this).text());
			return false;
		}
		else if($(this).find("i").length>0){
			var o = $(this).next();
			if($(this).attr("id")=="nav_orderall"){
				var o = $(this).parent().next();
			}

			if($(o).css("display")!="none"){
				$(this).find("i").addClass("icon-list");
				$(o).css("display","none");
			}
			else
			{
				$(this).find("i").removeClass("icon-list");
				$(o).css("display","");
			}
		}
	});

	//修改密码 start
	$("#updatePass a[id^='nav_order']").click(function(e){
		if(typeof($(this).attr("href"))!="undefined" && $(this).attr("href").length>0){
			if ( e && e.preventDefault )
				e.preventDefault();
			else
				window.event.returnValue = false;
			AddTab($(this).attr("href"),$(this).attr("id"),$(this).text());
			return false;
		}
	});
	//修改密码 end

	$(document).bind("click",function(e){
		var target  = $(e.target);
		if(target.closest("li[id^='tabitem_']").length == 0){
			$("#menu").hide();
		}
	})

	$("#menu").menu();

	//刷新
	$("#menu_refresh").bind("click",function(){
		var id = $(document).data("currentid");
		if(typeof(id)!="undefined"){
			var url= $("#iframe_"+id).attr("src");
			if(url.indexOf("?")>0){
				url=url+"&r="+Math.random();
			}
			else{
				url=url+"?r="+Math.random();
			}
			$("#iframe_"+id).attr("src",url);
		}
	});
	//关闭其它页面
	$("#menu_closecurrent").bind("click",function(){
		var id = $(document).data("currentid");
		CloseIframe(id);
		$("#tabitem_"+id).click();
	});
	//关闭所有
	$("#menu_closeall").bind("click",function(){
		CloseIframe("");
	});
	//复制新页面
	$("#menu_copy").bind("click",function(){
		var id = $(document).data("currentid");
		if(id && typeof(id)!="undefined"){
			id=id.replace("nav_order","");
			if(id.indexOf("_")>=0){
				var array=id.split('_');
				if(array.length==2){
					id=array[1];
				}
				if(array.length>2){
					alert("sorry!你只能从原始页继续复制！<br>不支持新生页复制！");
					return;
				}
			}
		}
		if(id && typeof(id)!="undefined"&&id!="")
			CopyTab(id);
	});
	//取消
	$("#menu_refresh").bind("click",function(){
		$("#menu").hide();
	});
	$("#slidenav a[id^='nav_order']").each(function(){
		var href = $(this).attr("href");
		if(href!=undefined&&href!="undefined"&&href.length>0){
			//获取菜单的地址可以用来判断是哪个菜单，然后做些事情..
		}
	});
	UnExpress();//默认菜单显示状态 express(收缩菜单显示)
	defaultMenuCN();//默认菜单区域显示 这里设置页面首次进入文字“关闭” 点击后，菜单区域会隐藏 并显示文字“展开”
});