package com.massivelyscalableteam.scalablejavausersmodule.commands;

import org.springframework.http.ResponseEntity;

public class CommandInvoker<T>{
    protected final Command<T> command;

    public CommandInvoker(Command<T> command) {
        this.command = command;
    }

    public T invoke() {
        return command.execute();
    }
}

