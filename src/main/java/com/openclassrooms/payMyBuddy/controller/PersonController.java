package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class PersonController {
    @Autowired
    PersonService personService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BankAccountService bankAccountService;


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

    @Transactional
    @GetMapping("/home")
    public ModelAndView getHomePage() {
        String viewName = "home";
        Map<String, Object> model = new HashMap<>();
        try {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("personDTO", personDTO);
        } catch (Exception ignored) {
        }
        model.put("activePage", "Home");
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/home/contact")
    public ModelAndView contact() {
        String viewName = "contact";
        Map<String, Object> model = new HashMap<>();
        model.put("activePage", "Contact");
        return new ModelAndView(viewName, model);
    }


    @GetMapping("/login")
    public ModelAndView login() {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home");
        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/home/profile/manageBankAccounts")
    public ModelAndView addNewBankAccount() {
        String viewName = "manageBankAccounts";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        BankAccountDTO bankAccountSelected = new BankAccountDTO();
        model.put("person", personDTO);
        model.put("bankAccountSelected", bankAccountSelected);
        model.put("activePage", "Profile");
        model.put("activeSubpage", "ManageBankAccounts");
        model.put("fromAppli",true);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile/submitNewBankAccount")
    public ModelAndView submitNewBankAccount(@Valid BankAccountDTO bankAccountSelected,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageBankAccounts";
            Map<String, Object> model = new HashMap<>();
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            model.put("person", personDTO);
            model.put("bankAccountSelected", bankAccountSelected);
            model.put("activePage", "Profile");
            model.put("activeSubpage", "ManageBankAccounts");
            return new ModelAndView(viewName,model);
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
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        List<PersonDTO> allNotFriends = personService.getAllActiveNotFriendPersonsDTO(personDTO);
        PersonDTO personSelected = new PersonDTO();
        model.put("personDTO", personDTO);
        model.put("allNotFriends", allNotFriends);
        model.put("personSelected", personSelected);
        model.put("activePage", "Transfer");
        model.put("activeSubpage", "ManageConnections");

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
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        TransactionDTO transactionDTO = new TransactionDTO();
        model.put("personDTO", personDTO);
        model.put("changePassword", changePasswordDTO);
        model.put("transactionDTO", transactionDTO);
        model.put("activePage", "Profile");
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
    public ModelAndView submitBankTransfer(@Valid TransactionDTO transactionDTO, BindingResult bindingResult, String button) {

        if (bindingResult.hasErrors()) {
            //remove error from list if it's getting money from bank
            List<ObjectError> errorList = bindingResult.getAllErrors()
                    .stream()
                    .filter(error -> (!((Objects.equals(error.getDefaultMessage(), "You don't have enough money to make this transaction")) && (button.equals("getMoneyFromBank")))))
                    .collect(Collectors.toList());
            //put the updated errorList into bindingResult
            bindingResult = new BeanPropertyBindingResult(transactionDTO, "transactionDTO");
            for (ObjectError objectError : errorList) {
                bindingResult.addError(objectError);
            }
            if (bindingResult.hasErrors()) {
                //if there are still errors returning profile page
                Map<String, Object> model = new HashMap<>();
                String mail = personService.getCurrentUserMail();
                PersonDTO personDTO = personService.getPersonDTO(mail);
                ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
                model.put("personDTO", personDTO);
                model.put("changePassword", changePasswordDTO);
                model.put("transactionDTO", transactionDTO);
                model.put("activePage", "Profile");
                return new ModelAndView("profile", model);
            }
        }
        //if there is no error getting connected user
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        //getting transaction information
        float amount = transactionDTO.getAmount();
        String bankNumber = transactionDTO.getReceiver();
        String message;
        //checking button value to make a credit or a debit
        if (button.equals("getMoneyFromBank")) {
            message = "Credit from your bank";
        } else {
            message = "Debit to your bank";
            amount = -amount;
        }
        //making transaction
        BankAccountDTO bankAccountDTO = bankAccountService.getBankAccountDTO(bankNumber);
        transactionService.makeANewTransactionWithBank(personDTO, amount, bankAccountDTO, message);
        //redirect to profile page
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
    public ModelAndView showTransfer(@Valid Integer currentPageMade, @Valid Integer currentPageReceived, @Valid String transactionType) {
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
        model.put("activePage", "Transfer");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/transfer")
    public ModelAndView submitTransfer(@Valid TransactionDTO transactionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> model = new HashMap<>();
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
        transactionService.makeANewTransactionBetweenPersons(sender, amount, receiver, description);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/transfer");

        return new ModelAndView(redirect);
    }

    @Transactional
    @GetMapping("/inscription")
    public ModelAndView showInscription() {
        String viewName = "inscription";
        Map<String, Object> model = new HashMap<>();
        PersonDTO personDTO = new PersonDTO();
        model.put("personDTO", personDTO);
        model.put("activePage", "Inscription");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/inscription")
    public ModelAndView submitInscription(@Valid PersonDTO personDTO, BindingResult bindingResult, String confirmPassword, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("inscription");
        }
        if (!confirmPassword.equals(personDTO.getPassword())) {
            throw new NotValidException("Password and confirmation should be the same.");
        } else {
            personService.createPerson(personDTO);
            String userName = personDTO.getEmail();
            String password = personDTO.getPassword();
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
