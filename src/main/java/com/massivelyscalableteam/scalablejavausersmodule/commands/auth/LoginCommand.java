package com.massivelyscalableteam.scalablejavausersmodule.commands.auth;
import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.AuthResponse;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.logging.Logger;

public class LoginCommand extends Command<AuthResponse> {
    private final LoginDto user;
    private final UserRepository userRepository;

    private Logger logger = Logger.getLogger(LoginCommand.class.getName());
    public LoginCommand(LoginDto user, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public AuthResponse execute() {
        User dbUser = this.userRepository.findByUsername(user.getUsername());
        if (dbUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        dbUser.setSession(java.util.UUID.randomUUID().toString());
        this.userRepository.save(dbUser);
        logger.info("Logging in user: " + dbUser.getUsername() + " with session: " + dbUser.getSession());
        return new AuthResponse(Map.of("session" ,dbUser.getSession()), dbUser);
    }
}
