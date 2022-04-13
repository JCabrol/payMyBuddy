package com.openclassrooms.paymybuddy.exceptions;

public class NothingToDoException extends RuntimeException {
    public NothingToDoException(String message) {
        super(message);
    }
}
