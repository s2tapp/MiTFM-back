package com.ginoquin.springboot.app.models.response;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranscriptionResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Schema(description = "Texto resultante de la transcripción")
    private String text;

    @Schema(description = "Tiempo de procesamiento en segundos")
    private BigDecimal time;

    @Schema(description = "Confianza de la transcripción")
    private BigDecimal confidence;
	
}
