package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public BankAccountDTO getBankAccountDTO(String iban) {

        return null;
    }

    @Override
    public String createBankAccount(BankAccountDTO bankAccountDTO) {
        return null;
    }

    @Override
    public String deleteBankAccount(BankAccountDTO bankAcocuntDTO) {
        return null;
    }
}
