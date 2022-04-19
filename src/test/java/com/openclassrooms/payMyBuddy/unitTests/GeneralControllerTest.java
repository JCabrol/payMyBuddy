package com.openclassrooms.payMyBuddy.unitTests;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ControllerTests")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
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
        @DisplayName("GIVEN an error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageErrorMessageLoginTest() throws Exception {
            //GIVEN
            //no connected user found by service and no error message
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
        @DisplayName("GIVEN an error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageErrorMessageTest() throws Exception {
            //GIVEN
            //no connected user found by service and no error message
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
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getRootPageTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(redirectedUrl("/home"));
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("GetLoginPageTest")
    class GetLoginPageTests {
        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getLoginPageTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/login");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(redirectedUrl("/home"));
        }
    }


    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("ShowInscriptionTest")
    class ShowInscriptionTests {
        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void showInscriptionTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/inscription");
            // WHEN
            //the uri "/home" is called,
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


}