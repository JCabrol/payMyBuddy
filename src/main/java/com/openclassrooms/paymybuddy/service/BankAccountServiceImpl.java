package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.paymybuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.paymybuddy.model.BankAccount;
import com.openclassrooms.paymybuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.paymybuddy.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public BankAccountDTO getBankAccountDTO(String iban) {
        log.debug("The function getBankAccountDTO in BankAccountService is beginning.");
        BankAccount bankAccount = getBankAccount(iban);
        BankAccountDTO bankAccountDTO = transformBankAccountToBankAccountDTO(bankAccount);
        log.debug("The function getBankAccountDTO in BankAccountService is ending without exception.");
        return bankAccountDTO;
    }

    @Override
    public BankAccount getBankAccount(String iban) throws NotFoundObjectException, ObjectNotExistingAnymoreException {
        log.debug("The function getBankAccount in BankAccountService is beginning.");
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(iban);
        if (bankAccountOptional.isPresent()) {
            BankAccount bankAccount = bankAccountOptional.get();
            if (bankAccount.isActiveBankAccount()) {
                log.debug("The function getBankAccount in BankAccountService is ending without exception.");
                return bankAccount;
            } else {
                throw new ObjectNotExistingAnymoreException("The bank account with IBAN number " + iban + " has been deleted from application.");
            }
        } else {
            throw new NotFoundObjectException("The bank account with IBAN number " + iban + " was not found.\n");
        }

    }

    @Override
    public BankAccount createBankAccount(BankAccountDTO bankAccountDTO) {
        log.debug("The function createBankAccount in BankAccountService is beginning.");
        String iban = bankAccountDTO.getIban();
        BankAccount bankAccount = null;
        try {
            getBankAccount(iban);
        } catch (ObjectNotExistingAnymoreException exception) {
            Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(iban);
            if (bankAccountOptional.isPresent()) {
                bankAccount = bankAccountOptional.get();
                bankAccount.setActiveBankAccount(true);
                bankAccount.setUsualName(bankAccountDTO.getUsualName());
            }
        } catch (NotFoundObjectException exception) {
            bankAccount = new BankAccount(iban, bankAccountDTO.getBic(), bankAccountDTO.getUsualName());
        }
        String message = "The bank account with IBAN number " + iban + "has been created.";
        log.info(message);
        log.debug("The function createBankAccount in BankAccountService is ending without exception.");
        return bankAccount;
    }

    @Override
    public BankAccountDTO transformBankAccountToBankAccountDTO(BankAccount bankAccount) {
        log.debug("The function transformBankAccountToBankAccountDTO in BankAccountService is beginning.");
        BankAccountDTO bankAccountDTO = new BankAccountDTO(bankAccount.getIban(), bankAccount.getBic(), bankAccount.getUsualName());
        log.debug("The function transformBankAccountToBankAccountDTO in BankAccountService is ending without exception.");
        return bankAccountDTO;
    }

    @Override
    public boolean ibanAlreadyExists(String iban) {
        return bankAccountRepository.existsById(iban);
    }
}