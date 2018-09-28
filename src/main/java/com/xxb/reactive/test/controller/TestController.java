package com.xxb.reactive.test.controller;

import java.time.Duration;
import java.util.Date;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xxb.reactive.test.entity.Test;
import com.xxb.reactive.test.entity.TestEvent;
import com.xxb.reactive.test.repository.TestEventRepository;
import com.xxb.reactive.test.repository.TestRepository;
import com.xxb.reactive.test.service.TestService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private TestService testService;

	@Autowired
	private TestRepository testRepo;

	@Autowired
	private TestEventRepository eventRepo;

	@GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> hello() {
		JSONObject json = new JSONObject();
		json.put("date", new Date());
		Mono<String> mono = Mono.just(JSON.toJSONString(json, SerializerFeature.WriteDateUseDateFormat));
		return mono;
	}

	@GetMapping(value = "/hellosse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<TestEvent> testsse() {
		JSONObject json = new JSONObject();
		json.put("date", new Date());
		Flux<TestEvent> eventFlux = Flux.interval(Duration.ofSeconds(1)).map(l -> {
			TestEvent e = new TestEvent();
			e.setCreateAt(new Date());
			e.setMessage("message-" + l);
			// return JSON.toJSONString(e, SerializerFeature.WriteDateUseDateFormat);
			return e;
//		}).take(5);
		}).onErrorResume(e->{logger.info("------error-----");return Flux.error(e);});
		return eventFlux;
	}

	@PostMapping(value = "/reactor/test", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Mono<Test> save(@RequestBody Test test) {
		return this.testRepo.save(test);
	}

	@DeleteMapping("/reactor/test/name/{name}")
	public Mono<Long> deleteByUsername(@PathVariable String name) {
		return this.testRepo.deleteByName(name);
	}

	@DeleteMapping("/reactor/test/{id}")
	public Mono<Void> deleteById(@PathVariable String id) {
		return this.testRepo.deleteById(new ObjectId(id));
	}

	@GetMapping("/reactor/test/name/{name}")
	public Mono<Test> findByUsername(@PathVariable String name) {
		return this.testService.findByName(name);
	}

	@GetMapping(value = "/reactor/test/", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Test> findAll() {
		return this.testService.findAll().delayElements(Duration.ofSeconds(2));
	}
	
//	@PostMapping(path = "/reactor/test/event", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public Mono<Void> loadEvents(@RequestBody TestEvent event) {
////		Flux<TestEvent> es = events.map(s->{
////			logger.info(s);
////			return JSON.parseObject(s, TestEvent.class);
////		});
//		logger.info(JSON.toJSONString(event));
//		return this.eventRepo.insert(event).then();
//	}

//	@PostMapping(path = "/reactor/test/event", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
	@PostMapping(path = "/reactor/test/event")
	public Mono<Void> loadEvents(@RequestBody Flux<TestEvent> events) {
		return this.eventRepo.insert(events).then();
	}

	@GetMapping(path = "/reactor/test/event", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<TestEvent> getEvents() {
//		return this.eventRepo.findBy().doOnComplete(()->{logger.info("finished------");});
		return this.eventRepo.findWithTailableCursorBy().doOnComplete(()->{logger.info("finished------");});
	}

}
