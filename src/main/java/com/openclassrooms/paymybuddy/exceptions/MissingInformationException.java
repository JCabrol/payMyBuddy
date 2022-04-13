package com.openclassrooms.paymybuddy.exceptions;

public class MissingInformationException extends RuntimeException {
    public MissingInformationException(String message) {
        super(message);
    }
}
