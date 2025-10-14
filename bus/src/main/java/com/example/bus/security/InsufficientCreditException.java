package com.example.bus.security;

public class InsufficientCreditException extends RuntimeException {
    public InsufficientCreditException(String msg) {
        super(msg);
    }
}