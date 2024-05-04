package com.ginoquin.springboot.app.clients;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.translator.SpeechRecognitionTranslatorFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Wav2VecConfig {

	@Bean
	ZooModel<Audio, String> loadModel() throws IOException, ModelException, TranslateException {
		String url = "https://resources.djl.ai/test-models/pytorch/wav2vec2.zip";

		// Definir el directorio donde se guardará el archivo descargado
        String destinationDirectory = "/tmp/springboot_data/";

        // Verificar si el directorio ya existe, si no, crearlo
        File directory = new File(destinationDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Definir la ruta completa del archivo descargado
        String destinationPath = destinationDirectory.concat("wav2vec2.zip");
        
        File file = new File(destinationPath);
        if (!file.exists()) {
            // Si el archivo no existe, descargarlo
            downloadFile(url, destinationPath, destinationDirectory);
        } else {
            log.info("El archivo ya existe en la ubicación especificada.");
        }
        
		Criteria<Audio, String> criteria = Criteria.builder().setTypes(Audio.class, String.class)
				.optModelPath(Path.of(destinationDirectory))
				.optTranslatorFactory(new SpeechRecognitionTranslatorFactory()).optModelName("wav2vec2.ptl")
				.optEngine("PyTorch").build();

		return criteria.loadModel();
	}

	@Bean
	Supplier<Predictor<Audio, String>> predictorProvider(ZooModel<Audio, String> model) {
		return model::newPredictor;
	}

	private static void downloadFile(String url, String destinationPath, String destinationDirectory) throws IOException {
		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
			byte[] dataBuffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		}
        
        unzipFile(destinationPath, destinationDirectory);
	}
	
	// Método para descomprimir un archivo ZIP
    private static void unzipFile(String zipFilePath, String destinationDirectory) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = destinationDirectory + entry.getName();
                if (!entry.isDirectory()) {
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }		

}
