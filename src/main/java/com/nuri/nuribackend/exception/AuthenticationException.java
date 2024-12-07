package com.nuri.nuribackend.exception;

public class AuthenticationException extends CustomException {
    private final String errorCode;
    public AuthenticationException(String errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
