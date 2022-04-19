package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * Get one BankAccountDTO object from its id
     *
     * @param iban a String which is the id of the researched bank account
     * @return the BankAccountDTO object having the researched iban as id
     * @throws NotFoundObjectException           when the researched iban is not registered
     * @throws ObjectNotExistingAnymoreException when the researched iban correspond to an inactive bankAccount
     */
    @Override
    public BankAccountDTO getBankAccountDTO(String iban) {
        log.debug("The function getBankAccountDTO in BankAccountService is beginning.");
        BankAccount bankAccount = getBankAccount(iban);
        BankAccountDTO bankAccountDTO = transformBankAccountToBankAccountDTO(bankAccount);
        log.debug("The function getBankAccountDTO in BankAccountService is ending without exception.");
        return bankAccountDTO;
    }

    /**
     * Get one bank account from his id
     *
     * @param iban a String which is the id of the researched bank account
     * @return the BankAccount object having the researched iban as id
     * @throws NotFoundObjectException           when the researched iban is not registered
     * @throws ObjectNotExistingAnymoreException when the researched iban correspond to an inactive bankAccount
     */
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

    /**
     * Create a bank account with information from a BankAccountDTO object
     *
     * @param bankAccountDTO the BankAccountDTO object containing information to create the bank account
     * @return the BankAccount object created
     */
    @Override
    public BankAccount createBankAccount(BankAccountDTO bankAccountDTO) {
        log.debug("The function createBankAccount in BankAccountService is beginning.");
        String iban = bankAccountDTO.getIban();
        BankAccount bankAccount = null;
        try {
           bankAccount= getBankAccount(iban);
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

    /**
     * Transforms a BankAccount into a BankAccountDTO object to send information to show
     *
     * @param bankAccount the BankAccount object to transform
     * @return a BankAccountDTO object containing all information to show
     */
    @Override
    public BankAccountDTO transformBankAccountToBankAccountDTO(BankAccount bankAccount) {
        log.debug("The function transformBankAccountToBankAccountDTO in BankAccountService is beginning.");
        BankAccountDTO bankAccountDTO = new BankAccountDTO(bankAccount.getIban(), bankAccount.getBic(), bankAccount.getUsualName());
        log.debug("The function transformBankAccountToBankAccountDTO in BankAccountService is ending without exception.");
        return bankAccountDTO;
    }

    /**
     * Indicates if an iban is already registered or not
     *
     * @param iban a String which is the id of the researched bank account
     * @return true if the iban is already registered, false otherwise
     */
    @Override
    public boolean ibanAlreadyExists(String iban) {
        return bankAccountRepository.existsById(iban);
    }
}
