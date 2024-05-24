package com.massivelyscalableteam.scalablejavausersmodule.rmq.dto;

public class VerifyUserDto {
    public String sessionId;
    public String username;
    public String messageId;
    public VerifyUserDto(String sessionId, String username, String messageId) {
        this.sessionId = sessionId;
        this.username = username;
        this.messageId = messageId;
    }
}
