package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.NotValidException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectAlreadyExistingException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.DTO.BankAccountDTO;
import com.openclassrooms.payMyBuddy.model.DTO.ListTransactionPagesDTO;
import com.openclassrooms.payMyBuddy.model.DTO.PersonDTO;
import com.openclassrooms.payMyBuddy.model.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Person;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAllPersonsDTO();

    List<PersonDTO> getAllActivePersonsDTO();

    List<PersonDTO> getAllActiveNotFriendPersonsDTO(PersonDTO personDTO);

    Person getPerson(String mail) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    PersonDTO getPersonDTO(String mail);

    String getCurrentUserMail() throws NotFoundObjectException;

    String createPerson(PersonDTO personDTO);

    String changePassword(PersonDTO personDTO, String password);

    String deletePerson(PersonDTO personDTO);

    String addPersonInGroup(PersonDTO groupOwnerDTO,PersonDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException,ObjectNotExistingAnymoreException;

    String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonDTO personRemovedFromGroup) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException;

    String addBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    String removeBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    String updatePerson(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsMade (PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsReceived (PersonDTO personDTO);

    ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType);
}
