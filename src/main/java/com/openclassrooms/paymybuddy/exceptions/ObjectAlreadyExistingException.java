package com.openclassrooms.paymybuddy.exceptions;

public class ObjectAlreadyExistingException extends RuntimeException{
    public ObjectAlreadyExistingException(String message) {
        super(message);
    }
}
