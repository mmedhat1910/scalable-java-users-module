package com.massivelyscalableteam.scalablejavausersmodule.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

public class CustomHeaderMessagePostProcessor implements MessagePostProcessor {
    private final Map<String, String> headers;

    public CustomHeaderMessagePostProcessor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Message postProcessMessage(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.getHeaders().putAll(headers);
        return message;
    }
}
