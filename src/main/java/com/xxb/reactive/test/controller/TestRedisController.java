package com.xxb.reactive.test.controller;

import java.nio.ByteBuffer;
import java.util.Date;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xxb.reactive.commons.MessResult;
import com.xxb.reactive.test.entity.Test;
import com.xxb.reactive.test.repository.TestRepository;

import reactor.core.publisher.Mono;

@RestController
public class TestRedisController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestRedisController.class);
	
	@Autowired
	private TestRepository testRepo;
	
	@Autowired
    private ReactiveRedisTemplate<String,String> reactiveRedisTemplate;
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ReactiveRedisConnection connection;
	
	@PostMapping(path="/test/redis/{name}")
	public Mono<MessResult> addTest(@PathVariable("name") String name) {
		Mono<MessResult> mono = testRepo.findByName(name).map(test->{
			logger.info(test.get_id());
			test.setCreateAt(new Date());
			MessResult ret = new MessResult(true,"exits",test);
			if (redisTemplate.opsForValue().setIfAbsent(test.get_id(), JSON.toJSONString(test))) {
				ret.setMsg("added");
			}
			// 异步执行添加
//			operations.opsForValue().setIfAbsent(test.get_id(), JSON.toJSONString(test)).subscribe(i->{logger.info(i.toString())});
			return ret;
		}).switchIfEmpty(Mono.just(new MessResult(false,"data not found")));
//		mono.subscribe(test->{
//			operations.opsForValue().set(test.get_id(), JSON.toJSONString(test)).subscribe(i->{logger.info(i.toString());});
//		});
		return mono;
	}
	
	@GetMapping(path="/test/redis")
	public void test(){
//		reactiveRedisTemplate.opsForSet().add("testkey", "小皮球").subscribe(System.out::println);
		connection.stringCommands().set(ByteBuffer.wrap("testkey".getBytes()), ByteBuffer.wrap("testval".getBytes())).subscribe(System.out::println);
	}
	
	@GetMapping(path="/test/redis/{id}")
	public Mono<MessResult> getTest(@PathVariable("id") String id) {
		MessResult ret = new MessResult(true,null);
		Mono<MessResult> mono = reactiveRedisTemplate.opsForValue().get(id).map(val->{
				ret.setMsg("found in redis");
				return JSON.parseObject(val, Test.class);
			})
			.switchIfEmpty(testRepo.findById(new ObjectId(id)).doOnNext(i->{ret.setMsg("found in db");}))
			.map(test->{
				ret.setData(test);
				return ret;
			})
			.switchIfEmpty(Mono.just(new MessResult(false,"data not found")));
		return mono;
	}

}
