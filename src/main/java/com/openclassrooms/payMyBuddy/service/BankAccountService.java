package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;

public interface BankAccountService {

    /**
     * Get one BankAccountDTO object from its id
     *
     * @param iban a String which is the id of the researched bank account
     * @return the BankAccountDTO object having the researched iban as id
     * @throws NotFoundObjectException           when the researched iban is not registered
     * @throws ObjectNotExistingAnymoreException when the researched iban correspond to an inactive bankAccount
     */
    BankAccountDTO getBankAccountDTO(String iban);

    /**
     * Get one bank account from its id
     *
     * @param iban a String which is the id of the researched bank account
     * @return the BankAccount object having the researched iban as id
     * @throws NotFoundObjectException           when the researched iban is not registered
     * @throws ObjectNotExistingAnymoreException when the researched iban correspond to an inactive bankAccount
     */
    BankAccount getBankAccount(String iban) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    /**
     * Create a bank account with information from a BankAccountDTO object
     *
     * @param bankAccountDTO the BankAccountDTO object containing information to create the bank account
     * @return the BankAccount object created
     */
    BankAccount createBankAccount(BankAccountDTO bankAccountDTO);

    /**
     * Transforms a BankAccount into a BankAccountDTO object to send information to show
     *
     * @param bankAccount the BankAccount object to transform
     * @return a BankAccountDTO object containing all information to show
     */
    BankAccountDTO transformBankAccountToBankAccountDTO(BankAccount bankAccount);

    /**
     * Indicates if an iban is already registered or not
     *
     * @param iban a String which is the id of the researched bank account
     * @return true if the iban is already registered, false otherwise
     */
    boolean ibanAlreadyExists(String iban);
}
