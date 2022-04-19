package com.openclassrooms.payMyBuddy.unitTests;


import com.openclassrooms.payMyBuddy.exceptions.MissingInformationException;
import com.openclassrooms.payMyBuddy.exceptions.NotAuthorizedException;
import com.openclassrooms.payMyBuddy.exceptions.NotEnoughMoneyException;
import com.openclassrooms.payMyBuddy.model.*;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.repository.TransactionBetweenPersonsRepository;
import com.openclassrooms.payMyBuddy.repository.TransactionWithBankRepository;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("ServiceTests")
@Slf4j
@SpringBootTest
public class TransactionServiceTest {


    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionBetweenPersonsRepository transactionBetweenPersonsRepository;

    @MockBean
    private TransactionWithBankRepository transactionWithBankRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private BankAccountService bankAccountService;

    @Nested
    @Tag("TransactionServiceTests")
    @DisplayName("makeANewTransactionWithBank tests:")
    class MakeANewTransactionWithBankTest {

        @DisplayName("GIVEN a transaction with a bank account belonging to the person and a credit from bank " +
                "WHEN the function makeANewTransactionWithBank() is called " +
                "THEN the transaction has been effectuated and a TransactionDTO object is returned with correct information.")
        @Test
        void makeANewTransactionWithBankCreditTest() {
            //GIVEN
            //a transaction with a bank account belonging to the person and a credit from bank
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String description = "description";
            float amount = 50F;
            float availableBalance = 60.25F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            person1.setAvailableBalance(availableBalance);
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            person1.setBankAccountList(List.of(bankAccount));
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            TransactionWithBank transaction = new TransactionWithBank(person1, amount, bankAccount, description);
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, amount, "From bank account \"usualName\"- n°" + iban, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), description);
            final ArgumentCaptor<TransactionWithBank> arg = ArgumentCaptor.forClass(TransactionWithBank.class);
            when(personService.getPerson(email)).thenReturn(person1);
            when(bankAccountService.getBankAccount(iban)).thenReturn(bankAccount);
            when(transactionWithBankRepository.save(any(TransactionWithBank.class))).thenReturn(transaction);
            //WHEN
            //the function makeANewTransactionWithBank() is called
            TransactionDTO result = transactionService.makeANewTransactionWithBank(personDTO1, amount, bankAccountDTO, description);
            //THEN
            // the transaction has been effectuated and a TransactionDTO object is returned with correct information
            assertThat(person1.getAvailableBalance()).isEqualTo(availableBalance + amount);
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(transactionWithBankRepository).save(arg.capture());
            assertEquals(person1, arg.getValue().getSender());
            assertEquals(bankAccount, arg.getValue().getRecipient());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(amount, arg.getValue().getAmount());
        }

        @DisplayName("GIVEN a transaction with a bank account belonging to the person and a debit to bank " +
                "WHEN the function makeANewTransactionWithBank() is called " +
                "THEN the transaction has been effectuated and a TransactionDTO object is returned with correct information.")
        @Test
        void makeANewTransactionWithBankDebitTest() {
            //GIVEN
            //a transaction with a bank account belonging to the person and a debit to bank
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String description = "description";
            float amount = -50F;
            float availableBalance = 60.25F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            person1.setAvailableBalance(availableBalance);
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            person1.setBankAccountList(List.of(bankAccount));
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            TransactionWithBank transaction = new TransactionWithBank(person1, amount, bankAccount, description);
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, abs(amount), "To bank account \"usualName\"- n°" + iban, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), description);
            final ArgumentCaptor<TransactionWithBank> arg = ArgumentCaptor.forClass(TransactionWithBank.class);
            when(personService.getPerson(email)).thenReturn(person1);
            when(bankAccountService.getBankAccount(iban)).thenReturn(bankAccount);
            when(transactionWithBankRepository.save(any(TransactionWithBank.class))).thenReturn(transaction);
            //WHEN
            //the function makeANewTransactionWithBank() is called
            TransactionDTO result = transactionService.makeANewTransactionWithBank(personDTO1, amount, bankAccountDTO, description);
            //THEN
            // the transaction has been effectuated and a TransactionDTO object is returned with correct information
            assertThat(person1.getAvailableBalance()).isEqualTo(availableBalance + amount);
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(transactionWithBankRepository).save(arg.capture());
            assertEquals(person1, arg.getValue().getSender());
            assertEquals(bankAccount, arg.getValue().getRecipient());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(amount, arg.getValue().getAmount());
        }

        @DisplayName("GIVEN a transaction with a bank not belonging to person " +
                "WHEN the function makeANewTransactionWithBank() is called " +
                "THEN a NotAuthorizedException is thrown with the expected error message.")
        @Test
        void makeANewTransactionWithBankDebitNotEnoughMoneyTest() {
            //GIVEN
            // a transaction with a bank without enough money
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String description = "description";
            float amount = 50F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            when(personService.getPerson(email)).thenReturn(person1);
            when(bankAccountService.getBankAccount(iban)).thenReturn(bankAccount);
            //WHEN
            //the function makeANewTransactionWithBank() is called
            //THEN
            //a NotEnoughMoneyException is thrown with the expected error message
            Exception exception = assertThrows(NotAuthorizedException.class, () -> transactionService.makeANewTransactionWithBank(personDTO1, amount, bankAccountDTO, description));
            assertEquals("The bank account " + iban + " doesn't belong to " + email + " so the transaction couldn't have been done.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(transactionWithBankRepository, Mockito.times(0)).save(any(TransactionWithBank.class));
        }
    }


    @Nested
    @Tag("TransactionServiceTests")
    @DisplayName("makeANewTransactionBetweenPersons tests:")
    class MakeANewTransactionBetweenPersonsTest {

        @DisplayName("GIVEN a transaction with someone in the group and enough money " +
                "WHEN the function makeANewTransactionBetweenPersons() is called " +
                "THEN the transaction has been effectuated and a TransactionDTO object is returned with correct information.")
        @Test
        void makeANewTransactionBetweenPersonsTest() {
            //GIVEN
            // a transaction with someone in the group and enough money
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            String description = "description";
            float amount = 50F;
            float amountToPay = amount * (new ApplicationPaymentRate().getRate());
            float availableBalance = 60.25F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            PersonDTO personDTO2 = new PersonDTO(email2, firstName2, lastname2);
            List<Person> relationsList = List.of(person2);
            person1.setRelations(relationsList);
            person1.setAvailableBalance(availableBalance);
            TransactionBetweenPersons transaction = new TransactionBetweenPersons(person1, amount, person2, description);
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, amount, firstName2 + " " + lastname2, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), description);
            final ArgumentCaptor<TransactionBetweenPersons> arg = ArgumentCaptor.forClass(TransactionBetweenPersons.class);
            when(personService.getPerson(email)).thenReturn(person1);
            when(personService.getPerson(email2)).thenReturn(person2);
            when(transactionBetweenPersonsRepository.save(any(TransactionBetweenPersons.class))).thenReturn(transaction);
            //WHEN
            //the function makeANewTransactionBetweenPersons() is called
            TransactionDTO result = transactionService.makeANewTransactionBetweenPersons(personDTO1, amount, personDTO2, description);
            //THEN
            // the transaction has been effectuated and a TransactionDTO object is returned with correct information
            assertThat(person1.getAvailableBalance()).isEqualTo(availableBalance - amountToPay);
            assertThat(person2.getAvailableBalance()).isEqualTo(amount);
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(personService, Mockito.times(1)).getPerson(email2);
            verify(transactionBetweenPersonsRepository).save(arg.capture());
            assertEquals(person1, arg.getValue().getSender());
            assertEquals(person2, arg.getValue().getRecipient());
            assertEquals(description, arg.getValue().getDescription());
            assertEquals(amount, arg.getValue().getAmount());
        }

        @DisplayName("GIVEN a transaction with someone not in the group" +
                "WHEN the function makeANewTransactionBetweenPersons() is called " +
                "THEN  a NotAuthorizedException is thrown with the expected error message.")
        @Test
        void makeANewTransactionBetweenPersonsNotInGroupTest() {
            //GIVEN
            //a transaction with someone not in the group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            String description = "description";
            float amount = 50F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            PersonDTO personDTO2 = new PersonDTO(email2, firstName2, lastname2);
            when(personService.getPerson(email)).thenReturn(person1);
            when(personService.getPerson(email2)).thenReturn(person2);
            //WHEN
            //the function makeANewTransactionBetweenPersons() is called
            //THEN
            //a NotAuthorizedException is thrown with the expected error message
            Exception exception = assertThrows(NotAuthorizedException.class, () -> transactionService.makeANewTransactionBetweenPersons(personDTO1, amount, personDTO2, description));
            assertEquals("The person " + email2 + " isn't part of " + email + "'s relations so the transaction couldn't have been done.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(personService, Mockito.times(1)).getPerson(email2);
            verify(transactionBetweenPersonsRepository, Mockito.times(0)).save(any(TransactionBetweenPersons.class));
        }

        @DisplayName("GIVEN a transaction with not enough money" +
                "WHEN the function makeANewTransactionBetweenPersons() is called " +
                "THEN  a NotEnoughMoneyException is thrown with the expected error message.")
        @Test
        void makeANewTransactionBetweenPersonsNotEnoughMoneyTest() {
            //GIVEN
            //a transaction with someone not in the group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            String description = "description";
            float amount = 50F;
            float amountToPay = amount * (new ApplicationPaymentRate().getRate());
            float availableBalance = 50F;
            Person person1 = new Person(email, password, firstName, lastname);
            PersonDTO personDTO1 = new PersonDTO(email, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            PersonDTO personDTO2 = new PersonDTO(email2, firstName2, lastname2);
            List<Person> relationsList = List.of(person2);
            person1.setRelations(relationsList);
            person1.setAvailableBalance(availableBalance);
            TransactionBetweenPersons transaction = new TransactionBetweenPersons(person1, amount, person2, description);
            when(personService.getPerson(email)).thenReturn(person1);
            when(personService.getPerson(email2)).thenReturn(person2);
            when(transactionBetweenPersonsRepository.save(any(TransactionBetweenPersons.class))).thenReturn(transaction);
            //WHEN
            //the function makeANewTransactionBetweenPersons() is called
            //THEN
            //a NotEnoughMoneyException is thrown with the expected error message
            Exception exception = assertThrows(NotEnoughMoneyException.class, () -> transactionService.makeANewTransactionBetweenPersons(personDTO1, amount, personDTO2, description));
            assertEquals("Your available balance is :" + availableBalance + "\u20ac\n" +
                    "so it's not possible to pay " + amountToPay + "\u20ac.\n", exception.getMessage());
            //and not any money has been transferred
            assertThat(person1.getAvailableBalance()).isEqualTo(availableBalance);
            assertThat(person2.getAvailableBalance()).isZero();
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(personService, Mockito.times(1)).getPerson(email2);
            verify(transactionBetweenPersonsRepository, Mockito.times(0)).save(any(TransactionBetweenPersons.class));
        }
    }

    @Nested
    @Tag("TransactionServiceTests")
    @DisplayName("transformTransactionToTransactionDTO tests:")
    class TransformTransactionToTransactionDTOTest {

        @DisplayName("GIVEN a transactionBetweenPersons " +
                "WHEN the function transformTransactionToTransactionDTO() is called " +
                "THEN a transactionDTO object is returned with the correct information to show.")
        @Test
        void transformTransactionBetweenPersonToTransactionDTOTest() {
            //GIVEN
            //a transactionBetweenPersons
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            String description = "description";
            float amount = 50F;
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            TransactionBetweenPersons transaction = new TransactionBetweenPersons(person1, amount, person2, description);
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, amount, firstName2 + " " + lastname2, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), description);
            //WHEN
            //the function transformTransactionToTransactionDTO() is called
            TransactionDTO result = transactionService.transformTransactionToTransactionDTO(transaction);
            //THEN
            //a transactionDTO object is returned with the correct information to show
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
        }

        @DisplayName("GIVEN a transactionWithBank which is a debit to a bankAccount " +
                "WHEN the function transformTransactionToTransactionDTO() is called " +
                "THEN a transactionDTO object is returned with the correct information to show.")
        @Test
        void transformTransactionWithBankDebitToTransactionDTOTest() {
            //GIVEN
            //a transactionWithBank which is a debit to a bankAccount
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            String receiver = "To bank account \"" + (bankAccount.getUsualName() + "\"- n°" + bankAccount.getIban());
            float amount = -50F;
            Person person1 = new Person(email, password, firstName, lastname);
            TransactionWithBank transaction = new TransactionWithBank(person1, amount, bankAccount, "");
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, abs(amount), receiver, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), "");
            //WHEN
            //the function transformTransactionToTransactionDTO() is called
            TransactionDTO result = transactionService.transformTransactionToTransactionDTO(transaction);
            //THEN
            //a transactionDTO object is returned with the correct information to show
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
        }

        @DisplayName("GIVEN a transactionWithBank which is a credit from a bankAccount " +
                "WHEN the function transformTransactionToTransactionDTO() is called " +
                "THEN a transactionDTO object is returned with the correct information to show.")
        @Test
        void transformTransactionWithBankCreditToTransactionDTOTest() {
            //GIVEN
            //a transactionWithBank which is a credit from a bankAccount
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "iban";
            String bic = "bic";
            String usualName = "usualName";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            String receiver = "From bank account \"" + (bankAccount.getUsualName() + "\"- n°" + bankAccount.getIban());
            float amount = 50F;
            Person person1 = new Person(email, password, firstName, lastname);
            TransactionWithBank transaction = new TransactionWithBank(person1, amount, bankAccount, "");
            LocalDateTime dateTime = transaction.getDateTime();
            TransactionDTO transactionDTO = new TransactionDTO(firstName + " " + lastname, abs(amount), receiver, dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateTime.format(DateTimeFormatter.ofPattern("hh:mm")), "");
            //WHEN
            //the function transformTransactionToTransactionDTO() is called
            TransactionDTO result = transactionService.transformTransactionToTransactionDTO(transaction);
            //THEN
            //a transactionDTO object is returned with the correct information to show
            assertThat(result.getSender()).isEqualTo(transactionDTO.getSender());
            assertThat(result.getAmount()).isEqualTo(transactionDTO.getAmount());
            assertThat(result.getReceiver()).isEqualTo(transactionDTO.getReceiver());
            assertThat(result.getDate()).isEqualTo(transactionDTO.getDate());
            assertThat(result.getTime()).isEqualTo(transactionDTO.getTime());
            assertThat(result.getDescription()).isEqualTo(transactionDTO.getDescription());
        }

        @DisplayName("GIVEN a transactionWithBank without any information " +
                "WHEN the function transformTransactionToTransactionDTO() is called " +
                "THEN an MissingInformationException is thrown with the expected error message.")
        @Test
        void transformTransactionWithBankWithoutInformationToTransactionDTOTest() {
            //GIVEN
            //a transactionWithBank without any information
            Transaction transaction = new TransactionWithBank();
            //WHEN
            //the function transformTransactionToTransactionDTO() is called
            //THEN
            //an MissingInformationException is thrown with the expected error message
            Exception exception = assertThrows(MissingInformationException.class, () -> transactionService.transformTransactionToTransactionDTO(transaction));
            assertEquals("There are missing information in the transaction", exception.getMessage());
        }

        @DisplayName("GIVEN a transactionBetweenPersons without any information " +
                "WHEN the function transformTransactionToTransactionDTO() is called " +
                "THEN an MissingInformationException is thrown with the expected error message.")
        @Test
        void transformTransactionBetweenPersonsWithoutInformationToTransactionDTOTest() {
            //GIVEN
            //a transactionBetweenPersons without any information
            Transaction transaction = new TransactionBetweenPersons();
            //WHEN
            //the function transformTransactionToTransactionDTO() is called
            //THEN
            //an MissingInformationException is thrown with the expected error message
            Exception exception = assertThrows(MissingInformationException.class, () -> transactionService.transformTransactionToTransactionDTO(transaction));
            assertEquals("There are missing information in the transaction", exception.getMessage());
        }
    }
}
