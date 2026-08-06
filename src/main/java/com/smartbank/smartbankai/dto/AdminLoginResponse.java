package com.smartbank.smartbankai.dto;

public class AdminLoginResponse {

    private String token;
    private String message;

    public AdminLoginResponse() {
    }

    public AdminLoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}