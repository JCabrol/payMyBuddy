package com.openclassrooms.payMyBuddy.exceptions;

public class NothingToDoException extends RuntimeException {
    public NothingToDoException(String message) {
        super(message);
    }
}
