package com.massivelyscalableteam.scalablejavausersmodule.commands.auth;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class RegisterCommand extends Command<AuthResponse> {

    private final User user;
    private final UserRepository userRepository;

    public RegisterCommand(User user, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public AuthResponse execute() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Registering user: " + user);
        User existing = this.userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = this.userRepository.save(user);
        // TODO: create JWT token
        return new AuthResponse(Map.of("message", "User created successfully", "session", created.getSession()), created);
    }
}
