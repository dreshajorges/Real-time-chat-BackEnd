package com.example.realtimechat.infrastructure.exceptions;

public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
