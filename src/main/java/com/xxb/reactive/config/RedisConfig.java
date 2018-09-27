package com.xxb.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {
	
	@Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory){
        return new ReactiveRedisTemplate<String, String>(connectionFactory, RedisSerializationContext.string());
    }

	@Bean
    public ReactiveRedisConnection connection(ReactiveRedisConnectionFactory connectionFactory){
        return connectionFactory.getReactiveConnection();
    }
}
