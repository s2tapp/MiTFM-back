package com.ginoquin.springboot.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.ginoquin.springboot.app.models.response.TranscriptionResponse;

@ControllerAdvice
public class FileUploadExceptionAdvice {
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<TranscriptionResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
		
		TranscriptionResponse transcriptionResponse = TranscriptionResponse.builder().text("El archivo excede el tamaño máximo permitido de 2MB.").build();
		
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(transcriptionResponse);
    }

}
