package com.massivelyscalableteam.scalablejavausersmodule.user.dto;

public class UpdateUserDto {
    private String full_name;
    private String password;
    private String email;

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "full_name='" + full_name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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



    public UpdateUserDto(String full_name, String password, String email) {
        this.full_name = full_name;
        this.password = password;
        this.email = email;
    }
}
