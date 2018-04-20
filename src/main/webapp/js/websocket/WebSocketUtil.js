var WebSocketUtil = function (obj,requrl) {
    var _this = this;
    this.timeout = parseInt(Math.random()*(360-30+1) + 30) * 1000;//30秒到4分钟随机，防止多个客户端同时心跳，导致被阻塞链接关闭。
    this.timeoutObj = null;
    this.socket = null;
    this.requesturl = null;
    this.initSocket = function () {
    	if(_this.socket == null || typeof(_this.socket)=='undefined'){
    		_this.getSocket();
    	}
 		//连接成功建立的回调方法
        this.socket.onopen = function (event) {
            _this.setMessageInnerHTML("open");
            _this.start();
        };
		//连接发生错误的回调方法
        this.socket.onerror = function () {
             _this.setMessageInnerHTML("error");
        };
        //接收到消息的回调方法
        this.socket.onmessage = this.socketMessage;
		//连接关闭的回调方法
        this.socket.onclose = function () {
            _this.setMessageInnerHTML("closed");
        };
		$(window).unload(function(){
			alert("获取到了页面要关闭的事件了！");
			_this.close();
		});
        console.log("-------websocket初始化完成执行回调函数-------当前心跳间隔："+_this.timeout);
    }
    this.send = function(message,callback){
        _this.sendMessage(message,callback);
    };
    this.close=function(){
        _this.socket.close();
    };
    this.setMessageInnerHTML =function(innerHTML){
        obj.innerHTML += innerHTML + '<br/>';
    };
    this.getSocket = function(){
      if(requrl == null || typeof(requrl) == 'undefined'){
	 	  alert('request url is null')
      }else{
      	  //判断当前浏览器是否支持WebSocket
	      if('WebSocket' in window){
	          _this.socket = new WebSocket(requrl);
	      }
	      else{
	          alert('Not support websocket')
	      }
      }
    };
    //接收到服务器的回复
    this.socketMessage = function (message) {
        _this.reset();
        var msg = message.data;
        console.info(msg);
        if(msg.indexOf('HEART_CONNECT')<0){
        	_this.setMessageInnerHTML(message.data);
        }
    };
    //向服务器发送信息
    this.sendMessage = function (message, callback) {
	    this.waitForConnection(function () {
            //var msgJson = JSON.stringify(message);
            if(_this.socket.readyState!==1){
    		_this.close();
	    	_this.getSocket();
	    	_this.initSocket();
    		}
            _this.socket.send(message);
            var suc = "send ok";
            if (typeof callback !== 'undefined') {
                callback(suc);
            }
        }, 1000);
    };
    this.waitForConnection = function (callback, interval) {
        if (_this.socket.readyState === 1) {
            callback();
        } else {
            setTimeout(function () {
                _this.waitForConnection(callback, interval);
            }, interval);
        }
    };
    this.start = function () {
        _this.timeoutObj = setTimeout(function () {
            /**
            var heartbeat = {
                type: "beat",
                msg: "HeartBeat"
            };
            **/
            var heartmessage = "HEART_CONNECT";
            _this.socket.send(heartmessage);
        }, _this.timeout)
    };
    this.reset = function () {
        clearTimeout(this.timeoutObj);
        _this.start();
    };
    _this.initSocket();
    return _this;
}