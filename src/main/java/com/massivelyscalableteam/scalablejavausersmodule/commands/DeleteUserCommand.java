package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.http.ResponseEntity;

public class DeleteUserCommand implements Command<String>{

    private final String session;
    private final String username;
    private final UserRepository userRepository;
    public DeleteUserCommand(String session, String username, UserRepository userRepository) {
        this.session = session;
        this.username = username;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<String> execute() {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(null);
        }
        if (!user.getSession().equals(session)) {
            return ResponseEntity.status(401).body(null);
        }
        this.userRepository.deleteById(user.getUser_id());
        return ResponseEntity.ok("User deleted");
    }
}
