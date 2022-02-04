package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.*;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionWithBankRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionBetweenPersonsRepository transactionBetweenPersonsRepository;
    @Autowired
    private TransactionWithBankRepository transactionWithBankRepository;
    @Autowired
    private PersonService personService;

    @Override
    public TransactionDTO transformTransactionToTransactionDTO(Transaction transaction) {
        log.debug("The function transformTransactionToTransactionDTO in TransactionService is beginning.");
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        transactionDTO.setTime(transaction.getDateTime().format(DateTimeFormatter.ofPattern("hh:mm")));
        transactionDTO.setDescription(transaction.getDescription());
        if (transaction instanceof TransactionBetweenPersons) {
            transactionDTO.setReceiver(((TransactionBetweenPersons) transaction).getRecipient().getFirstName() + " " + ((TransactionBetweenPersons) transaction).getRecipient().getLastName());
        } else {
            transactionDTO.setReceiver(((TransactionWithBank) transaction).getRecipient().getIban());
        }
        log.debug("The function transformTransactionToTransactionDTO in TransactionService is ending without any exception.");
        return transactionDTO;
    }
@Transactional
    @Override
    public TransactionDTO makeANewTransactionBetweenPersons(PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException {
        log.debug("The function makeANewTransactionBetweenPersons in TransactionService is beginning.");
        Person sender = personService.getPerson(senderDTO.getEmail());
        Person receiver = personService.getPerson(receiverDTO.getEmail());
        if (sender.getRelations().contains(receiver)) {
            float senderMoney = sender.getAvailableBalance();
            float amountToPay = amount * (new ApplicationPaymentRate().getRate());
            if (senderMoney >= amountToPay) {
                sender.setAvailableBalance(senderMoney - amountToPay);
                receiver.setAvailableBalance(receiver.getAvailableBalance() + amount);
                TransactionBetweenPersons transactionBetweenPersons = new TransactionBetweenPersons(sender, amount, receiver, description);
                sender.addTransactionMade(transactionBetweenPersons);
                transactionBetweenPersons = transactionBetweenPersonsRepository.save(transactionBetweenPersons);
                TransactionDTO transactionDTO = transformTransactionToTransactionDTO(transactionBetweenPersons);
                log.debug("The function makeANewTransactionBetweenPersons in TransactionService is ending without any exception.");
                return transactionDTO;
            } else {
                throw new NotEnoughMoneyException("Your available balance is :" + sender.getAvailableBalance() + "\u20ac\n" +
                        "so it's not possible to pay " + amountToPay + "\u20ac.\n");
            }
        } else {
            throw new NotAuthorizedException("The person " + receiver.getFirstName() + " " + receiver.getLastName() + " isn't part of your relations so you can't send money to this person.");
        }
    }

//    @Override
//    public TransactionDTO makeANewTransactionWithBank(PersonDTO senderDTO, float amount, BankAccountDTO receiverDTO) throws NotEnoughMoneyException, NotAuthorizedException {
//        log.debug("The function makeANewTransactionBetweenPersons in TransactionService is beginning.");
//        Person sender = personService.getPerson(senderDTO.getEmail());
//        Person receiver = personService.getPerson(receiverDTO.getEmail());
//        if (sender.getRelations().contains(receiver)) {
//            float senderMoney = sender.getAvailableBalance();
//            float amountToPay = amount * (new ApplicationPaymentRate().getRate());
//            if (senderMoney >= amountToPay) {
//                sender.setAvailableBalance(senderMoney - amountToPay);
//                receiver.setAvailableBalance(receiver.getAvailableBalance() + amount);
//                TransactionBetweenPersons transactionBetweenPersons = new TransactionBetweenPersons(sender, amount, receiver);
//                sender.addTransactionMade(transactionBetweenPersons);
//                transactionBetweenPersons = transactionBetweenPersonsRepository.save(transactionBetweenPersons);
//                TransactionDTO transactionDTO = transformTransactionToTransactionDTO(transactionBetweenPersons);
//                log.debug("The function makeANewTransactionBetweenPersons in TransactionService is ending without any exception.");
//                return transactionDTO;
//            } else {
//                throw new NotEnoughMoneyException("Your available balance is :" + sender.getAvailableBalance() + "\u20ac\n" +
//                        "so it's not possible to pay " + amountToPay + "\u20ac.\n");
//            }
//        } else {
//            throw new NotAuthorizedException("The person " + receiver.getFirstName() + " " + receiver.getLastName() + " isn't part of your relations so you can't send money to this person.");
//        }
//    }

}
