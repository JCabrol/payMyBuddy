package com.openclassrooms.payMyBuddy.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PayMyBuddyExceptionHandler {


    private ResponseEntity<Object> buildResponseEntity(PayMyBuddyError payMyBuddyError) {
        return new ResponseEntity<>(payMyBuddyError.getMessage(), payMyBuddyError.getStatus());
    }


    @ExceptionHandler(NotFoundObjectException.class)
    protected ResponseEntity<Object> handleNotFoundObject(
            NotFoundObjectException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(ObjectNotExistingAnymoreException.class)
    protected ResponseEntity<Object> handleObjectNotExistingAnymore(
            ObjectNotExistingAnymoreException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(GONE);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(ObjectAlreadyExistingException.class)
    protected ResponseEntity<Object> handleObjectAlreadyExisting(
            ObjectAlreadyExistingException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(NothingToDoException.class)
    protected ResponseEntity<Object> handleNothingToDo(
           NothingToDoException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_MODIFIED);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    protected ResponseEntity<Object> handleNotEnoughMoney(
            NotEnoughMoneyException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected ResponseEntity<Object> handleNotAuthorized(
            NotAuthorizedException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(FORBIDDEN);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(NotValidException.class)
    protected ResponseEntity<Object> handleNotValid(
            NotValidException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(EmptyObjectException.class)
    protected ResponseEntity<Object> handleEmptyObject(
            EmptyObjectException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

}
