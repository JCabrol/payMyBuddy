package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.model.DTO.MessageDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonInscriptionDTO;
import com.openclassrooms.payMyBuddy.service.MessageToAdminService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GeneralController {
    @Autowired
    PersonService personService;

    @Autowired
    MessageToAdminService messageToAdminService;

    @ApiOperation(value = "Invalidates old session and redirect to post root page.")
    @GetMapping("/")
    public ModelAndView getRootPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return postRootPage();
    }

    @ApiOperation(value = "Redirect to the home page.")
    @PostMapping("/")
    public ModelAndView postRootPage() {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home");
        return new ModelAndView(redirect);
    }

    @ApiOperation(value = "Redirect to the home page.")
    @GetMapping("/login")
    public ModelAndView login() {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home");
        return new ModelAndView(redirect);
    }

    @ApiOperation(value = "Displays the home page and the login modal.")
    @Transactional
    @GetMapping("/home")
    public ModelAndView getHomePage(String error) {
        String viewName = "home";
        Map<String, Object> model = new HashMap<>();
        if ((error != null) && error.equals("login")) {
            model.put("errorMessage", "Your email or your password hasn't been found, please try to login again");
        }
        try {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
        } catch (Exception ignored) {
        }
        model.put("activePage", "Home");
        return new ModelAndView(viewName, model);
    }

    @ApiOperation(value = "Displays the inscription form.")
    @Transactional
    @GetMapping("/inscription")
    public ModelAndView showInscription() {
        String viewName = "inscription";
        Map<String, Object> model = new HashMap<>();
        PersonInscriptionDTO personDTO = new PersonInscriptionDTO();
        model.put("personDTO", personDTO);
        model.put("activePage", "Inscription");
        return new ModelAndView(viewName, model);
    }

    @ApiOperation(value = "Submit and check information from inscription form." +
            "\nCreate a new user, log it and redirect to home page if everything is OK," +
            "\nredirect to inscription form otherwise.")
    @Transactional
    @PostMapping("/inscription")
    public ModelAndView submitInscription(@Valid @ModelAttribute("personDTO") PersonInscriptionDTO personDTO, BindingResult bindingResult, ModelMap model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("inscription");
        } else {
            if (personService.emailAlreadyExists(personDTO.getEmail())) {
                bindingResult.rejectValue("email", "", "This email address is already registered in this application");
                return new ModelAndView("inscription");
            } else {
                String userName = personDTO.getEmail();
                String password = personDTO.getPassword();
                PersonDTO personToInscribe = new PersonDTO(userName, password, personDTO.getFirstName(), personDTO.getLastName());
                personService.createPerson(personToInscribe);
                try {
                    request.login(userName, password);
                } catch (ServletException e) {
                    throw new NotValidException("The new user couldn't have been connected");
                }
                RedirectView redirect = new RedirectView();
                redirect.setUrl("/home");
                return new ModelAndView(redirect);
            }
        }
    }

    @ApiOperation(value = "Displays the form permitting to send messages to administrator.")
    @Transactional
    @GetMapping("/home/contact")
    public ModelAndView contact(String message) {
        String viewName = "contact";
        Map<String, Object> model = new HashMap<>();
        try {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
        } catch (Exception ignored) {
        }
        MessageDTO messageDTO = new MessageDTO();
        model.put("messageDTO", messageDTO);
        model.put("message", message);
        model.put("activePage", "Contact");
        return new ModelAndView(viewName, model);
    }

    @ApiOperation(value = "Submit and check message, " +
            "\nsend the message and redirect to contact page if everything is ok." +
            "\nredirect to contact page and displays error messages otherwise.")
    @Transactional
    @PostMapping("/home/contact")
    public ModelAndView sendMessage(@Valid @ModelAttribute("messageDTO") MessageDTO messageDTO, BindingResult bindingResult, ModelMap model) {
        PersonDTO personDTO;
        try {
            String mail = personService.getCurrentUserMail();
            personDTO = personService.getPersonDTO(mail);
        } catch (Exception ignored) {
            personDTO = null;
        }
        if (bindingResult.hasErrors()) {
            String viewName = "contact";
            model.put("messageDTO", messageDTO);
            model.put("activePage", "Contact");
            model.put("personDTO", personDTO);
            return new ModelAndView(viewName, model);
        } else {
            String message = messageToAdminService.sendMessage(messageDTO);
            model.put("message", message);
            model.put("personDTO", personDTO);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/home/contact");
            return new ModelAndView(redirect, model);
        }

    }
}
