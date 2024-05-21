package com.massivelyscalableteam.scalablejavausersmodule.user;

import com.massivelyscalableteam.scalablejavausersmodule.commands.GenerateJwtTokenCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.RegisterCommand;
import com.massivelyscalableteam.scalablejavausersmodule.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private JwtConfig jwtConfig;


    @GetMapping
    User getUser() {
        return new User("1", "mmedhat", "123456","mmedhat@example.com", "Mohamed Medhat");
    }

    @PostMapping("/register")
    User register(@RequestBody User user){
        return this.userService.register(user);
    }
}
