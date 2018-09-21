package com.xxb.reactive;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.alibaba.fastjson.JSON;
import com.xxb.reactive.test.entity.TestEvent;

import reactor.core.publisher.Flux;

public class WebClientTest {

	public static void main(String[] args) throws Exception {
		WebClientTest test = new WebClientTest();
		// test.webClientTest2();
		test.sendMsgs();
	}

	public void webClientTest2() throws InterruptedException {
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build(); // 1
		webClient.get().uri("/reactor/test/").accept(MediaType.APPLICATION_STREAM_JSON) // 2
				.exchange() // 3
				.flatMapMany(response -> response.bodyToFlux(String.class)) // 4
				.doOnNext(System.out::println) // 5
				.blockLast(); // 6
	}

	public void receiveMsg() throws InterruptedException {
		WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
		webClient.get().uri("/reactor/test/event").accept(MediaType.APPLICATION_STREAM_JSON).exchange()
				.flatMapMany(response -> response.bodyToFlux(String.class)).doOnNext(i -> {
					System.out.println("receive message::" + i);
				}).blockLast();
	}

	public void sendMsgs() throws InterruptedException {
		Flux.interval(Duration.ofSeconds(1)).map(l -> {
			TestEvent e = new TestEvent();
			e.setCreateAt(new Date());
			e.setMessage("message-" + l);
			sendTestMsg(e);
			return e;
		}).take(100).blockLast();
//		Thread.sleep(TimeUnit.SECONDS.toMillis(10));
		// WebClient.create("http://localhost:8080")
		// .post().uri("/reactor/test/event")
		// .contentType(MediaType.APPLICATION_STREAM_JSON)
		// .body(eventFlux, TestEvent.class)
		// .retrieve()
		// .bodyToMono(Void.class)
		// .block();
	}
	
	private void sendTestMsg(TestEvent e) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:8080/reactor/test/event");
		httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		StringEntity s = new StringEntity(JSON.toJSONString(e),"UTF-8");
		httpPost.setEntity(s);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("send message::ok");
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeHttpClient(client, response);
		}
		System.out.println("send message::error");
	}
	
	private static void closeHttpClient(CloseableHttpClient httpClient, CloseableHttpResponse response) {
		try {
			if (response!=null) response.close();
			if (httpClient!=null) httpClient.close();
		} catch (IOException e) {
			System.out.println("closeHttpClient error:");
		}
	}

	public void sendMsg(TestEvent e) {
		// Flux<TestEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
		// .map(l -> {
		// TestEvent e = new TestEvent();
		// e.setCreateAt(new Date());
		// e.setMessage("message-" + l);
		// return e;
		// }).take(5);
		// TestEvent e = new TestEvent();
		// e.setCreateAt(new Date());
		// e.setMessage("message-" + System.currentTimeMillis());
		WebClient.create("http://localhost:8080").post().uri("/reactor/test/event")
				.contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(e).retrieve().bodyToMono(Void.class).block();
	}

}
