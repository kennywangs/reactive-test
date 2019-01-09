package com.xxb.reactive.test.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import com.xxb.reactive.test.entity.TestEvent;

import reactor.core.publisher.Flux;

public interface TestEventRepository extends ReactiveMongoRepository<TestEvent, ObjectId> {
	
	@Tailable
//	Flux<TestEvent> findBy();
	public Flux<TestEvent> findWithTailableCursorBy();

}
