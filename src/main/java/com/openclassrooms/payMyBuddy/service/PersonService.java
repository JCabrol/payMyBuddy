package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.exceptions.NotFoundObjectException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectAlreadyExistingException;
import com.openclassrooms.payMyBuddy.exceptions.ObjectNotExistingAnymoreException;
import com.openclassrooms.payMyBuddy.model.DTO.*;
import com.openclassrooms.payMyBuddy.model.Person;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAllPersonsDTO();

    List<PersonDTO> getAllActivePersonsDTO();

    List<Person> getAllInactivePersons();

    List<PersonDTO> getAllNotFriendPersonsDTO(PersonDTO personDTO);

    Person getPerson(String mail) throws NotFoundObjectException, ObjectNotExistingAnymoreException;

    PersonDTO getPersonDTO(String mail);

    String getCurrentUserMail() throws NotFoundObjectException;

    String createPerson(PersonDTO personDTO);

    String changePassword(PersonDTO personDTO, String password);

    String deletePerson(String email);

    String addPersonInGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO newPersonInGroupDTO) throws ObjectAlreadyExistingException, ObjectNotExistingAnymoreException;

    String removePersonFromGroup(PersonDTO groupOwnerDTO, PersonConnectionDTO personRemovedFromGroup) throws ObjectNotExistingAnymoreException, ObjectAlreadyExistingException;

    String reactivateAccount(String email);

    String addBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    String removeBankAccount(PersonDTO personDTO, BankAccountDTO bankAccountDTO);

    String updatePerson(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsMade(PersonDTO personDTO);

    List<TransactionDTO> getPersonTransactionsReceived(PersonDTO personDTO);

    ListTransactionPagesDTO displayTransactionsByPage(PersonDTO personDTO, int pageNumber, int numberOfTransactionByPage, String transactionType);

    boolean emailAlreadyExists(String email);

}
