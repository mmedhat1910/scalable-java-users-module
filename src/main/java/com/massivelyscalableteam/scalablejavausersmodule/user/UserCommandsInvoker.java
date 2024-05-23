package com.massivelyscalableteam.scalablejavausersmodule.user;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import org.springframework.http.ResponseEntity;

public class UserCommandsInvoker<T>{
    private final Command<T> command;

    public UserCommandsInvoker(Command<T> command) {
        this.command = command;
    }

    public ResponseEntity<T> invoke() {
        return command.execute();
    }
}

