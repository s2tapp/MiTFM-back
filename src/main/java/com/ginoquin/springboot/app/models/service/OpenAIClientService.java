package com.ginoquin.springboot.app.models.service;

import org.springframework.stereotype.Service;

import com.ginoquin.springboot.app.clients.OpenAIClient;
import com.ginoquin.springboot.app.clients.OpenAIClientConfig;
import com.ginoquin.springboot.app.models.request.TranscriptionRequest;
import com.ginoquin.springboot.app.models.request.WhisperTranscriptionRequest;
import com.ginoquin.springboot.app.models.response.WhisperTranscriptionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAIClientService {

	private final OpenAIClient openAIClient;
	private final OpenAIClientConfig openAIClientConfig;

	public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest) {
		WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
				.model(openAIClientConfig.getAudioModel()).file(transcriptionRequest.getFile()).build();
		return openAIClient.createTranscription(whisperTranscriptionRequest);
	}

}
