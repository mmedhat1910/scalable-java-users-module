package com.massivelyscalableteam.scalablejavausersmodule.commands.auth;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class LogoutCommand extends Command<Map<String, String>> {
    private final UserRepository userRepository;
    private final String session;
    public LogoutCommand(String session, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public Map<String, String> execute() {
        User dbUser = this.userRepository.findBySession(session);
        if (dbUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        dbUser.setSession(null);
        this.userRepository.save(dbUser);
        return Map.of( "message", "Logged out successfully");
    }
}
