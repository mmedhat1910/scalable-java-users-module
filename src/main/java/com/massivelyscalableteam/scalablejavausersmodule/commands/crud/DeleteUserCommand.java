package com.massivelyscalableteam.scalablejavausersmodule.commands.crud;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class DeleteUserCommand extends Command<Map<String, String>> {

    private final String session;
    private final String username;
    private final UserRepository userRepository;
    public DeleteUserCommand(String session, String username, UserRepository userRepository) {
        this.session = session;
        this.username = username;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, String> execute() {
        User user = this.userRepository.findByUsername(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if (!user.getSession().equals(session)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        this.userRepository.deleteById(user.getUser_id());
        return Map.of("message", "User deleted successfully");
    }
}
