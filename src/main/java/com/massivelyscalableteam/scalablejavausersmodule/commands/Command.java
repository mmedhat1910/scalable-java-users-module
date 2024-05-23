package com.massivelyscalableteam.scalablejavausersmodule.commands;

import org.springframework.http.ResponseEntity;

public interface Command<T> {
    ResponseEntity<T> execute();
}
