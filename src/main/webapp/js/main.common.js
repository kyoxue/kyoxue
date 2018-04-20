/**
 * ------------------------Window----------------------------------------
 */
function openPage(url, title, width, height) {
	if($("#modeldiv_kyoxue").length > 0){
		$("#modeldiv_kyoxue").remove();
	}
	var div = $("<div></div>");
	div.attr("id", "modeldiv_kyoxue");
	div.attr("title", title);
	div.css("display", "none");
	var iframe = $("<iframe></iframe>");
	iframe.attr("frameborder", "0");
	iframe.attr("scrolling", "yes");
	iframe.css("width", "100%");
	iframe.css("height", "100%");
	iframe.appendTo(div);
	div.appendTo(document.body);
	$("#modeldiv_kyoxue").find("iframe").attr("src", url);
	$("#modeldiv_kyoxue").dialog({
		height : parseInt(height) - 10,
		width : width,
		modal : true,
		close : function(event, ui) {
			document.location.reload();
		}
	});
	return false;
}
//当<div id="msg" class="msg">${msg}</div>不为空时，点击X时，刷新父页面
//当<div id="msg" class="msg">${msg}</div>为空时，点击X时，不刷新父页面
function AddForm(url, title, width, height) {
	if($("#modeldiv_xxx").length > 0){
		$("#modeldiv_xxx").remove();
	}
	var div = $("<div></div>");
	div.attr("id", "modeldiv_xxx");
	div.attr("title", title);
	div.css("display", "none");
	var iframe = $("<iframe></iframe>");
	iframe.attr("id", "modeldiv_iframe_xxx");
	iframe.attr("name", "modeldiv_iframe_xxx");
	//iframe.attr("src", url);
	iframe.attr("frameborder", "0");
	iframe.attr("scrolling", "yes");
	iframe.css("width", "100%");
	iframe.css("height", "100%");
	iframe.appendTo(div);
	div.appendTo(document.body);
	$("#modeldiv_xxx").find("iframe").attr("src", url);
	$("#modeldiv_xxx").dialog({
		height : parseInt(height) - 10,
		width : width,
		modal : true,
		close : function(event, ui) {
			// 这里出现null的情况，js报错
			// var msg=window.frames["modeldiv_iframe_xxx"].document.getElementById("msg").innerHTML;
			// zsj-2014-5-6修改：增加null判断
			var msgObj=window.frames["modeldiv_iframe_xxx"].document.getElementById("msg");
			var msg = msgObj != null ? msgObj.innerHTML : "";
			if(msg != null && typeof(msg) != "undefined" && msg != ""){
				document.location.reload();
			}

			//document.location.reload();
			// window.parent.document.location.reload();
			/*
			 * var url=window.parent.document.location;
			 * if(typeof(url)!="undefined"){ if(url.indexOf("?")>0){
			 * url=url+"&r="+Math.random(); } else{ url=url+"?r="+Math.random(); }
			 * $("#iframe_"+id).css("src",url); }
			 */
		}
	});
	return false;
}

function redirect(url) {
	if (confirm("确定删除？")) {
		location.href = url;
		return true;
	} else {
		return false;
	}
}
/**
 * ------------------------Window----------------------------------------End.
 */
