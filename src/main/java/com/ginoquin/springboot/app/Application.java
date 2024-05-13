package com.ginoquin.springboot.app;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ginoquin.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
    private RabbitAdmin rabbitAdmin;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();		
		rabbitAdmin.initialize();
	}

}
