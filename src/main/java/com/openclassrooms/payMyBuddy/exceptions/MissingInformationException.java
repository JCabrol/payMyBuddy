package com.openclassrooms.payMyBuddy.exceptions;

public class MissingInformationException extends RuntimeException {
    public MissingInformationException(String message) {
        super(message);
    }
}
