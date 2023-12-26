package com.example.unicarapp.data.model;

public class AuthenticationResult {
    private boolean success;
    private String errorMessage;

    public AuthenticationResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
