package com.massivelyscalableteam.scalablejavausersmodule.rmq;

import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisReadCommand;
import com.massivelyscalableteam.scalablejavausersmodule.config.Constants;
import com.massivelyscalableteam.scalablejavausersmodule.rmq.dto.VerifyUserDto;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserService;
import com.massivelyscalableteam.scalablejavausersmodule.utils.CustomHeaderMessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class RMQVerifyUser {
    @Autowired
    private RabbitTemplate rmq;

    @Autowired
    private UserService userService;

    private final Logger logger = Logger.getLogger(RMQVerifyUser.class.getName());

    @RabbitListener(queues = Constants.VERIFICATION_QUEUE)
    public User verifyUser(VerifyUserDto verifyUserDto) {
        logger.info("Processing message on queue: " + Constants.VERIFICATION_QUEUE);
        if(verifyUserDto.sessionId == null || verifyUserDto.username == null) {
            logger.info("Invalid request: " + verifyUserDto);
            return null;
        }
        //check is session id is valid formatting
        if (verifyUserDto.sessionId.length() != 36) {
            logger.info("Invalid session id: " + verifyUserDto.sessionId);
            rmq.convertAndSend(Constants.EXCHANGE,
                    Constants.VERIFICATION_RESPONSE_ROUTING_KEY,
                    "Invalid Session ID",
                    new CustomHeaderMessagePostProcessor(Map.of(
                            "messageId", verifyUserDto.messageId,
                            "status" , "400"
                    )));
            return null;
        }

        User user = userService.getUserByUsername(verifyUserDto.username, verifyUserDto.sessionId);
        if (user == null) {
            logger.info("User not found: " + verifyUserDto.username);
            rmq.convertAndSend(Constants.EXCHANGE,
                    Constants.VERIFICATION_RESPONSE_ROUTING_KEY,
                    "User Not Found",
                    new CustomHeaderMessagePostProcessor(Map.of(
                            "messageId", verifyUserDto.messageId,
                            "status" , "404"
                    )));
            return null;
        }
        if(user.getSession() == null || !user.getSession().equals(verifyUserDto.sessionId)) {
            logger.info("User not logged in: " + verifyUserDto.username);
            rmq.convertAndSend(Constants.EXCHANGE,
                    Constants.VERIFICATION_RESPONSE_ROUTING_KEY,
                    "User Not Logged In",
                    new CustomHeaderMessagePostProcessor(Map.of(
                            "messageId", verifyUserDto.messageId,
                            "status" , "401"
                    )));
            return null;
        }

        logger.info("User verified: " + verifyUserDto.username);
        rmq.convertAndSend(Constants.EXCHANGE,
                Constants.VERIFICATION_RESPONSE_ROUTING_KEY,
                "Verified User",
                new CustomHeaderMessagePostProcessor(Map.of(
                        "messageId", verifyUserDto.messageId,
                        "status" , "200"
                )));

        logger.info("Finished processing message on queue: " + Constants.VERIFICATION_QUEUE);

        return user;
    }

    @RabbitListener(queues = Constants.USER_QUEUE)
    public void getUserByUsername(VerifyUserDto verifyUserDto) {
        logger.info("Processing message on queue: " + Constants.USER_QUEUE);
        User user = this.verifyUser(verifyUserDto);
        if (user != null) {
            logger.info("User found: " + verifyUserDto.username);
            rmq.convertAndSend(Constants.USER_EXCHANGE,
                    Constants.USER_RESPONSE_ROUTING_KEY,
                    user,
                    new CustomHeaderMessagePostProcessor(Map.of(
                            "messageId", verifyUserDto.messageId,
                            "status" , "200"
                    )));
        }else {
            logger.info("User not found: " + verifyUserDto.username);
            rmq.convertAndSend(Constants.USER_EXCHANGE,
                    Constants.USER_RESPONSE_ROUTING_KEY,
                    "User not found",
                    new CustomHeaderMessagePostProcessor(Map.of(
                            "messageId", verifyUserDto.messageId,
                            "status" , "404"
                    )));
        }

        logger.info("Finished processing message on queue: " + Constants.USER_QUEUE);
    }
}
