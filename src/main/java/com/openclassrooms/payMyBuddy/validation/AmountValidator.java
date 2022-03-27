package com.openclassrooms.payMyBuddy.validation;

import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, TransactionDTO> {
    @Autowired
    PersonService personService;

    @Override
    public boolean isValid(TransactionDTO value, ConstraintValidatorContext context) {
        if (value.getAmount() != null) {
            return (personService.getPerson(personService.getCurrentUserMail()).getAvailableBalance() >= value.getAmount());
        } else {
            return true;
        }
    }
}
