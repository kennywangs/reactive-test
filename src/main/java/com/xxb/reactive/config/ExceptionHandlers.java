package com.xxb.reactive.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.xxb.reactive.commons.MessResult;

@RestControllerAdvice
public class ExceptionHandlers {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessResult serverExceptionHandler(Exception ex) {
		logger.info("自定义异常处理-Exception");
//		logger.error(ex.getMessage(),ex);
		return new MessResult(false,ex.getMessage());
	}

}
