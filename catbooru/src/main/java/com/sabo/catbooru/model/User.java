package com.sabo.catbooru.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String token;
    private String password;


    public User(UUID id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User(@JsonProperty String username, @JsonProperty String password) {
        this.username = username;
        this.password = password;
        this.id = UUID.randomUUID();
    }

    public void generateId(){
        this.id = UUID.randomUUID();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
