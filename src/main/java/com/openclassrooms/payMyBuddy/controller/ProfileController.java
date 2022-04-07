package com.openclassrooms.payMyBuddy.controller;

import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.service.BankAccountService;
import com.openclassrooms.payMyBuddy.service.PersonService;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Api
public class ProfileController {
    @Autowired
    PersonService personService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    BankAccountService bankAccountService;

    @Transactional
    @GetMapping("/home/profile")
    public ModelAndView showProfile(String messageProfile, String messagePassword, String messageTransaction, String messageAccount) {
        String viewName = "profile";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        TransactionDTO transactionDTO = new TransactionDTO();
        UpdatePersonDTO updatePersonDTO = new UpdatePersonDTO();
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        model.put("personDTO", personDTO);
        model.put("changePasswordDTO", changePasswordDTO);
        model.put("transactionDTO", transactionDTO);
        model.put("updatePersonDTO", updatePersonDTO);
        model.put("activePage", "Profile");
        model.put("messageProfile", messageProfile);
        model.put("messagePassword", messagePassword);
        model.put("messageTransaction", messageTransaction);
        model.put("messageAccount", messageAccount);
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile")
    public ModelAndView submitProfile(@Valid @ModelAttribute("updatePersonDTO") UpdatePersonDTO updatePersonDTO, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "profile";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            TransactionDTO transactionDTO = new TransactionDTO();
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
            model.put("personDTO", personDTO);
            model.put("changePasswordDTO", changePasswordDTO);
            model.put("transactionDTO", transactionDTO);
            model.put("updatePersonDTO", updatePersonDTO);
            model.put("activePage", "Profile");
            return new ModelAndView(viewName, model);
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = new PersonDTO(mail, updatePersonDTO.getFirstName(), updatePersonDTO.getLastName());
        String message = personService.updatePerson(personDTO);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        model.put("messageProfile", message);
        return new ModelAndView(redirect, model);
    }

    @Transactional
    @PostMapping("/home/profile/password")
    public ModelAndView submitNewPassword(@Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO changePasswordDTO, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "profile";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            TransactionDTO transactionDTO = new TransactionDTO();
            UpdatePersonDTO updatePersonDTO = new UpdatePersonDTO();
            model.put("personDTO", personDTO);
            model.put("changePasswordDTO", changePasswordDTO);
            model.put("transactionDTO", transactionDTO);
            model.put("updatePersonDTO", updatePersonDTO);
            model.put("activePage", "Profile");
            return new ModelAndView(viewName, model);
        } else {
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            String message = personService.changePassword(personDTO, changePasswordDTO.getPassword());
            model.put("messagePassword", message);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/home/profile");
            return new ModelAndView(redirect, model);
        }
    }

    @Transactional
    @PostMapping("/home/profile/transferBank")
    public ModelAndView submitBankTransfer(@Valid @ModelAttribute("transactionDTO") TransactionBankDTO transactionDTO, BindingResult bindingResult, String button, ModelMap model) {

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
                String viewName = "profile";
                String mail = personService.getCurrentUserMail();
                PersonDTO personDTO = personService.getPersonDTO(mail);
                UpdatePersonDTO updatePersonDTO = new UpdatePersonDTO();
                ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
                model.put("personDTO", personDTO);
                model.put("changePasswordDTO", changePasswordDTO);
                model.put("transactionDTO", transactionDTO);
                model.put("updatePersonDTO", updatePersonDTO);
                model.put("activePage", "Profile");
                return new ModelAndView(viewName, model);
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
        TransactionDTO result = transactionService.makeANewTransactionWithBank(personDTO, amount, bankAccountDTO, message);
        String messageResult = "Your transaction of " + amount + " euros with your bank account n°" + transactionDTO.getReceiver() + " has been successful.";
        //redirect to profile page
        model.put("messageTransaction", messageResult);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect, model);
    }

    @Transactional
    @GetMapping("/home/profile/manageBankAccounts")
    public ModelAndView addNewBankAccount() {
        String viewName = "manageBankAccounts";
        Map<String, Object> model = new HashMap<>();
        String mail = personService.getCurrentUserMail();
        PersonDTO personDTO = personService.getPersonDTO(mail);
        BankAccountDTO bankAccountSelected = new BankAccountDTO();
        BankAccountTransactionDTO bankAccountToRemove = new BankAccountTransactionDTO();
        model.put("personDTO", personDTO);
        model.put("bankAccountSelected", bankAccountSelected);
        model.put("bankAccountToRemove", bankAccountToRemove);
        model.put("activePage", "Profile");
        model.put("activeSubpage", "ManageBankAccounts");
        return new ModelAndView(viewName, model);
    }

    @Transactional
    @PostMapping("/home/profile/submitNewBankAccount")
    public ModelAndView submitNewBankAccount(@Valid @ModelAttribute("bankAccountSelected") BankAccountDTO bankAccountSelected, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageBankAccounts";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            BankAccountTransactionDTO bankAccountToRemove = new BankAccountTransactionDTO();
            model.put("personDTO", personDTO);
            model.put("bankAccountSelected", bankAccountSelected);
            model.put("bankAccountToRemove", bankAccountToRemove);
            model.put("activePage", "Profile");
            model.put("activeSubpage", "ManageBankAccounts");
            return new ModelAndView(viewName, model);
        } else {
            if (bankAccountService.ibanAlreadyExists(bankAccountSelected.getIban())) {
                bindingResult.rejectValue("iban", "", "This bank account is already registered in this application");
                String viewName = "manageBankAccounts";
                String mail = personService.getCurrentUserMail();
                PersonDTO personDTO = personService.getPersonDTO(mail);
                BankAccountTransactionDTO bankAccountToRemove = new BankAccountTransactionDTO();
                model.put("personDTO", personDTO);
                model.put("bankAccountSelected", bankAccountSelected);
                model.put("bankAccountToRemove", bankAccountToRemove);
                model.put("activePage", "Profile");
                model.put("activeSubpage", "ManageBankAccounts");
                return new ModelAndView(viewName, model);
            } else {
                String mail = personService.getCurrentUserMail();
                PersonDTO personConnected = personService.getPersonDTO(mail);
                String message = personService.addBankAccount(personConnected, bankAccountSelected);
                model.put("messageAccount", message);
                RedirectView redirect = new RedirectView();
                redirect.setUrl("/home/profile");
                return new ModelAndView(redirect, model);
            }
        }
    }

    @Transactional
    @PostMapping("/home/profile/removeBankAccount")
    public ModelAndView removeBankAccount(@Valid @ModelAttribute("bankAccountToRemove") BankAccountTransactionDTO bankAccountToRemove, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            String viewName = "manageBankAccounts";
            String mail = personService.getCurrentUserMail();
            PersonDTO personDTO = personService.getPersonDTO(mail);
            BankAccountDTO bankAccountSelected = new BankAccountDTO();
            model.put("personDTO", personDTO);
            model.put("bankAccountSelected", bankAccountSelected);
            model.put("bankAccountToRemove", bankAccountToRemove);
            model.put("activePage", "Profile");
            model.put("activeSubpage", "ManageBankAccounts");
            return new ModelAndView(viewName, model);
        }
        String mail = personService.getCurrentUserMail();
        PersonDTO personConnected = personService.getPersonDTO(mail);
        BankAccountDTO bankAccountDTO = new BankAccountDTO(bankAccountToRemove.getIban(), bankAccountToRemove.getBic(), bankAccountToRemove.getUsualName());
        String message = personService.removeBankAccount(personConnected, bankAccountDTO);
        model.put("messageAccount", message);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/home/profile");
        return new ModelAndView(redirect, model);
    }
}
