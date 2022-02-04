package com.openclassrooms.payMyBuddy.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class PayMyBuddyError {

    private HttpStatus status;

    private String message;

    PayMyBuddyError(HttpStatus status) {
        this.status = status;
    }

    PayMyBuddyError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}