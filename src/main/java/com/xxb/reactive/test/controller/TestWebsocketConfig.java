package com.xxb.reactive.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import reactor.core.publisher.UnicastProcessor;

@Configuration
public class TestWebsocketConfig {
	@Bean
	public HandlerMapping webSocketMapping(TestWebsocketHandler handler) {
		final Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/testws", handler);

		/**
		 * SimpleUrlHandlerMapping 指定了 WebSocket 的路由配置
		 */
		final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
		mapping.setUrlMap(map);
		return mapping;
	}
	
	@Bean
	public TestWebsocketHandler testWebsocketHandler() {
		return new TestWebsocketHandler();
	}

	/**
	 * WebSocketHandlerAdapter 负责将 EchoHandler 处理类适配到 WebFlux 容器中
	 * 
	 * @return
	 */
	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
	
//	@Bean
//	public UnicastProcessor<String> messagePublisher(){
//		return UnicastProcessor.create();
//	}
}
