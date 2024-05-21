package com.massivelyscalableteam.scalablejavausersmodule.commands;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;

public class LoginCommand implements Command<User>{
    private final User user;
    public LoginCommand(User user) {
        this.user = user;
    }

    @Override
    public User execute() {
        return null;
    }
}
