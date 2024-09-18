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
		  final  Map <String, Object> response = (LinkedHashMap<String, Object>) objeto;
		  
		  final String text = getString(response, "respuesta");
		  BigDecimal time = getBigDecimal(response, "tiempoProceso");
		  BigDecimal confidence = getBigDecimal(response, "confianza");

	      return TranscriptionResponse.builder()
	                .text(text)
	                .time(time)
	                .confidence(confidence)
	                .build();
	   }
    }
    
    private static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (Objects.isNull(value) || !(value instanceof String)) {
            return null;
        }
        return String.valueOf(value);
    }

    private static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (Objects.isNull(value) || !(value instanceof Number)) {
            return null;
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
