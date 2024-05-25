package com.massivelyscalableteam.scalablejavausersmodule.commands.crud;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import com.massivelyscalableteam.scalablejavausersmodule.user.UserRepository;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.UpdateUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

public class UpdateUserCommand extends Command<User> {

    private final UpdateUserDto user;
    private final String sessionId;
    private final String username;
    private final UserRepository userRepository;

    private final Logger logger = Logger.getLogger(UpdateUserCommand.class.getName());

    public UpdateUserCommand(String sessionId,String username,  UpdateUserDto user, UserRepository userRepository) {
        this.user = user;
        this.sessionId = sessionId;
        this.userRepository = userRepository;
        this.username = username;
    }
    @Override
    public User execute() {
        User dbUser = this.userRepository.findByUsername(username);
        if (dbUser == null) {
           logger.info("User not found");
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!dbUser.getSession().equals(sessionId)) {
            logger.info("Unauthorized");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        //check all fields, if not null, update, if null, keep the same
        if(user.getFull_name()!=null){
            logger.info("Updating full name to: "+user.getFull_name());
            dbUser.setFull_name(user.getFull_name());
        }
        if(user.getEmail()!=null){
            logger.info("Updating email to: "+user.getEmail());
            List<User> users = this.userRepository.findByEmail(user.getEmail());
            if(!users.isEmpty()){
                logger.info("Email already in use");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
            dbUser.setEmail(user.getEmail());
        }
        if(user.getPassword()!=null){
            logger.info("Updating password");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updated = this.userRepository.save(dbUser);

        updated.setPassword(null);
        logger.info("User updated successfully");
        return updated;
    }
}
