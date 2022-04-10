package com.openclassrooms.payMyBuddy.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayMyBuddyError implements Serializable {

    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;

    PayMyBuddyError(HttpStatus status) {
        this.status = status;
    }
}