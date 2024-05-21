package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class RegisterCommand implements Command<User> {

    private final User user;
    public RegisterCommand(User user) {
        this.user = user;
    }
    @Override
    public User execute() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("Registering user: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // TODO: save user to database
        return user;
    }
}
