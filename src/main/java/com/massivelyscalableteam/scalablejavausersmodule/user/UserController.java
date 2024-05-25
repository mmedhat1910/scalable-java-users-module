package com.massivelyscalableteam.scalablejavausersmodule.user;

import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.UpdateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private Logger logger = Logger.getLogger(UserController.class.getName());


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        logger.info("Getting users");
        return this.userService.getUsers();
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsername(@PathVariable String username, @RequestHeader String Authorization) {
        logger.info("Getting user by username: "+username);
        return this.userService.getUserByUsername(username, Authorization);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Map<String, String> register(@RequestBody User user){
        logger.info("Registering user: "+user.getUsername());
        return this.userService.register(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> login(@RequestBody LoginDto user){
        logger.info("Logging in user: "+user.getUsername());
        return this.userService.login(user);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> logout(@RequestHeader String Authorization){
        return this.userService.logout(Authorization);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User updateUser(@RequestHeader String Authorization, @RequestBody UpdateUserDto user, @PathVariable String username){
        logger.info("Updating user: "+username);
        return this.userService.updateUser(Authorization, username, user);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> deleteUser(@RequestHeader String Authorization, @PathVariable String username){
        logger.info("Deleting user: "+username);
        return this.userService.deleteUser(Authorization, username);
    }


}
