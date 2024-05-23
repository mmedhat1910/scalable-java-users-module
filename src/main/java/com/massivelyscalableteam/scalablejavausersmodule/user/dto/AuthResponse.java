package com.massivelyscalableteam.scalablejavausersmodule.user.dto;

public class AuthResponse {
    private String sessionId;

    public AuthResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

}
