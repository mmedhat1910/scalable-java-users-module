package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LogoutCommand implements Command<String>{
    private final UserRepository userRepository;
    private final String session;
    public LogoutCommand(String session, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public ResponseEntity<String> execute() {
        User dbUser = this.userRepository.findBySession(session);
        if (dbUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        dbUser.setSession(null);
        this.userRepository.save(dbUser);
        return ResponseEntity.ok("Logged out successfully");
    }
}
