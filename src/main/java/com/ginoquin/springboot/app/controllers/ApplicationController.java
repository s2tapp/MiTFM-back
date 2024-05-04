package com.ginoquin.springboot.app.controllers;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ginoquin.springboot.app.models.entity.EstadoEnum;
import com.ginoquin.springboot.app.models.entity.Peticion;
import com.ginoquin.springboot.app.models.entity.ResultadoEnum;
import com.ginoquin.springboot.app.models.request.TranscriptionRequest;
import com.ginoquin.springboot.app.models.response.TranscriptionResponse;
import com.ginoquin.springboot.app.models.response.WhisperTranscriptionResponse;
import com.ginoquin.springboot.app.models.service.IPeticionService;
import com.ginoquin.springboot.app.models.service.OpenAIClientService;
import com.ginoquin.springboot.app.models.service.Wav2VecService;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@Slf4j
@Tag(name = "API de Transcripción de Audio", description = "API para servicios de transcripción de audio a texto")
@CrossOrigin(origins = "*")
public class ApplicationController {

	private final OpenAIClientService openAIClientService;

	@Autowired
	private final Wav2VecService wav2VecService;

	@Autowired
	private final IPeticionService peticionService;

	@Operation(summary = "Transcripción de audio por openai")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transcripción exitosa"),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta") })
	@PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public WhisperTranscriptionResponse createTranscription(@RequestBody TranscriptionRequest transcriptionRequest) {
		log.info("Petición recibida en createTranscription: " + transcriptionRequest);
		return openAIClientService.createTranscription(transcriptionRequest);
	}

	@Operation(summary = "Transcripción de audio por wav2vec")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transcripción exitosa"),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta") })
	@PostMapping(value = "/transcription2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TranscriptionResponse createTranscription2(@RequestBody TranscriptionRequest transcriptionRequest) {
		log.info("Petición recibida en createTranscription2: " + transcriptionRequest);

		TranscriptionResponse transcriptionResponse = null;
		Peticion peticion = null;

		try {
			Instant inicio = Instant.now();

			transcriptionResponse = wav2VecService.predict(transcriptionRequest.getFile().getInputStream());

			Instant fin = Instant.now();

			Duration duracion = Duration.between(inicio, fin);

			long duracionMillis = duracion.toMillis();

			peticion = Peticion.builder().estado(EstadoEnum.P).resultado(ResultadoEnum.OK)
					.transcripción(transcriptionResponse.getText()).tiempoProceso(Long.valueOf(duracionMillis)).fecha(new Date())
					.build();

			peticionService.savePeticion(peticion);

			return transcriptionResponse;

		} catch (IOException e) {
			log.error("Error al leer el flujo de entrada.", e);
			transcriptionResponse = TranscriptionResponse.builder().text("Error al leer el flujo de entrada.").build();
		} catch (ModelException e) {
			log.error("Error durante la predicción.", e);
			transcriptionResponse = TranscriptionResponse.builder().text("Error durante la predicción.").build();
		} catch (TranslateException e) {
			log.error("Error durante la traducción.", e);
			transcriptionResponse = TranscriptionResponse.builder().text("Error durante la traducción.").build();
		}

		peticion = Peticion.builder().estado(EstadoEnum.P).resultado(ResultadoEnum.E)
				.transcripción(transcriptionResponse.getText()).tiempoProceso(null).fecha(new Date()).build();

		return transcriptionResponse;
	}

}
