package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("ServiceTests")
@Slf4j
@SpringBootTest
public class BankAccountServiceTest {
    @Autowired
    private BankAccountService bankAccountService;

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Nested
    @Tag("BankAccountServiceTests")
    @DisplayName("getBankAccount tests:")
    class GetBankAccountTest {

        @DisplayName("GIVEN an existing iban " +
                "WHEN the function getBankAccount() is called " +
                "THEN the corresponding bank account is returned.")
        @Test
        void getBankAccountTest() {
            //GIVEN
            // an existing iban
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function getBankAccount() is called
            BankAccount result = bankAccountService.getBankAccount(iban);
            //THEN
            //the corresponding bank account is returned
            assertThat(result.getIban()).isEqualTo(bankAccount.getIban());
            assertThat(result.getBic()).isEqualTo(bankAccount.getBic());
            assertThat(result.getUsualName()).isEqualTo(bankAccount.getUsualName());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }

        @DisplayName("GIVEN a not active bank account " +
                "WHEN the function getBankAccount() is called " +
                "THEN an ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void getBankAccountNotActiveTest() {
            //GIVEN
            // a not active bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            bankAccount.setActiveBankAccount(false);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function getBankAccount() is called
            //THEN
            //an ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> bankAccountService.getBankAccount(iban));
            assertEquals("The bank account with IBAN number " + iban + " has been deleted from application.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }

        @DisplayName("GIVEN a not existing bank account " +
                "WHEN the function getBankAccount() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void getBankAccountNotExistingTest() {
            //GIVEN
            // a not existing bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            bankAccount.setActiveBankAccount(false);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.empty());
            //WHEN
            // the function getBankAccount() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> bankAccountService.getBankAccount(iban));
            assertEquals("The bank account with IBAN number " + iban + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }
    }

    @Nested
    @Tag("BankAccountServiceTests")
    @DisplayName("getBankAccountDTO tests:")
    class GetBankAccountDTOTest {

        @DisplayName("GIVEN an existing iban " +
                "WHEN the function getBankAccountDTO() is called " +
                "THEN the corresponding BankAccountDTO is returned.")
        @Test
        void getBankAccountDTOTest() {
            //GIVEN
            // an existing iban
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function getBankAccountDTO() is called
            BankAccountDTO result = bankAccountService.getBankAccountDTO(iban);
            //THEN
            //the corresponding bankAccountDTO is returned
            assertThat(result.getIban()).isEqualTo(bankAccount.getIban());
            assertThat(result.getBic()).isEqualTo(bankAccount.getBic());
            assertThat(result.getUsualName()).isEqualTo(bankAccount.getUsualName());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }

        @DisplayName("GIVEN a not active bank account " +
                "WHEN the function getBankAccountDTO() is called " +
                "THEN an ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void getBankAccountDTONotActiveTest() {
            //GIVEN
            // a not active bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            bankAccount.setActiveBankAccount(false);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function getBankAccount() is called
            //THEN
            //an ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> bankAccountService.getBankAccountDTO(iban));
            assertEquals("The bank account with IBAN number " + iban + " has been deleted from application.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }

        @DisplayName("GIVEN a not existing bank account " +
                "WHEN the function getBankAccountDTO() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void getBankAccountDTONotExistingTest() {
            //GIVEN
            // a not existing bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            bankAccount.setActiveBankAccount(false);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.empty());
            //WHEN
            // the function getBankAccountDTO() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> bankAccountService.getBankAccount(iban));
            assertEquals("The bank account with IBAN number " + iban + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }
    }

    @Nested
    @Tag("BankAccountServiceTests")
    @DisplayName("ibanAlreadyExists tests:")
    class IbanAlreadyExistsTest {

        @DisplayName("GIVEN an existing iban " +
                "WHEN the function ibanAlreadyExists() is called " +
                "THEN it returns true.")
        @Test
        void existsByIdExistingTest() {
            //GIVEN
            // an existing iban
            String iban = "iban";
            when(bankAccountRepository.existsById(iban)).thenReturn(true);
            //WHEN
            // the function ibanAlreadyExists() is called
            boolean result = bankAccountService.ibanAlreadyExists(iban);
            //THEN
            //it returns true
            assertTrue(result);
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).existsById(iban);
        }

        @DisplayName("GIVEN an non-existing iban " +
                "WHEN the function ibanAlreadyExists() is called " +
                "THEN it returns false.")
        @Test
        void existByIdNotExistingTest() {
            //GIVEN
            // an non-existing iban
            String iban = "iban";
            when(bankAccountRepository.existsById(iban)).thenReturn(false);
            //WHEN
            // the function ibanAlreadyExists() is called
            boolean result = bankAccountService.ibanAlreadyExists(iban);
            //THEN
            //it returns true
            assertFalse(result);
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).existsById(iban);
        }
    }

    @Nested
    @Tag("BankAccountServiceTests")
    @DisplayName("transformBankAccountToBankAccountDTO tests:")
    class TransformBankAccountToBankAccountDTOTest {

        @DisplayName("GIVEN a bank account " +
                "WHEN the function transformBankAccountToBankAccountDTO() is called " +
                "THEN it returns the corresponding BankAccountDTO.")
        @Test
        void transformBankAccountToBankAccountDTOTest() {
            //GIVEN
            //a bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            //WHEN
            // the function transformBankAccountToBankAccountDTO() is called
            BankAccountDTO result = bankAccountService.transformBankAccountToBankAccountDTO(bankAccount);
            //THEN
            //it returns the corresponding BankAccountDTO
            assertThat(result.getIban()).isEqualTo(iban);
            assertThat(result.getBic()).isEqualTo(bic);
            assertThat(result.getUsualName()).isEqualTo(usualName);
        }
    }

    @Nested
    @Tag("BankAccountServiceTests")
    @DisplayName("createBankAccount tests:")
    class CreateBankAccountTest {

        @DisplayName("GIVEN a bank account DTO with iban no existing" +
                "WHEN the function createBankAccount() is called " +
                "THEN it returns the new created BankAccount.")
        @Test
        void createBankAccountTest() {
            //GIVEN
            // a bank account DTO with iban no existing
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic,usualName);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.empty());
            //WHEN
            // the function createBankAccount() is called
            BankAccount result = bankAccountService.createBankAccount(bankAccountDTO);
            //THEN
            //it returns the new created BankAccount
            assertThat(result.getIban()).isEqualTo(iban);
            assertThat(result.getBic()).isEqualTo(bic);
            assertThat(result.getUsualName()).isEqualTo(usualName);
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }

        @DisplayName("GIVEN a bank account DTO with iban existing and corresponding to a not active bank account" +
                "WHEN the function createBankAccount() is called " +
                "THEN it returns the BankAccount which is reactivated.")
        @Test
        void createBankAccountInactiveTest() {
            //GIVEN
            //a bank account DTO with iban existing and corresponding to a not active bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic,usualName);
            BankAccount bankAccount = new BankAccount(iban, bic,usualName);
            bankAccount.setActiveBankAccount(false);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function createBankAccount() is called
            BankAccount result = bankAccountService.createBankAccount(bankAccountDTO);
            //THEN
            //it returns the BankAccount which is reactivated
            assertThat(result.getIban()).isEqualTo(iban);
            assertThat(result.getBic()).isEqualTo(bic);
            assertThat(result.getUsualName()).isEqualTo(usualName);
            assertTrue(result.isActiveBankAccount());
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(2)).findById(iban);
        }

        @DisplayName("GIVEN a bank account DTO with iban existing and corresponding to an active bank account" +
                "WHEN the function createBankAccount() is called " +
                "THEN it returns the BankAccount unchanged.")
        @Test
        void createBankAccountActiveTest() {
            //GIVEN
            //a bank account DTO with iban existing and corresponding to an active bank account
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic,usualName);
            BankAccount bankAccount = new BankAccount(iban, bic,usualName);
            when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));
            //WHEN
            // the function createBankAccount() is called
            BankAccount result = bankAccountService.createBankAccount(bankAccountDTO);
            //THEN
            //it returns the BankAccount unchanged
            assertThat(result.getIban()).isEqualTo(iban);
            assertThat(result.getBic()).isEqualTo(bic);
            assertThat(result.getUsualName()).isEqualTo(usualName);
            //and the expected methods have been called with expected arguments
            verify(bankAccountRepository, Mockito.times(1)).findById(iban);
        }
    }



}
