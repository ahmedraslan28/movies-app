package com.raslan.exception;

public class DuplicatedRowException extends RuntimeException {
    public DuplicatedRowException(String message) {
        super(message);
    }
}
