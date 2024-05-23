package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.UpdateUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UpdateUserCommand implements Command<User>{

    private final UpdateUserDto user;
    private final String sessionId;
    private final String username;
    private final UserRepository userRepository;

    public UpdateUserCommand(String sessionId,String username,  UpdateUserDto user, UserRepository userRepository) {
        this.user = user;
        this.sessionId = sessionId;
        this.userRepository = userRepository;
        this.username = username;
    }
    @Override
    public ResponseEntity<User> execute() {
        User dbUser = this.userRepository.findByUsername(username);
        System.out.println(dbUser);
        System.out.println(user);
        if (dbUser == null) {
            return ResponseEntity.status(404).body(null);
        }
        if (!dbUser.getSession().equals(sessionId)) {
            return ResponseEntity.status(401).body(null);
        }
        //check all fields, if not null, update, if null, keep the same
        if(user.getFull_name()!=null){
            System.out.println("Updating full name to: "+user.getFull_name());
            dbUser.setFull_name(user.getFull_name());
        }
        if(user.getEmail()!=null){
            System.out.println("Updating email to: "+user.getEmail());
            List<User> users = this.userRepository.findByEmail(user.getEmail());
            if(!users.isEmpty()){
                return ResponseEntity.status(409).body(null);
            }
            dbUser.setEmail(user.getEmail());
        }
        if(user.getPassword()!=null){
            System.out.println("Updating password");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updated = this.userRepository.save(dbUser);

        updated.setPassword(null);

        return ResponseEntity.ok(updated);
    }
}
