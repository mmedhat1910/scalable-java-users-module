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

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return this.userService.getUsers();
    }


    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    User getUser(@RequestHeader String Authorization) {
        System.out.println(Authorization);
        return this.userService.getUserBySessionId(Authorization);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsername(@PathVariable String username) {
        System.out.println("Username: "+username);
        return this.userService.getUserByUsername(username);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Map<String, String> register(@RequestBody User user){
        return this.userService.register(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> login(@RequestBody LoginDto user){
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
        return this.userService.updateUser(Authorization, username, user);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    Map<String, String> deleteUser(@RequestHeader String Authorization, @PathVariable String username){
        return this.userService.deleteUser(Authorization, username);
    }


}
