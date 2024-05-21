package com.massivelyscalableteam.scalablejavausersmodule;

import com.massivelyscalableteam.scalablejavausersmodule.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class ScalableJavaUsersModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScalableJavaUsersModuleApplication.class, args);
	}

}
