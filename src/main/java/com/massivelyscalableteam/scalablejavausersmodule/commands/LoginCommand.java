package com.massivelyscalableteam.scalablejavausersmodule.commands;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginCommand implements Command<String>{
    private final LoginDto user;
    private final UserRepository userRepository;
    public LoginCommand(LoginDto user, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    @Override
    public ResponseEntity<String> execute() {
        User dbUser = this.userRepository.findByUsername(user.getUsername());
        if (dbUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }
        dbUser.setSession(java.util.UUID.randomUUID().toString());
        this.userRepository.save(dbUser);
        System.out.println("Logging in user: " + dbUser.getUsername());
        return ResponseEntity.ok(dbUser.getSession());
    }
}
