package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.DTO.ListTransactionPagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonConnectionDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api
public class TransferController {
    @Autowired
    PersonService personService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @GetMapping("/home/transfer")
    public ModelAndView showTransfer(@Valid Integer currentPageMade, @Valid Integer currentPageReceived, @Valid String transactionType, String message) {
        String viewName;
        if (transactionType == null) {
            transactionType = "made";
        }
        if (transactionType.equals("made")) {
            viewName = "transfer";
        } else {
            viewName = "transferReceived";
        }
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        if (currentPageMade == null) {
            currentPageMade = 1;
        }
        if (currentPageReceived == null) {
            currentPageReceived = 1;
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        ListTransactionPagesDTO listTransactionsMade = personService.displayTransactionsByPage(personDTO, currentPageMade, 3, "made");
        ListTransactionPagesDTO listTransactionsReceived = personService.displayTransactionsByPage(personDTO, currentPageReceived, 3, "received");

        model.put("personDTO", personDTO);
        model.put("transactionDTO", transactionDTO);
        model.put("listTransactionsMade", listTransactionsMade);
        model.put("listTransactionsReceived", listTransactionsReceived);
        model.put("transactionType", transactionType);
        model.put("message", message);
        model.put("activePage", "Transfer");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/transfer")
    public ModelAndView submitTransfer(@Valid @ModelAttribute("transactionDTO")TransactionDTO transactionDTO, BindingResult bindingResult,ModelMap model) {
        if (bindingResult.hasErrors()) {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
            model.put("transactionDTO", transactionDTO);
            model.put("listTransactionsMade", personService.displayTransactionsByPage(personDTO, 1, 3, "made"));
            model.put("listTransactionsReceived", personService.displayTransactionsByPage(personDTO, 1, 3, "received"));
            model.put("transactionType", "made");
            model.put("activePage", "Transfer");
            return new ModelAndView("transfer", model);
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO sender = personService.getPersonDTO(mail);
        PersonDTO receiver = personService.getPersonDTO(transactionDTO.getReceiver());
        float amount = transactionDTO.getAmount();
        String description = transactionDTO.getDescription();
        transactionDTO = transactionService.makeANewTransactionBetweenPersons(sender, amount, receiver, description);
        String message = "Your transaction of " + transactionDTO.getAmount() + " euros to " + receiver.getFirstName() + " " + receiver.getLastName() + " has been successful.";
        model.put("message", message);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/transfer");
        return new ModelAndView(redirect,model);
    }

    @Transactional
    @GetMapping("/home/transfer/manageConnections")
    public ModelAndView manageConnections() {
        String viewName = "manageConnections";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        List<PersonDTO> allNotFriends = personService.getAllActiveNotFriendPersonsDTO(personDTO);
        PersonConnectionDTO personSelected = new PersonConnectionDTO();
        PersonConnectionDTO personToRemove = new PersonConnectionDTO();
        model.put("personDTO", personDTO);
        model.put("allNotFriends", allNotFriends);
        model.put("personSelected", personSelected);
        model.put("personToRemove", personToRemove);
        model.put("activePage", "Transfer");
        model.put("activeSubpage", "ManageConnections");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/transfer/submitNewConnection")
    public ModelAndView submitNewConnection(@Valid @ModelAttribute("personSelected") PersonConnectionDTO personSelected, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageConnections";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            List<PersonDTO> allNotFriends = personService.getAllActiveNotFriendPersonsDTO(personDTO);
            PersonConnectionDTO personToRemove = new PersonConnectionDTO();
            model.put("personDTO", personDTO);
            model.put("personSelected", personSelected);
            model.put("allNotFriends", allNotFriends);
            model.put("activePage", "Transfer");
            model.put("activeSubpage", "ManageConnections");
            model.put("personToRemove", personToRemove);
            return new ModelAndView(viewName, model);
        } else {
            String mail = personService.getCurrentUserMail();
            PersonDTO personConnected = personService.getPersonDTO(mail);
            String message = personService.addPersonInGroup(personConnected, personSelected);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/home/transfer");
            model.put("message", message);
            return new ModelAndView(redirect, model);
        }
    }

    @Transactional
    @PostMapping("/home/transfer/removeConnection")
    public ModelAndView removeConnection(@Valid @ModelAttribute("personToRemove") PersonConnectionDTO personToRemove, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageConnections";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            List<PersonDTO> allNotFriends = personService.getAllActiveNotFriendPersonsDTO(personDTO);
            PersonConnectionDTO personSelected = new PersonConnectionDTO();
            model.put("personSelected", personSelected);
            model.put("personDTO", personDTO);
            model.put("personToRemove", personToRemove);
            model.put("allNotFriends", allNotFriends);
            model.put("activePage", "Transfer");
            model.put("activeSubpage", "ManageConnections");
            return new ModelAndView(viewName, model);
        } else {
            String mail = personService.getCurrentUserMail();
            PersonDTO personConnected = personService.getPersonDTO(mail);
            String message = personService.removePersonFromGroup(personConnected, personToRemove);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/home/transfer");
            model.put("message", message);
            return new ModelAndView(redirect, model);
        }
    }
}
