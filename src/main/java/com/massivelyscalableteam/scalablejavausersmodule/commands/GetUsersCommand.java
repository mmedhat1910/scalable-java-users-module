package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GetUsersCommand implements Command<List<User>>{

    private final UserRepository userRepository;
    public GetUsersCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<List<User>> execute() {
        List<User> users = userRepository.findAll();
        for(User user :users){
            user.setPassword(null);
            user.setSession(null);
        }
        return ResponseEntity.ok(users);
    }
}
