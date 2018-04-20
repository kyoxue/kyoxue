/**
 * 悬浮提示窗插件
 * @author miraclerz
 */


function floatAddEvent(oEle, sEventName, fnHandler)
{
	if(oEle.attachEvent)
	{
		oEle.attachEvent('on'+sEventName, fnHandler);
	}
	else
	{
		oEle.addEventListener(sEventName, fnHandler, false);
	}
}
//隐藏
function hideXf(){
	var oDiv=document.getElementById('float_layer');
	oDiv.style.display='none';
}

//显示
 function showXf(title,content)
{
	var oDiv=document.getElementById('float_layer');
	var oBtnMin=document.getElementById('btn_min');
	var oBtnClose=document.getElementById('btn_close');
	var oDivContent=oDiv.getElementsByTagName('div')[0];

	var iMaxHeight=0;

	var isIE6=window.navigator.userAgent.match(/MSIE 6/ig) && !window.navigator.userAgent.match(/MSIE 7|8/ig);

	oDiv.style.display='block';
	iMaxHeight=oDivContent.offsetHeight;
	if(iMaxHeight==0){
	 	iMaxHeight=180;
	}

    $('#xfcontent').html(content);
    $('#xfTitle').html(title);
	if(isIE6)
	{
		oDiv.style.position='absolute';
		repositionAbsolute();
		floatAddEvent(window, 'scroll', repositionAbsolute);
		floatAddEvent(window, 'resize', repositionAbsolute);
	}
	else
	{
		oDiv.style.position='fixed';
		repositionFixed();
		floatAddEvent(window, 'resize', repositionFixed);
	}

	oBtnMin.timer=null;
	oBtnMin.isMax=true;

	if(oBtnMin.className=='max'){
		oBtnMin.isMax=false;
	}

	oBtnMin.onclick=function ()
	{

		startMove
		(
			oDivContent, (this.isMax=!this.isMax)?iMaxHeight:0,
			function ()
			{
				oBtnMin.className=oBtnMin.className=='min'?'max':'min';
			}
		);
	};

	oBtnClose.onclick=function ()
	{
		oDiv.style.display='none';
	};
};

function startMove(obj, iTarget, fnCallBackEnd)
{
	if(obj.timer)
	{
		clearInterval(obj.timer);
	}
	obj.timer=setInterval
	(
		function ()
		{
			doMove(obj, iTarget, fnCallBackEnd);
		},30
	);
}

function doMove(obj, iTarget, fnCallBackEnd)
{
	var iSpeed=(iTarget-obj.offsetHeight)/8;

	if(obj.offsetHeight==iTarget)
	{
		clearInterval(obj.timer);
		obj.timer=null;
		if(fnCallBackEnd)
		{
			fnCallBackEnd();
		}
	}
	else
	{
		iSpeed=iSpeed>0?Math.ceil(iSpeed):Math.floor(iSpeed);
		obj.style.height=obj.offsetHeight+iSpeed+'px';

		((window.navigator.userAgent.match(/MSIE 6/ig) && window.navigator.userAgent.match(/MSIE 6/ig).length==2)?repositionAbsolute:repositionFixed)()
	}
}

function repositionAbsolute()
{
	var oDiv=document.getElementById('float_layer');
	var left=document.body.scrollLeft||document.documentElement.scrollLeft;
	var top=document.body.scrollTop||document.documentElement.scrollTop;
	var width=document.documentElement.clientWidth;
	var height=document.documentElement.clientHeight;

	oDiv.style.left=left+width-oDiv.offsetWidth+'px';
	oDiv.style.top=top+height-oDiv.offsetHeight+'px';
}

function repositionFixed()
{
	var oDiv=document.getElementById('float_layer');
	var width=document.documentElement.clientWidth;
	var height=document.documentElement.clientHeight;

	oDiv.style.left=width-oDiv.offsetWidth+'px';
	oDiv.style.top=height-oDiv.offsetHeight+'px';
}