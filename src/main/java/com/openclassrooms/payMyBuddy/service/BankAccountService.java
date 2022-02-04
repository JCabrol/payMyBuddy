package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;

public interface BankAccountService {

    BankAccountDTO getBankAccountDTO(String iban);

   String createBankAccount(BankAccountDTO bankAccountDTO);

   String deleteBankAccount(BankAccountDTO bankAccountDTO);


}
