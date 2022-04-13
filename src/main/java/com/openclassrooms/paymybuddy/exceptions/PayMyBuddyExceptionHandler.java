package com.openclassrooms.paymybuddy.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PayMyBuddyExceptionHandler extends ResponseEntityExceptionHandler {


    @Autowired
    MessageSource messageSource;

    private ModelAndView buildErrorPage(PayMyBuddyError payMyBuddyError) {
        String viewName = "error";
        Map<String, Object> model = new HashMap<>();
        model.put("status", payMyBuddyError.getStatus());
        model.put("message", payMyBuddyError.getMessage());
        return new ModelAndView(viewName, model);
    }

    /**
     * {@link BindException}Handling
     *
     * @param bindException {@link BindException}
     * @param httpHeaders   {@link HttpHeaders}
     * @param httpStatus    {@link HttpStatus}
     * @param webRequest    {@link WebRequest}
     * @return Response to client
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException bindException,
            HttpHeaders httpHeaders,
            HttpStatus httpStatus,
            WebRequest webRequest
    ) {
        //Error list stored in the response body
        List<PayMyBuddyError> payMyBuddyErrorList = new ArrayList<>();

        List<ObjectError> objectErrorList = bindException.getAllErrors();

        for (ObjectError objectError : objectErrorList) {

            //Get message from error code
            String message = messageSource.getMessage(objectError, webRequest.getLocale());

            //Create an error object for the response body and store it in the list
            PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
            payMyBuddyError.setMessage(message);
            payMyBuddyErrorList.add(payMyBuddyError);
        }
        return new ResponseEntity<>(payMyBuddyErrorList, httpHeaders, httpStatus);
    }


    @ExceptionHandler(SQLException.class)
    protected ModelAndView handleSQL(
            SQLException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        String errorMessage = "Something went wrong with database: " + ex.getMessage().substring(0, ex.getMessage().indexOf("\n")) + "\n";
        payMyBuddyError.setMessage(errorMessage);
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ModelAndView handleConstraintViolation(
            ConstraintViolationException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        String message = "There is something wrong in the given data :\n" + ex.getConstraintViolations().stream().map(constraintViolation -> "- " + constraintViolation.getMessageTemplate() + "\n").collect(Collectors.joining());
        payMyBuddyError.setMessage(message);
        log.error(message);
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(NotFoundObjectException.class)
    protected ModelAndView handleNotFoundObject(
            NotFoundObjectException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(ObjectNotExistingAnymoreException.class)
    protected ModelAndView handleObjectNotExistingAnymore(
            ObjectNotExistingAnymoreException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(GONE);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(ObjectAlreadyExistingException.class)
    protected ModelAndView handleObjectAlreadyExisting(
            ObjectAlreadyExistingException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(NothingToDoException.class)
    protected ModelAndView handleNothingToDo(
            NothingToDoException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_MODIFIED);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    protected ModelAndView handleNotEnoughMoney(
            NotEnoughMoneyException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected ModelAndView handleNotAuthorized(
            NotAuthorizedException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(FORBIDDEN);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(NotValidException.class)
    protected ModelAndView handleNotValid(
            NotValidException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(EmptyObjectException.class)
    protected ModelAndView handleEmptyObject(
            EmptyObjectException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }

    @ExceptionHandler(MissingInformationException.class)
    protected ModelAndView handleMissingInformation(
            MissingInformationException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildErrorPage(payMyBuddyError);
    }
}
