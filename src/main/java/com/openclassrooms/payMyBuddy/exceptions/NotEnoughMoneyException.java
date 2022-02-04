package com.openclassrooms.payMyBuddy.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
