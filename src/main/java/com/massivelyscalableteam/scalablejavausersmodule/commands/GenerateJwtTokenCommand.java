package com.massivelyscalableteam.scalablejavausersmodule.commands;
import com.massivelyscalableteam.scalablejavausersmodule.commands.Command;
import com.massivelyscalableteam.scalablejavausersmodule.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GenerateJwtTokenCommand {


//    @Value("${jwt.secret}")
//    private String secretKey = "Y3t7OHAoQ/eFRB1ZrWSBSrroPOW7I0D3jw6cd23kiHzZobeEOaqM/6Bh3ocCQwotJ6VLtaYUmN0LUPY5QYYeJw";
    private String username;


    private final JwtConfig jwtConfig;

    @Autowired
    public GenerateJwtTokenCommand(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    public void setUserName(String username){
        this.username = username;
    }


    public String execute() {
        String secretKey = jwtConfig.getSecretKey();
        System.out.println(secretKey);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 2)) // 2 days
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
