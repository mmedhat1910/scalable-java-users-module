package com.massivelyscalableteam.scalablejavausersmodule.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    String user_id;
    String username;
    String password;
    String email;
    String full_name;



    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    String session;

    public User() {}

    public User(String username){
        this.username = username;
    }

    public User(String username, String password, String email, String full_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.full_name = full_name;
    }

    public static List<User> mapToUserList(List<Map<String, String>> users) {
        return users.stream().map(User::new).toList();
    }

    public static List<Map<String, String>> userListToMap(List<User> users) {
        List<Map<String, String>> result = new ArrayList<>();
        for (User user: users) {
            result.add(user.toMap());
        }
        return result;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("username", username);
        map.put("email", email);
        map.put("full_name", full_name);
        map.put("session", session);
        return map;
    }

    public User(Map<String, String> map) {
        this.user_id = map.get("user_id");
        this.username = map.get("username");
        this.password = map.get("password");
        this.email = map.get("email");
        this.full_name = map.get("full_name");
        this.session = map.get("session");
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                '}';
    }
}
