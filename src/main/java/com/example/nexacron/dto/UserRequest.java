package com.example.nexacron.dto;

import lombok.Data;

public class UserRequest {

    private String email;

    private String password;

    private String name;

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
}