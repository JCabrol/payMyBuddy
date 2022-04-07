package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;

public interface BankAccountService {

    BankAccountDTO getBankAccountDTO(String iban);

    BankAccount getBankAccount(String iban) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    BankAccount createBankAccount(BankAccountDTO bankAccountDTO);

   void deleteBankAccount(BankAccountDTO bankAccountDTO);


    BankAccountDTO transformBankAccountToBankAccountDTO(BankAccount bankAccount);

    String updateBankAccountUsualName(BankAccountDTO bankAccountDTO);

    boolean ibanAlreadyExists(String iban);
}
