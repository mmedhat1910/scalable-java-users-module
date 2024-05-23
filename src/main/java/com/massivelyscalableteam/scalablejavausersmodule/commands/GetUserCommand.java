package com.massivelyscalableteam.scalablejavausersmodule.commands;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.ResponseEntity;

public class GetUserCommand implements Command<User>{

    private final UserRepository userRepository;
    private final String sessionId;

    private String username;

    public GetUserCommand(UserRepository userRepository, String sessionId) {
        this.userRepository = userRepository;
        this.sessionId = sessionId;
    }

    public GetUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.sessionId = null;
    }

    public GetUserCommand(UserRepository userRepository, String sessionId, String username) {
        this.userRepository = userRepository;
        this.sessionId = sessionId;
        this.username = username;
    }


    @Override
    public ResponseEntity<User> execute() {
        if(username!= null){
            User user = this.userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(404).body(null);
            }
            user.setPassword(null);
            user.setSession(null);
            return ResponseEntity.ok(user);
        }else if (sessionId != null){
            User user = this.userRepository.findBySession(sessionId);
            if (user == null) {
                return ResponseEntity.status(404).body(null);
            }
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }
}
