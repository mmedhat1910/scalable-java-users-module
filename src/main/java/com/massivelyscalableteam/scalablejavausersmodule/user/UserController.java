package com.massivelyscalableteam.scalablejavausersmodule.user;

import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<List<User>> getUsers() {
        return this.userService.getUsers();
    }


    @GetMapping("/me")
    ResponseEntity<User> getUser(@RequestHeader String Authorization) {
        System.out.println(Authorization);
        return this.userService.getUserBySessionId(Authorization);
    }

    @GetMapping("/username/{username}")
    ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        System.out.println("Username: "+username);
        return this.userService.getUserByUsername(username);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody User user){
        return this.userService.register(user);
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginDto user){
        return this.userService.login(user);
    }

}
