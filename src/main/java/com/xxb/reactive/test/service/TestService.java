package com.xxb.reactive.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxb.reactive.test.entity.Test;
import com.xxb.reactive.test.repository.TestRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TestService {

	@Autowired
	private TestRepository testRepo;
	
	public Mono<Test> findByName(String name) {
        return testRepo.findByName(name);
    }
	
	public Flux<Test> findAll() {
        return testRepo.findAll();
    }
}
