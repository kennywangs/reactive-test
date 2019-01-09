package com.xxb.reactive.test.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.xxb.reactive.test.entity.Message;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

//@Component
public class TestWebsocketHandler implements WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(TestWebsocketHandler.class);

	private ConcurrentHashMap<String, UnicastProcessor<Message>> senders;
	
	private ConcurrentHashMap<String, String> heartbeatStatus;

	public TestWebsocketHandler() {
		senders = new ConcurrentHashMap<String, UnicastProcessor<Message>>();
		heartbeatStatus = new ConcurrentHashMap<String, String>();
	}

	@Override
	public Mono<Void> handle(WebSocketSession webSocketSession) {
		logger.info("ws sessionId:{}", webSocketSession.getId());
		WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(webSocketSession, senders);
		UnicastProcessor<Message> sender = UnicastProcessor.create();
		Flux<Message> msgFlux = sender.publish().autoConnect();
		msgFlux.doOnComplete(() -> {
			logger.info("receive complete");
		});
		senders.putIfAbsent(webSocketSession.getId(), sender);
		webSocketSession.receive().doOnNext(msg -> {
			
		}).subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
		
		new Thread(()->{
			AtomicBoolean running = new AtomicBoolean(true);
			heartbeatStatus.put(webSocketSession.getId(), "pong");
			while(running.get()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//超时停止
				if (heartbeatStatus.get(webSocketSession.getId()).equals("ping")) {
					webSocketSession.close();
					sender.dispose();
					logger.info("id:{},{}",webSocketSession.getId(),sender.isTerminated());
					heartbeatStatus.remove(webSocketSession.getId());
					senders.remove(webSocketSession.getId());
					break;
				}
				logger.debug("send ping");
				heartbeatStatus.put(webSocketSession.getId(), "ping");
				Message msg = new Message();
				msg.setEvent("ping");
				msg.setPayload("ping");
				sender.onNext(msg);
			}
		})
		.start();;
		
//		Flux<WebSocketMessage> hbFlux = Flux.interval(Duration.ofSeconds(1)).map(l->{
//			logger.debug("send heartbeat");
//			String ret = l.toString();
//			return webSocketSession.pingMessage(dbf -> {DataBuffer db = dbf.allocateBuffer(ret.length());return db.write(ret.getBytes());});
//		});
		
		return webSocketSession.send(Flux.from(msgFlux).map(msg -> handleMsg(webSocketSession, msg))).then();
	}

	private WebSocketMessage handleMsg(WebSocketSession webSocketSession, Message msg) {
		if (msg.getEvent().equals("ping")) {
			String payload = msg.getPayload().toString();
			return webSocketSession.pingMessage(dbf -> {DataBuffer db = dbf.allocateBuffer(payload.length());return db.write(payload.getBytes());});
		}
		return webSocketSession.textMessage(JSON.toJSONString(msg));
	}

	class WebSocketMessageSubscriber {
		
//		private Optional<Message> lastReceivedEvent = Optional.empty();
		private WebSocketSession webSocketSession;
		private ConcurrentHashMap<String, UnicastProcessor<Message>> senders;
		
		public WebSocketMessageSubscriber(WebSocketSession webSocketSession, ConcurrentHashMap<String, UnicastProcessor<Message>> senders) {
			this.webSocketSession = webSocketSession;
			this.senders = senders;
		}

		public void onNext(WebSocketMessage msg) {
//			lastReceivedEvent = Optional.of(event);
			// body 貌似只能拿一次，会被清空，
			String payload = msg.getPayloadAsText();
			if (msg.getType().equals(WebSocketMessage.Type.PONG)){
				logger.debug("get pong: " + payload);
				heartbeatStatus.put(webSocketSession.getId(), "pong");
				return;
			}
			logger.debug("payload: " + payload);
			Message message = JSON.parseObject(payload, Message.class);
			onMessage(webSocketSession, message);
		}

		public void onError(Throwable e) {
			logger.error("WebSocketMessageSubscriber error",e);
		}

		public void onComplete() {
			logger.info("WebSocketMessageSubscriber complete");
//			UnicastProcessor<Message> publisher = senders.get(webSocketSession.getId());
//			publisher.doOnTerminate(()->{logger.info("on Terminate");});
//			webSocketSession.close();
//			publisher.dispose();
//			logger.info("id:{},{}",webSocketSession.getId(),publisher.isDisposed());
			senders.remove(webSocketSession.getId());
			// lastReceivedEvent.ifPresent(event -> eventPublisher.onNext(
			// Event.type(USER_LEFT)
			// .withPayload()
			// .user(event.getUser())
			// .build()));
		}
		
		private void onMessage(WebSocketSession webSocketSession, Message message) {
			Message res = new Message();
			switch (message.getEvent()) {
			case "userlist":
				res.setEvent("userlist");
				res.setFrom(webSocketSession.getId());
				res.setPayload(senders.keys());
				senders.get(webSocketSession.getId()).onNext(res);
				break;
			case "message":
				res.setEvent("message");
				res.setPayload(message.getPayload());
				logger.info("{}",senders.get(message.getTo()).isTerminated());
				senders.get(message.getTo()).onNext(res);
				break;
			default:
				break;
			}
		}

	}
}
