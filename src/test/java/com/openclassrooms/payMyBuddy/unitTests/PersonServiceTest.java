package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.configuration.SpringSecurityConfiguration;
import com.openclassrooms.payMyBuddy.exceptions.EmptyObjectException;
import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.BankAccount;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.model.Role;
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
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.openclassrooms.payMyBuddy.model.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@Tag("PersonTests")
@Slf4j
@DirtiesContext(classMode = AFTER_CLASS)
@SpringBootTest
public class PersonServiceTest {

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
    @DisplayName("Get all personsDTO tests:")
    class GetAllPersonsDTOTest {

        @DisplayName("GIVEN persons returned by personRepository " +
                "WHEN the function getAllPersonsDTO() is called " +
                "THEN it returns the correct list of PersonDTO.")
        @Test
        public void getAllPersonsDTOWhenNotEmptyTest() {
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
            assertThat(result.size()).isEqualTo(3);
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
            verify(personRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN not any person returned by personRepository " +
                "WHEN the function getAllPersonsDTO() is called " +
                "THEN an EmptyObjectException is thrown with the expected error message.")
        @Test
        public void getAllPersonsDTOWhenEmptyTest() {
            //GIVEN
            //not any person returned by personRepository
            when(personRepository.findAll()).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAllPersonsDTO() is called
            //THEN
            //an EmptyObjectException is thrown with the expected error message
            Exception exception = assertThrows(EmptyObjectException.class, () -> personService.getAllPersonsDTO());
            assertEquals("There is not any person registered.\n", exception.getMessage());
            verify(personRepository, Mockito.times(1)).findAll();
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("Get all active personsDTO tests:")
    class GetAllActivePersonsDTOTest {
        @DisplayName("GIVEN active persons returned by personRepository " +
                "WHEN the function getAllActivePersonsDTO() is called " +
                "THEN it returns the correct list of PersonDTO.")
        @Test
        public void getAllActivePersonsDTOWhenNotEmptyTest() {
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
            assertThat(result.size()).isEqualTo(3);
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
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }

        @DisplayName("GIVEN not any active person returned by personRepository " +
                "WHEN the function getAllActivePersonsDTO() is called " +
                "THEN an EmptyObjectException is thrown with the expected error message.")
        @Test
        public void getAllActivePersonsDTOWhenEmptyTest() {
            //GIVEN
            //not any person returned by personRepository
            when(personRepository.findByActive(true)).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAllActivePersonsDTO() is called
            //THEN
            //an EmptyObjectException is thrown with the expected error message
            Exception exception = assertThrows(EmptyObjectException.class, () -> personService.getAllActivePersonsDTO());
            assertEquals("There is not any active person registered.\n", exception.getMessage());
            verify(personRepository, Mockito.times(1)).findByActive(true);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("Get person tests:")
    class GetPersonTest {
        @DisplayName("GIVEN an existing person " +
                "WHEN the function getPerson() is called " +
                "THEN it returns the correct person.")
        @Test
        public void getPersonExistingTest() {
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
            verify(personRepository, Mockito.times(1)).findById(email);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("Get personDTO tests:")
    class GetPersonDTOTest {
        @DisplayName("GIVEN an existing person " +
                "WHEN the function getPersonDTO() is called " +
                "THEN it returns a personDTO with all correct information.")
        @Test
        public void getPersonDTOExistingTest() {
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
//            assertThat(returnedPerson.getAvailableBalance()).isEqualTo(String.valueOf(person1.getAvailableBalance()));
            assertThat(returnedPerson.getGroup().size()).isEqualTo(1);
            assertThat(returnedPerson.getGroup().get(0).getEmail()).isEqualTo(person2.getEmail());
            assertThat(returnedPerson.getGroup().get(0).getFirstName()).isEqualTo(person2.getFirstName());
            assertThat(returnedPerson.getGroup().get(0).getLastName()).isEqualTo(person2.getLastName());
            assertThat(returnedPerson.getBankAccountDTOList().size()).isEqualTo(1);
            assertThat(returnedPerson.getBankAccountDTOList().get(0).getIban()).isEqualTo(person1.getBankAccountList().get(0).getIban());
            assertThat(returnedPerson.getBankAccountDTOList().get(0).getBic()).isEqualTo(person1.getBankAccountList().get(0).getBic());
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
            verify(personRepository, Mockito.times(1)).findById(email);
        }
    }

    @Nested
    @Tag("PersonServiceTests")
    @DisplayName("Get current user mail tests:")
    class GetCurrentUserMailTests {

        @DisplayName("GIVEN a connected user" +
                "WHEN the function getCurrentUserMail() is called " +
                "THEN it returns the correct email.")
        @Test
        public void getCurrentUserMailConnected() {

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
        public void getCurrentUserMailNotConnected() {

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
    @DisplayName("Create person tests:")
    class CreatePersonTests {

        @DisplayName("GIVEN a personDTO with all information" +
                "WHEN the function createPerson() is called " +
                "THEN a person with correct information is created.")
        @Test
        public void createPersonTest() {

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
            verify(personRepository).save(arg.capture());
            assertEquals(person.getFirstName(), arg.getValue().getFirstName());
            assertEquals(person.getLastName(), arg.getValue().getLastName());
            assertEquals(person.getEmail(), arg.getValue().getEmail());
            assertEquals(person.getPassword().substring(0, 7), arg.getValue().getPassword().substring(0, 7));
            assertEquals(person.getRole(), arg.getValue().getRole());
        }
    }

//    @Nested
//    @Tag("PersonServiceTests")
//    @DisplayName("Change password tests:")
//    class ChangePasswordTests {
//
//        @DisplayName("GIVEN an existing personDTO and a new password " +
//                "WHEN the function changePassword() is called " +
//                "THEN a success message is returned.")
//        @Test
//        public void changePasswordTest() {
//            //GIVEN
//            //an existing personDTO and a new password
//            String email = "person1@mail.fr";
//            String password = "password1";
//            String firstName = "firstName1";
//            String lastname = "lastName1";
//            String newPassword = "newPassword1";
//            SpringSecurityConfiguration springSecurityConfiguration = Mockito.mock(SpringSecurityConfiguration.class);
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String newEncodedPassword = passwordEncoder.encode(newPassword);
//            Person person = new Person(email, newEncodedPassword, firstName, lastname);
//            person.setRole(Role.USER);
//            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
//
//
//            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
//            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person);
//            Mockito.when(springSecurityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
//
//            //WHEN
//            //the function changePassword() is called
//            String result = personService.changePassword(personDTO,newPassword);
//
//            //THEN
//            //a success message is returned.
//            assertThat(result).isEqualTo("The firstName1 lastName1's password have been modified.\n");
//            verify(personRepository).save(arg.capture());
//            assertEquals(firstName, arg.getValue().getFirstName());
//            assertEquals(lastname, arg.getValue().getLastName());
//            assertEquals(email, arg.getValue().getEmail());
//            assertEquals(newEncodedPassword.substring(0, 7), arg.getValue().getPassword().substring(0, 7));
//            assertEquals(person.getRole(), arg.getValue().getRole());
//        }
//        @DisplayName("GIVEN an existing personDTO with only some information and a new password " +
//                "WHEN the function changePassword() is called " +
//                "THEN a success message is returned.")
//        @Test
//        public void changePasswordNotAllInformationTest() {
//            //GIVEN
//            //an existing personDTO and a new password
//            String email = "person1@mail.fr";
//            String password = "password1";
//            String newPassword = "newPassword1";
//            SpringSecurityConfiguration springSecurityConfiguration = Mockito.mock(SpringSecurityConfiguration.class);
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String newEncodedPassword = passwordEncoder.encode(newPassword);
//            Person person = new Person(email, newEncodedPassword, "", "");
//            person.setRole(Role.USER);
//            PersonDTO personDTO = new PersonDTO(email, password, "", "");
//
//
//            final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
//            Mockito.when(personRepository.save(any(Person.class))).thenReturn(person);
//            Mockito.when(springSecurityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
//
//            //WHEN
//            //the function changePassword() is called
//            String result = personService.changePassword(personDTO,newPassword);
//
//            //THEN
//            //a success message is returned.
//            assertThat(result).isEqualTo("The firstName1 lastName1's password have been modified.\n");
//            verify(personRepository).save(arg.capture());
//            assertEquals(email, arg.getValue().getEmail());
//            assertEquals(newEncodedPassword.substring(0, 7), arg.getValue().getPassword().substring(0, 7));
//            assertEquals(person.getRole(), arg.getValue().getRole());
//        }
//
//    }

}

