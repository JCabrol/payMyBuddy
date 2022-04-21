package com.openclassrooms.payMyBuddy.unitTests;

import com.openclassrooms.payMyBuddy.model.DTO.MessageDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.service.PersonService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ControllerTests")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GeneralControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("GetHomePageTest")
    class GetHomePageTests {

        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("personDTO", personDTO))
                    .andExpect(model().attribute("activePage", "Home"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(email);
        }


        @Test
        @DisplayName("GIVEN no connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageNotConnectedTest() throws Exception {
            //GIVEN
            //no connected user found by service and no error message
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home");
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(model().attribute("activePage", "Home"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }

        @Test
        @DisplayName("GIVEN an error message whose value is login " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageErrorMessageLoginTest() throws Exception {
            //GIVEN
            //an error message whose value is login
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home").param("error", "login");
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attribute("activePage", "Home"))
                    .andExpect(model().attribute("errorMessage", "Your email or your password hasn't been found, please try to login again"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }

        @Test
        @DisplayName("GIVEN an error message whose value is not login " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageErrorMessageTest() throws Exception {
            //GIVEN
            //an error message whose value is not login
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home").param("error", "");
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(model().attribute("activePage", "Home"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("GetRootPageTest")
    class GetRootPageTests {

        @Test
        @DisplayName("GIVEN  " +
                "WHEN the uri / is called, " +
                "THEN there is a redirection to the url /home.")
        void getRootPageTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/");
            // WHEN
            //the uri "/" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //there is a redirection to the url "/home".
                    .andExpect(redirectedUrl("/home"));
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("GetLoginPageTest")
    class GetLoginPageTests {
        @Test
        @DisplayName("GIVEN  " +
                "WHEN the uri \"/login\" is called, " +
                "THEN there is a redirection to the url \"/home\".")
        void getLoginPageTest() throws Exception {
            //GIVEN
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/login");
            // WHEN
            //the uri "/login" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //there is a redirection to the url "/home".
                    .andExpect(redirectedUrl("/home"));
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("ShowInscriptionTest")
    class ShowInscriptionTests {
        @Test
        @DisplayName("GIVEN no connected user" +
                "WHEN the uri \"/inscription\" is called, " +
                "THEN the status is \"isOk\" and correct information is attached to model.")
        void showInscriptionTest() throws Exception {
            //GIVEN
            //no connected user
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/inscription");
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/inscription" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("inscription"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Inscription"))
                    .andExpect(model().attributeExists("personDTO"));
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("ContactTest")
    class ContactTests {
        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void contactTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/contact");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Contact"))
                    .andExpect(model().attributeExists("messageDTO"))
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeDoesNotExist("message"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }

        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void contactConnectedTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTOcreated = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTOcreated);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/contact");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Contact"))
                    .andExpect(model().attributeExists("messageDTO"))
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeDoesNotExist("message"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(anyString());
        }

        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void contactErrorTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            String email = "person1@mail.fr";
            String password = "password1";
            String firstName = "firstName1";
            String lastname = "lastName1";
            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
            when(personService.getCurrentUserMail()).thenReturn(email);
            when(personService.getPersonDTO(email)).thenReturn(personDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/home/contact")
                    .param("message", "error message");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("activePage", "Contact"))
                    .andExpect(model().attribute("message", "error message"))
                    .andExpect(model().attributeExists("messageDTO"))
                    .andExpect(model().attributeExists("personDTO"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(1)).getPersonDTO(anyString());
        }
    }

    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("SendMessageTest")
    class SendMessageTests {
        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void sendMessageWrongEmailTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            String subject = "subject";
            String message = "message";
            String firstName = "firstName";
            String lastName = "lastName";
            String email = "email";
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/contact")
                    .param("subject", subject)
                    .param("message", message)
                    .param("firstName", firstName)
                    .param("lastName", lastName)
                    .param("email", email);
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(model().attribute("activePage", "Contact"))
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeExists("messageDTO"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }

        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void sendMessageMissingInformationTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            MessageDTO messageDTO = new MessageDTO();
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/home/contact")
                    .param("subject", messageDTO.getSubject())
                    .param("message", messageDTO.getMessage())
                    .param("firstName", messageDTO.getFirstName())
                    .param("lastName", messageDTO.getLastName())
                    .param("email", messageDTO.getEmail());
            when(personService.getCurrentUserMail()).thenReturn(null);
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("contact"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(model().attribute("activePage", "Contact"))
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeExists("messageDTO"));
            //and the expected methods have been called with expected arguments
            verify(personService, Mockito.times(1)).getCurrentUserMail();
            verify(personService, Mockito.times(0)).getPersonDTO(anyString());
        }
    }
}