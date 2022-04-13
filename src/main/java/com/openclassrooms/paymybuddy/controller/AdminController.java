package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.DTO.ListMessagesDTO;
import com.openclassrooms.paymybuddy.model.DTO.MessageDTO;
import com.openclassrooms.paymybuddy.model.DTO.PersonConnectionDTO;
import com.openclassrooms.paymybuddy.model.DTO.PersonDTO;
import com.openclassrooms.paymybuddy.model.MessageToAdmin;
import com.openclassrooms.paymybuddy.model.Person;
import com.openclassrooms.paymybuddy.service.MessageToAdminService;
import com.openclassrooms.paymybuddy.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    PersonService personService;
    @Autowired
    MessageToAdminService messageToAdminService;

    @Transactional
    @GetMapping("/admin")
    public ModelAndView admin() {
        String viewName = "admin";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        model.put("personDTO", personDTO);
        model.put("activePage", "Admin");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @GetMapping("/admin/manageApplication")
    public ModelAndView manageApplication() {
        String viewName = "manageApplication";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        model.put("personDTO", personDTO);
        model.put("activePage", "Admin");
        model.put("activeSubpage", "ManageApplication");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @GetMapping("/admin/manageUsers")
    public ModelAndView manageUsers(Integer currentPage, String messageResult, Integer messageNumber) {
        String viewName = "manageUsers";
        Map<String, Object> model = new HashMap<>();

        if (currentPage == null) {
            currentPage = 1;
        }
        MessageDTO readingMessage = null;
        if (messageNumber != null) {
            MessageToAdmin messageToAdmin = messageToAdminService.getMessage(messageNumber);
            messageToAdminService.readMessage(messageToAdmin);
            readingMessage = messageToAdminService.transformToDTO(messageToAdmin);
        }
        List<PersonDTO> allActiveAccounts = personService.getAllActivePersonsDTO();
        List<Person> allInactiveAccounts = personService.getAllInactivePersons();
        PersonConnectionDTO personToDeactivate = new PersonConnectionDTO();
        PersonConnectionDTO personToReactivate = new PersonConnectionDTO();
        ListMessagesDTO listMessage = messageToAdminService.displayMessagesByPage(currentPage, 5);
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);

        model.put("personDTO", personDTO);
        model.put("listMessage", listMessage);
        model.put("messageResult", messageResult);
        model.put("readingMessage", readingMessage);
        model.put("allActiveAccounts", allActiveAccounts);
        model.put("allInactiveAccounts", allInactiveAccounts);
        model.put("personToDeactivate", personToDeactivate);
        model.put("personToReactivate", personToReactivate);
        model.put("activePage", "Admin");
        model.put("activeSubpage", "ManageUsers");
        return new ModelAndView(viewName, model);
    }


    @Transactional
    @PostMapping("/admin/manageUsers/deactivateAccount")
    public ModelAndView deactivateAccount(@Valid @ModelAttribute("personToDeactivate") PersonConnectionDTO personToDeactivate, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageUsers";
            List<PersonDTO> allActiveAccounts = personService.getAllActivePersonsDTO();
            List<Person> allInactiveAccounts = personService.getAllInactivePersons();
            PersonConnectionDTO personToReactivate = new PersonConnectionDTO();
            ListMessagesDTO listMessage = messageToAdminService.displayMessagesByPage(1, 5);
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
            model.put("listMessage", listMessage);
            model.put("allActiveAccounts", allActiveAccounts);
            model.put("allInactiveAccounts", allInactiveAccounts);
            model.put("personToDeactivate", personToDeactivate);
            model.put("personToReactivate", personToReactivate);
            model.put("activePage", "Admin");
            model.put("activeSubpage", "ManageUsers");
            return new ModelAndView(viewName, model);
        } else {

            String message = personService.deletePerson(personToDeactivate.getEmail());
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/admin/manageUsers");
            model.put("messageResult", message);
            return new ModelAndView(redirect, model);
        }
    }

    @Transactional
    @PostMapping("/admin/manageUsers/reactivateAccount")
    public ModelAndView reactivateAccount(@Valid @ModelAttribute("personToReactivate") PersonConnectionDTO personToReactivate, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageUsers";
            List<PersonDTO> allActiveAccounts = personService.getAllActivePersonsDTO();
            List<Person> allInactiveAccounts = personService.getAllInactivePersons();
            PersonConnectionDTO personToDeactivate = new PersonConnectionDTO();
            ListMessagesDTO listMessage = messageToAdminService.displayMessagesByPage(1, 5);
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
            model.put("listMessage", listMessage);
            model.put("allActiveAccounts", allActiveAccounts);
            model.put("allInactiveAccounts", allInactiveAccounts);
            model.put("personToDeactivate", personToDeactivate);
            model.put("personToReactivate", personToReactivate);
            model.put("activePage", "Admin");
            model.put("activeSubpage", "ManageUsers");
            return new ModelAndView(viewName, model);
        } else {

            String message = personService.reactivateAccount(personToReactivate.getEmail());
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/admin/manageUsers");
            model.put("messageResult", message);
            return new ModelAndView(redirect, model);
        }
    }

}
