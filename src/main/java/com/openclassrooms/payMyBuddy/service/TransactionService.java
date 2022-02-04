package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Transaction;

import java.util.List;

public interface TransactionService {

    TransactionDTO transformTransactionToTransactionDTO(Transaction transaction);

    TransactionDTO makeANewTransactionBetweenPersons (PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException;

//    TransactionDTO makeANewTransactionWithBank (PersonDTO sender, float amount, BankAccountDTO receiverDTO) throws NotEnoughMoneyException, NotAuthorizedException;

}
