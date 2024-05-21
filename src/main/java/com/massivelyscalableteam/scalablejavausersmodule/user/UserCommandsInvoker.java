package com.massivelyscalableteam.scalablejavausersmodule.user;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;

public class UserCommandsInvoker<T>{
    private final Command<T> command;

    public UserCommandsInvoker(Command<T> command) {
        this.command = command;
    }

    public T invoke() {
        return command.execute();
    }
}

