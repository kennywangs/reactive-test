package com.xxb.reactive.test.controller;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Mono;

//@Component
public class TestWebsocketHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		return webSocketSession.send(webSocketSession.receive().map(msg -> {
			// body 貌似只能拿一次，会被清空，
			String ret = "data: " + msg.getPayloadAsText();
			System.out.println(ret);
			return webSocketSession.textMessage(ret);
		}));
	}

}
