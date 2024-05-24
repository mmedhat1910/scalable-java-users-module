package com.massivelyscalableteam.scalablejavausersmodule.commands;

import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisReadCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisSaveCommand;
import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

public class CommandInvokerWithCache<T> extends CommandInvoker<T>{
    private final RedisTemplate<String, T> template;

    public CommandInvokerWithCache(Command<T> command, RedisTemplate<String, T> template) {
        super(command);
        this.template = template;
    }

    @Override
    public T invoke() {
        // Get the key from the command
        // Check if the key exists in the cache
        // If it does, return the value from the cache
        // If it doesn't, invoke the command and save the result in the cache
        RedisReadCommand<T> redisReadCommand = new RedisReadCommand<>(template, this.command.key);
        CommandInvoker<T> redisInvoker = new CommandInvoker<>(redisReadCommand);
        T cached = redisInvoker.invoke();
        if (cached != null) {
            System.out.println("Cache hit");
            return cached;
        }
        System.out.println("Cache miss");
        T invocation = super.invoke();

        RedisSaveCommand<T> redisSaveCommand = new RedisSaveCommand<>(template, this.command.key, invocation);
        CommandInvoker<T> redisReadInvoker = new CommandInvoker<>(redisSaveCommand);
        redisReadInvoker.invoke();

        return invocation;
    }

}
