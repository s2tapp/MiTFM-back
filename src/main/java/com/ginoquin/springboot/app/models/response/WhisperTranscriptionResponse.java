package com.ginoquin.springboot.app.models.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WhisperTranscriptionResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
	private String text;
}
