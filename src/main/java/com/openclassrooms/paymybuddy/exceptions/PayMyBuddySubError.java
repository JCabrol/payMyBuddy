package com.openclassrooms.paymybuddy.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

abstract class PayMyBuddySubError {
}


@Data
@EqualsAndHashCode(callSuper = false)
class PayMyBuddyValidationError extends PayMyBuddySubError {

    private String message;

    PayMyBuddyValidationError(String message) {
        this.message = message;
    }
}