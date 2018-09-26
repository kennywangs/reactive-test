package com.xxb.reactive.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

//@Component
public class TestWebsocketHandler implements WebSocketHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(TestWebsocketHandler.class);

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		return webSocketSession.send(webSocketSession.receive().map(msg -> {
			// body 貌似只能拿一次，会被清空，
			String ret = "data: " + msg.getPayloadAsText();
			logger.debug(ret);
			return webSocketSession.textMessage(ret);
		}));
	}

}
