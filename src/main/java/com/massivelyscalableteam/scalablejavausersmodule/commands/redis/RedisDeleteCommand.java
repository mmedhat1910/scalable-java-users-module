package com.massivelyscalableteam.scalablejavausersmodule.commands.redis;

import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.config.RedisConfig;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

public class RedisDeleteCommand<T> extends Command<T> {
    private final RedisTemplate<String, T> template;
    private final String key;
    private final Class<T> type;

    private final String toRemove;

    public RedisDeleteCommand(RedisTemplate<String, T> template, String key, Class<T> type, String toRemove) {
        this.template = template;
        this.key = key;
        this.type = type;
        this.toRemove = toRemove;
    }
    @Override
    public T execute() {
        String fullKey = RedisConfig.STRING_KEY_PREFIX + key;
        if (List.class.isAssignableFrom(type)) {
            //get list from redis then remove the element and return the list
            List<Map<String,String>> list = (List<Map<String, String>>) template.opsForList().range(fullKey, 0, -1);
            for (Map m : list) {
                User u = new User(m);
                if (u.getUsername().equals(toRemove)) {
                    list.remove(m);
                    break;
                }
            }

            template.delete(fullKey);
            template.opsForList().leftPushAll(fullKey, (List) list);

            return (T) list;
        } else if (Map.class.isAssignableFrom(type)) {
            template.delete(fullKey);
            return null;
        } else {
            return template.opsForValue().get(fullKey);
        }
    }
}
