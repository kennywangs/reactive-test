package com.xxb.reactive.test.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.xxb.reactive.test.entity.Test;

import reactor.core.publisher.Mono;

public interface TestRepository extends ReactiveMongoRepository<Test, ObjectId> {
	
	public Mono<Test> findByName(String name);
	
	public Mono<Long> deleteByName(String name);

}
