package com.example.demo;
import java.util.UUID;


public class Token {

    private String token;

    public String getToken() {
        return token;
    }

    public Token() {
        UUID uuid = UUID.randomUUID();
        this.token = uuid.toString();
    }
}
