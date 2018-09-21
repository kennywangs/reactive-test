package com.xxb.reactive;

import java.util.TimeZone;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xxb.reactive" })
public class App {
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
//		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
	}

	public static void main(String[] args) {
		Stream.of(args).forEach(arg -> {
			logger.info("command arg:{}",arg);
		});
		new SpringApplicationBuilder(App.class).run(args);
	}

}
