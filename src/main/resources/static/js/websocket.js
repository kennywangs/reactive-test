define(function(require) {
	var ws = null;
	var $ = require('jquery');
	var wsUrl = 'ws://192.168.1.44:6677';
	var messageCallback = null;
	var connected = false;
	var heartbeatTimeout = 45 * 1000;
	var heartbeatTimeoutObj = null;
	var reconnectTimeout = 5 * 1000;
	var reconnectInterval = null;
	
	var heartCheck = function() {
		heartbeatTimeoutObj = setTimeout(function(){
            var ping = { "type": "ping" };
			ws.send(JSON.stringify(ping));
			console.log("发送心跳");
        }, heartbeatTimeout);
	}
	
	var init = function() {
		ws.onmessage = function (event) {
			messageCallback(event.data);
			clearTimeout(heartbeatTimeoutObj);
			heartCheck();
		};
			
		ws.onerror = function (event) {
			connected = false;
		};
		
		ws.onopen = function(event) {
			heartCheck();
			if(reconnectInterval != null) {
				clearInterval(reconnectInterval);
				reconnectInterval = null;
			}
			connected = true;
		};
	}
	
	var reconnect = function() {
		reconnectInterval = setInterval(function(){
			console.log("重连中...")
			ws = new WebSocket(wsUrl);
			init();
		}, reconnectTimeout);	
	}
	
	return {
		connect: function(userId, group, receiveMessageCallback) {
			messageCallback = receiveMessageCallback;
			var url = wsUrl += '?group=' + group;
			if(userId) {
				url += "&userId=" + userId;
			}
			wsUrl = url;
			ws = new WebSocket(url);
			ws.onclose = function (event) {
				reconnect();
			};	
			init();
		},
		
		send: function(message) {
			if(connected) {
				ws.send(message);
			} else {
				console.log("WebSocket未连接！");
			}
		}
	}
});