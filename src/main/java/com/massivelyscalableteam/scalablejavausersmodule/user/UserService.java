package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.RegisterCommand;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    protected User register(User user) {
        RegisterCommand registerCommand = new RegisterCommand(user);
        UserCommandsInvoker<User> invoker = new UserCommandsInvoker<>(registerCommand);
        System.out.println(user);
        return invoker.invoke();
    }



}
