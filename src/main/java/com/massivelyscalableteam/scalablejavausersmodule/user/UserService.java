package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.CommandInvoker;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.LoginCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.LogoutCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.RegisterCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.DeleteUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.GetUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.GetUsersCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.UpdateUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisDeleteCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisReadCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisSaveCommand;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.AuthResponse;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.UpdateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private UserRepository userRepository;
    private RedisTemplate<String, List<Map<String, String>>> redisList;
    private RedisTemplate<String, Map<String, String>> redis;



    @Autowired
    public UserService(UserRepository userRepository, RedisTemplate<String, List<Map<String, String>>> redisList, RedisTemplate<String, Map<String, String>> redis) {
        this.userRepository = userRepository;
        this.redisList = redisList;
        this.redis = redis;
    }

    protected List<User> getUsers() {
        RedisReadCommand<List<Map<String, String>>> redisReadCommand = new RedisReadCommand<>(redisList, "users", (Class<List<Map<String, String>>>) (Class<?>) List.class);
        CommandInvoker<List<Map<String, String>>> redisInvoker = new CommandInvoker<>(redisReadCommand);
        List<Map<String, String>> response = redisInvoker.invoke();
        if (response!=null && !response.isEmpty()) {
            System.out.println("Cache hit");
            return User.mapToUserList(response);
        }
        System.out.println("Cache miss");
        GetUsersCommand getUsersCommand = new GetUsersCommand(userRepository);
        CommandInvoker<List<User>> invoker = new CommandInvoker<>(getUsersCommand);
        List<User> dbResponse =  invoker.invoke();
        if (dbResponse!=null) {
            List<Map<String, String>> usersMap = User.userListToMap(dbResponse);
            RedisSaveCommand<List<Map<String, String>>> redisSaveCommand = new RedisSaveCommand<>(redisList, "users", usersMap);
            redisInvoker = new CommandInvoker<>(redisSaveCommand);
            redisInvoker.invoke();
        }
        return dbResponse;
    }

    protected Map<String, String> register(User user) {
        RegisterCommand registerCommand = new RegisterCommand(user, userRepository);
        CommandInvoker<AuthResponse> invoker = new CommandInvoker<>(registerCommand);
        AuthResponse response = invoker.invoke();
        response.user.setSession(null);
        RedisSaveCommand redisSaveCommand = new RedisSaveCommand<>(redisList, "users", List.of(response.user.toMap()));
        CommandInvoker redisInvoker = new CommandInvoker<>(redisSaveCommand);
        redisInvoker.invoke();
        return response.response;
    }

    protected Map<String, String> login(LoginDto user) {
        LoginCommand loginCommand = new LoginCommand(user, userRepository);
        CommandInvoker<AuthResponse> invoker = new CommandInvoker<>(loginCommand);

        AuthResponse response = invoker.invoke();

        RedisSaveCommand redisSaveCommand = new RedisSaveCommand<>(redis, response.user.username, response.user.toMap());
        CommandInvoker redisInvoker = new CommandInvoker<>(redisSaveCommand);
        redisInvoker.invoke();

        return response.response;
    }


    public User getUserByUsername(String username, String sessionId) {
        RedisReadCommand<Map<String, String>> redisReadCommand = new RedisReadCommand<>(redis, username, (Class<Map<String, String>>) (Class<?>) Map.class);
        CommandInvoker<Map<String, String>> redisInvoker = new CommandInvoker<>(redisReadCommand);
        Map<String, String> response = redisInvoker.invoke();
        if (response!=null && !response.isEmpty()) {
            System.out.println("Cache hit");
            User cached = new User(response);
            if (cached.session == null || !cached.session.equals(sessionId)) {
                cached.setSession(null);
            }
            return cached;
        }
        System.out.println("Cache miss");

        GetUserCommand getUserCommand = new GetUserCommand(userRepository, sessionId, username);
        CommandInvoker<User> invoker = new CommandInvoker<>(getUserCommand);
        User dbResponse = invoker.invoke();

        if (dbResponse!=null) {
            Map<String, String> userMap = dbResponse.toMap();
            RedisSaveCommand<Map<String, String>> redisSaveCommand = new RedisSaveCommand<>(redis, username, userMap);
            redisInvoker = new CommandInvoker<>(redisSaveCommand);
            redisInvoker.invoke();
        }
        return dbResponse;
    }

    protected Map<String, String> logout(String sessionId) {
        LogoutCommand logoutCommand = new LogoutCommand(sessionId, userRepository);
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(logoutCommand);
        return invoker.invoke();
    }

    protected User updateUser(String sessionId, String username, UpdateUserDto user) {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(sessionId, username, user, userRepository);
        CommandInvoker<User> invoker = new CommandInvoker<>(updateUserCommand);
        User updated = invoker.invoke();

        RedisSaveCommand redisSaveCommand = new RedisSaveCommand<>(redis, updated.username, updated.toMap());
        CommandInvoker redisInvoker = new CommandInvoker<>(redisSaveCommand);
        redisInvoker.invoke();

        return updated;
    }

    public Map<String, String> deleteUser(String authorization, String username) {
        DeleteUserCommand deleteUserCommand = new DeleteUserCommand(authorization, username, userRepository);
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(deleteUserCommand);

        RedisDeleteCommand<Map<String, String>> redisDeleteCommand = new RedisDeleteCommand<>(redis, username, (Class<Map<String, String>>) (Class<?>) Map.class, username);
        CommandInvoker<Map<String, String>> redisInvoker = new CommandInvoker<>(redisDeleteCommand);

        RedisDeleteCommand<List<Map<String, String>>> listRedisDeleteCommand = new RedisDeleteCommand<>(redisList, "users", (Class<List<Map<String, String>>>) (Class<?>) List.class, username);
        CommandInvoker<List<Map<String, String>>> redisInvokerList = new CommandInvoker<>(listRedisDeleteCommand);


        Map<String, String> response = redisInvoker.invoke();
        List<Map<String, String>> responseList = redisInvokerList.invoke();

        return invoker.invoke();
    }
}
