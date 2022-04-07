package com.openclassrooms.payMyBuddy.validation;

import com.openclassrooms.payMyBuddy.model.DTO.ChangePasswordDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonInscriptionDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String password;
        if (value instanceof ChangePasswordDTO) {
            password = ((ChangePasswordDTO) value).getPassword();
        } else {
            password = ((PersonInscriptionDTO) value).getPassword();
        }

        if (password.equals("")) {
            return true;
        } else {
            boolean sizeOk = password.length() >= 8;
            boolean notContainsWhiteSpace = password.chars().noneMatch(Character::isWhitespace);
            boolean containsUpperCase = password.chars().anyMatch(Character::isUpperCase);
            boolean containsLowerCase = password.chars().anyMatch(Character::isLowerCase);
            boolean containsNumber = password.chars().anyMatch(Character::isDigit);
            boolean containsSpecialCharacter = password.chars().anyMatch(c -> (!Character.isLetterOrDigit(c)));
            return sizeOk && notContainsWhiteSpace && containsLowerCase && containsUpperCase && containsNumber && containsSpecialCharacter;
        }
    }
}

