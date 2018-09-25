package com.xxb.reactive.config;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnderTowServer {
	
	@Bean
	public CommandLineRunner startup() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				System.out.println("startup");
				Stream.of(args).forEach(System.out::println);
//				factory.addBuilderCustomizers(customizers);
//				System.out.println(undertow.getXnio().getName());
			}
		};
		
	}
}
