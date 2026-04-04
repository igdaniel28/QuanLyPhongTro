package com.example.bigproject1.exception;

public class DuplicateRoomCodeException extends RuntimeException {
    public DuplicateRoomCodeException(String message) {
        super(message);
    }
}