package com.openclassrooms.payMyBuddy.exceptions;

import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private ResponseEntity<Object> buildResponseEntity(PayMyBuddyError payMyBuddyError) {
        return new ResponseEntity<>(payMyBuddyError.getMessage(), payMyBuddyError.getStatus());
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
//            return buildResponseEntity(payMyBuddyError);
            return new ResponseEntity<>(payMyBuddyErrorList, httpHeaders, httpStatus);
        }
//
//    @ExceptionHandler(NotEnoughMoneyException.class)
//    protected ResponseEntity<Object> handleNotEnoughMoney(
//            NotEnoughMoneyException ex,
//            HttpHeaders httpHeaders,
//            HttpStatus httpStatus,
//            WebRequest webRequest) {
//        List<PayMyBuddyError> payMyBuddyErrorList = new ArrayList<>();
//        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
//        payMyBuddyError.setMessage(ex.getMessage());
//        log.error(ex.getMessage());
//        payMyBuddyErrorList.add(payMyBuddyError);
//        return new ResponseEntity<>(payMyBuddyErrorList, httpHeaders, httpStatus);
////        return buildResponseEntity(payMyBuddyError);
//    }




    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSQL(
            SQLException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(NOT_FOUND);
        String errorMessage = "Something went wrong with database: " + ex.getMessage().substring(0, ex.getMessage().indexOf("\n")) + "\n";
        payMyBuddyError.setMessage(errorMessage);
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        String message = "There is something wrong in the given data :\n" + ex.getConstraintViolations().stream().map(constraintViolation -> "- " + constraintViolation.getMessageTemplate() + "\n").collect(Collectors.joining());
        payMyBuddyError.setMessage(message);
        log.error(message);
        return buildResponseEntity(payMyBuddyError);
    }


//    @Override
//    @ExceptionHandler(BindException.class)
//    protected ResponseEntity<Object> handleBind(
//            BindException ex) {
//        System.out.println("in exception handler");
//        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
//       String message= ex.getMessage();
//        log.error(message);
//        return buildResponseEntity(payMyBuddyError);
//    }

//    @ExceptionHandler(BindException.class)
//    protected ModelAndView handleBind(
//            BindException ex) {
//        System.out.println("in exception handler");
//        BindingResult bindingResult=ex.getBindingResult();
//        Map<String, Object> model = new HashMap<>();
//        model.put("bindingResult", bindingResult);
//        model.put("personDTO", new PersonDTO());
//        return new ModelAndView("inscription",model);
//    }



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

//    @ExceptionHandler(NotEnoughMoneyException.class)
//    protected ModelAndView handleNotEnoughMoney(
//            NotEnoughMoneyException ex) {
//        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
//        payMyBuddyError.setMessage(ex.getMessage());
//        log.error(ex.getMessage());
//        PersonService personService = new PersonServiceImpl();
//        String mail = personService.getCurrentUserMail();
//        PersonDTO personDTO = personService.getPersonDTO(mail);
//        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("person", personDTO);
//        model.put("error", ex.getMessage());
//        return new ModelAndView("transfer",model);
//    }
//
    @ExceptionHandler(NotEnoughMoneyException.class)
    protected ResponseEntity<Object> handleNotEnoughMoney(
            NotEnoughMoneyException ex) {
        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
        payMyBuddyError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(payMyBuddyError);
    }

//    public ModelAndView handleNotEnoughMoney(HttpServletRequest req, NotEnoughMoneyException ex) {
//        PayMyBuddyError payMyBuddyError = new PayMyBuddyError(BAD_REQUEST);
//        payMyBuddyError.setMessage(ex.getMessage());
//        log.error(ex.getMessage());
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", ex);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName("transfer");
//        return mav;
//    }


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
