package com.openclassrooms.payMyBuddy.integrationTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Tag("ControllerTests")
@Slf4j
@SpringBootTest
@DirtiesContext(classMode = AFTER_CLASS)
@AutoConfigureMockMvc
public class GeneralControllerIntegrationTests {


    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Tag("GeneralControllerTests")
    @DisplayName("GetHomePageTest")
    class GetHomePageTests {

        @WithMockUser(username = "person1@mail.fr", password = "password1", roles = "USER")
        @Test
        @DisplayName("GIVEN a connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageTest() throws Exception {
            //GIVEN
            //a connected user found by service and no error message
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(get("/home"))
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("personDTO"))
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(model().attribute("activePage", "Home"));

        }

        @Test
        @DisplayName("GIVEN no connected user found by service and no error message " +
                "WHEN the uri /home is called, " +
                "THEN the status is isOk and correct information is attached to model.")
        void getHomePageNotConnectedTest() throws Exception {
            //GIVEN
            //no connected user found by service and no error message
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(get("/home"))
                    // THEN
                    //the status is "isOk" and correct information is attached to model.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("personDTO"))
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(model().attribute("activePage", "Home"));

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
        }
    }
}