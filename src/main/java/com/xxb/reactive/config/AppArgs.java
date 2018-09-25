package com.xxb.reactive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class AppArgs {
	
	@Autowired
	public AppArgs(ApplicationArguments args) {
		System.out.println("----------ApplicationArguments-------------");
		args.getNonOptionArgs().forEach(System.out::println);
//		args.getOptionNames().forEach(System.out::println);
	}

}
