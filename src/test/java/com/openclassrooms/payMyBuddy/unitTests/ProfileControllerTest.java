package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.ChangePasswordDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Person;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ControllerTests")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProfileControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private BankAccountService bankAccountService;

    @Nested
    @Tag("ProfileControllerTests")
    @DisplayName("ShowProfileTest")
    class ShowProfileTests {

        @Test
        @DisplayName("GIVEN not any message to display " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showProfileTest() throws Exception {
            //GIVEN
            //not any message to display
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Profile"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeExists("changePasswordDTO"))
                    .andExpect(model().attributeExists("transactionDTO"))
                    .andExpect(model().attributeExists("updatePersonDTO"))
                    .andExpect(model().attributeDoesNotExist("messageProfile"))
                    .andExpect(model().attributeDoesNotExist("messagePassword"))
                    .andExpect(model().attributeDoesNotExist("messageTransaction"))
                    .andExpect(model().attributeDoesNotExist("messageAccount"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }
        @Test
        @DisplayName("GIVEN message profile to display " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showProfileWithMessageProfileTest() throws Exception {
            //GIVEN
            //message profile to display
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile")
                    .param("messageProfile", "message to display");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Profile"))
                    .andExpect(model().attribute("messageProfile", "message to display"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeExists("changePasswordDTO"))
                    .andExpect(model().attributeExists("transactionDTO"))
                    .andExpect(model().attributeExists("updatePersonDTO"))
                    .andExpect(model().attributeDoesNotExist("messagePassword"))
                    .andExpect(model().attributeDoesNotExist("messageTransaction"))
                    .andExpect(model().attributeDoesNotExist("messageAccount"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }
        @Test
        @DisplayName("GIVEN message password to display " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showProfileWithMessagePasswordTest() throws Exception {
            //GIVEN
            //message password to display
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile")
                    .param("messagePassword", "message to display");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Profile"))
                    .andExpect(model().attribute("messagePassword", "message to display"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeExists("changePasswordDTO"))
                    .andExpect(model().attributeExists("transactionDTO"))
                    .andExpect(model().attributeExists("updatePersonDTO"))
                    .andExpect(model().attributeDoesNotExist("messageProfile"))
                    .andExpect(model().attributeDoesNotExist("messageTransaction"))
                    .andExpect(model().attributeDoesNotExist("messageAccount"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }
        @Test
        @DisplayName("GIVEN message transaction to display " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showProfileWithMessageTransactionTest() throws Exception {
            //GIVEN
            //message transaction to display
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile")
                    .param("messageTransaction", "message to display");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Profile"))
                    .andExpect(model().attribute("messageTransaction", "message to display"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeExists("changePasswordDTO"))
                    .andExpect(model().attributeExists("transactionDTO"))
                    .andExpect(model().attributeExists("updatePersonDTO"))
                    .andExpect(model().attributeDoesNotExist("messageProfile"))
                    .andExpect(model().attributeDoesNotExist("messagePassword"))
                    .andExpect(model().attributeDoesNotExist("messageAccount"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }
        @Test
        @DisplayName("GIVEN message account to display " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showProfileWithMessageAccountTest() throws Exception {
            //GIVEN
            //message account to display
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile")
                    .param("messageAccount", "message to display");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("profile"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Profile"))
                    .andExpect(model().attribute("messageAccount", "message to display"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeExists("changePasswordDTO"))
                    .andExpect(model().attributeExists("transactionDTO"))
                    .andExpect(model().attributeExists("updatePersonDTO"))
                    .andExpect(model().attributeDoesNotExist("messageProfile"))
                    .andExpect(model().attributeDoesNotExist("messageTransaction"))
                    .andExpect(model().attributeDoesNotExist("messagePassword"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }
        @Test
        @DisplayName("GIVEN not connected user " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN an error page is displayed .")
        void showProfileNotConnectedTest() throws Exception {
            //GIVEN
            //not connected user
            when(personService.getCurrentUserMail()).thenThrow(NotFoundObjectException.class);

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/profile");
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //an error page is displayed.
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }
    }
    @Nested
    @Tag("ProfileControllerTests")
    @DisplayName("SubmitProfileTest")
    class SubmitProfileTests {

        @Test
        @DisplayName("GIVEN no errors " +
                "WHEN the uri \"/home/profile\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void submitProfileTest() throws Exception {
            //GIVEN
            //no errors
            String email = "person1@mail.fr";
            String firstName = "firstName1";
            String lastname = "lastName1";
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.updatePerson(any(PersonDTO.class))).thenReturn("success");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile")
                    .param("firstName", firstName)
                    .param("lastName", lastname);
            // WHEN
            //the uri "/home/profile" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("messageProfile","success"))
                    .andExpect(redirectedUrl("/home/profile?messageProfile=success"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).updatePerson(any(PersonDTO.class));
        }
    }


    @Nested
    @Tag("ProfileControllerTests")
    @DisplayName("SubmitPasswordTest")
    class SubmitPasswordTests {
        @Test
        @DisplayName("GIVEN no errors" +
                "WHEN the uri \"/home/profile/password\" is called, " +
                "THEN correct information is attached to model and the page is correctly redirected.")
        void submitPasswordTest() throws Exception {
            //GIVEN
            //no errors
            String email = "person1@mail.fr";
            String password = "Password1!";
            String confirmationPassword = "Password1!";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(password, confirmationPassword);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            when(personService.changePassword(any(PersonDTO.class), eq(changePasswordDTO.getPassword()))).thenReturn("Password changed");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile/password")
                    .param("password",changePasswordDTO.getPassword())
                    .param("confirmationPassword", changePasswordDTO.getConfirmationPassword());
            // WHEN
            //the uri "/home/profile/password" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //correct information is attached to model and the page is correctly redirected.
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("messagePassword", "Password changed"))
                    .andExpect(redirectedUrl("/home/profile?messagePassword=Password+changed"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
            verify(personService, Mockito.times(1)).changePassword(any(PersonDTO.class), eq(changePasswordDTO.getPassword()));
        }
        @Test
        @DisplayName("GIVEN errors in BindingResult" +
                "WHEN the uri \"/home/profile/password\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void submitPasswordErrorsTest() throws Exception {
            //GIVEN
            //errors in BindingResult
            String email = "person1@mail.fr";
            String password = "password";
            String confirmationPassword = "Password1!";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(password, confirmationPassword);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile/password")
                    .param("password",changePasswordDTO.getPassword())
                    .param("confirmationPassword", changePasswordDTO.getConfirmationPassword());
            // WHEN
            //the uri "/home/profile/password" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    // correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(2))
                    .andExpect(view().name("profile"))
           ;
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
            verify(personService, Mockito.times(0)).changePassword(any(PersonDTO.class), anyString());
        }
    }

    @Nested
    @Tag("ProfileControllerTests")
    @DisplayName("SubmitBankTransferTest")
    class SubmitBankTransferTests {
        @Test
        @DisplayName("GIVEN no errors" +
                "WHEN the uri \"/home/profile/transferBank\" is called, " +
                "THEN correct information is attached to model and the page is correctly redirected.")
        void submitTransferBankTest() throws Exception {
            //GIVEN
            //no errors
            String email = "person1@mail.fr";
            String password = "Password1!";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person = new Person(email,password,firstName,lastname);
            person.setAvailableBalance(25F);
            BankAccountDTO bankAccountDTO = new BankAccountDTO("FR12345678912345678912345","123456789");
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setSender(email);
            transactionDTO.setAmount(20F);
            transactionDTO.setReceiver("FR12345678912345678912345");
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            when(personService.getPerson(email)).thenReturn(person);
            when(bankAccountService.getBankAccountDTO("FR12345678912345678912345")).thenReturn(bankAccountDTO);
            when(transactionService.makeANewTransactionWithBank(any(PersonDTO.class), anyFloat(), any(BankAccountDTO.class), anyString())).thenReturn(transactionDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile/transferBank")
                    .param("receiver", "FR12345678912345678912345")
                    .param("amount", "20")
                    .param("button","getMoneyFromBank");
            // WHEN
            //the uri "/home/profile/transferBank" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //correct information is attached to model and the page is correctly redirected.
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("messageTransaction", "Your transaction of 20.0 euros with your bank account n°FR12345678912345678912345 has been successful."))
                    .andExpect(redirectedUrl("/home/profile?messageTransaction=Your+transaction+of+20.0+euros+with+your+bank+account+n%B0FR12345678912345678912345+has+been+successful."));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(2)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccountDTO("FR12345678912345678912345");
            verify(transactionService, Mockito.times(1)).makeANewTransactionWithBank(any(PersonDTO.class), anyFloat(), any(BankAccountDTO.class),anyString());
        }
        @Test
        @DisplayName("GIVEN no errors" +
                "WHEN the uri \"/home/profile/transferBank\" is called, " +
                "THEN correct information is attached to model and the page is correctly redirected.")
        void submitTransferBankDebitTest() throws Exception {
            //GIVEN
            //no errors
            String email = "person1@mail.fr";
            String password = "Password1!";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person = new Person(email,password,firstName,lastname);
            person.setAvailableBalance(25F);
            BankAccountDTO bankAccountDTO = new BankAccountDTO("FR12345678912345678912345","123456789");
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setSender(email);
            transactionDTO.setAmount(20F);
            transactionDTO.setReceiver("FR12345678912345678912345");
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            when(personService.getPerson(email)).thenReturn(person);
            when(bankAccountService.getBankAccountDTO("FR12345678912345678912345")).thenReturn(bankAccountDTO);
            when(transactionService.makeANewTransactionWithBank(any(PersonDTO.class), anyFloat(), any(BankAccountDTO.class), anyString())).thenReturn(transactionDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile/transferBank")
                    .param("receiver", "FR12345678912345678912345")
                    .param("amount", "20")
                    .param("button","sendMoneyToBank");
            // WHEN
            //the uri "/home/profile/transferBank" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //correct information is attached to model and the page is correctly redirected.
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("messageTransaction", "Your transaction of -20.0 euros with your bank account n°FR12345678912345678912345 has been successful."))
                    .andExpect(redirectedUrl("/home/profile?messageTransaction=Your+transaction+of+-20.0+euros+with+your+bank+account+n%B0FR12345678912345678912345+has+been+successful."));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(2)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(1)).getBankAccountDTO("FR12345678912345678912345");
            verify(transactionService, Mockito.times(1)).makeANewTransactionWithBank(any(PersonDTO.class), anyFloat(), any(BankAccountDTO.class),anyString());
        }


        @Test
        @DisplayName("GIVEN errors in BindingResult" +
                "WHEN the uri \"/home/profile/transferBank\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void submitTransferBankErrorsTest() throws Exception {
            //GIVEN
            //errors in binding result
            String email = "person1@mail.fr";
            String password = "Password1!";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, firstName, lastname);
            Person person = new Person(email,password,firstName,lastname);
            person.setAvailableBalance(10F);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            when(personService.getPerson(email)).thenReturn(person);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/profile/transferBank")
                    .param("receiver", "FR12345678912345678912345")
                    .param("amount", "20")
                    .param("button","sendMoneyToBank");
            // WHEN
            //the uri "/home/profile/transferBank" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //correct information is attached to model and the page is correctly redirected.
                    .andExpect(status().isOk())
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(view().name("profile"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(2)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
            verify(personService, Mockito.times(1)).getPerson(email);
            verify(bankAccountService, Mockito.times(0)).getBankAccountDTO(anyString());
            verify(transactionService, Mockito.times(0)).makeANewTransactionWithBank(any(PersonDTO.class), anyFloat(), any(BankAccountDTO.class),anyString());
        }
    }
}