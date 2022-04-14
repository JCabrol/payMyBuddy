package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.configuration.SpringSecurityConfiguration;
import com.openclassrooms.payMyBuddy.exceptions.*;
import com.openclassrooms.payMyBuddy.model.*;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonConnectionDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.repository.PersonRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.openclassrooms.payMyBuddy.model.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("PersonTests")
@Slf4j
@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private BankAccountService bankAccountService;

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getAllPersonsDTO tests:")
    class GetAllPersonsDTOTest {

        @DisplayName("GIVEN persons returned by personRepository " +
                "WHEN the function getAllPersonsDTO() is called " +
                "THEN it returns the correct list of PersonDTO.")
        @Test
        void getAllPersonsDTOWhenNotEmptyTest() {
            //GIVEN
            //persons returned by personRepository
            List<Person> AllPersonsTest = new ArrayList<>();
            for (int numberOfPersonsTest = 0; numberOfPersonsTest < 3; numberOfPersonsTest++) {
                Person person = new Person("person" + (numberOfPersonsTest + 1) + "@mail.fr",
                        "password" + (numberOfPersonsTest + 1),
                        "firstName" + (numberOfPersonsTest + 1),
                        "lastName" + (numberOfPersonsTest + 1));
                AllPersonsTest.add(person);
            }
            when(personRepository.findAll()).thenReturn(AllPersonsTest);
            //WHEN
            //the function getAllPersonsDTO() is called
            List<PersonDTO> result = personService.getAllPersonsDTO();
            //THEN
            //it returns the correct list of PersonDTO
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(1).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(2).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(0).getEmail()).isEqualTo("person1@mail.fr");
            assertThat(result.get(1).getEmail()).isEqualTo("person2@mail.fr");
            assertThat(result.get(2).getEmail()).isEqualTo("person3@mail.fr");
            assertThat(result.get(0).getPassword()).isEqualTo(null);
            assertThat(result.get(1).getPassword()).isEqualTo(null);
            assertThat(result.get(2).getPassword()).isEqualTo(null);
            assertThat(result.get(0).getFirstName()).isEqualTo("firstName1");
            assertThat(result.get(1).getFirstName()).isEqualTo("firstName2");
            assertThat(result.get(2).getFirstName()).isEqualTo("firstName3");
            assertThat(result.get(0).getLastName()).isEqualTo("lastName1");
            assertThat(result.get(1).getLastName()).isEqualTo("lastName2");
            assertThat(result.get(2).getLastName()).isEqualTo("lastName3");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN not any person returned by personRepository " +
                "WHEN the function getAllPersonsDTO() is called " +
                "THEN an EmptyObjectException is thrown with the expected error message.")
        @Test
        void getAllPersonsDTOWhenEmptyTest() {
            //GIVEN
            //not any person returned by personRepository
            when(personRepository.findAll()).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAllPersonsDTO() is called
            //THEN
            //an EmptyObjectException is thrown with the expected error message
            Exception exception = assertThrows(EmptyObjectException.class, () -> personService.getAllPersonsDTO());
            assertEquals("There is not any person registered.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findAll();
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getAllActivePersonsDTO tests:")
    class GetAllActivePersonsDTOTest {

        @DisplayName("GIVEN active persons returned by personRepository " +
                "WHEN the function getAllActivePersonsDTO() is called " +
                "THEN it returns the correct list of PersonDTO.")
        @Test
        void getAllActivePersonsDTOWhenNotEmptyTest() {
            //GIVEN
            //active persons returned by personRepository
            List<Person> AllActivePersonsTest = new ArrayList<>();
            for (int numberOfPersonsTest = 0; numberOfPersonsTest < 3; numberOfPersonsTest++) {
                Person person = new Person("person" + (numberOfPersonsTest + 1) + "@mail.fr",
                        "password" + (numberOfPersonsTest + 1),
                        "firstName" + (numberOfPersonsTest + 1),
                        "lastName" + (numberOfPersonsTest + 1));
                AllActivePersonsTest.add(person);
            }
            when(personRepository.findByActive(true)).thenReturn(AllActivePersonsTest);
            //WHEN
            //the function getAllActivePersonsDTO() is called
            List<PersonDTO> result = personService.getAllActivePersonsDTO();
            //THEN
            //it returns the correct list of PersonDTO
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(1).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(2).getAvailableBalance()).isEqualTo("0");
            assertThat(result.get(0).getEmail()).isEqualTo("person1@mail.fr");
            assertThat(result.get(1).getEmail()).isEqualTo("person2@mail.fr");
            assertThat(result.get(2).getEmail()).isEqualTo("person3@mail.fr");
            assertThat(result.get(0).getPassword()).isEqualTo(null);
            assertThat(result.get(1).getPassword()).isEqualTo(null);
            assertThat(result.get(2).getPassword()).isEqualTo(null);
            assertThat(result.get(0).getFirstName()).isEqualTo("firstName1");
            assertThat(result.get(1).getFirstName()).isEqualTo("firstName2");
            assertThat(result.get(2).getFirstName()).isEqualTo("firstName3");
            assertThat(result.get(0).getLastName()).isEqualTo("lastName1");
            assertThat(result.get(1).getLastName()).isEqualTo("lastName2");
            assertThat(result.get(2).getLastName()).isEqualTo("lastName3");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }

        @DisplayName("GIVEN not any active person returned by personRepository " +
                "WHEN the function getAllActivePersonsDTO() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllActivePersonsDTOWhenEmptyTest() {
            //GIVEN
            //not any person returned by personRepository
            when(personRepository.findByActive(true)).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAllActivePersonsDTO() is called
            List<PersonDTO> result = personService.getAllActivePersonsDTO();
            //THEN
            //it returns an empty list
            assertThat(result.size()).isEqualTo(0);
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getAllInactivePersons tests:")
    class GetAllInactivePersonsTest {

        @DisplayName("GIVEN inactive persons returned by personRepository " +
                "WHEN the function getAllInactivePersons() is called " +
                "THEN it returns the correct list of Person.")
        @Test
        void getAllInactivePersonsDTOWhenNotEmptyTest() {
            //GIVEN
            //inactive persons returned by personRepository
            List<Person> AllInactivePersonsTest = new ArrayList<>();
            for (int numberOfPersonsTest = 0; numberOfPersonsTest < 3; numberOfPersonsTest++) {
                Person person = new Person("person" + (numberOfPersonsTest + 1) + "@mail.fr",
                        "password" + (numberOfPersonsTest + 1),
                        "firstName" + (numberOfPersonsTest + 1),
                        "lastName" + (numberOfPersonsTest + 1));
                AllInactivePersonsTest.add(person);
            }
            when(personRepository.findByActive(false)).thenReturn(AllInactivePersonsTest);
            //WHEN
            //the function getAllInactivePersons() is called
            List<Person> result = personService.getAllInactivePersons();
            //THEN
            //it returns the correct list of Person
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getAvailableBalance()).isZero();
            assertThat(result.get(1).getAvailableBalance()).isZero();
            assertThat(result.get(2).getAvailableBalance()).isZero();
            assertThat(result.get(0).getEmail()).isEqualTo("person1@mail.fr");
            assertThat(result.get(1).getEmail()).isEqualTo("person2@mail.fr");
            assertThat(result.get(2).getEmail()).isEqualTo("person3@mail.fr");
            assertThat(result.get(0).getPassword()).isEqualTo("password1");
            assertThat(result.get(1).getPassword()).isEqualTo("password2");
            assertThat(result.get(2).getPassword()).isEqualTo("password3");
            assertThat(result.get(0).getFirstName()).isEqualTo("firstName1");
            assertThat(result.get(1).getFirstName()).isEqualTo("firstName2");
            assertThat(result.get(2).getFirstName()).isEqualTo("firstName3");
            assertThat(result.get(0).getLastName()).isEqualTo("lastName1");
            assertThat(result.get(1).getLastName()).isEqualTo("lastName2");
            assertThat(result.get(2).getLastName()).isEqualTo("lastName3");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(false);
        }

        @DisplayName("GIVEN not any inactive person returned by personRepository " +
                "WHEN the function getAllInactivePersons() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllInactivePersonsWhenEmptyTest() {
            //GIVEN
            //not any person returned by personRepository
            when(personRepository.findByActive(false)).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAllActivePersons() is called
            List<Person> result = personService.getAllInactivePersons();
            //THEN
            //it returns an empty list
            assertThat(result.size()).isZero();
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(false);
        }
    }


    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getPerson tests:")
    class GetPersonTest {

        @DisplayName("GIVEN an existing person " +
                "WHEN the function getPerson() is called " +
                "THEN it returns the correct person.")
        @Test
        void getPersonExistingTest() {
            //GIVEN
            //an existing person
            String email = "person1@mail.fr";
            Person person = new Person(email, "password1", "firstName1", "lastName1");
            doReturn(Optional.of(person)).when(personRepository).findById(email);
            //WHEN
            //the function getPerson() is called
            Person returnedPerson = personService.getPerson(email);
            //THEN
            //it returns the correct person
            assertThat(returnedPerson).isEqualTo(person);
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
        }

        @Test
        @DisplayName("GIVEN a non-existing person " +
                "WHEN the function getPerson() is called " +
                "THEN a NotFoundObjectException should be thrown with the expected error message.")
        void getPersonNotExistingTest() {
            // GIVEN
            //a non-existing person
            String email = "person1@mail.fr";
            doReturn(Optional.empty()).when(personRepository).findById(email);
            //WHEN
            //the function getPerson() is called
            //THEN
            //an NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.getPerson(email));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
        }

        @Test
        @DisplayName("GIVEN a person which is unsubscribed from application " +
                "WHEN the function getPerson() is called " +
                "THEN an ObjectNotExistingAnymoreException is thrown with the expected error message.")
        void getPersonNotActiveTest() {
            // GIVEN
            //a person which is unsubscribed from application
            String email = "person1@mail.fr";
            Person person = new Person(email, "password1", "firstName1", "lastName1");
            person.setActive(false);
            doReturn(Optional.of(person)).when(personRepository).findById(email);
            //WHEN
            //the function getPerson() is called
            //THEN
            //an ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.getPerson(email));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getPersonDTO tests:")
    class GetPersonDTOTest {

        @DisplayName("GIVEN an existing person " +
                "WHEN the function getPersonDTO() is called " +
                "THEN it returns a personDTO with all correct information.")
        @Test
        void getPersonDTOExistingTest() {
            //GIVEN
            //an existing person
            String email = "person1@mail.fr";
            Person person1 = new Person(email, "password1", "firstName1", "lastName1");
            Person person2 = new Person("person2@mail.fr", "password2", "firstName2", "lastName2");
            List<Person> relationsList = List.of(person2);
            person1.setRelations(relationsList);
            person1.setAvailableBalance(12.25F);
            String iban = "123456789123";
            String bic = "12345678";
            BankAccount bankAccount = new BankAccount(iban, bic);
            List<BankAccount> bankAccountList = List.of(bankAccount);
            person1.setBankAccountList(bankAccountList);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic);
            doReturn(bankAccountDTO).when(bankAccountService).transformBankAccountToBankAccountDTO(bankAccount);
            doReturn(Optional.of(person1)).when(personRepository).findById(email);
            //WHEN
            //the function getPersonDTO() is called
            PersonDTO returnedPerson = personService.getPersonDTO(email);
            //THEN
            //it returns a personDTO with all correct information
            assertThat(returnedPerson.getFirstName()).isEqualTo(person1.getFirstName());
            assertThat(returnedPerson.getLastName()).isEqualTo(person1.getLastName());
            assertThat(returnedPerson.getEmail()).isEqualTo(person1.getEmail());
            assertThat(returnedPerson.getPassword()).isNull();
            assertThat(returnedPerson.getAvailableBalance()).isEqualTo(String.valueOf(person1.getAvailableBalance()).replace(".", ","));
            assertThat(returnedPerson.getGroup()).hasSize(1);
            assertThat(returnedPerson.getGroup().get(0).getEmail()).isEqualTo(person2.getEmail());
            assertThat(returnedPerson.getGroup().get(0).getFirstName()).isEqualTo(person2.getFirstName());
            assertThat(returnedPerson.getGroup().get(0).getLastName()).isEqualTo(person2.getLastName());
            assertThat(returnedPerson.getBankAccountDTOList()).hasSize(1);
            assertThat(returnedPerson.getBankAccountDTOList().get(0).getIban()).isEqualTo(person1.getBankAccountList().get(0).getIban());
            assertThat(returnedPerson.getBankAccountDTOList().get(0).getBic()).isEqualTo(person1.getBankAccountList().get(0).getBic());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(2)).findById(email);
            verify(bankAccountService, Mockito.times(1)).transformBankAccountToBankAccountDTO(bankAccount);
        }

        @Test
        @DisplayName("GIVEN a non-existing person " +
                "WHEN the function getPersonDTO() is called " +
                "THEN a NotFoundObjectException should be thrown with the expected error message.")
        void getPersonDTONotExistingTest() {
            // GIVEN
            //a non-existing person
            String email = "person1@mail.fr";
            doReturn(Optional.empty()).when(personRepository).findById(email);
            //WHEN
            //the function getPersonDTO() is called
            //THEN
            //an NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.getPersonDTO(email));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
        }

        @Test
        @DisplayName("GIVEN a person which is unsubscribed from application " +
                "WHEN the function getPersonDTO() is called " +
                "THEN an ObjectNotExistingAnymoreException is thrown with the expected error message.")
        void getPersonDTONotActiveTest() {
            // GIVEN
            //a person which is unsubscribed from application
            String email = "person1@mail.fr";
            Person person = new Person(email, "password1", "firstName1", "lastName1");
            person.setActive(false);
            doReturn(Optional.of(person)).when(personRepository).findById(email);
            //WHEN
            //the function getPersonDTO() is called
            //THEN
            //an ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.getPersonDTO(email));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getCurrentUserMail tests:")
    class GetCurrentUserMailTests {

        @DisplayName("GIVEN a connected user" +
                "WHEN the function getCurrentUserMail() is called " +
                "THEN it returns the correct email.")
        @Test
        void getCurrentUserMailConnected() {
            //GIVEN
            //a connected user
            Authentication authentication = Mockito.mock(Authentication.class);
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            Mockito.when(authentication.isAuthenticated()).thenReturn(true);
            SecurityContextHolder.setContext(securityContext);
            String idCurrentUser = "person1@mail.fr";
            Mockito.when(authentication.getName()).thenReturn(idCurrentUser);
            //WHEN
            //the function getCurrentUserMail() is called
            String result = personService.getCurrentUserMail();
            //THEN
            //it returns the correct email
            assertThat(result).isEqualTo(idCurrentUser);
        }

        @DisplayName("GIVEN a not connected user" +
                "WHEN the function getCurrentUserMail() is called " +
                "THEN a NotFoundObjectException should be thrown with the expected error message.")
        @Test
        void getCurrentUserMailNotConnected() {
            //GIVEN
            //a not connected user
            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.isAuthenticated()).thenReturn(false);
            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            //WHEN
            //the function getCurrentUserMail() is called
            //THEN
            //an NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.getCurrentUserMail());
            assertEquals("The current user's mail couldn't have been found.\n", exception.getMessage());
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("createPerson tests:")
    class CreatePersonTests {

        @DisplayName("GIVEN a personDTO with all information" +
                "WHEN the function createPerson() is called " +
                "THEN a person with correct information is created.")
        @Test
        void createPersonTest() {
            //GIVEN
            //a personDTO with all information
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            SpringSecurityConfiguration springSecurityConfiguration = Mockito.mock(SpringSecurityConfiguration.class);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            Person person = new Person(email, encodedPassword, firstName, lastname);
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            person.setRole(USER);

            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person);
            Mockito.when(springSecurityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);

            //WHEN
            //the function createPerson() is called
            String result = personService.createPerson(personDTO);

            //THEN
            //a person with correct information is created.
            assertThat(result).isEqualTo("The person firstName1 lastName1 have been created.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository).save(arg.capture());
            assertEquals(person.getFirstName(), arg.getValue().getFirstName());
            assertEquals(person.getLastName(), arg.getValue().getLastName());
            assertEquals(person.getEmail(), arg.getValue().getEmail());
            assertEquals(person.getPassword().substring(0, 7), arg.getValue().getPassword().substring(0, 7));
            assertEquals(person.getRole(), arg.getValue().getRole());
        }

        @DisplayName("GIVEN a personDTO with not any information" +
                "WHEN the function createPerson() is called " +
                "THEN a MissingInformationException is thrown with the expected error message.")
        @Test
        void createPersonNoInformationTest() {
            //GIVEN
            //a personDTO with all information
            PersonDTO personDTO = new PersonDTO();
            //WHEN
            //the function createPerson() is called
            //THEN
            //a MissingInformationException is thrown with the expected error message
            Exception exception = assertThrows(MissingInformationException.class, () -> personService.createPerson(personDTO));
            assertEquals("There is not all required information to create a person,\n" +
                    "email, password, first name and last name are mandatory.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a personDTO with not any information" +
                "WHEN the function createPerson() is called " +
                "THEN a MissingInformationException is thrown with the expected error message.")
        @Test
        void createPersonNotAllInformationTest() {
            //GIVEN
            //a personDTO with all information
            String password = "password1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO();
            personDTO.setPassword(password);
            personDTO.setLastName(lastname);
            //WHEN
            //the function createPerson() is called
            //THEN
            //a MissingInformationException is thrown with the expected error message
            Exception exception = assertThrows(MissingInformationException.class, () -> personService.createPerson(personDTO));
            assertEquals("There is not all required information to create a person,\n" +
                    "email, password, first name and last name are mandatory.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("emailAlreadyExists tests:")
    class EmailAlreadyExistsTests {

        @DisplayName("GIVEN an email already registered" +
                "WHEN the function emailAlreadyExists() is called " +
                "THEN it returns true.")
        @Test
        void emailAlreadyExistsExistingTest() {
            //GIVEN
            //an email already registered
            String email = "person1@mail.fr";
            Mockito.when(personRepository.existsById(email)).thenReturn(true);
            //WHEN
            //the function emailAlreadyExists() is called
            boolean result = personService.emailAlreadyExists(email);
            //THEN
            //it returns true.
            assertTrue(result);
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).existsById(email);
        }

        @DisplayName("GIVEN an email not registered" +
                "WHEN the function emailAlreadyExists() is called " +
                "THEN it returns false.")
        @Test
        void emailAlreadyExistsNotExistingTest() {
            //GIVEN
            //an email not registered
            String email = "person1@mail.fr";
            Mockito.when(personRepository.existsById(email)).thenReturn(false);
            //WHEN
            //the function emailAlreadyExists() is called
            boolean result = personService.emailAlreadyExists(email);
            //THEN
            //it returns false.
            assertFalse(result);
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).existsById(email);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("updatePerson tests:")
    class UpdatePersonTests {

        @DisplayName("GIVEN an existing person and a personDTO with all updatable information" +
                "WHEN the function updatePerson() is called " +
                "THEN all the new information is updated, the email is still the same.")
        @Test
        void updatePersonTest() {
            //GIVEN
            //an existing person and a personDTO with all updatable information
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonDTO personDTO = new PersonDTO(email, password2, firstName2, lastname2);
            Person person1 = new Person(email, password, firstName, lastname);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function updatePerson() is called
            String result = personService.updatePerson(personDTO);
            //THEN
            //all the new information is updated, the email is still the same.
            assertThat(result).isEqualTo("Your user information has been updated.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository).save(arg.capture());
            assertEquals(firstName2, arg.getValue().getFirstName());
            assertEquals(lastname2, arg.getValue().getLastName());
            assertEquals(password2, arg.getValue().getPassword());
            assertEquals(email, arg.getValue().getEmail());
        }

        @DisplayName("GIVEN an existing person and a personDTO with only some updatable information" +
                "WHEN the function updatePerson() is called " +
                "THEN all the new information is updated, the other information are unchanged.")
        @Test
        void updatePersonSomeInformationTest() {
            //GIVEN
            //an existing person and a personDTO with only some updatable information
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonDTO personDTO = new PersonDTO(email, firstName2, lastname2);
            Person person1 = new Person(email, password, firstName, lastname);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function updatePerson() is called
            String result = personService.updatePerson(personDTO);
            //THEN
            //all the new information is updated, the other information are unchanged.
            assertThat(result).isEqualTo("Your user information has been updated.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository).save(arg.capture());
            assertEquals(firstName2, arg.getValue().getFirstName());
            assertEquals(lastname2, arg.getValue().getLastName());
            assertEquals(password, arg.getValue().getPassword());
            assertEquals(email, arg.getValue().getEmail());
        }

        @DisplayName("GIVEN an existing person and a personDTO with not new information" +
                "WHEN the function updatePerson() is called " +
                "THEN all the person information is still the same.")
        @Test
        void updatePersonNotNewInformationTest() {
            //GIVEN
            //an existing person and a personDTO with not new information
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO();
            personDTO.setEmail(email);
            Person person1 = new Person(email, password, firstName, lastname);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function updatePerson() is called
            String result = personService.updatePerson(personDTO);
            //THEN
            //all the person information is still the same.
            assertThat(result).isEqualTo("Your user information has been updated.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(password, arg.getValue().getPassword());
            assertEquals(email, arg.getValue().getEmail());
        }

        @DisplayName("GIVEN a personDTO with not any information" +
                "WHEN the function updatePerson() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void updatePersonNoInformationTest() {
            //GIVEN
            // a personDTO with not any information
            PersonDTO personDTO = new PersonDTO();
            //WHEN
            //the function updatePerson() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message.
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.updatePerson(personDTO));
            assertEquals("The person whose mail is " + null + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(any());
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a non-existing person" +
                "WHEN the function updatePerson() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void updatePersonNoExistingTest() {
            //GIVEN
            // a non-existing person
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function updatePerson() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message.
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.updatePerson(personDTO));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(any());
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a not active person" +
                "WHEN the function updatePerson() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void updatePersonNotActiveTest() {
            //GIVEN
            // a not active person
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String password = "password1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person = new Person(email, password, firstName, lastname);
            person.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person));
            //WHEN
            //the function updatePerson() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message.
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.updatePerson(personDTO));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(any());
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("changePassword tests:")
    class ChangePasswordTests {

        @DisplayName("GIVEN an existing personDTO and a new password " +
                "WHEN the function changePassword() is called " +
                "THEN a success message is returned.")
        @Test
        void changePasswordTest() {
            //GIVEN
            //an existing personDTO and a new password
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String newPassword = "newPassword1";
            SpringSecurityConfiguration springSecurityConfiguration = Mockito.mock(SpringSecurityConfiguration.class);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String newEncodedPassword = passwordEncoder.encode(newPassword);
            Person person = new Person(email, newEncodedPassword, firstName, lastname);
            person.setRole(Role.USER);
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(springSecurityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person);
            //WHEN
            //the function changePassword() is called
            String result = personService.changePassword(personDTO, newPassword);
            //THEN
            //a success message is returned.
            assertThat(result).isEqualTo("Your password have been successfully modified.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(email, arg.getValue().getEmail());
            assertEquals(newEncodedPassword.substring(0, 7), arg.getValue().getPassword().substring(0, 7));
            assertEquals(person.getRole(), arg.getValue().getRole());
            verify(personRepository, Mockito.times(1)).findById(email);
        }

        @DisplayName("GIVEN a non-existing personDTO and a new password " +
                "WHEN the function changePassword() is called " +
                "THEN a NotFoundObjectException should be thrown with the expected error message.")
        @Test
        void changePasswordNotExistingPersonDTOTest() {
            //GIVEN
            //a non-existing personDTO and a new password
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String newPassword = "newPassword1";
            SpringSecurityConfiguration springSecurityConfiguration = Mockito.mock(SpringSecurityConfiguration.class);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            Mockito.when(springSecurityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());

            //WHEN
            //the function changePassword() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.changePassword(personDTO, newPassword));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("deletePerson tests:")
    class DeletePersonTests {

        @DisplayName("GIVEN an existing personDTO " +
                "WHEN the function deletePerson() is called " +
                "THEN a success message is returned.")
        @Test
        void deletePersonTest() {
            //GIVEN
            //an existing personDTO
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person("person2@mail.fr", "password2", "firstName2", "lastName2");
            BankAccount bankAccount = new BankAccount("iban1", "bic1");
            List<Person> allPersons = List.of(person1, person2);
            List<Person> relations = new ArrayList<>();
            List<BankAccount> bankAccountList = new ArrayList<>();
            relations.add(person2);
            bankAccountList.add(bankAccount);
            person1.setRelations(relations);
            person1.setBankAccountList(bankAccountList);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            Mockito.when(personRepository.findAll()).thenReturn(allPersons);
            //WHEN
            //the function deletePerson() is called
            String result = personService.deletePerson(email);
            //THEN
            //a success message is returned
            assertThat(result).isEqualTo("The person " + email + " have been deleted.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findAll();
            verify(personRepository, Mockito.times(1)).save(any(Person.class));
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(email, arg.getValue().getEmail());
            assertEquals(Collections.emptyList(), arg.getValue().getRelations());
            assertEquals(Collections.emptyList(), arg.getValue().getBankAccountList());
            assertFalse(arg.getValue().isActive());
        }

        @DisplayName("GIVEN an existing personDTO in list of other person " +
                "WHEN the function deletePerson() is called " +
                "THEN a success message is returned and is not in the person's list anymore.")
        @Test
        void deletePersonInPersonListTest() {
            //GIVEN
            //an existing personDTO in list of other person
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person("person2@mail.fr", "password2", "firstName2", "lastName2");
            List<Person> allPersons = List.of(person1, person2);
            List<Person> relations = new ArrayList<>();
            relations.add(person1);
            person2.setRelations(relations);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person2).thenReturn(person1);
            Mockito.when(personRepository.findAll()).thenReturn(allPersons);
            //WHEN
            //the function deletePerson() is called
            String result = personService.deletePerson(email);
            //THEN
            //a success message is returned and is not in the person's list anymore
            assertThat(result).isEqualTo("The person " + email + " have been deleted.\n");
            assertTrue(person2.getRelations().isEmpty());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findAll();
            verify(personRepository, Mockito.times(2)).save(any(Person.class));
        }


        @DisplayName("GIVEN a non-existing personDTO " +
                "WHEN the function deletePerson() is called " +
                "THEN a NothingToDoException should be thrown with the expected error message.")
        @Test
        void deletePersonNotExistingTest() {
            //GIVEN
            //a non-existing personDTO
            String email = "person1@mail.fr";
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());

            //WHEN
            //the function deletePerson() is called
            //THEN
            //a NothingToDoException is thrown with the expected error message
            Exception exception = assertThrows(NothingToDoException.class, () -> personService.deletePerson(email));
            assertEquals("The person " + email + " was not found, so it couldn't have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a not active personDTO " +
                "WHEN the function deletePerson() is called " +
                "THEN a NothingToDoException should be thrown with the expected error message.")
        @Test
        void deletePersonNotActiveTest() {
            //GIVEN
            //a non-existing personDTO
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            //WHEN
            //the function deletePerson() is called
            //THEN
            //a NothingToDoException is thrown with the expected error message
            Exception exception = assertThrows(NothingToDoException.class, () -> personService.deletePerson(email));
            assertEquals("The person " + email + " was not found, so it couldn't have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("reactivateAccount tests:")
    class ReactivateAccountTests {

        @DisplayName("GIVEN an account deactivated" +
                "WHEN the function reactivate() is called " +
                "THEN a success message is returned and the account is active.")
        @Test
        void reactivateAccountTest() {
            //GIVEN
            //an account deactivated
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(false);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function reactivate() is called
            String result = personService.reactivateAccount(email);
            //THEN
            //a success message is returned and the account is active
            assertThat(result).isEqualTo("The person whose mail is " + email + " has been reactivated.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository).save(arg.capture());
            assertTrue(arg.getValue().isActive());
        }

        @DisplayName("GIVEN an active account" +
                "WHEN the function reactivate() is called " +
                "THEN a success message is returned and the account is active.")
        @Test
        void reactivateAccountWhenAlreadyActiveTest() {
            //GIVEN
            //an active account
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(true);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function reactivate() is called
            String result = personService.reactivateAccount(email);
            //THEN
            //a success message is returned and the account is active
            assertThat(result).isEqualTo("The person whose mail is " + email + " has been reactivated.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository).save(arg.capture());
            assertTrue(arg.getValue().isActive());
        }

        @DisplayName("GIVEN a non-existing person" +
                "WHEN the function reactivate() is called " +
                "THEN a NotFoundObjectException is thrown with the expected message.")
        @Test
        void reactivateAccountNotExistingTest() {
            //GIVEN
            //a non-existing person
            String email = "person1@mail.fr";
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function reactivate() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.reactivateAccount(email));
            assertEquals("The person " + email + " was not found", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }


    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("addPersonInGroup tests:")
    class AddPersonInGroupTests {

        @DisplayName("GIVEN an existing group owner and an existing person not in group" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a success message is returned and the expected methods have been called with expected arguments.")
        @Test
        void addPersonInGroupTest() {
            //GIVEN
            //an existing group owner and an existing person not in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            List<Person> relations = new ArrayList<>();
            relations.add(person2);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function addPersonInGroup() is called
            String result = personService.addPersonInGroup(personDTO1, personDTO2);
            //THEN
            //a success message is returned
            assertThat(result).isEqualTo("The person " + firstName2 + " " + lastname2 + " has been added in your relations.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(1)).save(any(Person.class));
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(email, arg.getValue().getEmail());
            assertEquals(relations, arg.getValue().getRelations());
        }

        @DisplayName("GIVEN an existing group owner and an existing person not in group but having group owner in his relations" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a success message is returned and the expected methods have been called with expected arguments.")
        @Test
        void addPersonWhichHasGroupOwnerInHisRelationsInGroupTest() {
            //GIVEN
            //an existing group owner and an existing person not in group but having group owner in his group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            List<Person> relations = new ArrayList<>();
            List<Person> relations2 = new ArrayList<>();
            relations.add(person2);
            relations2.add(person1);
            person2.setRelations(relations2);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function addPersonInGroup() is called
            String result = personService.addPersonInGroup(personDTO1, personDTO2);
            //THEN
            //a success message is returned
            assertThat(result).isEqualTo("The person " + firstName2 + " " + lastname2 + " has been added in your relations.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(1)).save(any(Person.class));
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(email, arg.getValue().getEmail());
            assertEquals(relations, arg.getValue().getRelations());
        }


        @DisplayName("GIVEN an existing group owner and an existing person already in group" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a ObjectAlreadyExistingException is thrown with the expected error message.")
        @Test
        void addPersonAlreadyInGroupTest() {
            //GIVEN
            //an existing group owner and an existing person already in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            List<Person> relations = new ArrayList<>();
            relations.add(person2);
            person1.setRelations(relations);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            //WHEN
            //the function addPersonInGroup() is called
            //THEN
            //a ObjectAlreadyExistingException is thrown with the expected error message
            Exception exception = assertThrows(ObjectAlreadyExistingException.class, () -> personService.addPersonInGroup(personDTO1, personDTO2));
            assertEquals("The person " + firstName2 + " " + lastname2 + " is already present in your relations.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a non-existing group owner and an existing person not in group" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void addPersonInGroupNonExistingTest() {
            //GIVEN
            //a non-existing group owner and an existing person not in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function addPersonInGroup() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.addPersonInGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN an existing group owner and a non-existing person to add" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void addPersonNotExistingInGroupTest() {
            //GIVEN
            //an existing group owner and a non-existing person to add
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);

            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.empty());
            //WHEN
            //the function addPersonInGroup() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.addPersonInGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email2 + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a not active group owner and an existing person not in group" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void addPersonInGroupNotActiveTest() {
            //GIVEN
            // a not active group owner and an existing person not in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            //WHEN
            //the function addPersonInGroup() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.addPersonInGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }


        @DisplayName("GIVEN an existing group owner and a not active person to add" +
                "WHEN the function addPersonInGroup() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void addPersonNotActiveInGroupTest() {
            //GIVEN
            // an existing group owner and a not active person to add
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            person2.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            //WHEN
            //the function addPersonInGroup() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.addPersonInGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email2 + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("removePersonFromGroup tests:")
    class RemovePersonFromGroupTests {

        @DisplayName("GIVEN an existing group owner and an existing person in group" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a success message is returned and the expected methods have been called with expected arguments.")
        @Test
        void removePersonFromGroupTest() {
            //GIVEN
            //an existing group owner and an existing person in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            List<Person> relations = new ArrayList<>();
            relations.add(person2);
            person1.setRelations(relations);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            //WHEN
            //the function removePersonFromGroup() is called
            String result = personService.removePersonFromGroup(personDTO1, personDTO2);
            //THEN
            //a success message is returned
            assertThat(result).isEqualTo("The person " + firstName2 + " " + lastname2 + " has been removed from your relations.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(1)).save(any(Person.class));
            verify(personRepository).save(arg.capture());
            assertEquals(firstName, arg.getValue().getFirstName());
            assertEquals(lastname, arg.getValue().getLastName());
            assertEquals(email, arg.getValue().getEmail());
            assertEquals(Collections.emptyList(), arg.getValue().getRelations());
        }

        @DisplayName("GIVEN an existing group owner and an existing person not in group" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a NothingToDoException is thrown with the expected error message.")
        @Test
        void removePersonFromGroupNotInGroupTest() {
            //GIVEN
            //an existing group owner and an existing person not in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            //WHEN
            //the function removePersonFromGroup() is called
            //THEN
            //a NothingToDoException is thrown with the expected error message
            Exception exception = assertThrows(NothingToDoException.class, () -> personService.removePersonFromGroup(personDTO1, personDTO2));
            assertEquals("The person " + firstName2 + " " + lastname2 + " wasn't present in your relations.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a non-existing group owner and an existing person in group" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void removePersonFromGroupNonExistingTest() {
            //GIVEN
            //a non-existing group owner and an existing person in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function removePersonFromGroup() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.removePersonFromGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN an existing group owner and a non-existing person to remove" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void removePersonNotExistingFromGroupTest() {
            //GIVEN
            //an existing group owner and a non-existing person to remove
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);

            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.empty());
            //WHEN
            //the function removePersonFromGroup() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.removePersonFromGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email2 + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a not active group owner and an existing person in group" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void removePersonFromGroupNotActiveTest() {
            //GIVEN
            // a not active group owner and an existing person in group
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            //WHEN
            //the function removePersonFromGroup() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.removePersonFromGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }


        @DisplayName("GIVEN an existing group owner and a not active person to remove" +
                "WHEN the function removePersonFromGroup() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void removePersonNotActiveFromGroupTest() {
            //GIVEN
            // an existing group owner and a not active person to remove
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO1 = new PersonDTO(email, password, firstName, lastname);
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonConnectionDTO personDTO2 = new PersonConnectionDTO(email2);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            person2.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.findById(email2)).thenReturn(Optional.of(person2));
            //WHEN
            //the function removePersonFromGroup() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.removePersonFromGroup(personDTO1, personDTO2));
            assertEquals("The person whose mail is " + email2 + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(1)).findById(email2);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getAllNotFriendPersonsDTO tests:")
    class GetAllNotFriendPersonsDTOTests {

        @DisplayName("GIVEN a person with friends and not friends" +
                "WHEN the function getAllNotFriendPersonsDTO() is called " +
                "THEN it returns a list of all the PersonDTO which are not in the person's group.")
        @Test
        void getAllNotFriendPersonsDTOTest() {
            //GIVEN
            //a person with friends and not friends
            List<Person> AllPersonsTest = new ArrayList<>();
            for (int numberOfPersonsTest = 0; numberOfPersonsTest < 6; numberOfPersonsTest++) {
                Person person = new Person("person" + (numberOfPersonsTest + 1) + "@mail.fr",
                        "password" + (numberOfPersonsTest + 1),
                        "firstName" + (numberOfPersonsTest + 1),
                        "lastName" + (numberOfPersonsTest + 1));
                AllPersonsTest.add(person);
            }
            Person person1 = AllPersonsTest.get(0);
            Person person2 = AllPersonsTest.get(1);
            Person person3 = AllPersonsTest.get(2);
            PersonDTO personDTO1 = new PersonDTO("person1@mail.fr", "firstname1", "lastName1");
            PersonDTO personDTO2 = new PersonDTO("person2@mail.fr", "firstname2", "lastName2");
            PersonDTO personDTO3 = new PersonDTO("person3@mail.fr", "firstname3", "lastName3");
            List<PersonDTO> person1Group = List.of(personDTO2, personDTO3);
            person1.addPersonInGroup(person2);
            person1.addPersonInGroup(person3);
            personDTO1.setGroup(person1Group);
            Mockito.when(personRepository.findByActive(true)).thenReturn(AllPersonsTest);
            //WHEN
            //the function getAllNotFriendPersonsDTO() is called
            List<PersonDTO> result = personService.getAllNotFriendPersonsDTO(personDTO1);
            //THEN
            //it returns a list of all the PersonDTO which are not in the person's group
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getEmail()).isEqualTo("person4@mail.fr");
            assertThat(result.get(1).getEmail()).isEqualTo("person5@mail.fr");
            assertThat(result.get(2).getEmail()).isEqualTo("person6@mail.fr");
            assertFalse(result.contains(personDTO1));
            assertFalse(result.contains(personDTO2));
            assertFalse(result.contains(personDTO3));
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }

        @DisplayName("GIVEN a person with only friends" +
                "WHEN the function getAllNotFriendPersonsDTO() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllNotFriendEmptyPersonsDTOTest() {
            //GIVEN
            //a person with only friends
            List<Person> AllPersonsTest = new ArrayList<>();
            for (int numberOfPersonsTest = 0; numberOfPersonsTest < 3; numberOfPersonsTest++) {
                Person person = new Person("person" + (numberOfPersonsTest + 1) + "@mail.fr",
                        "password" + (numberOfPersonsTest + 1),
                        "firstName" + (numberOfPersonsTest + 1),
                        "lastName" + (numberOfPersonsTest + 1));
                AllPersonsTest.add(person);
            }
            Person person1 = AllPersonsTest.get(0);
            Person person2 = AllPersonsTest.get(1);
            Person person3 = AllPersonsTest.get(2);
            PersonDTO personDTO1 = new PersonDTO("person1@mail.fr", "firstName1", "lastName1");
            PersonDTO personDTO2 = new PersonDTO("person2@mail.fr", "firstName2", "lastName2");
            PersonDTO personDTO3 = new PersonDTO("person3@mail.fr", "firstName3", "lastName3");
            List<PersonDTO> person1Group = List.of(personDTO2, personDTO3);
            person1.addPersonInGroup(person2);
            person1.addPersonInGroup(person3);
            personDTO1.setGroup(person1Group);
            Mockito.when(personRepository.findByActive(true)).thenReturn(AllPersonsTest);
            //WHEN
            //the function getAllNotFriendPersonsDTO() is called
            List<PersonDTO> result = personService.getAllNotFriendPersonsDTO(personDTO1);
            //THEN
            //it returns an empty list
            assertThat(result).hasSize(0);
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }
    }


    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("addBankAccount tests:")
    class AddBankAccountTests {

        @DisplayName("GIVEN a person without bank account" +
                "WHEN the function addBankAccount() is called " +
                "THEN a bank account is created and added to his bankAccountList.")
        @Test
        void addBankAccountTest() {
            //GIVEN
            //a person without bank account
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            BankAccount bankAccount1 = new BankAccount(iban, bic, usualName);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            Mockito.when(bankAccountService.createBankAccount(bankAccountDTO)).thenReturn(bankAccount1);
            //WHEN
            //the function addBankAccount() is called
            String result = personService.addBankAccount(personDTO, bankAccountDTO);
            //THEN
            //a bank account is created and added to his bankAccountList
            assertThat(result).isEqualTo("The bank account with IBAN number " + iban + " has been created and added to your bank account list.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).createBankAccount(bankAccountDTO);
            verify(personRepository).save(arg.capture());
            assertTrue(arg.getValue().getBankAccountList().contains(bankAccount1));
        }

        @DisplayName("GIVEN a person with some bank account" +
                "WHEN the function addBankAccount() is called " +
                "THEN a bank account is created and added to his bankAccountList.")
        @Test
        void addBankAccountWithOthersTest() {
            //GIVEN
            //a person without bank account
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            String iban2 = "bankAccountIban2";
            String bic2 = "bankAccountBic2";
            String usualName2 = "usualName2";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            BankAccount bankAccount1 = new BankAccount(iban, bic, usualName);
            BankAccount bankAccount2 = new BankAccount(iban2, bic2, usualName2);
            List<BankAccount> bankAccountList = new ArrayList<>();
            bankAccountList.add(bankAccount2);
            person1.setBankAccountList(bankAccountList);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            Mockito.when(bankAccountService.createBankAccount(bankAccountDTO)).thenReturn(bankAccount1);
            //WHEN
            //the function addBankAccount() is called
            String result = personService.addBankAccount(personDTO, bankAccountDTO);
            //THEN
            //a bank account is created and added to his bankAccountList
            assertThat(result).isEqualTo("The bank account with IBAN number " + iban + " has been created and added to your bank account list.\n");
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).createBankAccount(bankAccountDTO);
            verify(personRepository).save(arg.capture());
            assertThat(arg.getValue().getBankAccountList()).hasSize(2);
            assertTrue(arg.getValue().getBankAccountList().contains(bankAccount1));
            assertTrue(arg.getValue().getBankAccountList().contains(bankAccount2));
        }

        @DisplayName("GIVEN a non-existing person " +
                "WHEN the function addBankAccount() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void addBankAccountNonExistingPersonTest() {
            //GIVEN
            //a person without bank account
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function addBankAccount() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.addBankAccount(personDTO, bankAccountDTO));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
            verify(bankAccountService, Mockito.times(0)).createBankAccount(bankAccountDTO);
        }

        @DisplayName("GIVEN a person not active " +
                "WHEN the function addBankAccount() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void addBankAccountNotActivePersonTest() {
            //GIVEN
            //a person without bank account
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String password = "password1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Person person1 = new Person(email, password, firstName, lastname);
            person1.setActive(false);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            //WHEN
            //the function addBankAccount() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.addBankAccount(personDTO, bankAccountDTO));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
            verify(bankAccountService, Mockito.times(0)).createBankAccount(bankAccountDTO);
        }
    }


    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("removeBankAccount tests:")
    class RemoveBankAccountTests {

        @DisplayName("GIVEN a bankAccount in a person's bankAccountList" +
                "WHEN the function removeBankAccount() is called " +
                "THEN the bankAccount is inactive and removed from his bankAccountList.")
        @Test
        void removeBankAccountTest() {
            //GIVEN
            //a bankAccount in a person's bankAccountList
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            BankAccount bankAccount1 = new BankAccount(iban, bic, usualName);
            List<BankAccount> bankAccountList = new ArrayList<>();
            bankAccountList.add(bankAccount1);
            person1.setBankAccountList(bankAccountList);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            Mockito.when(bankAccountService.getBankAccount(iban)).thenReturn(bankAccount1);
            //WHEN
            //the function removeBankAccount() is called
            String result = personService.removeBankAccount(personDTO, bankAccountDTO);
            //THEN
            //the bankAccount is inactive and removed from his bankAccountList.
            assertThat(result).isEqualTo("The bank account with IBAN number " + iban + " has been removed from your bank account list.\n");
            assertFalse(bankAccount1.isActiveBankAccount());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(personRepository).save(arg.capture());
            assertFalse(arg.getValue().getBankAccountList().contains(bankAccount1));
        }

        @DisplayName("GIVEN a bankAccount in a person's bankAccountList with another bankAccount" +
                "WHEN the function removeBankAccount() is called " +
                "THEN the bankAccount is inactive and removed from his bankAccountList, the other bankAccount is still active in the list.")
        @Test
        void removeBankAccountWithOtherTest() {
            //GIVEN
            //a bankAccount in a person's bankAccountList with another bankAccount
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            String iban2 = "bankAccountIban2";
            String bic2 = "bankAccountBic2";
            String usualName2 = "usualName2";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            BankAccount bankAccount1 = new BankAccount(iban, bic, usualName);
            BankAccount bankAccount2 = new BankAccount(iban2, bic2, usualName2);
            List<BankAccount> bankAccountList = new ArrayList<>();
            bankAccountList.add(bankAccount1);
            bankAccountList.add(bankAccount2);
            person1.setBankAccountList(bankAccountList);
            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person1);
            Mockito.when(bankAccountService.getBankAccount(iban)).thenReturn(bankAccount1);
            //WHEN
            //the function removeBankAccount() is called
            String result = personService.removeBankAccount(personDTO, bankAccountDTO);
            //THEN
            //the bankAccount is inactive and removed from his bankAccountList.
            assertThat(result).isEqualTo("The bank account with IBAN number " + iban + " has been removed from your bank account list.\n");
            assertFalse(bankAccount1.isActiveBankAccount());
            assertTrue(bankAccount2.isActiveBankAccount());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(personRepository).save(arg.capture());
            assertFalse(arg.getValue().getBankAccountList().contains(bankAccount1));
            assertTrue(arg.getValue().getBankAccountList().contains(bankAccount2));
        }

        @DisplayName("GIVEN a NotFoundObjectException thrown by the bankAccountService" +
                "WHEN the function removeBankAccount() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void removeBankAccountNotExistingTest() {
            //GIVEN
            //a NotFoundObjectException thrown by the bankAccountService
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            String errorMessage = "The bank account with IBAN number " + iban + " was not found.\n";
            NotFoundObjectException notFoundObjectException = new NotFoundObjectException(errorMessage);
            Mockito.when(bankAccountService.getBankAccount(iban)).thenThrow(notFoundObjectException);
            //WHEN
            //the function removeBankAccount() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message.
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.removeBankAccount(personDTO, bankAccountDTO));
            assertEquals(errorMessage, exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a ObjectNotExistingAnymoreException thrown by the bankAccountService" +
                "WHEN the function removeBankAccount() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void removeBankAccountNotActiveTest() {
            //GIVEN
            //a ObjectNotExistingAnymoreException thrown by the bankAccountService
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            String errorMessage = "The bank account with IBAN number " + iban + " has been deleted from application.";
            ObjectNotExistingAnymoreException objectNotExistingAnymoreException = new ObjectNotExistingAnymoreException(errorMessage);
            Mockito.when(bankAccountService.getBankAccount(iban)).thenThrow(objectNotExistingAnymoreException);
            //WHEN
            //the function removeBankAccount() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.removeBankAccount(personDTO, bankAccountDTO));
            assertEquals(errorMessage, exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccount(iban);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a non-existing person" +
                "WHEN the function removeBankAccount() is called " +
                "THEN a NotFoundObjectException is thrown with the expected error message.")
        @Test
        void removeBankAccountPersonNonExistingTest() {
            //GIVEN
            //a non-existing person
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.empty());
            //WHEN
            //the function removeBankAccount() is called
            //THEN
            //a NotFoundObjectException is thrown with the expected error message
            Exception exception = assertThrows(NotFoundObjectException.class, () -> personService.removeBankAccount(personDTO, bankAccountDTO));
            assertEquals("The person whose mail is " + email + " was not found.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(0)).getBankAccount(iban);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }

        @DisplayName("GIVEN a not active person" +
                "WHEN the function removeBankAccount() is called " +
                "THEN a ObjectNotExistingAnymoreException is thrown with the expected error message.")
        @Test
        void removeBankAccountPersonNotActiveTest() {
            //GIVEN
            //a non-existing person
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String password = "password1";
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person = new Person(email, password, firstName, lastname);
            person.setActive(false);
            BankAccountDTO bankAccountDTO = new BankAccountDTO(iban, bic, usualName);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person));
            //WHEN
            //the function removeBankAccount() is called
            //THEN
            //a ObjectNotExistingAnymoreException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotExistingAnymoreException.class, () -> personService.removeBankAccount(personDTO, bankAccountDTO));
            assertEquals("The person whose mail is " + email + " doesn't exist anymore in the application.\n", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(bankAccountService, Mockito.times(0)).getBankAccount(iban);
            verify(personRepository, Mockito.times(0)).save(any(Person.class));
        }
    }



    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("getPersonTransactionsMade tests:")
    class GetPersonTransactionsMadeTests {

        @DisplayName("GIVEN an existing person with a transaction list not empty" +
                "WHEN the function getPersonTransactionsMade() is called " +
                "THEN a correct list of transactionDTO is returned.")
        @Test
        void getPersonTransactionsMadeTest() {
            //GIVEN
            //an existing person with a transaction list not empty
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            String email2 = "person2@mail.fr";
            String password2 = "password2";
            String firstName2 = "firstName2";
            String lastname2 = "lastName2";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            Person person1 = new Person(email, password, firstName, lastname);
            Person person2 = new Person(email2, password2, firstName2, lastname2);
            String iban = "bankAccountIban1";
            String bic = "bankAccountBic1";
            String usualName = "usualName1";
            BankAccount bankAccount = new BankAccount(iban, bic, usualName);
            Transaction transaction1 = new TransactionWithBank(person1, 50, bankAccount, "");
            TransactionDTO transactionDTO1 = new TransactionDTO(email, 50F, iban, "", "", "");
            Transaction transaction2 = new TransactionWithBank(person1, -20, bankAccount, "");
            TransactionDTO transactionDTO2 = new TransactionDTO(email, -20F, iban, "", "", "");
            Transaction transaction3 = new TransactionBetweenPersons(person1, 10, person2, "");
            TransactionDTO transactionDTO3 = new TransactionDTO(email, 10F, email2, "", "", "");
            List<Transaction> transactionsList = new ArrayList<>();
            transactionsList.add(transaction1);
            transactionsList.add(transaction2);
            transactionsList.add(transaction3);
            person1.setTransactionMadeList(transactionsList);
            Mockito.when(personRepository.findById(email)).thenReturn(Optional.of(person1));
            Mockito.when(transactionService.transformTransactionToTransactionDTO(transaction1)).thenReturn(transactionDTO1);
            Mockito.when(transactionService.transformTransactionToTransactionDTO(transaction2)).thenReturn(transactionDTO2);
            Mockito.when(transactionService.transformTransactionToTransactionDTO(transaction3)).thenReturn(transactionDTO3);
            //WHEN
            //the function getPersonTransactionsMade() is called
            List<TransactionDTO> result = personService.getPersonTransactionsMade(personDTO);
            //THEN
            //a correct list of transactionDTO is returned
            assertThat(result).hasSize(3);
            assertTrue(result.contains(transactionDTO1));
            assertTrue(result.contains(transactionDTO2));
            assertTrue(result.contains(transactionDTO3));
            //and the expected methods have been called with expected arguments
            verify(personRepository, Mockito.times(1)).findById(email);
            verify(transactionService, Mockito.times(1)).transformTransactionToTransactionDTO(transaction1);
            verify(transactionService, Mockito.times(1)).transformTransactionToTransactionDTO(transaction2);
            verify(transactionService, Mockito.times(1)).transformTransactionToTransactionDTO(transaction3);
        }
    }

}

