package com.ginoquin.springboot.app.models.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ginoquin.springboot.app.models.response.TranscriptionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {
	
	@Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.routing.key}")
    private String routingKey;
	
	@Autowired
    private RabbitTemplate rabbitTemplate;

    @SuppressWarnings("unchecked")
	public TranscriptionResponse sendAudioAndText(String audioName, String technology) throws Exception {
		Map<String, String> message = new HashMap<>();
		message.put("audioName", audioName);
		message.put("technology", technology);		
		
	   log.info("Audio y texto enviados a RabbitMQ.");
	   Object objeto = rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
	   log.info("transcripción recibida de servidor transcripción.");
	   
	   if(Objects.isNull(objeto)) {
		  return TranscriptionResponse.builder().text("Se ha superado el tiempo de respuesta.")
				  .time(null).build();
	   } else {
		  Map <String, Object> response = (LinkedHashMap<String, Object>) objeto;
		  
		  return TranscriptionResponse.builder().text(String.valueOf(response.get("respuesta")))
				  .time(new BigDecimal(String.valueOf(response.get("tiempoProceso"))))
				  .confidence(new BigDecimal(String.valueOf(response.get("confianza")))).build();
	   }
    }

}
