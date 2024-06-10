package com.ginoquin.springboot.app.controllers;

import java.net.MalformedURLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ginoquin.springboot.app.models.entity.EstadoEnum;
import com.ginoquin.springboot.app.models.entity.Peticion;
import com.ginoquin.springboot.app.models.entity.ResultadoEnum;
import com.ginoquin.springboot.app.models.entity.TechnologyEnum;
import com.ginoquin.springboot.app.models.request.TranscriptionRequest;
import com.ginoquin.springboot.app.models.response.TranscriptionResponse;
import com.ginoquin.springboot.app.models.service.ProducerService;
import com.ginoquin.springboot.app.models.service.IPeticionService;
import com.ginoquin.springboot.app.models.service.UploadFileServiceImpl;

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

	@Autowired
	private final ProducerService producerService;
	
	@Autowired 
	private UploadFileServiceImpl fileStorageService;

	@Autowired
	private final IPeticionService peticionService;

	@Operation(summary = "Transcripción de audio")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transcripción exitosa"),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta") })
	@PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TranscriptionResponse createTranscription(@RequestBody TranscriptionRequest transcriptionRequest) {
		log.info("Petición recibida en createTranscription: " + transcriptionRequest);
		
		TranscriptionResponse transcriptionResponse = null;
		Peticion peticion = null;
		String nombreFichero = null;
		String tecnologia = null;
		try {
			
			nombreFichero = fileStorageService.save(transcriptionRequest.getFile());
			tecnologia = transcriptionRequest.getTechnology();
	        
	        peticion = Peticion.builder().estado(EstadoEnum.PP).resultado(ResultadoEnum.OK)
	        		.nombreFichero(nombreFichero)
					.fecha(new Date())
					.technology(TechnologyEnum.fromCodigo(tecnologia))
					.build();

			peticionService.savePeticion(peticion);
			
			transcriptionResponse = producerService.sendAudioAndText(nombreFichero, tecnologia);
			
			peticion.setEstado(EstadoEnum.P);			
			peticion.setTranscripcion(transcriptionResponse.getText());
			peticion.setTiempoProceso(transcriptionResponse.getTime());
			peticion.setConfianza(transcriptionResponse.getConfidence());
			
			peticionService.savePeticion(peticion);

			return transcriptionResponse;
	        
		} catch (Exception e) {
			log.error("Error al procesar audio.", e);
			peticion = Peticion.builder().estado(EstadoEnum.P).resultado(ResultadoEnum.E)
					.nombreFichero(nombreFichero)
					.fecha(new Date()).build();
			peticionService.savePeticion(peticion);
			return transcriptionResponse = TranscriptionResponse.builder().text("Error al procesar audio.").build();
		}
		
	}

	@Operation(summary = "Descargar archivo de audio")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Descarga exitosa"),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta") })
	@GetMapping(value = "/files/{fileName:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
		log.info("Petición recibida en getFile: " + fileName);
		Resource resource;
		try {
			resource = fileStorageService.load(fileName);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (MalformedURLException e) {
			log.error("Error al descargar archivo.", e);
			return ResponseEntity.badRequest().build();
		}
	}

}
