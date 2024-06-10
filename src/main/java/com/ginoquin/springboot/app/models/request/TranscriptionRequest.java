package com.ginoquin.springboot.app.models.request;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptionRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "se debe indicar una tecnología")
    @Schema(description = "Tecnología usada para la transcripción", example = "Whisper", allowableValues = {"Whisper", "Wav2vec"})
    private String technology;
    
    @NotNull(message = "Se debe indicar el fichero de audio")
    @Schema(description = "Fichero de audio para la transcripción")
	private MultipartFile file;
}
