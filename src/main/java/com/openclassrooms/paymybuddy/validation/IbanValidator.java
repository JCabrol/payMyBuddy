package com.openclassrooms.paymybuddy.validation;

import com.openclassrooms.paymybuddy.model.DTO.BankAccountDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IbanValidator implements ConstraintValidator<ValidIban, BankAccountDTO> {

    @Override
    public boolean isValid(BankAccountDTO value, ConstraintValidatorContext context) {
       String iban = value.getIban();
       if(iban.equals("")){
           return true;
       }else {
           Pattern patternFrenchIban = Pattern.compile("FR\\w{25}", Pattern.CASE_INSENSITIVE);
           Pattern patternGermanIban = Pattern.compile("DE\\w{20}", Pattern.CASE_INSENSITIVE);
           Pattern patternBelgianIban = Pattern.compile("BE\\w{14}", Pattern.CASE_INSENSITIVE);
           Pattern patternSpanishIban = Pattern.compile("ES\\w{22}", Pattern.CASE_INSENSITIVE);
           Matcher matcherFrench = patternFrenchIban.matcher(iban);
           Matcher matcherGerman = patternGermanIban.matcher(iban);
           Matcher matcherBelgian = patternBelgianIban.matcher(iban);
           Matcher matcherSpanish = patternSpanishIban.matcher(iban);
           return matcherFrench.find() || matcherBelgian.find() || matcherSpanish.find() || matcherGerman.find();
       }
    }
}