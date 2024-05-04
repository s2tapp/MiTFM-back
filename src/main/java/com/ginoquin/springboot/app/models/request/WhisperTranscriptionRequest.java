package com.ginoquin.springboot.app.models.request;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhisperTranscriptionRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
	private String model;
	private MultipartFile file;
}
