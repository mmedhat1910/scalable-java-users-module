package com.massivelyscalableteam.scalablejavausersmodule.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue verificationQueue() {
        return new Queue(Constants.VERIFICATION_QUEUE, true);
    }

    @Bean
    public Queue verificationResponseQueue() {
        return new Queue(Constants.VERIFICATION_RESPONSE_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(Constants.EXCHANGE);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(this.verificationQueue()).to(this.exchange()).with(Constants.VERIFICATION_ROUTING_KEY);
    }
    @Bean
    public Binding bindingReply(){
        return BindingBuilder.bind(this.verificationResponseQueue()).to(this.exchange()).with(Constants.VERIFICATION_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(this.converter());
        return rabbitTemplate;
    }


    @Bean
    public Queue userQueue() {
        return new Queue(Constants.USER_QUEUE, true);
    }

    @Bean
    public Queue userResponseQueue() {
        return new Queue(Constants.USER_RESPONSE_QUEUE, true);
    }

    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange(Constants.USER_EXCHANGE);
    }

    @Bean
    public Binding userBinding(){
        return BindingBuilder.bind(this.userQueue()).to(this.userExchange()).with(Constants.USER_ROUTING_KEY);
    }

    @Bean
    public Binding userBindingReply(){
        return BindingBuilder.bind(this.userResponseQueue()).to(this.userExchange()).with(Constants.USER_RESPONSE_ROUTING_KEY);
    }


}
