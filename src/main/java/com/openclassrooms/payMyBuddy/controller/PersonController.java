package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PersonController {
    @Autowired
    PersonService personService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("/home")
    public ModelAndView getHomePage() {
        String viewName = "home";
        Map<String, Object> model = new HashMap<String, Object>();
//        String mail = personService.getCurrentUserMail();
//        PersonDTO personDTO = personService.getPersonDTO(mail);
//        model.put("personDTO", personDTO);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/home/contact")
    public ModelAndView contact() {
        String viewName = "contact";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(viewName, model);
    }


    @GetMapping("/login")
    public ModelAndView login() {
        String viewName = "login";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/home/logout")
    public ModelAndView logout() {
        String viewName = "logout";
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @GetMapping("/home/profile/manageBankAccounts")
    public ModelAndView addNewBankAccount() {
        String viewName = "manageBankAccounts";
        Map<String, Object> model = new HashMap<String, Object>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        BankAccountDTO bankAccountSelected = new BankAccountDTO();
        model.put("person", personDTO);
        model.put("bankAccountSelected", bankAccountSelected);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile/submitNewBankAccount")
    public ModelAndView submitNewBankAccount(@Valid BankAccountDTO bankAccountSelected, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("manageBankAccounts");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personConnected = personService.getPersonDTO(mail);
        personService.addBankAccount(personConnected, bankAccountSelected);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/home/transfer/manageConnections")
    public ModelAndView addConnection() {
        String viewName = "manageConnections";
        Map<String, Object> model = new HashMap<String, Object>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        List<PersonDTO> allNotFriends = personService.getAllActiveNotFriendPersonsDTO(personDTO);
        PersonDTO personSelected = new PersonDTO();
        model.put("person", personDTO);
        model.put("allNotFriends", allNotFriends);
        model.put("personSelected", personSelected);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile/removeBankAccount")
    public ModelAndView removeBankAccount(@Valid BankAccountDTO bankAccountSelected, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("manageBankAccounts");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personConnected = personService.getPersonDTO(mail);
        personService.removeBankAccount(personConnected, bankAccountSelected);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect);
    }

    @Transactional
    @PostMapping("/home/transfer/submitNewConnection")
    public ModelAndView submitNewConnection(@Valid PersonDTO personSelected, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("manageConnections");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personConnected = personService.getPersonDTO(mail);
        personService.addPersonInGroup(personConnected, personSelected);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/transfer");
        return new ModelAndView(redirect);
    }


    @Transactional
    @PostMapping("/home/transfer/removeConnection")
    public ModelAndView removeConnection(@Valid PersonDTO personToRemove, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("manageConnections");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personConnected = personService.getPersonDTO(mail);
        personService.removePersonFromGroup(personConnected, personToRemove);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/transfer");
        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/home/profile")
    public ModelAndView showProfile() {
        String viewName = "profile";
        Map<String, Object> model = new HashMap<String, Object>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        TransactionDTO transactionDTO = new TransactionDTO();
        model.put("personDTO", personDTO);
        model.put("changePassword", changePasswordDTO);
        model.put("transactionDTO", transactionDTO);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile/password")
    public ModelAndView submitNewPassword(@Valid ChangePasswordDTO changePasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile");
        }
        String newPassword = changePasswordDTO.getPassword();
        if (newPassword == null) {
            return new ModelAndView("profile");
        }
        if (!newPassword.equals(changePasswordDTO.getConfirmationPassword())) {
            bindingResult.rejectValue("confirmationPassword", "", "Confirmation should be identical to password.");
            return new ModelAndView("profile");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);

        personService.changePassword(personDTO, newPassword);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect);
    }

    @Transactional
    @PostMapping("/home/profile/transferBank")
    public ModelAndView submitBankTransfer(@Valid TransactionDTO transactionDTO, String button, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        float amount = transactionDTO.getAmount();
        String bankNumber = transactionDTO.getReceiver();
        String message;
        if (button.equals("getMoneyFromBank")) {
            message = "Credit from your bank";
        } else {
            message = "Debit to your bank";
            amount = -amount;
        }
        BankAccountDTO bankAccountDTO = bankAccountService.getBankAccountDTO(bankNumber);
        transactionService.makeANewTransactionWithBank(personDTO, amount, bankAccountDTO, message);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect);
    }

    @Transactional
    @PostMapping("/home/profile")
    public ModelAndView submitProfile(@Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("profile");
        }
        String mail = personService.getCurrentUserMail();
        personDTO.setEmail(mail);
        personService.updatePerson(personDTO);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/home/transfer")
    public ModelAndView showTransfer(@Valid Integer currentPage, @Valid String transactionType) {
        String viewName = "transfer";
        Map<String, Object> model = new HashMap<String, Object>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        if (currentPage == null) {
            currentPage = 1;
        }
        if (transactionType == null) {
            transactionType = "made";
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        ListTransactionPagesDTO listTransactionPagesDTO = personService.displayTransactionsByPage(personDTO, currentPage, 3, transactionType);
        model.put("person", personDTO);
        model.put("transactionDTO", transactionDTO);
        model.put("listTransactionPages", listTransactionPagesDTO);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/transfer")
    public ModelAndView submitTransfer(@Valid TransactionDTO transactionDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("transfer");
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO sender = personService.getPersonDTO(mail);
        PersonDTO receiver = personService.getPersonDTO(transactionDTO.getReceiver());
        float amount = transactionDTO.getAmount();
        String description = transactionDTO.getDescription();
        transactionService.makeANewTransactionBetweenPersons(sender, amount, receiver, description);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/transfer");

        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/inscription")
    public ModelAndView showInscription() {
        String viewName = "inscription";
        Map<String, Object> model = new HashMap<String, Object>();
        PersonDTO personDTO = new PersonDTO();
        model.put("personDTO", personDTO);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/inscription")
    public ModelAndView submitInscription(@Valid PersonDTO personDTO,String confirmPassword, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("inscription");
        }
        System.out.println(personDTO.getFirstName());
        System.out.println(personDTO.getPassword());
        System.out.println(confirmPassword);
        if (!confirmPassword.equals(personDTO.getPassword())) {
throw new NotValidException("Password and confirmation should be the same.");
        } else {
            personService.createPerson(personDTO);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/home");
            return new ModelAndView(redirect);
        }
    }
}
