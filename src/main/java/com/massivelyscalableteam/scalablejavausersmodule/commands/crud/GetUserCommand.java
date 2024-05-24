package com.massivelyscalableteam.scalablejavausersmodule.commands.crud;
import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class GetUserCommand extends Command<User> {

    private final UserRepository userRepository;
    private final String sessionId;

    private String username;

    public GetUserCommand(UserRepository userRepository, String sessionId) {
        this.userRepository = userRepository;
        this.sessionId = sessionId;

        this.key = "user:" + sessionId;
    }

    public GetUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.sessionId = null;
    }

    public GetUserCommand(UserRepository userRepository, String sessionId, String username) {
        this.userRepository = userRepository;
        this.sessionId = sessionId;
        this.username = username;

        this.key = "user:" + username;
    }


    @ResponseStatus(HttpStatus.OK)
    public User execute() {
        if(username!= null){
            User user = this.userRepository.findByUsername(username);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            user.setPassword(null);
            user.setSession(null);
            return user;
        }else if (sessionId != null){
            User user = this.userRepository.findBySession(sessionId);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            user.setPassword(null);
            return user;
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
    }
}
