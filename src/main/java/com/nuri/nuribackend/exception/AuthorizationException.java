package com.nuri.nuribackend.exception;

public class AuthorizationException extends CustomException {
    public AuthorizationException(String message) {
        super("AUTHZ_ERROR", message);
    }
}
