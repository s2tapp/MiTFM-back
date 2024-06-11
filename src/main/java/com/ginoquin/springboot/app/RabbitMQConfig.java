package com.ginoquin.springboot.app;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	@Value("${rabbitmq.queue.name}")
    private  String queue;

    @Value("${rabbitmq.queue.exchange}")
    private  String exchange;

    @Value("${rabbitmq.queue.routing.key}")
    private  String routingKey;
    
    // it will create automatic queue, no need to create manually
    @Bean
    Queue queue(){
        return new Queue(this.queue, true);
    }
    
    @Bean
    TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }
    
    @Bean
    MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setReplyTimeout(60000);
        return rabbitTemplate;
    }
    
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
