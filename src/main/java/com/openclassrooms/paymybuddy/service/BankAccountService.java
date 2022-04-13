package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.paymybuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DTO.BankAccountDTO;

public interface BankAccountService {

    BankAccountDTO getBankAccountDTO(String iban);

    BankAccount getBankAccount(String iban) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    BankAccount createBankAccount(BankAccountDTO bankAccountDTO);

    BankAccountDTO transformBankAccountToBankAccountDTO(BankAccount bankAccount);

    boolean ibanAlreadyExists(String iban);
}
