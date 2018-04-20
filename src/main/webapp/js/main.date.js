/**
 * 日期处理
 * kyoxue 2017.11.18
 */

/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，
 * 毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 * example：
 * var format_date = (new Date()).format("yyyy-MM-dd hh:mm:ss.S");
 * result ===>  2017-11-18 21:30:33.301
 */
Date.prototype.format = function(fmt)
{ 
  var o = {
    "M+" : this.getMonth()+1,                 //月份
    "d+" : this.getDate(),                    //日
    "h+" : this.getHours(),                   //小时
    "m+" : this.getMinutes(),                 //分
    "s+" : this.getSeconds(),                 //秒
    "q+" : Math.floor((this.getMonth()+3)/3), //季度
    "S"  : this.getMilliseconds()             //毫秒
  };
  if(/(y+)/.test(fmt))
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)
    if(new RegExp("("+ k +")").test(fmt))
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
  return fmt;
}
/**
 * 工作区域显示当前动态实时的日期时间
 * 在页面加载时，递归调用$(function(){...})
 * setInterval('getNow',1000,$("#datetime"));
 */
var days=new  Array ("日", "一", "二", "三", "四", "五", "六");
function getNow(showID) {
  var currentDT = new Date();
  var y,m,date,day,hs,ms,ss,theDateStr;
  y = currentDT.getFullYear(); //四位整数表示的年份
  m = currentDT.getMonth()+1; //月
  date = currentDT.getDate(); //日
  day = currentDT.getDay(); //星期
  hs = currentDT.getHours(); //时
  ms = currentDT.getMinutes(); //分
  ss = currentDT.getSeconds(); //秒
  theDateStr = y+"年"+  (m>=10?m:"0"+m) +"月"+(date>=10?date:"0"+date)+"日 星期"+days[day]+" "+(hs>=10?hs:"0"+hs)+":"+(ms>=10?ms:"0"+ms)+":"+(ss>=10?ss:"0"+ss);
  showID.html(theDateStr);
}
$.datepicker._gotoToday = function (id) {
	var target = $(id);
	var inst = this._getInst(target[0]);
	if (this._get(inst, 'gotoCurrent') && inst.currentDay) {
		inst.selectedDay = inst.currentDay;
		inst.drawMonth = inst.selectedMonth = inst.currentMonth;
		inst.drawYear = inst.selectedYear = inst.currentYear;
	}
	else {
		var date = new Date();
		inst.selectedDay = date.getDate();
		inst.drawMonth = inst.selectedMonth = date.getMonth();
		inst.drawYear = inst.selectedYear = date.getFullYear();
		this._setDateDatepicker(target, date);
		this._selectDate(id, this._getDateDatepicker(target));
	}
	this._notifyChange(inst);
	this._adjustDate(target);
}