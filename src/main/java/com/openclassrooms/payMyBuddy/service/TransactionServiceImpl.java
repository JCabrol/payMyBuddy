package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.*;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionWithBankRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;

import static java.lang.Math.abs;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionBetweenPersonsRepository transactionBetweenPersonsRepository;
    @Autowired
    private TransactionWithBankRepository transactionWithBankRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public TransactionDTO transformTransactionToTransactionDTO(Transaction transaction) {
        log.debug("The function transformTransactionToTransactionDTO in TransactionService is beginning.");
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSender(transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName());
        float amount = transaction.getAmount();

        transactionDTO.setAmount(abs(amount));
        transactionDTO.setDate(transaction.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        transactionDTO.setTime(transaction.getDateTime().format(DateTimeFormatter.ofPattern("hh:mm")));
        transactionDTO.setDescription(transaction.getDescription());
        if (transaction instanceof TransactionBetweenPersons) {
            transactionDTO.setReceiver(((TransactionBetweenPersons) transaction).getRecipient().getFirstName() + " " + ((TransactionBetweenPersons) transaction).getRecipient().getLastName());
        } else {
            if(amount>=0) {
                transactionDTO.setReceiver("From bank account \"" + ((TransactionWithBank) transaction).getRecipient().getUsualName() + "\"- n°" + ((TransactionWithBank) transaction).getRecipient().getIban());
            }else{ transactionDTO.setReceiver("To bank account \"" + ((TransactionWithBank) transaction).getRecipient().getUsualName() + "\"- n°" + ((TransactionWithBank) transaction).getRecipient().getIban());}
        }
        log.debug("The function transformTransactionToTransactionDTO in TransactionService is ending without any exception.");
        return transactionDTO;
    }
@Transactional
    @Override
    public TransactionDTO makeANewTransactionBetweenPersons(PersonDTO senderDTO, float amount, PersonDTO receiverDTO, String description) throws NotEnoughMoneyException, NotAuthorizedException {
        log.debug("The function makeANewTransactionBetweenPersons in TransactionService is beginning.");
        String receiverMail = receiverDTO.getEmail();
        String senderMail = senderDTO.getEmail();
        Person sender = personService.getPerson(senderMail);
        Person receiver = personService.getPerson(receiverMail);
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
                throw new NotEnoughMoneyException("Your available balance is :" + senderMoney + "\u20ac\n" +
                        "so it's not possible to pay " + amountToPay + "\u20ac.\n");
            }
        } else {
            throw new NotAuthorizedException("The person " + receiverMail + " isn't part of " + senderMail + "'s relations so the transaction couldn't have been done.\n");
        }
    }

    @Override
    public TransactionDTO makeANewTransactionWithBank(PersonDTO ownerDTO, float amount, BankAccountDTO bankAccountDTO,String description) throws NotEnoughMoneyException, NotAuthorizedException {
        log.debug("The function makeANewTransactionBetweenPersons in TransactionService is beginning.");
        String mail = ownerDTO.getEmail();
        Person owner = personService.getPerson(mail);
        String iban = bankAccountDTO.getIban();
        BankAccount bankAccount = bankAccountService.getBankAccount(iban);
        if (owner.getBankAccountList().contains(bankAccount)) {
                owner.setAvailableBalance(owner.getAvailableBalance() + amount);

                TransactionWithBank transactionWithBank = new TransactionWithBank(owner, amount, bankAccount,description);
                owner.addTransactionMade(transactionWithBank);
                transactionWithBank = transactionWithBankRepository.save(transactionWithBank);
                TransactionDTO transactionDTO = transformTransactionToTransactionDTO(transactionWithBank);
                log.debug("The function makeANewTransactionBetweenPersons in TransactionService is ending without any exception.");
                return transactionDTO;
            } else  {
            throw new NotAuthorizedException("The bank account " + iban + " doesn't belong to " + mail + " so the transaction couldn't have been done.\n");
        }
    }

}
