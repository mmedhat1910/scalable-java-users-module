package com.massivelyscalableteam.scalablejavausersmodule.commands;

import org.springframework.http.ResponseEntity;

public abstract class Command<T> {
    public String key = "";
    public abstract T execute();
}
