package com.massivelyscalableteam.scalablejavausersmodule.commands.crud;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class GetUsersCommand extends Command<List<User>> {

    private final UserRepository userRepository;
    public GetUsersCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<User> execute() {
        List<User> users = userRepository.findAll();
        for(User user :users){
            user.setPassword(null);
            user.setSession(null);
        }
        return users;
    }
}
