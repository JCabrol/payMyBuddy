package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.MissingInformationException;
import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Transaction;

public interface TransactionService {

    /**
     * Transform a Transaction into a TransactionDTO object
     *
     * @param transaction a Transaction object which is the transaction to transform. It can be either a TransactionBetweenPerson or a TransactionWithBankObject
     * @return a transactionDTO object containing all information to show about the transaction
     * @throws MissingInformationException when there is missing information in the transaction
     */
    TransactionDTO transformTransactionToTransactionDTO(Transaction transaction);

    /**
     * Make a transaction between two persons
     *
     * @param senderDTO a PersonDTO object representing the person who send money
     * @param amount a float representing the amount to transfer
     * @param receiverDTO a PersonDTO object representing the person who receive the money
     * @param description a String to describe what is the transaction for
     * @return a transactionDTO object containing all information to show about the effectuated transaction
     * @throws NotEnoughMoneyException when the transaction amount is higher than the sender's available balance
     * @throws NotAuthorizedException when the receiver doesn't belong to the sender's group
     */
    TransactionDTO makeANewTransactionBetweenPersons (PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;

    /**
     * Make a transaction between a person and one of its bank accounts
     *
     * @param ownerDTO a PersonDTO object representing the person possessing the account
     * @param amount a float representing the amount to transfer
     * @param bankAccountDTO a BankAccountDTO object representing the bank account with which the transaction is made
     * @param description a String to describe what is the transaction for
     * @return a transactionDTO object containing all information to show about the effectuated transaction
     * @throws NotAuthorizedException when the bank account doesn't belong to the person
     */
    TransactionDTO makeANewTransactionWithBank(PersonDTO ownerDTO, float amount, BankAccountDTO bankAccountDTO, String description) throws NotAuthorizedException;
}
