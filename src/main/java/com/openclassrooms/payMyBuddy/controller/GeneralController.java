package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonInscriptionDTO;
import com.openclassrooms.payMyBuddy.service.PersonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api
public class GeneralController {
    @Autowired
    PersonService personService;

    @GetMapping("/")
    public ModelAndView getRootPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return postRootPage();
    }

    @PostMapping("/")
    public ModelAndView postRootPage() {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home");
        return new ModelAndView(redirect);
    }

//    @GetMapping("/login")
//    public ModelAndView login(String error) {
//        String viewName = "login";
//        Map<String, Object> model = new HashMap<>();
//        if(error.equals("true")){model.put("errorMessage","Your email or your password hasn't been found, please try to login again");}
//        model.put("activePage", "Home");
//        return new ModelAndView(viewName, model);
//    }

    @Transactional
    @GetMapping("/home")
    public ModelAndView getHomePage(String error) {
        String viewName = "home";
        Map<String, Object> model = new HashMap<>();
        if((error != null)&&error.equals("login")){
            model.put("errorMessage","Your email or your password hasn't been found, please try to login again");
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

    @Transactional
    @PostMapping("/inscription")
    public ModelAndView submitInscription(@Valid @ModelAttribute("personDTO")PersonInscriptionDTO personDTO, BindingResult bindingResult, ModelMap model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("inscription");
        }
         else {
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

    @GetMapping("/home/contact")
    public ModelAndView contact() {
        String viewName = "contact";
        Map<String, Object> model = new HashMap<>();
        try {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
        } catch (Exception ignored) {
        }
        model.put("activePage", "Contact");
        return new ModelAndView(viewName, model);
    }
}
