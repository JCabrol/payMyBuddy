package com.openclassrooms.paymybuddy.validation;

import com.openclassrooms.paymybuddy.model.DTO.TransactionBankDTO;
import com.openclassrooms.paymybuddy.model.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, Object> {
    @Autowired
    PersonService personService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Float amount;
        if (value instanceof TransactionDTO) {
            amount = ((TransactionDTO) value).getAmount();
        } else {
            amount = ((TransactionBankDTO) value).getAmount();
        }
        if (amount != null) {
            return (personService.getPerson(personService.getCurrentUserMail()).getAvailableBalance() >= amount);
        } else {
            return true;
        }
    }
}
