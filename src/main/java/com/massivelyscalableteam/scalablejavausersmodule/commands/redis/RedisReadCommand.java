package com.massivelyscalableteam.scalablejavausersmodule.commands.redis;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class RedisReadCommand<T> extends Command<T> {
    private final RedisTemplate<String, T> template;
    private final String key;

    public RedisReadCommand(RedisTemplate<String, T> template, String key) {
        this.template = template;
        this.key = key;
    }

    private static final String STRING_KEY_PREFIX = "scalable:users:";


    @Override
    public T execute() {
        return template.opsForValue().get(STRING_KEY_PREFIX + key);
    }
}
