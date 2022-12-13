package com.nd2k.authenticationapi.model.exception;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
