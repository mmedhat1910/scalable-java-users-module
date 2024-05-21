package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class RegisterCommand implements Command<User> {

    private final User user;
    private final UserRepository userRepository;

    public RegisterCommand(User user, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public User execute() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Registering user: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        // TODO: create JWT token
        return user;
    }
}
