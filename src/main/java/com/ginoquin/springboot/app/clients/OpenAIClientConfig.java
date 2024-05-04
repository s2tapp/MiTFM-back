package com.ginoquin.springboot.app.clients;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Indexed;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import lombok.Data;

@Configuration
@ConfigurationProperties
@Indexed
@Data
public class OpenAIClientConfig {

	@Value("${openai-service.http-client.read-timeout}")
	private int readTimeout;

	@Value("${openai-service.http-client.connect-timeout}")
	private int connectTimeout;

	@Value("${openai-service.api-key}")
	private String apiKey;

	@Value("${openai-service.gpt-model}")
	private String model;

	@Value("${openai-service.audio-model}")
	private String audioModel;

	@Bean
	Request.Options options() {
		return new Request.Options(getConnectTimeout(), TimeUnit.MILLISECONDS, getReadTimeout(), TimeUnit.MILLISECONDS,
				false);
	}

	@Bean
	Logger.Level feignLogger() {
		return Logger.Level.FULL;
	}

	@Bean
	Retryer retryer() {
		return new Retryer.Default();
	}

	@Bean
	RequestInterceptor apiKeyInterceptor() {
		return request -> request.header("Authorization", "Bearer " + apiKey);
	}
}
