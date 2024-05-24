package com.massivelyscalableteam.scalablejavausersmodule.commands.redis;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.config.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

public class RedisReadCommand<T> extends Command<T> {
    private final RedisTemplate<String, T> template;
    private final String key;
    private final Class<T> type;

    public RedisReadCommand(RedisTemplate<String, T> template, String key, Class<T> type) {
        this.template = template;
        this.key = key;
        this.type = type;
    }




    @Override
    public T execute() {
        String fullKey = RedisConfig.STRING_KEY_PREFIX + key;
        if (List.class.isAssignableFrom(type)) {
            List<?> list = template.opsForList().range(fullKey, 0, -1);
            return (T) list;
        } else if (Map.class.isAssignableFrom(type)) {
            Map<Object, Object> map = template.opsForHash().entries(fullKey);
            return (T) map;
        } else {
            return template.opsForValue().get(fullKey);
        }
//        return template.opsForValue().get(STRING_KEY_PREFIX + key);
    }
}
