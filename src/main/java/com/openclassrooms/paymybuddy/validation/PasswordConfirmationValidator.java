package com.openclassrooms.paymybuddy.validation;

import com.openclassrooms.paymybuddy.model.DTO.ChangePasswordDTO;
import com.openclassrooms.paymybuddy.model.DTO.PersonInscriptionDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<ValidPasswordConfirmation,Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        String password;
        String confirmation;
        if (value instanceof ChangePasswordDTO) {
            password = ((ChangePasswordDTO) value).getPassword();
            confirmation = ((ChangePasswordDTO) value).getConfirmationPassword();
        } else {
            password = ((PersonInscriptionDTO) value).getPassword();
            confirmation = ((PersonInscriptionDTO) value).getConfirmationPassword();
        }
            return (password.equals("")||confirmation.equals("")||password.equals(confirmation));
    }
}
