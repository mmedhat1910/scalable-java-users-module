package com.massivelyscalableteam.scalablejavausersmodule.commands.redis;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class RedisSaveCommand<T> extends Command<T> {

    private final RedisTemplate<String, T> template;
    private final String key;
    private final T value;

    public RedisSaveCommand(RedisTemplate<String, T> template, String key, T map) {
        this.template = template;
        this.key = key;
        this.value = map;
    }

    private static final String STRING_KEY_PREFIX = "scalable:users:";

    @Override
    public T execute() {
        if(value instanceof List){
            template.opsForList().leftPushAll(STRING_KEY_PREFIX + key, (List) value);
            return value;
        }else if(value instanceof Map){
            template.opsForHash().putAll(STRING_KEY_PREFIX + key, (Map) value);
            return value;
        }else {
            template.opsForValue().set(STRING_KEY_PREFIX + key, value);
            return value;
        }
    }
}
