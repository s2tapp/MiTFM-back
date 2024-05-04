package com.ginoquin.springboot.app.models.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.ginoquin.springboot.app.models.response.TranscriptionResponse;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.translate.TranslateException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Wav2VecService {

	@Resource
	private Supplier<Predictor<Audio, String>> predictorProvider;

	public TranscriptionResponse predict(InputStream stream) throws IOException, ModelException, TranslateException  {

		// Crea una instancia de Audio desde el flujo de entrada
		final Audio audio = AudioFactory.newInstance().fromInputStream(stream);

		// Procesa la predicción
		try (var predictor = predictorProvider.get()) {
			// Realiza la predicción y obtén el texto transcribido
			String transcribedText = predictor.predict(audio);

			TranscriptionResponse transcriptionResponse = TranscriptionResponse.builder().text(transcribedText).build();
			return transcriptionResponse;
		}
	}

}
