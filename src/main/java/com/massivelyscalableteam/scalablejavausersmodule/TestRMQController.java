package com.massivelyscalableteam.scalablejavausersmodule;

import com.massivelyscalableteam.scalablejavausersmodule.config.Constants;
import com.massivelyscalableteam.scalablejavausersmodule.rmq.dto.VerifyUserDto;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rmq")
public class TestRMQController {

    @Autowired
    private RabbitTemplate template;

    @GetMapping("/publish")
    public String publish(@RequestParam String sessionId, @RequestParam String username) {
        String messageId = UUID.randomUUID().toString();
        this.template.convertAndSend(Constants.EXCHANGE, Constants.VERIFICATION_ROUTING_KEY, new VerifyUserDto(sessionId, username, messageId));
        return "Message published: " + sessionId + " " + username + " " + messageId;
    }

    @GetMapping("/publish/user")
    public String publishUser(@RequestParam String sessionId, @RequestParam String username) {
        String messageId = UUID.randomUUID().toString();
        this.template.convertAndSend(Constants.USER_EXCHANGE, Constants.USER_ROUTING_KEY, new VerifyUserDto(sessionId, username, messageId));
        return "Message published: " + sessionId + " " + username + " " + messageId;
    }



}
