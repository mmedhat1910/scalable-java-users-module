package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.*;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.UpdateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected ResponseEntity<List<User>> getUsers() {
        GetUsersCommand getUsersCommand = new GetUsersCommand(userRepository);
        UserCommandsInvoker<List<User>> invoker = new UserCommandsInvoker<List<User>>(getUsersCommand);
        return invoker.invoke();
    }

    protected ResponseEntity<String> register(User user) {
        RegisterCommand registerCommand = new RegisterCommand(user, userRepository);
        UserCommandsInvoker<String> invoker = new UserCommandsInvoker<>(registerCommand);
        return invoker.invoke();
    }

    protected ResponseEntity<String> login(LoginDto user) {
        LoginCommand loginCommand = new LoginCommand(user, userRepository);
        UserCommandsInvoker<String> invoker = new UserCommandsInvoker<>(loginCommand);
        return invoker.invoke();

    }

    protected ResponseEntity<User> getUserBySessionId(String sessionId) {
        GetUserCommand getUserCommand = new GetUserCommand(userRepository, sessionId);
        UserCommandsInvoker<User> invoker = new UserCommandsInvoker<>(getUserCommand);
        return invoker.invoke();
    }

    protected ResponseEntity<User> getUserByUsername(String username) {
        GetUserCommand getUserCommand = new GetUserCommand(userRepository, null, username);
        UserCommandsInvoker<User> invoker = new UserCommandsInvoker<>(getUserCommand);
        return invoker.invoke();
    }

    protected ResponseEntity<String> logout(String sessionId) {
        LogoutCommand logoutCommand = new LogoutCommand(sessionId, userRepository);
        UserCommandsInvoker<String> invoker = new UserCommandsInvoker<>(logoutCommand);
        return invoker.invoke();
    }

    protected ResponseEntity<User> updateUser(String sessionId, String username, UpdateUserDto user) {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(sessionId, username, user, userRepository);
        UserCommandsInvoker<User> invoker = new UserCommandsInvoker<>(updateUserCommand);
        return invoker.invoke();
    }

}
