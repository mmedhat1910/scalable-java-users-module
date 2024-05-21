package com.massivelyscalableteam.scalablejavausersmodule.user;
import com.massivelyscalableteam.scalablejavausersmodule.commands.RegisterCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User register(User user) {
        RegisterCommand registerCommand = new RegisterCommand(user, userRepository);
        UserCommandsInvoker<User> invoker = new UserCommandsInvoker<>(registerCommand);
        System.out.println(user);
        return invoker.invoke();
    }



}
