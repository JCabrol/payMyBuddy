package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Transaction;

public interface TransactionService {

    TransactionDTO transformTransactionToTransactionDTO(Transaction transaction);

    TransactionDTO makeANewTransactionBetweenPersons (PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;

    TransactionDTO makeANewTransactionWithBank(PersonDTO ownerDTO, float amount, BankAccountDTO bankAccountDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;
}
