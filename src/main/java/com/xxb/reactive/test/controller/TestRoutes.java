package com.xxb.reactive.test.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
//@EnableWebFlux
public class TestRoutes {

	@Bean
	public RouterFunction<ServerResponse> testRouteFunc() {
		System.out.println("init test routes-------------");
		return RouterFunctions.route(GET("/test/routes").and(accept(MediaType.TEXT_PLAIN)), request -> {
//			return handleRequest(request);
			return ok().body(BodyInserters.fromObject("test routes"));
		});
	}

	public Mono<ServerResponse> handleRequest(ServerRequest request) {
		return ok().body(BodyInserters.fromObject("test routes"));
	}

}
