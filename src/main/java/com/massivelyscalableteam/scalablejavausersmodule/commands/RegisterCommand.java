package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class RegisterCommand implements Command<String> {

    private final User user;
    private final UserRepository userRepository;

    public RegisterCommand(User user, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public ResponseEntity<String> execute() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Registering user: " + user);
        User existing = this.userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            return ResponseEntity.status(409).body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = this.userRepository.save(user);
        // TODO: create JWT token
        return ResponseEntity.ok(created.getSession());
    }
}
