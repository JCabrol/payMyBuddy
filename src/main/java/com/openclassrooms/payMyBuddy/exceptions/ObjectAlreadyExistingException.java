package com.openclassrooms.payMyBuddy.exceptions;

public class ObjectAlreadyExistingException extends RuntimeException{
    public ObjectAlreadyExistingException(String message) {
        super(message);
    }
}
