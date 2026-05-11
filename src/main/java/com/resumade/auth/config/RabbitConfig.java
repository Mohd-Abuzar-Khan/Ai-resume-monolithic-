package com.resumade.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${export.queue.name}")
    private String exportQueueName;

    @Value("${export.queue.exchange}")
    private String exportExchange;

    @Value("${export.queue.routing-key}")
    private String exportRoutingKey;

    @Value("${notification.queue}")
    private String notificationQueueName;

    @Value("${notification.exchange}")
    private String notificationExchange;

    @Value("${notification.routing-key}")
    private String notificationRoutingKey;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Export Queue Configuration
    @Bean
    public Queue exportQueue() {
        return new Queue(exportQueueName, true);
    }

    @Bean
    public DirectExchange exportExchange() {
        return new DirectExchange(exportExchange);
    }

    @Bean
    public Binding exportBinding(Queue exportQueue, DirectExchange exportExchange) {
        return BindingBuilder.bind(exportQueue).to(exportExchange).with(exportRoutingKey);
    }

    // Notification Queue Configuration
    @Bean
    public Queue notificationQueue() {
        return new Queue(notificationQueueName, true);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(notificationExchange);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(notificationRoutingKey);
    }
}

