package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.CommandInvoker;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.LoginCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.LogoutCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.auth.RegisterCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.DeleteUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.GetUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.GetUsersCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.crud.UpdateUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisReadCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.redis.RedisSaveCommand;
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
        RedisReadCommand<List<Map<String, String>>> redisReadCommand = new RedisReadCommand<>(redisList, "users");
        CommandInvoker<List<Map<String, String>>> redisInvoker = new CommandInvoker<>(redisReadCommand);
        List<Map<String, String>> response = redisInvoker.invoke();
        if (response!=null) {
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
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(registerCommand);
        return invoker.invoke();
    }

    protected Map<String, String> login(LoginDto user) {
        LoginCommand loginCommand = new LoginCommand(user, userRepository);
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(loginCommand);
        return invoker.invoke();

    }

    protected User getUserBySessionId(String sessionId) {
        GetUserCommand getUserCommand = new GetUserCommand(userRepository, sessionId);
        CommandInvoker<User> invoker = new CommandInvoker<>(getUserCommand);
        return invoker.invoke();
    }

    protected User getUserByUsername(String username) {
        GetUserCommand getUserCommand = new GetUserCommand(userRepository, null, username);
        CommandInvoker<User> invoker = new CommandInvoker<>(getUserCommand);
        return invoker.invoke();
    }

    protected Map<String, String> logout(String sessionId) {
        LogoutCommand logoutCommand = new LogoutCommand(sessionId, userRepository);
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(logoutCommand);
        return invoker.invoke();
    }

    protected User updateUser(String sessionId, String username, UpdateUserDto user) {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(sessionId, username, user, userRepository);
        CommandInvoker<User> invoker = new CommandInvoker<>(updateUserCommand);
        return invoker.invoke();
    }

    public Map<String, String> deleteUser(String authorization, String username) {
        DeleteUserCommand deleteUserCommand = new DeleteUserCommand(authorization, username, userRepository);
        CommandInvoker<Map<String, String>> invoker = new CommandInvoker<>(deleteUserCommand);
        return invoker.invoke();
    }
}
