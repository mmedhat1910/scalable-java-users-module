package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.GetUserCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.GetUsersCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.LoginCommand;
import com.massivelyscalableteam.scalablejavausersmodule.commands.RegisterCommand;
import com.massivelyscalableteam.scalablejavausersmodule.user.dto.LoginDto;
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

}
