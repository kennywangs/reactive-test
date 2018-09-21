package com.xxb.reactive.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionHandlers {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

	@ExceptionHandler(Exception.class)
	public Mono<ServerResponse> serverExceptionHandler(Exception ex) {
		logger.info("自定义异常处理-Exception");
		logger.error(ex.getMessage(),ex);
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
		         .contentType(MediaType.APPLICATION_JSON_UTF8)
		         .body(BodyInserters.fromObject("error"));
	}

}
