package com.ginoquin.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	
	// Definir el directorio donde se guardará el archivo descargado
   private static final Path UPLOADS_FOLDER = Paths.get("/tmp/springboot-data/");

	@Override
	public Resource load(String filename) throws MalformedURLException {
		 Path filePath = getPath(filename);
         Resource resource = new UrlResource(filePath.toUri());
         
         if(!resource.exists() || !resource.isReadable()) {
      	   throw new RuntimeException("Error: no se puede cargar el fichero: " + filePath.toString());
         }
         return resource;
	}

	@Override
	public String save(MultipartFile file) throws IOException {
		
	   String fileName = StringUtils.cleanPath(file.getOriginalFilename());	
		
	   long milliseconds = Instant.now().toEpochMilli();

       String extension = fileName.substring(fileName.lastIndexOf("."));
       String uniqueFileName = UUID.randomUUID().toString() + "_" + milliseconds + extension;

	   Path filePath = getPath(uniqueFileName);
	   try (InputStream in = file.getInputStream()) {
		   Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
	   }
		
		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		 Path filePath = getPath(filename);
	       File file = filePath.toFile();
	       if(file.exists() && file.canRead()) {
	    	  if(file.delete()) {
	        	  return true;
	    	  }
	       }       
	       return false;     
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(UPLOADS_FOLDER.toFile());
		
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(UPLOADS_FOLDER);		
	}
	
	public Path getPath(String filename) {
		return UPLOADS_FOLDER.resolve(filename).toAbsolutePath();
	}
	
}
