package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.paymybuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.paymybuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.paymybuddy.model.DTO.PersonDTO;
import com.openclassrooms.paymybuddy.model.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.model.Transaction;

public interface TransactionService {

    TransactionDTO transformTransactionToTransactionDTO(Transaction transaction);

    TransactionDTO makeANewTransactionBetweenPersons (PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;

    TransactionDTO makeANewTransactionWithBank(PersonDTO ownerDTO, float amount, BankAccountDTO bankAccountDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;
}
