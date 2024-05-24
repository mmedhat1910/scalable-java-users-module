package com.massivelyscalableteam.scalablejavausersmodule.user.dto;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;

import java.util.Map;

public class AuthResponse {
    public Map<String, String> response;
    public User user;

    public AuthResponse(Map<String, String> response, User user) {
        this.response = response;
        this.user = user;
    }
}
